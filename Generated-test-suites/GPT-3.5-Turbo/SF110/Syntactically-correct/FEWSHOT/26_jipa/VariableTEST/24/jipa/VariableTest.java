package jipa;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VariableTest {
    
    @Before
    public void setup() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("varName1"));
        assertTrue(Variable.addVariable("varName2"));
        assertFalse(Variable.addVariable("varName1"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("varName1");
        Variable.addVariable("varName2");
        Variable.removeVariable("varName1");
        assertEquals(-1, findVariableIndex("varName1"));
        assertEquals(-1, findVariableIndex("varName2"));
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("varName1");
        Variable.addVariable("varName2");
        Variable.setVariableValue("varName1", 10);
        Variable.setVariableValue("varName2", 20);
        assertEquals(10, Variable.getVariableValue("varName1"));
        assertEquals(20, Variable.getVariableValue("varName2"));
        assertEquals(0, Variable.getVariableValue("varName3"));
        assertEquals(10, Variable.getVariableValue("*varName1"));
        assertEquals(20, Variable.getVariableValue("*varName2"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("varName1");
        Variable.addVariable("varName2");
        Variable.setVariableValue("varName1", 10);
        Variable.setVariableValue("varName2", 20);
        assertEquals(10, Variable.getVariableValue("varName1"));
        assertEquals(20, Variable.getVariableValue("varName2"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("varName1");
        Variable.addVariable("varName2");
        assertEquals(0, findVariableIndex("varName1"));
        assertEquals(1, findVariableIndex("varName2"));
        assertEquals(-1, findVariableIndex("varName3"));
    }
    
    private int findVariableIndex(String varName) {
        final ListIterator<Variable> i = Variable.variableList.listIterator();
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
    
}