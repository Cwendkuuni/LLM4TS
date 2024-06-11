import static org.junit.Assert.*;
import org.junit.Test;

public class VariableTest {
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        int value = Variable.getVariableValue("var1");
        assertEquals(10, value);
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 20);
        int value = Variable.getVariableValue("var1");
        assertEquals(20, value);
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        int index = Variable.findVariable("var1");
        assertEquals(0, index);
    }
    
    @Test
    public void testFindVariableWithReference() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 30);
        int value = Variable.findVariable("&var1");
        assertEquals(30, value);
    }
    
}