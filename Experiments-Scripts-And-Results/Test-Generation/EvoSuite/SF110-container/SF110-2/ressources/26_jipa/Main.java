package jipa;

import java.io.*;
import java.util.*;

public class Main
{
    public static int TOTAL_INSTRUCTIONS;
    public static String[] instruction;
    public static int iPtr;
    
    public static void main(final String[] args) {
        while (!loadInstructions((args.length > 0) ? args[0] : "")) {}
        while (true) {
            processInstruction(Main.instruction[Main.iPtr]);
        }
    }
    
    public static void processInstruction(final String instruction) {
        final int spaceLoc = instruction.indexOf(32);
        final int commaLoc = instruction.indexOf(44);
        String opcode;
        String[] operand;
        if (spaceLoc != -1) {
            opcode = instruction.substring(0, spaceLoc);
            operand = splitArguments(instruction.substring(spaceLoc + 1));
        }
        else {
            opcode = instruction;
            operand = new String[] { null };
        }
        switch (opcodes.toOpcode(opcode.toLowerCase())) {
            case halt: {
                System.exit(0);
                break;
            }
            case out: {
                System.out.println(getStringValue(operand[0]));
                break;
            }
            case in: {
                Variable.setVariableValue(operand[0], readValue());
                break;
            }
            case mov: {
                Variable.setVariableValue(operand[1], Variable.getVariableValue(operand[0]));
                Variable.setVariableValue(operand[0], 0);
                break;
            }
            case cpy: {
                Variable.setVariableValue(operand[1], Variable.getVariableValue(operand[0]));
                break;
            }
            case set: {
                Variable.setVariableValue(operand[0], getValue(operand[1]));
                break;
            }
            case del: {
                Variable.removeVariable(operand[0]);
                break;
            }
            case arr: {
                Variable.addVariable(operand[0]);
                for (int i = 1; i < Integer.parseInt(operand[1]); ++i) {
                    Variable.addVariable(operand[0] + "" + i);
                }
                break;
            }
            case var: {
                Variable.addVariable(operand[0]);
                break;
            }
            case inc: {
                Variable.setVariableValue(operand[0], Variable.getVariableValue(operand[0]) + 1);
                break;
            }
            case dec: {
                Variable.setVariableValue(operand[0], Variable.getVariableValue(operand[0]) - 1);
                break;
            }
            case add: {
                Variable.setVariableValue(operand[0], Variable.getVariableValue(operand[0]) + getValue(operand[1]));
                break;
            }
            case sub: {
                Variable.setVariableValue(operand[0], Variable.getVariableValue(operand[0]) - getValue(operand[1]));
                break;
            }
            case mul: {
                Variable.setVariableValue(operand[0], Variable.getVariableValue(operand[0]) * getValue(operand[1]));
                break;
            }
            case div: {
                Variable.setVariableValue(operand[0], Variable.getVariableValue(operand[0]) / getValue(operand[1]));
                break;
            }
            case mod: {
                Variable.setVariableValue(operand[0], Variable.getVariableValue(operand[0]) % getValue(operand[1]));
                break;
            }
            case xor: {
                Variable.setVariableValue(operand[0], Variable.getVariableValue(operand[0]) ^ getValue(operand[1]));
                break;
            }
            case or: {
                Variable.setVariableValue(operand[0], Variable.getVariableValue(operand[0]) | getValue(operand[1]));
                break;
            }
            case and: {
                Variable.setVariableValue(operand[0], Variable.getVariableValue(operand[0]) + getValue(operand[1]));
                break;
            }
            case jmp: {
                Main.iPtr = getJumpValue(operand[0]);
                break;
            }
            case jnz: {
                if (getValue(operand[0]) != 0) {
                    Main.iPtr = getJumpValue(operand[1]);
                    break;
                }
                break;
            }
            case ja: {
                if (getValue(operand[0]) > getValue(operand[1])) {
                    Main.iPtr = getJumpValue(operand[2]);
                    break;
                }
                break;
            }
            case jae: {
                if (getValue(operand[0]) >= getValue(operand[1])) {
                    Main.iPtr = getJumpValue(operand[2]);
                    break;
                }
                break;
            }
            case jb: {
                if (getValue(operand[0]) < getValue(operand[1])) {
                    Main.iPtr = getJumpValue(operand[2]);
                    break;
                }
                break;
            }
            case jbe: {
                if (getValue(operand[0]) <= getValue(operand[1])) {
                    Main.iPtr = getJumpValue(operand[2]);
                    break;
                }
                break;
            }
            case jz: {
                if (getValue(operand[0]) == 0) {
                    Main.iPtr = getJumpValue(operand[1]);
                    break;
                }
                break;
            }
            case je: {
                if (getValue(operand[0]) == getValue(operand[1])) {
                    Main.iPtr = getJumpValue(operand[2]);
                    break;
                }
                break;
            }
            case jne: {
                if (getValue(operand[0]) != getValue(operand[1])) {
                    Main.iPtr = getJumpValue(operand[2]);
                    break;
                }
                break;
            }
            case cmp: {
                if (getValue(operand[1]) == getValue(operand[0])) {
                    Variable.setVariableValue(operand[2], 1);
                    break;
                }
                Variable.setVariableValue(operand[2], 0);
                break;
            }
        }
        ++Main.iPtr;
    }
    
    public static String getFileName() {
        final Scanner in = new Scanner(System.in);
        System.out.println("Enter the full path to the file or type q to quit :");
        return in.nextLine();
    }
    
    public static boolean loadInstructions(String fileName) {
        int i = 0;
        resetInstructions();
        if (fileName.isEmpty()) {
            fileName = getFileName();
        }
        if (fileName.equalsIgnoreCase("q")) {
            System.exit(0);
        }
        try {
            final FileInputStream fin = new FileInputStream(fileName);
            final BufferedReader inputFile = new BufferedReader(new InputStreamReader(fin));
            final LineNumberReader linecount = new LineNumberReader(new FileReader(fileName));
            String currentLine;
            while ((currentLine = linecount.readLine()) != null) {}
            Main.TOTAL_INSTRUCTIONS = linecount.getLineNumber();
            Main.instruction = new String[Main.TOTAL_INSTRUCTIONS];
            linecount.close();
            while ((currentLine = inputFile.readLine()) != null) {
                currentLine = removeComment(currentLine);
                Main.instruction[i] = currentLine.trim();
                ++i;
            }
            Label.buildLabelList();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static String removeComment(final String line) {
        if (line.lastIndexOf(59) == -1) {
            return line.trim();
        }
        if (line.lastIndexOf(59) > line.lastIndexOf(34)) {
            return line.substring(0, line.lastIndexOf(59)).trim();
        }
        return line.trim();
    }
    
    public static void resetInstructions() {
        for (int i = 0; i < Main.TOTAL_INSTRUCTIONS; ++i) {
            Main.instruction[i] = "";
        }
    }
    
    public static String[] splitArguments(final String original) {
        int lastArg = 0;
        final Collection<String> argList = new ArrayList<String>();
        boolean inQuotes = false;
        if (original.indexOf(44) == -1) {
            final String[] args = { original.trim() };
            return args;
        }
        for (int i = 0; i < original.length(); ++i) {
            if (original.charAt(i) == '\"') {
                inQuotes = !inQuotes;
            }
            else if (original.charAt(i) == ',' && !inQuotes) {
                argList.add(original.substring(lastArg, i).trim());
                lastArg = i + 1;
            }
        }
        argList.add(original.substring(lastArg).trim());
        final String[] args = argList.toArray(new String[0]);
        return args;
    }
    
    public static int getValue(final String operand) {
        if (!isNumeric(operand)) {
            return Variable.getVariableValue(operand);
        }
        return Integer.parseInt(operand);
    }
    
    public static int getJumpValue(final String operand) {
        if (isNumeric(operand)) {
            return Integer.parseInt(operand) - 1;
        }
        return Label.findLabel(operand) - 1;
    }
    
    public static int readValue() {
        final Scanner in = new Scanner(System.in);
        final String tmpInt = in.nextLine();
        if (isNumeric(tmpInt)) {
            return Integer.parseInt(tmpInt);
        }
        return 0;
    }
    
    public static String getStringValue(final String original) {
        String finalString = "";
        boolean inQuotes = false;
        int lastQuotes = 0;
        if (original.indexOf(34) == -1) {
            return Integer.toString(getValue(original));
        }
        for (int i = 0; i < original.length(); ++i) {
            if (original.charAt(i) == '\"') {
                if (inQuotes) {
                    finalString += original.substring(lastQuotes, i);
                    inQuotes = false;
                    lastQuotes = i + 1;
                }
                else {
                    inQuotes = true;
                    if (i != 0) {
                        finalString += getValue(original.substring(lastQuotes, i));
                    }
                    lastQuotes = i + 1;
                }
            }
        }
        if (lastQuotes != original.length()) {
            finalString += getValue(original.substring(lastQuotes));
        }
        return finalString;
    }
    
    public static boolean isNumeric(final String str) {
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    static {
        Main.TOTAL_INSTRUCTIONS = 0;
        Main.instruction = new String[0];
        Main.iPtr = 0;
    }
    
    public enum opcodes
    {
        halt, 
        novalue, 
        set, 
        mov, 
        cpy, 
        var, 
        del, 
        arr, 
        add, 
        sub, 
        mul, 
        div, 
        mod, 
        inc, 
        dec, 
        in, 
        out, 
        jmp, 
        jnz, 
        jz, 
        je, 
        ja, 
        jae, 
        jb, 
        jbe, 
        jne, 
        xor, 
        or, 
        and, 
        cmp;
        
        public static opcodes toOpcode(final String str) {
            try {
                return valueOf(str);
            }
            catch (Exception e) {
                return opcodes.novalue;
            }
        }
    }
}
