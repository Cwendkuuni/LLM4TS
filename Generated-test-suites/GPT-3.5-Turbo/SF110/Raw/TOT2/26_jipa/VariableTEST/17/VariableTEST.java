import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("var1");
        assertEquals(0, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test
    public void testFindVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("&var1"));
    }
}