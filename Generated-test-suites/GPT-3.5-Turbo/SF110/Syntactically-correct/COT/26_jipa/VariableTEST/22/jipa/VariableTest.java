package jipa;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {
    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        boolean result1 = Variable.addVariable("x");
        assertTrue(result1);
        
        boolean result2 = Variable.addVariable("x");
        assertFalse(result2);
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("x");
        
        Variable.removeVariable("x");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("x");
        Variable.setVariableValue("x", 5);
        
        int result1 = Variable.getVariableValue("x");
        assertEquals(5, result1);
        
        int result2 = Variable.getVariableValue("*x");
        assertEquals(5, result2);
        
        int result3 = Variable.getVariableValue("y");
        assertEquals(0, result3);
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("x");
        
        Variable.setVariableValue("x", 5);
        int result = Variable.getVariableValue("x");
        assertEquals(5, result);
    }
    
    @Test
    public void testFindVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("x");
        
        int result1 = Variable.findVariable("x");
        assertEquals(0, result1);
        
        int result2 = Variable.findVariable("&x");
        assertEquals(0, result2);
        
        int result3 = Variable.findVariable("y");
        assertEquals(-1, result3);
    }
}