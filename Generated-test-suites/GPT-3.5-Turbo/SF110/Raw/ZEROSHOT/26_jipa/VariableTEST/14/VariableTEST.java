import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
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
        Variable.removeVariable("var3");
        assertEquals(2, Variable.variableList.size());
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var2", 10);
        assertEquals(0, Variable.getVariableValue("var2"));
        assertEquals(5, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.variableList.get(0).getValue());
        Variable.setVariableValue("var2", 10);
        assertEquals(0, Variable.variableList.size());
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 10);
        assertEquals(10, Variable.variableList.get(0).getValue());
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        assertEquals(-1, Variable.findVariable("var3"));
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.findVariable("&var1"));
    }
}