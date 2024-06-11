import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

    @BeforeClass
    public static void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @AfterClass
    public static void tearDown() {
        Variable.variableList = null;
    }

    // Testing the addVariable method
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
    }

    // Testing the removeVariable method
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var2");
        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());
    }

    // Testing the getVariableValue method
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var3");
        assertEquals(0, Variable.getVariableValue("var3"));
    }

    // Testing the setVariableValue method
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var4");
        Variable.setVariableValue("var4", 10);
        assertEquals(10, Variable.getVariableValue("var4"));
    }

    // Testing the findVariable method with a variable name that exists
    @Test
    public void testFindVariable_existingVariable() {
        Variable.addVariable("var5");
        int index = Variable.findVariable("var5");
        assertEquals(0, index);
    }

    // Testing the findVariable method with a variable name that does not exist
    @Test
    public void testFindVariable_nonExistingVariable() {
        int index = Variable.findVariable("var6");
        assertEquals(-1, index);
    }

    // Testing the findVariable method with a reference variable name
    @Test
    public void testFindVariable_referenceVariable() {
        Variable.addVariable("var7");
        Variable.setVariableValue("var7", 20);
        int value = Variable.findVariable("&var7");
        assertEquals(20, value);
    }

    // Testing the getValue method
    @Test
    public void testGetValue() {
        Variable.addVariable("var8");
        Variable.setVariableValue("var8", 30);
        int value = Variable.variableList.get(0).getValue();
        assertEquals(30, value);
    }

    // Testing the getName method
    @Test
    public void testGetName() {
        Variable.addVariable("var9");
        String name = Variable.variableList.get(0).getName();
        assertEquals("var9", name);
    }

    // Testing the setValue method
    @Test
    public void testSetValue() {
        Variable.addVariable("var10");
        Variable.variableList.get(0).setValue(40);
        int value = Variable.variableList.get(0).getValue();
        assertEquals(40, value);
    }
    
    // Add more test cases for edge cases and exception handling scenarios
    
    
    // Add the individual test cases above this line

    // Testing a null variableList when using addVariable
    @Test(expected = NullPointerException.class)
    public void testAddVariable_nullVariableList() {
        Variable.variableList = null;
        Variable.addVariable("var11");
    }

    // Testing removing a non-existing variable
    @Test
    public void testRemoveVariable_nonExistingVariable() {
        Variable.removeVariable("var12");
        assertEquals(0, Variable.variableList.size());
    }

    // Testing getting the value of a non-existing variable
    @Test
    public void testGetVariableValue_nonExistingVariable() {
        int value = Variable.getVariableValue("var13");
        assertEquals(0, value);
    }

    // Testing setting the value of a non-existing variable
    @Test
    public void testSetVariableValue_nonExistingVariable() {
        Variable.setVariableValue("var14", 50);
        assertEquals(0, Variable.variableList.size());
    }

    // Testing finding a non-existing variable
    @Test
    public void testFindVariable_nonExistingVariable2() {
        int index = Variable.findVariable("var15");
        assertEquals(-1, index);
    }

    // Testing finding a null variable name
    @Test(expected = NullPointerException.class)
    public void testFindVariable_nullVariableName() {
        Variable.findVariable(null);
    }

    // Testing getting the value of a reference variable with a non-existing variable name
    @Test
    public void testFindVariable_nonExistingReferenceVariable() {
        int value = Variable.findVariable("&var16");
        assertEquals(-1, value);
    }

    // Testing getting the value of a reference variable with a null variable name
    @Test(expected = NullPointerException.class)
    public void testFindVariable_nullReferenceVariableName() {
        Variable.findVariable("&null");
    }

    // Testing getting the value of a reference variable with a variable name that exists
    @Test
    public void testFindVariable_existingReferenceVariable() {
        Variable.addVariable("var17");
        Variable.setVariableValue("var17", 60);
        int value = Variable.findVariable("&var17");
        assertEquals(60, value);
    }

    // Testing getting the value of a reference variable with a variable name that has no value set
    @Test
    public void testFindVariable_existingReferenceVariableNoValue() {
        Variable.addVariable("var18");
        int value = Variable.findVariable("&var18");
        assertEquals(0, value);
    }

    // Add more test cases for edge cases and exception handling scenarios
    
    
    // Test file ends here
}