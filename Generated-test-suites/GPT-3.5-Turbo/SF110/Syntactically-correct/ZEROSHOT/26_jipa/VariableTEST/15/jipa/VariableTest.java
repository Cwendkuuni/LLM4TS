import org.junit.*;
import java.util.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.addVariable("var1");
        assertEquals(1, Variable.variableList.size());
        Variable.addVariable("var2");
        assertEquals(2, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("*var1"));
        assertEquals(0, Variable.getVariableValue("*var2"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var2", 10);
        assertEquals(10, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("var3"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        assertEquals(-1, Variable.findVariable("var3"));
        assertEquals(0, Variable.findVariable("&var1"));
        assertEquals(0, Variable.findVariable("&var2"));
    }
}