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
        assertTrue(Variable.addVariable("var1")); // typical use case
        assertFalse(Variable.addVariable("var1")); // edge case: adding existing variable
        assertTrue(Variable.addVariable("var2")); // typical use case
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1")); // typical use case
        assertEquals(1, Variable.variableList.size()); // typical use case
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2")); // typical use case
        assertEquals(0, Variable.variableList.size()); // typical use case
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1")); // typical use case
        assertEquals(2, Variable.getVariableValue("var2")); // typical use case
        assertEquals(1, Variable.getVariableValue("*var1")); // edge case: wildcard variable
        assertEquals(0, Variable.getVariableValue("*var3")); // edge case: non-existent variable
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1")); // typical use case
        assertEquals(2, Variable.getVariableValue("var2")); // typical use case
    }

}