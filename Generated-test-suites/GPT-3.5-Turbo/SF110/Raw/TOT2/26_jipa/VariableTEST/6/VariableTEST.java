package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class VariableTest {
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("testVar1"));
        assertFalse(Variable.addVariable("testVar1"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("testVar2");
        Variable.addVariable("testVar3");
        
        Variable.removeVariable("testVar2");
        
        assertEquals(1, Variable.variableList.size());
        assertEquals("testVar3", Variable.variableList.get(0).getName());
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("testVar4");
        Variable.setVariableValue("testVar4", 5);
        
        assertEquals(5, Variable.getVariableValue("testVar4"));
        assertEquals(0, Variable.getVariableValue("testVar5"));
        assertEquals(5, Variable.getVariableValue("*testVar4"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("testVar6");
        Variable.setVariableValue("testVar6", 10);
        
        assertEquals(10, Variable.getVariableValue("testVar6"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("testVar7");
        
        assertEquals(0, Variable.findVariable("testVar7"));
        assertEquals(-1, Variable.findVariable("testVar8"));
    }
    
    @Test
    public void testBoundaryCases() {
        // Edge case: testing when variable name is an empty string
        assertFalse(Variable.addVariable(""));
    }
    
    // Add more edge cases and error scenario tests here
    
}