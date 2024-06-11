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
        // Typical use case
        assertTrue(Variable.addVariable("var1"));
        // Duplicate variable
        assertFalse(Variable.addVariable("var1"));
        // Adding a new variable
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        // Removing an existing variable
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        assertEquals(1, Variable.variableList.size());
        // Removing the only variable
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        // Getting variable values
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        // Non-existing variable value with and without wildcard
        assertEquals(1, Variable.getVariableValue("*var1"));
        assertEquals(0, Variable.getVariableValue("*var3"));
    }

    @Test
    public void testSetVariableValue() {
        // Setting variable values
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
    }

    // Edge cases and error scenarios
    @Test
    public void testAddVariableEdgeCases() {
        // Testing adding empty variable
        assertFalse(Variable.addVariable(""));
    }

    @Test
    public void testRemoveVariableEdgeCases() {
        // Removing non-existing variable
        assertFalse(Variable.removeVariable("var3"));
    }

    @Test
    public void testGetVariableValueEdgeCases() {
        // Testing non-existing variable value
        assertEquals(0, Variable.getVariableValue("nonexistent"));
    }

    @Test
    public void testSetVariableValueEdgeCases() {
        // Setting value for non-existing variable
        Variable.setVariableValue("var3", 3);
        assertEquals(3, Variable.getVariableValue("var3"));
    }
}