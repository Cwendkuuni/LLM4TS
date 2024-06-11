import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        // Typical use case
        assertTrue(Variable.addVariable("var1"));
        
        // Edge case: adding an existing variable should return false
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        // Typical use case
        Variable.addVariable("var2");
        Variable.removeVariable("var2");
        assertNull(Variable.getVariableValue("var2"));
        
        // Edge case: trying to remove a non-existent variable
        Variable.removeVariable("nonExistentVar");
    }

    @Test
    public void testGetVariableValue() {
        // Typical use case
        Variable.addVariable("var3");
        Variable.setVariableValue("var3", 100);
        assertEquals(100, Variable.getVariableValue("var3"));
        
        // Edge case: getting the value of a variable that does not exist in the list
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
    }

    @Test
    public void testSetVariableValue() {
        // Typical use case
        Variable.addVariable("var4");
        Variable.setVariableValue("var4", 200);
        assertEquals(200, Variable.getVariableValue("var4"));
        
        // Edge case: setting value for a variable that does not exist
        Variable.setVariableValue("nonExistentVar", 300);
    }

    @Test
    public void testFindVariable() {
        // Typical use case
        Variable.addVariable("var5");
        assertEquals(0, Variable.findVariable("var5"));
        
        // Edge case: finding a non-existent variable
        assertEquals(-1, Variable.findVariable("nonExistentVar"));
    }

}