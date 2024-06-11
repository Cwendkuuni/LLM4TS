import static org.junit.Assert.*;
import org.junit.Test;

public class VariableTest {

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("testVar"));
        assertFalse(Variable.addVariable("testVar"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("testVar");
        Variable.removeVariable("testVar");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("testVar");
        Variable.setVariableValue("testVar", 5);
        assertEquals(5, Variable.getVariableValue("testVar"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("testVar");
        Variable.setVariableValue("testVar", 10);
        assertEquals(10, Variable.getVariableValue("testVar"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("testVar");
        assertEquals(0, Variable.findVariable("testVar"));
    }
}