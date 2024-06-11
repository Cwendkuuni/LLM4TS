import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {
    
    @BeforeClass
    public static void setUp() {
        // Initialize variableList before each test case
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @Test
    public void testAddVariable() {
        // Add a new variable and check if it is added successfully to variableList
        assertTrue(Variable.addVariable("var1"));
        
        // Add a variable with the same name, it should not be added again
        assertFalse(Variable.addVariable("var1"));
    }
    
    @Test
    public void testRemoveVariable() {
        // Add a variable and then remove it, it should be removed successfully
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
        
        // Remove a variable that is not present in variableList, nothing should happen
        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        // Add a variable and check if its value can be retrieved correctly
        Variable.addVariable("var1");
        assertEquals(0, Variable.getVariableValue("var1"));
        
        // Add a variable with a value and check if its value can be retrieved correctly
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        // Try to retrieve value of a variable that is not present in variableList, it should return 0
        assertEquals(0, Variable.getVariableValue("var2"));
        
        // Try to retrieve the value of a variable using a reference, it should return the referenced value
        Variable.setVariableValue("var2", 20);
        Variable.setVariableValue("&var1", 1);
        assertEquals(1, Variable.getVariableValue("*var1"));
    }
    
    @Test
    public void testSetVariableValue() {
        // Add a variable and set its value, and then check if the value is set correctly
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        // Set the value of a variable that is not present in variableList, nothing should happen
        Variable.setVariableValue("var2", 20);
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testFindVariable() {
        // Add a variable and find it, it should return its index in variableList
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
        
        // Find a variable that is not present in variableList, it should return -1
        assertEquals(-1, Variable.findVariable("var2"));
        
        // Find the value of a variable using a reference, it should return the referenced value
        Variable.setVariableValue("var2", 30);
        Variable.setVariableValue("&var1", 1);
        assertEquals(1, Variable.findVariable("*var1"));
    }
  
}