import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class VariableTest {
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1")); // Adding a new variable should return true
        assertFalse(Variable.addVariable("var1")); // Adding an existing variable should return false
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        Variable.removeVariable("var1"); // Removing an existing variable should remove it from the list
        assertFalse(Variable.variableList.contains("var1")); // Verify that the variable was removed
        
        Variable.removeVariable("var3"); // Removing a non-existing variable should not cause any effect
        assertTrue(Variable.variableList.contains("var2")); // Verify that the list remains unchanged
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        
        assertEquals(5, Variable.getVariableValue("var1")); // Getting the value of an existing variable should return the correct value
        assertEquals(0, Variable.getVariableValue("var2")); // Getting the value of a non-existing variable should return 0
        assertEquals(5, Variable.getVariableValue("*var1")); // Getting the value of a referenced variable should return the referenced variable's value
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        
        Variable.setVariableValue("var1", 5); // Setting the value of an existing variable should update its value
        assertEquals(5, Variable.getVariableValue("var1")); // Verify that the value was updated
        
        Variable.setVariableValue("var2", 10); // Setting the value of a non-existing variable should not cause any effect
        assertEquals(0, Variable.getVariableValue("var2")); // Verify that the list remains unchanged
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("&var1", 5);
        
        assertEquals(0, Variable.findVariable("var1")); // Finding an existing variable should return its index
        assertEquals(5, Variable.findVariable("&var1")); // Finding a referenced variable should return its value
        assertEquals(-1, Variable.findVariable("var2")); // Finding a non-existing variable should return -1
    }

}