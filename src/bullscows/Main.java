package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static char[] characters = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    public static void main(String[] args) {
        playGame();
    }

    private static void playGame() {
        Scanner scanner = new Scanner(System.in);
        String secretCode;
        System.out.println("Input the length of the secret code:");
        String temp = scanner.nextLine();
        int lengthOfSecretCode = 0;
        try {
            lengthOfSecretCode = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            System.out.printf("Error: %s isn't a valid number.", temp);
            return;
        }
        if (lengthOfSecretCode == 0) {
            System.out.println("Error: Not possible to generate the secret code of length 0.");
            return;
        }
        System.out.println("Input the number of possible symbols in the code:");
        int numberOfSymbols = 0;
        temp = scanner.nextLine();
        try {
            numberOfSymbols = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            System.out.printf("Error: %s isn't a valid number.", temp);
            return;
        }

        if (lengthOfSecretCode > numberOfSymbols) {
            System.out.println("Error: it's not possible to generate a code with a length of" + lengthOfSecretCode + "with " + numberOfSymbols + " unique symbols.");
            return;
        } else if (lengthOfSecretCode > 36 || numberOfSymbols > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return;
        } else {
            secretCode = generateString(lengthOfSecretCode, numberOfSymbols);
            System.out.println("The secret is prepared: " + printStars(lengthOfSecretCode)  + " " + printRange(numberOfSymbols));
        }

        startGame(secretCode);
    }

    private static String printRange(int numberOfSymbols) {
        if (numberOfSymbols <= 10) {
            return "(0-" + characters[numberOfSymbols-1]+").";
        } else if (numberOfSymbols == 11){
            return "(0-9, a).";
        } else {
            return "(0-9, a-" + characters[numberOfSymbols-1] + ").";
        }
    }

    private static String printStars(int lengthOfSecretCode) {
        return "*".repeat(Math.max(0, lengthOfSecretCode));
    }

    private static void startGame(String secretCode) {
        System.out.println("Okay, let's start a game!");
        int turnNumber = 1;
        boolean isBull = false;
        while (!isBull) {
            isBull = calculateAndPrint(secretCode, turnNumber);
            turnNumber++;
        }
    }

    private static String generateString(int lengthOfSecretCode, int numberOfSymbols) {
        StringBuilder str = new StringBuilder();
        boolean[] arr = new boolean[numberOfSymbols];
        Random random = new Random();
        while (str.length() < lengthOfSecretCode) {
            int temp = random.nextInt();
            while (temp > 0) {
                if (!arr[temp%numberOfSymbols] && str.length() < lengthOfSecretCode) {
                    arr[temp%numberOfSymbols] = true;
                    str.append(characters[temp%numberOfSymbols]);
                }
                temp /= numberOfSymbols;
            }
        }
        return str.toString();
    }


    public static boolean calculateAndPrint(String secretCode, int turnNumber) {
        Scanner scanner = new Scanner(System.in);
        int n = secretCode.length();

        System.out.printf("Turn %d. Answer:\n", turnNumber);
        String numberToCheck = scanner.next();

        int cows = getCows(numberToCheck, secretCode);
        int bulls = getBulls(numberToCheck, secretCode);
        cows -= bulls;

        printGrade(cows, bulls);

        if (bulls == n) {
            System.out.println("Congratulations! You guessed the secret code.");
            return true;
        }
        return false;
    }

    private static void printGrade(int cows, int bulls) {
        if (cows == 0 && bulls == 0) {
            System.out.println("Grade: None.");
        } else if (cows == 0) {
            if (bulls == 1) {
                System.out.println("Grade: 1 bull.");
            } else {
                System.out.println("Grade: " + bulls + " bulls.");
            }
        } else if (bulls == 0) {
            if (cows == 1) {
                System.out.println("Grade: 1 cow.");
            } else {
                System.out.println("Grade: " + cows + " cows.");
            }
        } else if (bulls > 1) {
            if (cows == 1) {
                System.out.println("Grade: " + bulls + " bulls and 1 cow.");
            } else {
                System.out.println("Grade: " + bulls + " bulls and " + cows + " cows.");
            }
        } else if (cows > 1) {
            if (bulls == 1) {
                System.out.println("Grade: 1 bull and " + cows + " cows.");
            } else {
                System.out.println("Grade: " + bulls + " bulls and " + cows + " cows.");
            }
        }
    }

    private static int getBulls(String str1, String number) {
        int count = 0;
        String str2 = String.valueOf(number);
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) == str2.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    private static int getCows(String secretCode, String number) {
        int count = 0;
        boolean[] vis = new boolean[secretCode.length()];

        for (int i = 0; i < secretCode.length(); i++) {
            for (int j = 0; j < secretCode.length(); j++) {
                if (secretCode.charAt(i) == number.charAt(j) && !vis[j]) {
                    count++;
                    vis[j] = true;
                    break;
                }
            }
        }
        return count;
    }
}
