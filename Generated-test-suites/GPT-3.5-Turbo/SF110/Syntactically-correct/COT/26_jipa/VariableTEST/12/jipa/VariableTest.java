

package jipa;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class VariableTest {

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
        int expectedValue = 10;
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
        boolean actualResult = Variable.addVariable(varName);
        assertTrue(actualResult);
        int expectedSize = 1;
        int actualSize = Variable.variableList.size();
        assertEquals(expectedSize, actualSize);
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
        Variable.addVariable(varName);
        int varValue = 10;
        Variable.setVariableValue(varName, varValue);
        int expectedValue = 10;
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
