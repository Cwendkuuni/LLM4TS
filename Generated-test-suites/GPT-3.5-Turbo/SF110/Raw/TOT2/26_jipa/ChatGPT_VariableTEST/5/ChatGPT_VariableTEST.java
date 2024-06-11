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
        // Edge case - trying to add the same variable again
        assertFalse(Variable.addVariable("var1"));
        // Typical use case
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        // Typical use case
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        // Check if variable being removed is actually removed
        assertFalse(Variable.variableList.contains("var1"));
        // Check the size of the variable list after removal
        assertEquals(1, Variable.variableList.size());
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        assertEquals(0, Variable.variableList.size());
        // Edge case - trying to remove a non-existent variable
        Variable.removeVariable("var3");
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
        // Edge case - getting value of a variable with unknown prefix
        assertEquals(1, Variable.getVariableValue("*var1"));
        // Edge case - getting value of a non-existent variable
        assertEquals(0, Variable.getVariableValue("*var3"));
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
        // Edge case - setting value of a non-existent variable
        Variable.setVariableValue("var3", 3);
    }

}