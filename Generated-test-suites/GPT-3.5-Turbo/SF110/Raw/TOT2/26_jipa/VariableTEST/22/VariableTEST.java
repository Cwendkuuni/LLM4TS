import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("x"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("x");
        Variable.removeVariable("x");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("x");
        Variable.setVariableValue("x", 10);
        assertEquals(10, Variable.getVariableValue("x"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("x");
        Variable.setVariableValue("x", 15);
        assertEquals(15, Variable.getVariableValue("x"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("x");
        assertEquals(0, Variable.findVariable("x"));
    }

    @Test
    public void testFindVariableWithReference() {
        Variable.addVariable("x");
        Variable.setVariableValue("x", 20);

        assertEquals(20, Variable.findVariable("&x"));
    }

    @Test
    public void testFindVariableNotFound() {
        assertEquals(-1, Variable.findVariable("y"));
    }

    @Test
    public void testFindVariableWithReferenceNotFound() {
        assertEquals(-1, Variable.findVariable("&y"));
    }
}