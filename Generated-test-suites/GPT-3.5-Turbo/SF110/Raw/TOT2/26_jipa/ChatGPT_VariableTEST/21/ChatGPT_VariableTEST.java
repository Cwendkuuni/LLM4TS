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
        assertFalse(Variable.addVariable("var1")); // Testing duplicate variable
        assertTrue(Variable.addVariable("var2")); // Typical use case
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1"); // Typical use case
        assertFalse(Variable.variableList.contains("var1")); // Testing removal
        assertEquals(1, Variable.variableList.size()); // Testing size after removal
        Variable.removeVariable("var2"); // Typical use case
        assertFalse(Variable.variableList.contains("var2")); // Testing removal
        assertEquals(0, Variable.variableList.size()); // Testing size after removal
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1")); // Typical use case
        assertEquals(2, Variable.getVariableValue("var2")); // Typical use case
        assertEquals(1, Variable.getVariableValue("*var1")); // Testing wildcard variable name
        assertEquals(0, Variable.getVariableValue("*var3")); // Testing non-existing variable
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1); // Typical use case
        Variable.setVariableValue("var2", 2); // Typical use case
        assertEquals(1, Variable.getVariableValue("var1")); // Testing value set
        assertEquals(2, Variable.getVariableValue("var2")); // Testing value set
    }
}