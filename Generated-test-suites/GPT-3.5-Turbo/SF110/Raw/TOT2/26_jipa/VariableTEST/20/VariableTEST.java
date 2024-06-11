import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("testVariable"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("testVariable");
        Variable.removeVariable("testVariable");
        assertFalse(Variable.variableList.contains("testVariable"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 5);
        assertEquals(5, Variable.getVariableValue("testVariable"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 10);
        assertEquals(10, Variable.getVariableValue("testVariable"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("testVariable");
        assertEquals(0, Variable.findVariable("testVariable"));
    }

    @Test
    public void testGetVariableValueWithReference() {
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 15);
        assertEquals(15, Variable.getVariableValue("*testVariable"));
    }

    @Test
    public void testFindVariableWithReference() {
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 20);
        assertEquals(20, Variable.findVariable("&testVariable"));
    }

    @Test
    public void testAddExistingVariable() {
        Variable.addVariable("testVariable");
        assertFalse(Variable.addVariable("testVariable"));
    }
}