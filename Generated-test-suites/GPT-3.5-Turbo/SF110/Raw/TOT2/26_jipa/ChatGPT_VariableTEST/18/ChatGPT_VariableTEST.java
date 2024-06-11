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
        // Test adding a new variable
        assertTrue(Variable.addVariable("var1"));
        // Test adding an existing variable
        assertFalse(Variable.addVariable("var1"));
        // Test adding another new variable
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        // Test removing an existing variable
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        assertEquals(1, Variable.variableList.size());
        // Test removing another existing variable
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        assertEquals(0, Variable.variableList.size());
        // Test removing a non-existing variable
        Variable.removeVariable("var3");
    }

    @Test
    public void testGetVariableValue() {
        // Test getting the value of an existing variable
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        // Test getting the value of an invalid variable name
        assertEquals(1, Variable.getVariableValue("*var1"));
        // Test getting the value of a non-existing variable
        assertEquals(0, Variable.getVariableValue("*var3"));
    }

    @Test
    public void testSetVariableValue() {
        // Test setting the value of an existing variable
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        // Test setting the value of a non-existing variable
        Variable.setVariableValue("var3", 3);
        assertEquals(3, Variable.getVariableValue("var3"));
    }

    @Test(expected = NullPointerException.class)
    public void testVariableNull() {
        // Test adding a null variable
        Variable.addVariable(null);
    }

    @Test
    public void testVariableNameStartWithAsterisk() {
        // Test adding a variable name starting with an asterisk
        assertFalse(Variable.addVariable("*var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistingVariable() {
        // Test removing a non-existing variable
        Variable.removeVariable("var1");
    }

}