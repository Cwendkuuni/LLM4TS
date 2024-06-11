import static org.junit.Assert.*;
import org.junit.Test;

public class VariableTest {

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("testVariable"));
        assertFalse(Variable.addVariable("testVariable")); // Testing duplicate variable addition
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("testVariable");
        Variable.removeVariable("testVariable");
        assertEquals(0, Variable.getVariableValue("testVariable")); // Testing variable removal
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("testVariable");
        assertEquals(0, Variable.getVariableValue("testVariable")); // Testing default value
        Variable.setVariableValue("testVariable", 5);
        assertEquals(5, Variable.getVariableValue("testVariable")); // Testing value setting
        assertEquals(0, Variable.getVariableValue("*testVariable")); // Testing reference variable retrieval
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 10);
        assertEquals(10, Variable.getVariableValue("testVariable")); // Testing value setting
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("testVariable");
        assertEquals(0, Variable.findVariable("testVariable")); // Testing findVariable
        assertEquals(0, Variable.findVariable("*testVariable")); // Testing reference variable retrieval
        assertEquals(-1, Variable.findVariable("nonExistentVariable")); // Testing non-existent variable
    }

}