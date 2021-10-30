package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    static final String[] OPTION_LIST = {"add", "remove", "list", "exit"};
    static final String FILE_NAME = "tasks.csv";
    static String[][] tasks;

    public static void main(String[] args) {
        tasks = uploadData();
        listOptions();

        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            switch (line) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask();
                    break;
                case "list":
                    listTasks();
                    break;
                case "exit":
                    exitTasks();
                    System.out.println(ConsoleColors.RED + "Bye bye...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }
            System.out.println(ConsoleColors.BLUE + "Select from: add, remove, list and exit" + ConsoleColors.RESET);
        }
    }

    public static void listOptions() {
        System.out.println(ConsoleColors.BLUE + "Please select an option:");
        System.out.print(ConsoleColors.RESET);
        for (int i = 0; i < OPTION_LIST.length; i++) {
            System.out.println(OPTION_LIST[i]);
        }
    }

    public static String[][] uploadData() {
        File file = new File(FILE_NAME);
        String[][] dataTable = new String[0][3];

        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                dataTable = Arrays.copyOf(dataTable, dataTable.length + 1);
                dataTable[dataTable.length - 1] = scan.nextLine().split(",", 3);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Brak pliku.");
        }
        return dataTable;
    }

    public static void addTask() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Please add task description");
        String taskName = scan.nextLine();
        System.out.println("Please add task due date");
        String deadline = scan.nextLine();
        System.out.println("Is your task important: true/false");
        String isImportant = scan.nextLine();

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = taskName;
        tasks[tasks.length - 1][1] = deadline;
        tasks[tasks.length - 1][2] = isImportant;
    }

    public static void listTasks() {
        for (int i = 0; i < tasks.length; i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < tasks[0].length; j++) {
                System.out.print(tasks[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public static void removeTask() {
        Scanner scan = new Scanner(System.in);
        int rowToDelete = 0;
        System.out.println("Podaj liczbę");

        while (scan.hasNextLine()) {
            String n = scan.nextLine();

            if (NumberUtils.isParsable(n)) {
                rowToDelete = Integer.parseInt(n);
                if (rowToDelete >= 0 && rowToDelete < tasks.length) {
                    break;
                }
            }
            System.out.println("Podaj liczbę całkowitą pomiędzy 0 a " + (tasks.length - 1));
        }
        tasks = ArrayUtils.remove(tasks, rowToDelete);
    }

    public static void exitTasks() {
        Path path = Paths.get(FILE_NAME);
        List<String> inputLines = new ArrayList<>();

        for (int i = 0; i < tasks.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < tasks[i].length; j++) {
                if (j == tasks[i].length - 1) {
                    sb = sb.append(tasks[i][j]);
                } else {
                    sb = sb.append(tasks[i][j]).append(",");
                }
            }
            inputLines.add(sb.toString());
        }

        if (Files.exists(path)) {
            try {
                Files.write(path, inputLines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
