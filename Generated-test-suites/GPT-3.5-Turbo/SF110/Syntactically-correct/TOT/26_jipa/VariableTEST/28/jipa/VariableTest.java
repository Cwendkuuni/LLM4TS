import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        // Typical use case: Add variable that does not exist
        assertTrue(Variable.addVariable("var1"));
        
        // Error scenario: Add variable that already exists
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        // Typical use case: Remove variable that exists
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");
        Variable.removeVariable("var2");
        assertEquals(0, Variable.getVariableValue("var2"));
        
        // Error scenario: Remove variable that does not exist
        Variable.removeVariable("var2");
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testGetVariableValue() {
        // Typical use case: Get value of existing variable
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        // Typical use case: Get value of referenced variable
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 20);
        assertEquals(20, Variable.getVariableValue("*var2"));
        
        // Error scenario: Get value of non-existing variable
        assertEquals(0, Variable.getVariableValue("var3"));
    }

    @Test
    public void testSetVariableValue() {
        // Typical use case: Set value of existing variable
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        // Error scenario: Set value of non-existing variable
        Variable.setVariableValue("var2", 20);
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testFindVariable() {
        // Typical use case: Find existing variable by name
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
        
        // Typical use case: Find existing variable by reference name
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 20);
        assertEquals(20, Variable.findVariable("&var2"));
        
        // Error scenario: Find non-existing variable
        assertEquals(-1, Variable.findVariable("var3"));
    }
}