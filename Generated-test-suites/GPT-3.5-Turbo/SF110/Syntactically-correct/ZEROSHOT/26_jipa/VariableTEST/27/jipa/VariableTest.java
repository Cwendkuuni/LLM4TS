package jipa;

import org.junit.*;
import java.util.*;

public class VariableTest {
    
    @BeforeClass
    public static void setup() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @Test
    public void testAddVariable() {
        // Test adding a new variable
        assertTrue(Variable.addVariable("var1"));
        
        // Test adding an existing variable
        assertFalse(Variable.addVariable("var1"));
    }
    
    @Test
    public void testRemoveVariable() {
        // Add a variable to the list
        Variable.addVariable("var2");
        
        // Test removing an existing variable
        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());
        
        // Test removing a non-existing variable
        Variable.removeVariable("var3");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        // Add a variable to the list
        Variable.addVariable("var2");
        
        // Test getting the value of an existing variable
        assertEquals(0, Variable.getVariableValue("var2"));
        
        // Test getting the value of a non-existing variable
        assertEquals(0, Variable.getVariableValue("var3"));
    }
    
    @Test
    public void testSetVariableValue() {
        // Add a variable to the list
        Variable.addVariable("var2");
        
        // Test setting the value of an existing variable
        Variable.setVariableValue("var2", 5);
        assertEquals(5, Variable.getVariableValue("var2"));
        
        // Test setting the value of a non-existing variable
        Variable.setVariableValue("var3", 10);
        assertEquals(0, Variable.getVariableValue("var3"));
    }
    
    @Test
    public void testFindVariable() {
        // Add a variable to the list
        Variable.addVariable("var2");
        
        // Test finding an existing variable
        assertEquals(0, Variable.findVariable("var2"));
        
        // Test finding a non-existing variable
        assertEquals(-1, Variable.findVariable("var3"));
    }
    
    @AfterClass
    public static void teardown() {
        Variable.variableList = null;
    }
}