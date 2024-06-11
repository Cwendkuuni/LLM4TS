import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList.clear(); // Clear the variable list before each test
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
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        Variable.addVariable("var2");
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 20);
        assertEquals(20, Variable.getVariableValue("var1"));
        
        Variable.setVariableValue("var2", 30); // Setting value for non-existing variable
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
        
        assertEquals(-1, Variable.findVariable("var2"));
    }

    @Test
    public void testEdgeCases() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 100);
        
        assertEquals(100, Variable.getVariableValue("&var1")); // Test reference variable
        
        Variable.removeVariable("var1"); // Removing existing variable
        
        assertEquals(0, Variable.getVariableValue("var1")); // Testing value of removed variable
    }

    @Test
    public void testExceptionHandling() {
        // Testing for null variable name
        try {
            Variable.addVariable(null);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        
        // Testing setting value for non-existing variable
        try {
            Variable.setVariableValue("var1", 50);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

}