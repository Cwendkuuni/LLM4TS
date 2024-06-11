package jipa;

import static org.junit.Assert.*;
import org.junit.*;

public class VariableTest {

    @BeforeClass
    public static void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        assertTrue(Variable.variableList.size() == 1);
        Variable.removeVariable("var1");
        assertTrue(Variable.variableList.size() == 0);
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.variableList.get(0).getValue());
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        assertTrue(Variable.findVariable("var1") != -1);
        assertTrue(Variable.findVariable("var2") == -1);
    }

    @Test
    public void testFindVariableWithReference() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.findVariable("&var1"));
    }

    @Test
    public void testFindVariableWithUnmatchedReference() {
        Variable.addVariable("var1");
        assertEquals(-1, Variable.findVariable("&var2"));
    }

    @Test
    public void testFindVariableWithNonReference() {
        Variable.addVariable("var1");
        assertTrue(Variable.findVariable("var1") != -1);
    }
}