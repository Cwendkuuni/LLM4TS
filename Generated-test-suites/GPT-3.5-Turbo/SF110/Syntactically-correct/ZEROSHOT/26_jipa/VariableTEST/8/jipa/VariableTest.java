import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class VariableTest {

    @BeforeClass
    public static void setUp() {
        Variable.variableList.clear();
    }

    @Test
    public void testGetValue() {
        Variable variable = new Variable("test");
        assertEquals(0, variable.getValue());
        variable.setValue(10);
        assertEquals(10, variable.getValue());
    }

    @Test
    public void testSetValue() {
        Variable variable = new Variable("test");
        variable.setValue(20);
        assertEquals(20, variable.getValue());
    }

    @Test
    public void testGetName() {
        Variable variable = new Variable("test");
        assertEquals("test", variable.getName());
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("test1"));
        assertTrue(Variable.addVariable("test2"));
        assertFalse(Variable.addVariable("test1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("test1");
        Variable.addVariable("test2");
        Variable.removeVariable("test1");
        assertEquals(1, Variable.variableList.size());
        Variable.removeVariable("test3");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("test1");
        Variable.setVariableValue("test1", 5);
        assertEquals(5, Variable.getVariableValue("test1"));
        assertEquals(5, Variable.getVariableValue("*test1"));
        assertEquals(0, Variable.getVariableValue("test2"));
        assertEquals(0, Variable.getVariableValue("*test2"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("test1");
        Variable.addVariable("test2");
        Variable.setVariableValue("test1", 10);
        assertEquals(10, Variable.getVariableValue("test1"));
        Variable.setVariableValue("test2", 20);
        assertEquals(20, Variable.getVariableValue("test2"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("test1");
        Variable.addVariable("test2");
        assertEquals(0, Variable.findVariable("test1"));
        assertEquals(1, Variable.findVariable("test2"));
        assertEquals(-1, Variable.findVariable("test3"));
    }
}