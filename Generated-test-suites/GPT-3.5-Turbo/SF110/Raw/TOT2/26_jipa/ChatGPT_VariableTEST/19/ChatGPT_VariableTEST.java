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
        // Typical use case
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var2"));
        
        // Edge case - Empty variable name
        assertFalse(Variable.addVariable(""));
        
        // Edge case - Maximum length variable name
        assertTrue(Variable.addVariable("verylongvariablename1234567890"));
    }

    @Test
    public void testRemoveVariable() {
        // Typical use case
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        assertEquals(1, Variable.variableList.size());
        
        // Edge case - Removing non-existent variable
        Variable.removeVariable("var3");
        assertEquals(1, Variable.variableList.size());
        
        // Error scenario - Removing null variable
        assertFalse(Variable.removeVariable(null));
    }

    @Test
    public void testGetVariableValue() {
        // Typical use case
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // Edge case - Getting value for invalid variable name
        assertEquals(0, Variable.getVariableValue("invalidVar"));
        
        // Error scenario - Getting value for null variable name
        assertEquals(0, Variable.getVariableValue(null));
    }

    @Test
    public void testSetVariableValue() {
        // Typical use case
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // Edge case - Setting value for non-existent variable
        assertFalse(Variable.setVariableValue("var3", 3));
        
        // Error scenario - Setting value for null variable name
        assertFalse(Variable.setVariableValue(null, 3));
    }

}