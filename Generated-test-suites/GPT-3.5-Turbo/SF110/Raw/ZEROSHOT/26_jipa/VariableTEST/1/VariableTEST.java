import org.junit.*;
import java.util.*;

public class VariableTest {

    @BeforeClass
    public static void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }

    @AfterClass
    public static void tearDown() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable() {
        Assert.assertTrue(Variable.addVariable("var1"));
        Assert.assertTrue(Variable.addVariable("var2"));
        Assert.assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        Assert.assertEquals(1, Variable.variableList.size());
        Assert.assertEquals("var2", Variable.variableList.get(0).getName());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        Assert.assertEquals(10, Variable.getVariableValue("var1"));
        Assert.assertEquals(10, Variable.getVariableValue("*var1"));
        Assert.assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        Assert.assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 20);
        Assert.assertEquals(0, Variable.findVariable("var1"));
        Assert.assertEquals(20, Variable.findVariable("&var2"));
        Assert.assertEquals(-1, Variable.findVariable("var3"));
    }
    
    // Additional tests can be added to cover all methods in Variable.java
    
}