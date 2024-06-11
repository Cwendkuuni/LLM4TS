import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }

    @After
    public void tearDown() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable() {
        boolean result = Variable.addVariable("testVariable");
        Assert.assertTrue(result);
    }

    @Test
    public void testAddVariable_existingVariable() {
        Variable.variableList.add(new Variable("existingVariable"));
        boolean result = Variable.addVariable("existingVariable");
        Assert.assertFalse(result);
    }

    @Test
    public void testRemoveVariable() {
        Variable.variableList.add(new Variable("testVariable"));
        Variable.removeVariable("testVariable");
        Assert.assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_nonExistingVariable() {
        Variable.removeVariable("nonExistingVariable");
        Assert.assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.variableList.add(new Variable("testVariable"));
        Variable.setVariableValue("testVariable", 10);
        int value = Variable.getVariableValue("testVariable");
        Assert.assertEquals(10, value);
    }

    @Test
    public void testGetVariableValue_referenceVariable() {
        Variable.variableList.add(new Variable("testVariable"));
        Variable.setVariableValue("testVariable", 10);
        int value = Variable.getVariableValue("*testVariable");
        Assert.assertEquals(10, value);
    }

    @Test
    public void testGetVariableValue_nonExistingVariable() {
        int value = Variable.getVariableValue("nonExistingVariable");
        Assert.assertEquals(0, value);
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.variableList.add(new Variable("testVariable"));
        Variable.setVariableValue("testVariable", 10);
        int value = Variable.getVariableValue("testVariable");
        Assert.assertEquals(10, value);
    }

    @Test
    public void testSetVariableValue_nonExistingVariable() {
        Variable.setVariableValue("nonExistingVariable", 10);
        Assert.assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testFindVariable() {
        Variable.variableList.add(new Variable("testVariable"));
        Variable.variableList.add(new Variable("otherVariable"));
        int index = Variable.findVariable("testVariable");
        Assert.assertEquals(0, index);
    }

    @Test
    public void testFindVariable_nonExistingVariable() {
        int index = Variable.findVariable("nonExistingVariable");
        Assert.assertEquals(-1, index);
    }

}