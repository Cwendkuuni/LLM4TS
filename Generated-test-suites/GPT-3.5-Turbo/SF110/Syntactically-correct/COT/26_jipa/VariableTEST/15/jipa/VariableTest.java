package jipa;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Before
    public void setup() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable() {
        Assert.assertTrue(Variable.addVariable("var1"));
    }

    @Test
    public void testAddExistingVariable() {
        Variable.addVariable("var1");
        Assert.assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        Assert.assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testRemoveNonExistingVariable() {
        Variable.removeVariable("var1");
        Assert.assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        Assert.assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test
    public void testGetVariableValueWithInvalidVariableName() {
        Assert.assertEquals(0, Variable.getVariableValue("var1"));
    }

    @Test
    public void testGetVariableValueWithDereferencedVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        Assert.assertEquals(10, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        Assert.assertEquals(10, Variable.variableList.get(0).getValue());
    }

    @Test
    public void testSetVariableValueWithNonExistingVariable() {
        Variable.setVariableValue("var1", 10);
        Assert.assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Assert.assertEquals(0, Variable.findVariable("var1"));
    }

    @Test
    public void testFindVariableWithDereferencedVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        Assert.assertEquals(10, Variable.findVariable("*var1"));
    }

    @Test
    public void testFindVariableWithNonExistingVariable() {
        Assert.assertEquals(-1, Variable.findVariable("var1"));
    }

    @Test
    public void testFindVariableWithNonExistingDereferencedVariable() {
        Assert.assertEquals(-1, Variable.findVariable("*var1"));
    }

    @Test
    public void testGetValue() {
        Variable.varName = "var1";
        Variable variable = new Variable("var1");
        Assert.assertEquals(0, variable.getValue());
    }

    @Test
    public void testSetValue() {
        Variable.varName = "var1";
        Variable variable = new Variable("var1");
        variable.setValue(10);
        Assert.assertEquals(10, variable.getValue());
    }

    @Test
    public void testGetName() {
        Variable.varName = "var1";
        Variable variable = new Variable("var1");
        Assert.assertEquals("var1", variable.getName());
    }
}