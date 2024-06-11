import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {
    
    @Test
    public void testGetValue() {
        Variable variable = new Variable("test");
        variable.setValue(10);
        assertEquals(10, variable.getValue());
    }
    
    @Test
    public void testSetValue() {
        Variable variable = new Variable("test");
        variable.setValue(10);
        assertEquals(10, variable.getValue());
    }
    
    @Test
    public void testGetName() {
        Variable variable = new Variable("test");
        assertEquals("test", variable.getName());
    }
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("test"));
        assertFalse(Variable.addVariable("test"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("test");
        Variable.removeVariable("test");
        assertFalse(Variable.variableList.contains("test"));
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("test");
        Variable.setVariableValue("test", 10);
        assertEquals(10, Variable.getVariableValue("test"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("test");
        Variable.setVariableValue("test", 10);
        assertEquals(10, Variable.getVariableValue("test"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("test");
        assertEquals(0, Variable.findVariable("test"));
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testEmptyVariableName() {
        assertFalse(Variable.addVariable(""));
        assertEquals(0, Variable.getVariableValue(""));
    }
    
    @Test
    public void testEmptyVariableValue() {
        Variable.setVariableValue("test", 0);
        assertEquals(0, Variable.getVariableValue("test"));
    }
    
    @Test
    public void testVariableNameWithSpecialCharacter() {
        Variable.addVariable("*test");
        assertEquals(0, Variable.getVariableValue("*test"));
    }
    
    @Test
    public void testReferenceVariableName() {
        Variable.addVariable("test");
        Variable.setVariableValue("test", 10);
        assertEquals(10, Variable.getVariableValue("&test"));
    }
    
    @Test
    public void testNonExistentVariable() {
        assertEquals(0, Variable.getVariableValue("nonexistent"));
        Variable.setVariableValue("nonexistent", 10);
        assertEquals(0, Variable.getVariableValue("nonexistent"));
        Variable.removeVariable("nonexistent");
    }
}