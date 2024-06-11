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
        
        // Edge case: Test adding empty variable
        assertFalse(Variable.addVariable(""));
    }

    @Test
    public void testRemoveVariable() {
        // Typical use case
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        assertEquals(1, Variable.variableList.size());
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        assertEquals(0, Variable.variableList.size());
        
        // Edge case: Test removing non-existent variable
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
        
        // Edge cases: Test getting value of non-existent variable and wildcard variable
        assertEquals(0, Variable.getVariableValue("*var3"));
        assertEquals(0, Variable.getVariableValue("dummy"));
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

        // Edge case: Test setting value for non-existent variable
        Variable.setVariableValue("unknown", 10);
    }

}