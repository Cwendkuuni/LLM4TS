import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VariableTest {
    
    @Test
    public void testAddVariable_success() {
        // Typical use case: Adding a variable for the first time
        assertTrue("Adding variable failed", Variable.addVariable("x"));
    }
    
    @Test
    public void testAddVariable_existingVariable() {
        // Edge case: Trying to add an existing variable
        assertFalse("Adding existing variable should fail", Variable.addVariable("x"));
    }
    
    @Test
    public void testRemoveVariable_success()  {
        // Typical use case: Removing an existing variable
        Variable.addVariable("x");
        Variable.removeVariable("x");
        assertEquals("Removing variable failed", 0, Variable.getVariableValue("x"));
    }
    
    @Test
    public void testRemoveVariable_nonExistingVariable() {
        // Edge case: Trying to remove a non-existing variable
        Variable.removeVariable("y");
    }
    
    @Test
    public void testGetVariableValue_normalVariable() {
        // Typical use case: Getting value of a normal variable
        Variable.addVariable("x");
        assertEquals("Getting variable value failed", 0, Variable.getVariableValue("x"));
    }
    
    @Test
    public void testGetVariableValue_referencedVariable() {
        // Typical use case: Getting value of a referenced variable
        Variable.addVariable("y");
        Variable.setVariableValue("y", 5);
        assertEquals("Getting referenced variable value failed", 5, Variable.getVariableValue("*y"));
    }
    
    @Test
    public void testGetVariableValue_nonExistingVariable() {
        // Edge case: Trying to get value of a non-existing variable
        assertEquals("Getting non-existing variable value should return 0", 0, Variable.getVariableValue("z"));
    }
    
    @Test
    public void testSetVariableValue_success() {
        // Typical use case: Setting value of an existing variable
        Variable.addVariable("x");
        Variable.setVariableValue("x", 10);
        assertEquals("Setting variable value failed", 10, Variable.getVariableValue("x"));
    }
    
    @Test
    public void testSetVariableValue_nonExistingVariable() {
        // Edge case: Trying to set value of a non-existing variable
        Variable.setVariableValue("y", 15);
    }
    
    @Test
    public void testSetVariableValue_referencedVariable() {
        // Error scenario: Trying to set value of a referenced variable
        Variable.addVariable("z");
        Variable.setVariableValue("z", 20);
        Variable.setVariableValue("*z", 25);
    }
    
    @Test
    public void testFindVariable_existingVariable() {
        // Typical use case: Finding an existing variable
        Variable.addVariable("a");
        assertEquals("Finding existing variable failed", 0, Variable.findVariable("a"));
    }
    
    @Test
    public void testFindVariable_referencedVariable() {
        // Error scenario: Trying to find a referenced variable
        Variable.addVariable("b");
        Variable.setVariableValue("b", 30);
        assertEquals("Finding referenced variable should fail", -1, Variable.findVariable("*b"));
    }
    
    @Test
    public void testFindVariable_nonExistingVariable() {
        // Edge case: Trying to find a non-existing variable
        assertEquals("Finding non-existing variable should return -1", -1, Variable.findVariable("c"));
    }
}