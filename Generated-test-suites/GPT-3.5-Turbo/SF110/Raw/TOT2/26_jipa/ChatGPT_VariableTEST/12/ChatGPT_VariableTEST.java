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
        assertTrue(Variable.addVariable("var1")); // Typical use case
        assertFalse(Variable.addVariable("var1")); // Edge case: Adding duplicate variable
        assertTrue(Variable.addVariable("var2")); // Typical use case
        assertFalse(Variable.addVariable(null)); // Error scenario: Adding null variable
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1")); // Typical use case
        assertEquals(1, Variable.variableList.size());
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2")); // Typical use case
        assertEquals(0, Variable.variableList.size());
        Variable.removeVariable("var3"); // Edge case: Removing non-existent variable
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1")); // Typical use case
        assertEquals(2, Variable.getVariableValue("var2")); // Typical use case
        assertEquals(0, Variable.getVariableValue("var3")); // Edge case: Getting value of non-existent variable
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1")); // Typical use case
        assertEquals(2, Variable.getVariableValue("var2")); // Typical use case
        Variable.setVariableValue("var3", 3); // Edge case: Setting value for non-existent variable
        assertEquals(0, Variable.getVariableValue("var3"));
    }

}