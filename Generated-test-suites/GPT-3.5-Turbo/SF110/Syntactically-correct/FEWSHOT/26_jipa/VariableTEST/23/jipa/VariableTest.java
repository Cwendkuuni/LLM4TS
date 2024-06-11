import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class VariableTest {
    
    @BeforeClass
    public static void setUp() {
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var2"));
        assertFalse(Variable.addVariable("var1"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        Variable.removeVariable("var2");
        assertTrue(Variable.variableList.isEmpty());
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        
        Variable.setVariableValue("var1", 10);
        
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        
        Variable.setVariableValue("var2", 20);
        
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(20, Variable.getVariableValue("var2"));
        
        assertEquals(10, Variable.getVariableValue("*var1"));
        assertEquals(20, Variable.getVariableValue("*var2"));
        
        assertEquals(0, Variable.getVariableValue("*var3"));
        
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);
        
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(20, Variable.getVariableValue("var2"));
        
        Variable.setVariableValue("var1", 5);
        Variable.setVariableValue("var2", 15);
        
        assertEquals(5, Variable.getVariableValue("var1"));
        assertEquals(15, Variable.getVariableValue("var2"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
    }
    
    @Test
    public void testFindVariableWithReference() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);
        
        assertEquals(10, Variable.findVariable("*var1"));
        assertEquals(20, Variable.findVariable("*var2"));
    }
    
    @Test
    public void testFindVariableWithInvalidName() {
        assertNull(Variable.findVariable("var3"));
    }
}