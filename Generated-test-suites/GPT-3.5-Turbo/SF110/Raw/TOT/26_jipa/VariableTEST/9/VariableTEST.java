import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {
    
    @Test
    public void testAddVariable_success() {
        assertTrue(Variable.addVariable("test"));
    }
    
    @Test
    public void testAddVariable_duplicate() {
        Variable.addVariable("test");
        assertFalse(Variable.addVariable("test"));
        Variable.removeVariable("test");
    }
    
    @Test
    public void testRemoveVariable_success() {
        Variable.addVariable("test");
        Variable.removeVariable("test");
        assertEquals(0, Variable.getVariableValue("test"));
        Variable.addVariable("test");
    }
    
    @Test
    public void testRemoveVariable_invalid() {
        Variable.removeVariable("invalid");
    }
    
    @Test
    public void testGetVariableValue_exists() {
        Variable.addVariable("test");
        Variable.setVariableValue("test", 10);
        assertEquals(10, Variable.getVariableValue("test"));
        Variable.removeVariable("test");
    }
    
    @Test
    public void testGetVariableValue_notExists() {
        assertEquals(0, Variable.getVariableValue("invalid"));
    }
    
    @Test
    public void testGetVariableValue_withReference() {
        Variable.addVariable("test");
        Variable.setVariableValue("test", 10);
        assertEquals(10, Variable.getVariableValue("*test"));
        Variable.removeVariable("test");
    }
    
    @Test
    public void testSetVariableValue_success() {
        Variable.addVariable("test");
        Variable.setVariableValue("test", 10);
        assertEquals(10, Variable.getVariableValue("test"));
        Variable.removeVariable("test");
    }
    
    @Test
    public void testSetVariableValue_invalid() {
        Variable.setVariableValue("invalid", 10);
    }
    
    @Test
    public void testFindVariable_exists() {
        Variable.addVariable("test");
        assertEquals(0, Variable.findVariable("test"));
        Variable.removeVariable("test");
    }
    
    @Test
    public void testFindVariable_notExists() {
        assertEquals(-1, Variable.findVariable("invalid"));
    }
    
    @Test
    public void testFindVariable_withReference() {
        Variable.addVariable("test");
        Variable.setVariableValue("test", 10);
        assertEquals(10, Variable.findVariable("&test"));
        Variable.removeVariable("test");
    }
    
}