
import org.junit.*;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("test")); // Adding a new variable should return true
        assertFalse(Variable.addVariable("test")); // Adding the same variable again should return false
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("test");
        Variable.removeVariable("test"); // Removing an existing variable should not throw any exceptions
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("test");
        assertEquals(0, Variable.getVariableValue("test")); // Variable value should be 0 by default
        
        Variable.setVariableValue("test", 10);
        assertEquals(10, Variable.getVariableValue("test")); // After setting the variable value, it should return the new value
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("test");
        Variable.setVariableValue("test", 10); // Setting the variable value should not throw any exceptions
        
        assertEquals(10, Variable.getVariableValue("test")); // After setting the variable value, it should return the new value
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("test");
        assertEquals(0, Variable.findVariable("test")); // Index of existing variable should be 0
        
        assertEquals(-1, Variable.findVariable("nonexistent")); // Index of nonexistent variable should be -1
    }
}

