import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {
    
    @BeforeClass
    public static void setUp() {
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable_success() {
        assertTrue(Variable.addVariable("variable1")); // Adding a new variable
    }
    
    @Test
    public void testAddVariable_alreadyExists() {
        Variable.addVariable("variable2");
        assertFalse(Variable.addVariable("variable2")); // Adding an existing variable
    }
    
    @Test
    public void testRemoveVariable_success() {
        Variable.addVariable("variable3");
        Variable.removeVariable("variable3"); // Removing an existing variable
        assertEquals(-1, Variable.findVariable("variable3"));
    }
    
    @Test
    public void testRemoveVariable_notFound() {
        Variable.addVariable("variable4");
        Variable.removeVariable("nonExistentVariable"); // Removing a non-existent variable
        assertNotEquals(-1, Variable.findVariable("variable4"));
    }
    
    @Test
    public void testGetVariableValue_normalVariable() {
        Variable.addVariable("variable5");
        Variable.setVariableValue("variable5", 10); // Setting variable value
        assertEquals(10, Variable.getVariableValue("variable5"));
    }
    
    @Test
    public void testGetVariableValue_referencedVariable_notFound() {
        Variable.addVariable("variable6");
        assertEquals(0, Variable.getVariableValue("*nonExistentVariable")); // Attempting to get value of non-existent referenced variable
    }
    
    @Test
    public void testGetVariableValue_referencedVariable_exists() {
        Variable.addVariable("variable7");
        Variable.setVariableValue("variable7", 20);
        assertEquals(20, Variable.getVariableValue("*variable7")); // Getting value of an existing referenced variable
    }
    
    @Test
    public void testSetVariableValue_existingVariable() {
        Variable.addVariable("variable8");
        Variable.setVariableValue("variable8", 30); // Setting value of an existing variable
        assertEquals(30, Variable.getVariableValue("variable8"));
    }
    
    @Test
    public void testSetVariableValue_nonExistingVariable() {
        Variable.setVariableValue("nonExistentVariable", 40); // Setting value of a non-existent variable
        assertEquals(0, Variable.getVariableValue("nonExistentVariable"));
    }
    
    @Test
    public void testFindVariable_existingVariable() {
        Variable.addVariable("variable9"); // Adding a variable
        assertEquals(0, Variable.findVariable("variable9")); // Finding the added variable
    }
    
    @Test
    public void testFindVariable_nonExistingVariable() {
        assertEquals(-1, Variable.findVariable("nonExistentVariable")); // Finding a non-existent variable
    }
}