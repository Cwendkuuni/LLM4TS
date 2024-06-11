import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class VariableTest {
    
    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
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
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("*var1"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 15);
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(15, Variable.findVariable("&var1"));
        assertEquals(-1, Variable.findVariable("var2"));
    }
    
    @Test
    public void testEdgeCases() {
        // Edge case 1: Adding variable with special character
        assertTrue(Variable.addVariable("&var2"));
        
        // Edge case 2: Getting variable value with reference
        Variable.setVariableValue("&var2", 20);
        assertEquals(20, Variable.getVariableValue("&var2"));
    }
    
    @Test
    public void testExceptions() {
        // Exception handling 1: Removing non-existent variable
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
        
        // Exception handling 2: Setting value to non-existent variable
        Variable.setVariableValue("var2", 10);
        assertEquals(0, Variable.variableList.size());
    }
    
}