package jipa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class VariableTest {
    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }

    @Test
    public void testAddVariable_newVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testAddVariable_existingVariable() {
        Variable.addVariable("var1");
        assertFalse(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_existingVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_nonExistingVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var2");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue_existingVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
    }

    @Test
    public void testGetVariableValue_nonExistingVariable() {
        assertEquals(0, Variable.getVariableValue("var1"));
    }

    @Test
    public void testGetVariableValue_referencedVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue_existingVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValue_nonExistingVariable() {
        Variable.setVariableValue("var1", 5);
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testFindVariable_existingVariable() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
    }

    @Test
    public void testFindVariable_nonExistingVariable() {
        assertEquals(-1, Variable.findVariable("var1"));
    }

    @Test
    public void testFindVariable_referencedVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.findVariable("*var1"));
    }
}