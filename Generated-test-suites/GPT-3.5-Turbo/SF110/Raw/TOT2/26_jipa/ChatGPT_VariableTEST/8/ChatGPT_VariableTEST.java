import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ChatGPT_VariableTest {

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
        assertEquals(1, Variable.getVariableValue("*var1")); // Edge case
        assertEquals(0, Variable.getVariableValue("*var3")); // Edge case
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRemovingNonExistentVariableThrowsException() {
        Variable.removeVariable("non_existent_var"); // Exception handling scenario
    }
    
}