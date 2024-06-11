import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class VariableTest {
    
    @Test
    public void testAddVariable() {
        Variable.addVariable("var1");
        assertTrue(Variable.variableList.size() == 1);
        assertEquals("var1", Variable.variableList.get(0).getName());
        assertEquals(0, Variable.variableList.get(0).getValue());
        
        assertFalse(Variable.addVariable("var1"));
        
        Variable.addVariable("var2");
        assertTrue(Variable.variableList.size() == 2);
        assertEquals("var2", Variable.variableList.get(1).getName());
        assertEquals(0, Variable.variableList.get(1).getValue());
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        Variable.removeVariable("var1");
        assertTrue(Variable.variableList.size() == 1);
        assertEquals("var2", Variable.variableList.get(0).getName());
        assertEquals(0, Variable.variableList.get(0).getValue());
        
        Variable.removeVariable("var3");
        assertTrue(Variable.variableList.size() == 1);
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        
        int value = Variable.getVariableValue("var1");
        assertEquals(10, value);
        
        value = Variable.getVariableValue("*var1");
        assertEquals(10, value);
        
        value = Variable.getVariableValue("var2");
        assertEquals(0, value);
        
        value = Variable.getVariableValue("*var2");
        assertEquals(0, value);
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.variableList.get(0).getValue());
        
        Variable.setVariableValue("var2", 20);
        assertEquals(0, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        
        int index = Variable.findVariable("var1");
        assertTrue(index == 0);
        
        index = Variable.findVariable("*var1");
        assertTrue(index == 10);
        
        index = Variable.findVariable("var2");
        assertTrue(index == -1);
    }
}