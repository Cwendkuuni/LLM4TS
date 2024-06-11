
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @BeforeClass
    public static void init() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));

        Variable.setVariableValue("var1", 20);
        assertEquals(20, Variable.getVariableValue("var1"));

        Variable.setVariableValue("var1", 30);
        assertEquals(30, Variable.getVariableValue("var1"));

        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));

        Variable.setVariableValue("var1", 20);
        assertEquals(20, Variable.getVariableValue("var1"));

        Variable.setVariableValue("var1", 30);
        assertEquals(30, Variable.getVariableValue("var1"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(-1, Variable.findVariable("var2"));

        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 20);
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
    }
}