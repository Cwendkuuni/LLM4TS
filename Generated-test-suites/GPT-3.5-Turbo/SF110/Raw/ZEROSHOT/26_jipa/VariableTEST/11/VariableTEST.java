import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        // Test adding a new variable
        assertTrue(Variable.addVariable("var1"));
        // Test adding an existing variable
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        // Add a variable
        Variable.addVariable("var1");
        // Test removing the variable
        Variable.removeVariable("var1");
        assertEquals(0, Variable.getVariableValue("var1"));
    }

    @Test
    public void testGetVariableValue() {
        // Add a variable
        Variable.addVariable("var1");
        // Set value of the variable
        Variable.setVariableValue("var1", 10);
        // Test getting the variable value
        assertEquals(10, Variable.getVariableValue("var1"));
        // Test getting the value of a nonexistent variable
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testSetVariableValue() {
        // Add a variable
        Variable.addVariable("var1");
        // Set value of the variable
        Variable.setVariableValue("var1", 10);
        // Test setting the variable value
        assertEquals(10, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testFindVariable() {
        // Add a variable
        Variable.addVariable("var1");
        // Test finding the variable by name
        assertEquals(0, Variable.findVariable("var1"));
        // Test finding the variable by reference
        assertEquals(10, Variable.findVariable("*var1"));
        // Test finding a nonexistent variable
        assertEquals(-1, Variable.findVariable("var2"));
    }
}