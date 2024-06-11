package jipa;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {
    
    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var2"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        int index = Variable.findVariable("var1");
        assertEquals(0, index);
        
        Variable.addVariable("var2");
        index = Variable.findVariable("var2");
        assertEquals(1, index);
        
        index = Variable.findVariable("var3");
        assertEquals(-1, index);
    }
}