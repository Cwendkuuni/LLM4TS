package jipa;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {
    @Before
    public void setUp() {
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
        assertTrue(Variable.variableList.isEmpty());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
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
        Variable.setVariableValue("var1", 5);
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(0, Variable.findVariable("*var1"));
        assertEquals(-1, Variable.findVariable("var2"));
    }
}