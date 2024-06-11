
package jipa;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class VariableTest {

    @BeforeClass
    public static void setUp() {
        Variable.variableList.clear(); // Clean variable list before each test case
    }

    @Test
    public void testAddVariable_typicalCase() {
        assertTrue(Variable.addVariable("x"));
        assertEquals(1, Variable.variableList.size()); // Variable list should have one element
        assertEquals("x", Variable.variableList.get(0).getName()); // The added variable should have the name "x"
        assertEquals(0, Variable.variableList.get(0).getValue()); // The added variable should have value 0
    }

    @Test
    public void testAddVariable_alreadyExists() {
        Variable.addVariable("x"); // Add "x" once
        assertFalse(Variable.addVariable("x")); // Adding "x" again should return false
        assertEquals(1, Variable.variableList.size()); // Variable list should still have one element
    }

    @Test
    public void testRemoveVariable_typicalCase() {
        Variable.addVariable("x"); // Add "x" once
        Variable.removeVariable("x"); // Remove "x"
        assertEquals(0, Variable.variableList.size()); // Variable list should be empty
    }

    @Test
    public void testRemoveVariable_notExists() {
        Variable.removeVariable("x"); // Removing a non-existing variable should not affect the variable list
        assertEquals(0, Variable.variableList.size()); // Variable list should still be empty
    }

    @Test
    public void testGetVariableValue_typicalCase() {
        Variable.addVariable("x"); // Add "x"
        Variable.setVariableValue("x", 5); // Set "x" value to 5
        assertEquals(5, Variable.getVariableValue("x")); // Variable "x" should have value 5
    }

    @Test
    public void testGetVariableValue_notExists() {
        assertEquals(0, Variable.getVariableValue("x")); // Getting non-existing variable should return 0
    }

    @Test
    public void testGetVariableValue_reference() {
        Variable.addVariable("x"); // Add "x"
        Variable.setVariableValue("x", 2); // Set "x" value to 2
        assertEquals(2, Variable.getVariableValue("*x")); // Getting referenced variable should return its value
    }

    @Test
    public void testSetVariableValue_typicalCase() {
        Variable.addVariable("x"); // Add "x"
        Variable.setVariableValue("x", 10); // Set "x" value to 10
        assertEquals(10, Variable.variableList.get(0).getValue()); // The value of "x" should be 10
    }

    @Test
    public void testSetVariableValue_notExists() {
        Variable.setVariableValue("x", 5); // Setting value of non-existing variable should not affect the variable list
        assertEquals(0, Variable.variableList.size()); // Variable list should still be empty
    }

    @Test
    public void testSetVariableValue_reference() {
        Variable.addVariable("x"); // Add "x"
        Variable.setVariableValue("x", 5); // Set "x" value to 5
        Variable.setVariableValue("*x", 9); // Setting referenced variable should update its value
        assertEquals(9, Variable.variableList.get(0).getValue()); // The value of "x" should be 9
    }

    @Test
    public void testFindVariable_typicalCase() {
        Variable.addVariable("x"); // Add "x"
        assertEquals(0, Variable.findVariable("x")); // "x" should be found at index 0
    }

    @Test
    public void testFindVariable_notExists() {
        assertEquals(-1, Variable.findVariable("x")); // Finding a non-existing variable should return -1
    }

    @Test
    public void testFindVariable_reference() {
        Variable.addVariable("x"); // Add "x"
        Variable.setVariableValue("x", 8); // Set "x" value to 8
        assertEquals(8, Variable.findVariable("&x")); // Finding referenced variable should return its value
    }
}

