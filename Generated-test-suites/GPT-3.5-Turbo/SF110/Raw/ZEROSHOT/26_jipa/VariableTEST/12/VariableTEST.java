import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.variableList.clear();
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
        assertTrue(Variable.variableList.size() == 1);
    }

    @Test
    public void testRemoveVariable() {
        Variable.variableList.clear();
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        assertTrue(Variable.variableList.contains("var2"));
        assertTrue(Variable.variableList.size() == 1);
        
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        assertTrue(Variable.variableList.size() == 0);
    }

    @Test
    public void testGetVariableValue() {
        Variable.variableList.clear();
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        
        int value = Variable.getVariableValue("var1");
        assertEquals(10, value);
        
        value = Variable.getVariableValue("*var1");
        assertEquals(0, value);
        
        value = Variable.getVariableValue("var2");
        assertEquals(0, value);
    }

    @Test
    public void testSetVariableValue() {
        Variable.variableList.clear();
        Variable.addVariable("var1");
        
        Variable.setVariableValue("var1", 10);
        int value = Variable.getVariableValue("var1");
        assertEquals(10, value);
        
        Variable.setVariableValue("var2", 20);
        value = Variable.getVariableValue("var2");
        assertEquals(0, value);
    }

    @Test
    public void testFindVariable() {
        Variable.variableList.clear();
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        int var1Index = Variable.findVariable("var1");
        assertEquals(0, var1Index);
        
        int var2Index = Variable.findVariable("var2");
        assertEquals(1, var2Index);
        
        int var3Index = Variable.findVariable("var3");
        assertEquals(-1, var3Index);
        
        int var1Value = Variable.findVariable("*var1");
        assertEquals(0, var1Value);
    }

}