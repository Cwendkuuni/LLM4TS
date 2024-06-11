import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class VariableTest {

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("test"));
        assertFalse(Variable.addVariable("test"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("test");
        Variable.removeVariable("test");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("test");
        assertEquals(0, Variable.getVariableValue("test"));
        
        Variable.setVariableValue("test", 5);
        assertEquals(5, Variable.getVariableValue("test"));
        
        Variable.setVariableValue("test", -10);
        assertEquals(-10, Variable.getVariableValue("test"));
        
        assertEquals(0, Variable.getVariableValue("*test"));
        
        Variable.addVariable("*test");
        Variable.setVariableValue("*test", 10);
        assertEquals(10, Variable.getVariableValue("*test"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("test");
        Variable.setVariableValue("test", 5);
        assertEquals(5, Variable.getVariableValue("test"));
        
        Variable.setVariableValue("test", -10);
        assertEquals(-10, Variable.getVariableValue("test"));
        
        Variable.setVariableValue("nonExistentVariable", 2);
        assertEquals(0, Variable.getVariableValue("nonExistentVariable"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("test");
        assertEquals(0, Variable.findVariable("test"));
        
        Variable.setVariableValue("test", 5);
        assertEquals(5, Variable.findVariable("&test"));
        
        assertEquals(-1, Variable.findVariable("nonExistentVariable"));
    }
}