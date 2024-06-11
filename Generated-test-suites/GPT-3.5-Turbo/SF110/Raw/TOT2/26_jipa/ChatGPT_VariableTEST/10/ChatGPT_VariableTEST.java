import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable() {
        // Test adding a variable
        assertTrue(Variable.addVariable("var1"));
        
        // Test adding the same variable again
        assertFalse(Variable.addVariable("var1"));
        
        // Test adding another variable
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        // Test removing a variable
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        assertEquals(1, Variable.variableList.size());
        
        // Test removing the second variable
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        // Test getting variable value after setting it
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // Test getting value for a non-existing variable
        assertEquals(0, Variable.getVariableValue("*var3"));
        
        // Test getting value for a variable with special character
        assertEquals(1, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue() {
        // Test setting variable value
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
    }
}