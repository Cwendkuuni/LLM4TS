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
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        assertEquals(1, Variable.variableList.size());
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        assertEquals(1, Variable.getVariableValue("*var1"));
        assertEquals(0, Variable.getVariableValue("*var3"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // Testing exception handling
        try {
            Variable.getVariableValue(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Variable name is null", e.getMessage());
        }
    }

    // Edge case testing
    @Test
    public void testEdgeCases() {
        // Adding and removing multiple variables
        assertTrue(Variable.addVariable(""));
        assertTrue(Variable.addVariable(" "));
        assertTrue(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("$%#@"));

        Variable.removeVariable("");
        Variable.removeVariable(" ");
        Variable.removeVariable("var1");
        Variable.removeVariable("$%#@");

        assertEquals(0, Variable.variableList.size());
        
        // Setting variable value to negative number
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", -5);
        assertEquals(-5, Variable.getVariableValue("var1"));
    }
}