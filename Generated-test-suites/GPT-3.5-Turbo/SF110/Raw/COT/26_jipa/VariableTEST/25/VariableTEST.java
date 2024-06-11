
package jipa;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class VariableTest {
    
    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @Test
    public void testGetValue() {
        Variable variable = new Variable("test");
        int expectedValue = 0;
        int actualValue = variable.getValue();
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testSetValue() {
        Variable variable = new Variable("test");
        int value = 10;
        variable.setValue(value);
        int expectedValue = value;
        int actualValue = variable.getValue();
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testGetName() {
        Variable variable = new Variable("test");
        String expectedName = "test";
        String actualName = variable.getName();
        assertEquals(expectedName, actualName);
    }
    
    @Test
    public void testAddVariable() {
        String varName = "test";
        boolean expected = true;
        boolean actual = Variable.addVariable(varName);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemoveVariable() {
        String varName = "test";
        Variable.addVariable(varName);
        Variable.removeVariable(varName);
        int expectedSize = 0;
        int actualSize = Variable.variableList.size();
        assertEquals(expectedSize, actualSize);
    }
    
    @Test
    public void testGetVariableValue() {
        String varName = "test";
        Variable.addVariable(varName);
        int expectedValue = 0;
        int actualValue = Variable.getVariableValue(varName);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testSetVariableValue() {
        String varName = "test";
        int varValue = 10;
        Variable.addVariable(varName);
        Variable.setVariableValue(varName, varValue);
        int expectedValue = varValue;
        int actualValue = Variable.getVariableValue(varName);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testFindVariable() {
        String varName = "test";
        Variable.addVariable(varName);
        int expectedIndex = 0;
        int actualIndex = Variable.findVariable(varName);
        assertEquals(expectedIndex, actualIndex);
    }
}
