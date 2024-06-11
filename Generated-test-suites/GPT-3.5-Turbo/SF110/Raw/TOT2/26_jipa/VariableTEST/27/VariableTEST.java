import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VariableTest {
    @Before
    public void setUp() {
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("x"));
        assertFalse(Variable.addVariable("x"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("x");
        Variable.removeVariable("x");
        assertEquals(0, Variable.getVariableValue("x"));
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("x");
        assertEquals(0, Variable.getVariableValue("x"));
        Variable.setVariableValue("x", 100);
        assertEquals(100, Variable.getVariableValue("x"));
        assertEquals(0, Variable.getVariableValue("*x"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("x");
        Variable.setVariableValue("x", 50);
        assertEquals(50, Variable.getVariableValue("x"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("x");
        Variable.addVariable("y");
        assertEquals(0, Variable.findVariable("x"));
        assertEquals(1, Variable.findVariable("y"));
        assertEquals(0, Variable.findVariable("&x"));
        assertEquals(50, Variable.findVariable("*x"));
    }
}