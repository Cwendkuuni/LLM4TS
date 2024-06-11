import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ChatGPT_VariableTest {
    
    @Before
    public void setUp() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1")); // Attempting to add the same variable again should return false
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        // Test removing variables
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1")); // Check if var1 is removed
        assertEquals(1, Variable.variableList.size()); // Ensure only 1 variable is left
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2")); // Check if var2 is removed
        assertEquals(0, Variable.variableList.size()); // Ensure no variables remain
    }

    @Test
    public void testGetVariableValue() {
        // Test getting variable values
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        
        // Check if values are retrieved correctly
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // Test edge cases
        assertEquals(1, Variable.getVariableValue("*var1")); // Check wildcard handling
        assertEquals(0, Variable.getVariableValue("*var3")); // Check for unknown variable
    }

    @Test
    public void testSetVariableValue() {
        // Test setting variable values
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        
        // Check if values are set correctly
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
    }

}