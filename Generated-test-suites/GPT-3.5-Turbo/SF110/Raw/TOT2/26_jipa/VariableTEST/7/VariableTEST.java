import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("testVar1"));
        assertFalse(Variable.addVariable("testVar1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("testVar2");
        Variable.removeVariable("testVar2");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("testVar3");
        Variable.setVariableValue("testVar3", 5);
        assertEquals(5, Variable.getVariableValue("testVar3"));
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
        assertEquals(5, Variable.getVariableValue("*testVar3"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("testVar4");
        Variable.setVariableValue("testVar4", 10);
        assertEquals(10, Variable.getVariableValue("testVar4"));
        Variable.setVariableValue("nonExistentVar", 7);
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("testVar5");
        assertEquals(0, Variable.findVariable("testVar5"));
        assertEquals(5, Variable.findVariable("*testVar5"));
        assertEquals(-1, Variable.findVariable("nonExistentVar"));
    }
}