import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {
    
    @Test
    public void testAddVariable() {
        assertFalse(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var2"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var3");
        Variable.removeVariable("var3");
        assertEquals(0, Variable.getVariableValue("var3"));
    }
    
    @Test
    public void testGetVariableValue() {
        assertEquals(0, Variable.getVariableValue("var4"));
        Variable.addVariable("var4");
        assertEquals(0, Variable.getVariableValue("var4"));
        Variable.setVariableValue("var4", 10);
        assertEquals(10, Variable.getVariableValue("var4"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var5");
        Variable.setVariableValue("var5", 5);
        assertEquals(5, Variable.getVariableValue("var5"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var6");
        assertEquals(0, Variable.findVariable("var6"));
        
        Variable.setVariableValue("var6", 15);
        assertEquals(15, Variable.findVariable("&var6"));
    }
}