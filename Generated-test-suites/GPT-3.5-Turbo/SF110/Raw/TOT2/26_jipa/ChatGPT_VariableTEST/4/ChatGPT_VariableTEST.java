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
        // Typical case: Adding a valid variable
        assertTrue(Variable.addVariable("var1"));

        // Error case: Trying to add the same variable again
        assertFalse(Variable.addVariable("var1"));

        // Typical case: Adding another valid variable
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        // Typical case: Removing an existing variable
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        assertEquals(1, Variable.variableList.size());

        // Error case: Removing a non-existing variable
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        // Typical case: Getting the value of an existing variable
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));

        // Edge case: Trying to get the value of a wildcard variable
        assertEquals(1, Variable.getVariableValue("*var1"));

        // Error case: Getting the value of a non-existing variable
        assertEquals(0, Variable.getVariableValue("*var3"));
    }

    @Test
    public void testSetVariableValue() {
        // Typical case: Setting the value of an existing variable
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));

        // Error case: Setting the value of a non-existing variable
        assertThrows(NoSuchElementException.class, () -> Variable.setVariableValue("var3", 3));
    }

}