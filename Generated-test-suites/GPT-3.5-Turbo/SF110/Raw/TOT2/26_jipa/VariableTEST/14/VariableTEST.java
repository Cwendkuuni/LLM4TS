import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("test"));
        assertFalse(Variable.addVariable("test")); // Adding same variable name should return false
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("test");
        Variable.removeVariable("test");
        assertEquals(0, Variable.getVariableValue("test"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("test");
        assertEquals(0, Variable.getVariableValue("test"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("test");
        Variable.setVariableValue("test", 5);
        assertEquals(5, Variable.getVariableValue("test"));
    }

}