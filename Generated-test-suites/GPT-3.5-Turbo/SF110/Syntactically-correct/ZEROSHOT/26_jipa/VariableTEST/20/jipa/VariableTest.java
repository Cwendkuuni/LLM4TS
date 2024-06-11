import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<Variable>();
        assertTrue(Variable.addVariable("varName1"));
        assertFalse(Variable.addVariable("varName1"));
        assertTrue(Variable.addVariable("varName2"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.addVariable("varName1");
        Variable.addVariable("varName2");
        Variable.removeVariable("varName1");
        assertFalse(Variable.variableList.contains("varName1"));
        assertTrue(Variable.variableList.contains("varName2"));
        Variable.removeVariable("varName2");
        assertFalse(Variable.variableList.contains("varName2"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.addVariable("varName1");
        Variable.setVariableValue("varName1", 10);
        assertEquals(10, Variable.getVariableValue("varName1"));
        Variable.addVariable("varName2");
        assertEquals(0, Variable.getVariableValue("varName2"));
        assertEquals(10, Variable.getVariableValue("*varName1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.addVariable("varName1");
        Variable.setVariableValue("varName1", 10);
        assertEquals(10, Variable.getVariableValue("varName1"));
        Variable.setVariableValue("varName1", 20);
        assertEquals(20, Variable.getVariableValue("varName1"));
        Variable.addVariable("varName2");
    }

    @Test
    public void testFindVariable() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.addVariable("varName1");
        Variable.addVariable("varName2");
        assertEquals(0, Variable.findVariable("varName1"));
        assertEquals(1, Variable.findVariable("varName2"));
        assertEquals(-1, Variable.findVariable("varName3"));
    }
}