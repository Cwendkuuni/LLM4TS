import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        // Typical use case
        assertTrue(Variable.addVariable("var1"));
        
        // Edge case: Trying to add an existing variable
        assertFalse(Variable.addVariable("var1"));
        
        // Error scenario: Adding null variable
        assertFalse(Variable.addVariable(null));
    }
    
    @Test
    public void testRemoveVariable() {
        // Typical use case
        Variable.addVariable("var2");
        Variable.addVariable("var3");
        Variable.removeVariable("var2");
        assertEquals(0, Variable.getVariableValue("var2"));
        
        // Edge case: Trying to remove a non-existing variable
        Variable.removeVariable("var2");
        assertEquals(0, Variable.getVariableValue("var2"));
        
        // Error scenario: Removing null variable
        Variable.removeVariable(null);
        assertEquals(0, Variable.getVariableValue(null));
    }
    
    @Test
    public void testGetVariableValue() {
        // Typical use case
        Variable.addVariable("var4");
        Variable.setVariableValue("var4", 10);
        assertEquals(10, Variable.getVariableValue("var4"));
        
        // Edge case: Variable name starts with *
        Variable.addVariable("var5");
        Variable.setVariableValue("var5", 15);
        assertEquals(15, Variable.getVariableValue("*var5"));
        
        // Error scenario: Variable does not exist
        assertEquals(0, Variable.getVariableValue("var6"));
        
        // Error scenario: Variable name is null
        assertEquals(0, Variable.getVariableValue(null));
    }
    
    @Test
    public void testSetVariableValue() {
        // Typical use case
        Variable.addVariable("var7");
        Variable.setVariableValue("var7", 20);
        assertEquals(20, Variable.getVariableValue("var7"));
        
        // Error scenario: Variable does not exist
        Variable.setVariableValue("var8", 25);
        assertEquals(0, Variable.getVariableValue("var8"));
        
        // Error scenario: Variable name is null
        Variable.setVariableValue(null, 30);
        assertEquals(0, Variable.getVariableValue(null));
    }
    
    @Test
    public void testFindVariable() {
        // Typical use case
        Variable.addVariable("var9");
        assertEquals(0, Variable.findVariable("var9"));
        
        // Edge case: Variable name starts with &
        Variable.addVariable("var10");
        Variable.setVariableValue("var10", 35);
        assertEquals(35, Variable.findVariable("&var10"));
        
        // Error scenario: Variable does not exist
        assertEquals(-1, Variable.findVariable("var11"));
        
        // Error scenario: Variable name is null
        assertEquals(-1, Variable.findVariable(null));
    }
}