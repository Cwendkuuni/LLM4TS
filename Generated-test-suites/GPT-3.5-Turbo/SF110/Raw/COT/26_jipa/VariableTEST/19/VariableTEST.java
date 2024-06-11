
import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        // Test case - Add a variable that doesn't already exist
        assertTrue(Variable.addVariable("var1"));
        
        // Test case - Add a variable that already exists
        assertFalse(Variable.addVariable("var1"));
    }
    
    @Test
    public void testRemoveVariable() {
        // Test case - Remove a variable that exists
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
        
        // Test case - Remove a variable that doesn't exist
        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        // Test case - Get the value of a variable that exists
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        // Test case - Get the value of a variable that doesn't exist
        assertEquals(0, Variable.getVariableValue("var2"));
        
        // Test case - Get the value of a variable using a reference
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("*var1"));
    }
    
    @Test
    public void testSetVariableValue() {
        // Test case - Set the value of a variable that exists
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.variableList.get(0).getValue());
        
        // Test case - Set the value of a variable that doesn't exist
        Variable.setVariableValue("var2", 20);
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testFindVariable() {
        // Test case - Find a variable that exists
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
        
        // Test case - Find a variable that doesn't exist
        assertEquals(-1, Variable.findVariable("var2"));
        
        // Test case - Find a variable using a reference
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.findVariable("*var1"));
    }
}

