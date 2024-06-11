package jipa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class VariableTest {
    
    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @After
    public void tearDown() {
        Variable.variableList = null;
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
        // Test removing an existing variable
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
        
        // Test removing a non-existing variable
        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        // Test getting the value of an existing variable
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        // Test getting the value of a non-existing variable
        assertEquals(0, Variable.getVariableValue("var2"));
    }
    
    @Test
    public void testSetVariableValue() {
        // Test setting the value of an existing variable
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        // Test setting the value of a non-existing variable
        Variable.setVariableValue("var2", 20);
        assertEquals(0, Variable.getVariableValue("var2"));
    }
    
    @Test
    public void testFindVariable() {
        // Test finding an existing variable
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
        
        // Test finding a non-existing variable
        assertEquals(-1, Variable.findVariable("var2"));
    }
}