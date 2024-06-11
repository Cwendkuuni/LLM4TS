package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList.clear();
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("newVar"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        Variable.removeVariable("var4");
    }

    @Test
    public void testGetVariableValue() {
        assertEquals(0, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var4"));
        assertEquals(0, Variable.getVariableValue("*var1"));
        Variable.setVariableValue("var2", 5);
        assertEquals(5, Variable.getVariableValue("*var2"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var4", 5);
        assertEquals(0, Variable.getVariableValue("var4"));
    }

    @Test
    public void testFindVariable() {
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(-1, Variable.findVariable("var4"));
        Variable.setVariableValue("var2", 5);
        assertEquals(5, Variable.findVariable("*var2"));
    }
}