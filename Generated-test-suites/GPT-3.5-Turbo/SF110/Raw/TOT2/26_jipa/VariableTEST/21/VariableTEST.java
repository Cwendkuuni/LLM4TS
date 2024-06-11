import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1")); // Testing duplicate variable addition
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        Variable.removeVariable("var1");
        assertEquals(0, Variable.getVariableValue("var1")); // Testing removal of variable
        assertEquals(-1, Variable.findVariable("var1")); // Testing variable does not exist after removal
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        
        assertEquals(5, Variable.getVariableValue("var1")); // Testing retrieval of variable value
        assertEquals(0, Variable.getVariableValue("var2")); // Testing non-existing variable
        assertEquals(5, Variable.getVariableValue("*var1")); // Testing reference variable
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        
        assertEquals(10, Variable.getVariableValue("var1")); // Testing setting variable value
        Variable.setVariableValue("var2", 15); // Testing setting value to non-existing variable
        assertEquals(0, Variable.getVariableValue("var2")); // Confirming non-existing variable remains unchanged
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 20);
        
        assertEquals(0, Variable.findVariable("var1")); // Testing finding variable by name
        assertEquals(20, Variable.findVariable("&var1")); // Testing finding variable value by reference
        assertEquals(-1, Variable.findVariable("var2")); // Testing non-existing variable
    }
}