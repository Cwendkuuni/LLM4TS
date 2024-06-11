import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VariableTest {
    
    @Before
    public void setup() {
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        assertEquals(0, Variable.getVariableValue("var1"));
        
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        Variable.addVariable("&var1");
        Variable.setVariableValue("var1", 20);
        assertEquals(20, Variable.getVariableValue("*var1"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 30);
        assertEquals(30, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
        
        Variable.addVariable("var2");
        assertEquals(1, Variable.findVariable("var2"));
        
        Variable.addVariable("&var1");
        assertEquals(30, Variable.findVariable("&var1"));
    }
}