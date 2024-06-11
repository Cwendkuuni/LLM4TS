package jipa;

import java.util.*;

public class Variable
{
    public static List<Variable> variableList;
    private String varName;
    private int varValue;
    
    private Variable(final String varName) {
        this.varName = varName;
        this.varValue = 0;
    }
    
    private int getValue() {
        return this.varValue;
    }
    
    private void setValue(final int varValue) {
        this.varValue = varValue;
    }
    
    private String getName() {
        return this.varName;
    }
    
    public static boolean addVariable(final String varName) {
        if (findVariable(varName) == -1) {
            Variable.variableList.add(new Variable(varName));
            return true;
        }
        return false;
    }
    
    public static void removeVariable(final String varName) {
        final int index = findVariable(varName);
        if (index != -1) {
            Variable.variableList.remove(index);
        }
    }
    
    public static int getVariableValue(final String varName) {
        if (varName.startsWith("*")) {
            return findVariable(varName.substring(1));
        }
        final int varIndex = findVariable(varName);
        if (varIndex != -1) {
            return Variable.variableList.get(varIndex).getValue();
        }
        return 0;
    }
    
    public static void setVariableValue(final String varName, final int varValue) {
        final int varIndex = findVariable(varName);
        if (varIndex != -1) {
            Variable.variableList.get(varIndex).setValue(varValue);
        }
    }
    
    private static int findVariable(String varName) {
        final ListIterator i = Variable.variableList.listIterator();
        final boolean isReference = varName.startsWith("&");
        if (isReference) {
            varName = varName.substring(1);
        }
        while (i.hasNext()) {
            final Variable curVariable = i.next();
            if (curVariable.getName().equals(varName)) {
                if (!isReference) {
                    return i.previousIndex();
                }
                return curVariable.getValue();
            }
        }
        return -1;
    }
    
    static {
        Variable.variableList = new ArrayList<Variable>();
    }
}
