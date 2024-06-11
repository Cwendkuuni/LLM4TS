package jipa;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class VariableTest {

    @Before
    public void setUp() {
        // Initialize variableList before each test case
        Variable.variableList = new ArrayList<Variable>();
    }

    @After
    public void tearDown() {
        // Reset variableList after each test case
        Variable.variableList = new ArrayList<Variable>();
    }

    @Test
    public void testAddVariable() {
        // Test adding a variable that does not exist
        Assert.assertTrue(Variable.addVariable("var1"));

        // Test adding a variable that already exists
        Assert.assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        // Test removing a variable that exists
        Variable.variableList.add(new Variable("var1"));
        Variable.removeVariable("var1");
        Assert.assertTrue(Variable.variableList.isEmpty());

        // Test removing a variable that does not exist
        Variable.removeVariable("var1");
        Assert.assertTrue(Variable.variableList.isEmpty());
    }

    @Test
    public void testGetVariableValue() {
        // Test retrieving value of a variable that does not exist
        Assert.assertEquals(0, Variable.getVariableValue("var1"));

        // Test retrieving value of a variable that exists
        Variable.variableList.add(new Variable("var1"));
        Variable.setVariableValue("var1", 10);
        Assert.assertEquals(10, Variable.getVariableValue("var1"));

        // Test retrieving value of a reference variable that exists
        Variable.variableList.add(new Variable("var2"));
        Variable.setVariableValue("var2", 20);
        Assert.assertEquals(20, Variable.getVariableValue("*var2"));
    }

    @Test
    public void testSetVariableValue() {
        // Test setting value of a variable that exists
        Variable.variableList.add(new Variable("var1"));
        Variable.setVariableValue("var1", 10);
        Assert.assertEquals(10, Variable.variableList.get(0).getValue());

        // Test setting value of a variable that does not exist
        Variable.setVariableValue("var2", 20);
        Assert.assertTrue(Variable.variableList.isEmpty());
    }

    @Test
    public void testFindVariable() {
        // Test finding a variable that exists
        Variable.variableList.add(new Variable("var1"));
        Assert.assertEquals(0, Variable.findVariable("var1"));

        // Test finding a variable that does not exist
        Assert.assertEquals(-1, Variable.findVariable("var2"));

        // Test finding a reference variable that exists
        Variable.setVariableValue("var3", 30);
        Assert.assertEquals(30, Variable.findVariable("*var3"));

        // Test finding a reference variable that does not exist
        Assert.assertEquals(-1, Variable.findVariable("*var4"));
    }
}