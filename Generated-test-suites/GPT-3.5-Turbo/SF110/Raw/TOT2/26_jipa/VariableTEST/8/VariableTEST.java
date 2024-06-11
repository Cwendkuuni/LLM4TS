import static org.junit.Assert.*;
import org.junit.Test;

public class VariableTest {

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var2");
        Variable.removeVariable("var2");
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var3");
        Variable.setVariableValue("var3", 5);
        assertEquals(5, Variable.getVariableValue("var3"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var4");
        Variable.setVariableValue("var4", 10);
        assertEquals(10, Variable.getVariableValue("var4"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var5");
        assertEquals(-1, Variable.findVariable("var6"));
    }
}