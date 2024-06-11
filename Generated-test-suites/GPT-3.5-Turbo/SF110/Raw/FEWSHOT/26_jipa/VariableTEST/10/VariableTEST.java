
package jipa;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

public class VariableTest {
    private List<Variable> variableList;

    @Before
    public void setUp() {
        variableList = new ArrayList<>();
    }

    @Test
    public void testAddVariable() {
        assertFalse(Variable.addVariable(""));
        assertFalse(Variable.addVariable(null));
        
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        Variable.removeVariable("var3");
        assertEquals(2, Variable.variableList.size());
        
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertEquals("var2", Variable.variableList.get(0).getName());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        assertEquals(0, Variable.getVariableValue("var3"));
        assertEquals(0, Variable.getVariableValue(""));
        
        assertEquals(0, Variable.getVariableValue("*var1"));
        assertEquals(0, Variable.getVariableValue("var1"));
        
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        Variable.setVariableValue("var3", 5);
        assertEquals(0, Variable.variableList.get(0).getValue());
        
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.variableList.get(0).getValue());
        
        Variable.setVariableValue("var2", 10);
        assertEquals(10, Variable.variableList.get(1).getValue());
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        
        assertEquals(-1, Variable.findVariable("var3"));
    }
}
