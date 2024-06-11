import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VariableTest {

    private Variable variable;

    @Before
    public void setUp() {
        Variable.variableList.clear();
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 100);
        variable = new Variable("var3");
    }

    // Test getValue method
    @Test
    public void testGetValue() {
        assertEquals(0, variable.getValue());
    }

    // Test setValue method
    @Test
    public void testSetValue() {
        variable.setValue(50);
        assertEquals(50, variable.getValue());
    }

    // Test getName method
    @Test
    public void testGetName() {
        assertEquals("var3", variable.getName());
    }

    // Test addVariable method for adding a new variable
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var4"));
        assertEquals(4, Variable.variableList.size());
    }

    // Test addVariable method for adding an existing variable
    @Test
    public void testAddVariableExisting() {
        assertFalse(Variable.addVariable("var1"));
        assertEquals(3, Variable.variableList.size());
    }

    // Test removeVariable method for removing an existing variable
    @Test
    public void testRemoveVariable() {
        Variable.removeVariable("var2");
        assertEquals(2, Variable.variableList.size());
    }

    // Test removeVariable method for removing a non-existing variable
    @Test
    public void testRemoveVariableNonExisting() {
        Variable.removeVariable("var4");
        assertEquals(3, Variable.variableList.size());
    }

    // Test getVariableValue method for getting a variable value without prefix
    @Test
    public void testGetVariableValue() {
        assertEquals(100, Variable.getVariableValue("var1"));
    }

    // Test getVariableValue method for getting a variable value with "*" prefix
    @Test
    public void testGetVariableValueWithPrefix() {
        assertEquals(100, Variable.getVariableValue("*var1"));
    }

    // Test getVariableValue method for getting a non-existing variable value
    @Test
    public void testGetVariableValueNonExisting() {
        assertEquals(0, Variable.getVariableValue("var4"));
    }

    // Test setVariableValue method for setting a value of an existing variable
    @Test
    public void testSetVariableValue() {
        Variable.setVariableValue("var2", 50);
        assertEquals(50, Variable.getVariableValue("var2"));
    }

    // Test setVariableValue method for setting a value of a non-existing variable
    @Test
    public void testSetVariableValueNonExisting() {
        Variable.setVariableValue("var4", 50);
        assertEquals(0, Variable.getVariableValue("var4"));
    }

    // Test findVariable method for finding an existing variable
    @Test
    public void testFindVariable() {
        assertEquals(1, Variable.findVariable("var2"));
    }

    // Test findVariable method for finding a non-existing variable
    @Test
    public void testFindVariableNonExisting() {
        assertEquals(-1, Variable.findVariable("var4"));
    }

}