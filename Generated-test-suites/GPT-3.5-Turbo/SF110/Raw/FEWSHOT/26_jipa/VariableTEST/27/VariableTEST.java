package jipa;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<>();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("x"));
        assertFalse(Variable.addVariable("x"));
        assertTrue(Variable.addVariable("y"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("x");
        Variable.addVariable("y");
        Variable.removeVariable("x");
        assertEquals(1, Variable.variableList.size());
        assertEquals("y", Variable.variableList.get(0).getName());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("x");
        Variable.setVariableValue("x", 10);
        assertEquals(10, Variable.getVariableValue("x"));
        assertEquals(0, Variable.getVariableValue("y"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("x");
        Variable.setVariableValue("x", 10);
        assertEquals(10, Variable.variableList.get(0).getValue());
        Variable.setVariableValue("y", 20);
        assertEquals(0, Variable.getVariableValue("y"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("x");
        Variable.addVariable("y");
        assertEquals(0, Variable.findVariable("x"));
        assertEquals(-1, Variable.findVariable("z"));
    }
}