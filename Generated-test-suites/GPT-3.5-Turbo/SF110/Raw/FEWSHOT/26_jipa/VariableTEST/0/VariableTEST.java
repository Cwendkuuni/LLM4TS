package jipa;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class VariableTest {
    
    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<>();
    }
    
    @Test
    public void testAddVariable() {
        // Test adding new variable
        assertTrue(Variable.addVariable("var1"));
        
        // Test adding existing variable
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        // Add a variable to the list
        Variable.addVariable("var1");
        
        // Test removing existing variable
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
        
        // Test removing non-existing variable
        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        // Add a variable to the list
        Variable.addVariable("var1");
        
        // Test getting value of existing variable
        assertEquals(0, Variable.getVariableValue("var1"));
        
        // Test getting value of non-existing variable
        assertEquals(0, Variable.getVariableValue("var2"));
        
        // Test getting value of referenced variable
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue() {
        // Add a variable to the list
        Variable.addVariable("var1");
        
        // Test setting value of existing variable
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        
        // Test setting value of non-existing variable
        Variable.setVariableValue("var2", 10);
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testFindVariable() {
        // Add variables to the list
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        // Test finding existing variable
        assertEquals(0, Variable.findVariable("var1"));
        
        // Test finding non-existing variable
        assertEquals(-1, Variable.findVariable("var3"));
        
        // Test finding referenced variable
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.findVariable("&var1"));
    }
    
}