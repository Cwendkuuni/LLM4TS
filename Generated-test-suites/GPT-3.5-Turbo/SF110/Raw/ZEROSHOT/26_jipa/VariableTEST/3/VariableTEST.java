package jipa;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        // Test adding a variable for the first time
        assertTrue(Variable.addVariable("var1"));

        // Test adding an existing variable
        assertFalse(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        // Remove a variable that exists
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains(new Variable("var1")));

        // Remove a variable that doesn't exist
        Variable.removeVariable("var2");
    }

    @Test
    public void testGetVariableValue() {
        // Get the value of a variable that exists
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));

        // Get the value of a variable that doesn't exist
        assertEquals(0, Variable.getVariableValue("var2"));

        // Get the value of a reference variable
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 10);
        assertEquals(10, Variable.getVariableValue("*var2"));
    }

    @Test
    public void testSetVariableValue() {
        // Set the value of a variable that exists
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));

        // Set the value of a variable that doesn't exist
        Variable.setVariableValue("var2", 10);
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testFindVariable() {
        // Find a variable that exists
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));

        // Find a variable that doesn't exist
        assertEquals(-1, Variable.findVariable("var2"));

        // Find a reference variable
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 10);
        assertEquals(10, Variable.findVariable("*var2"));
    }
}