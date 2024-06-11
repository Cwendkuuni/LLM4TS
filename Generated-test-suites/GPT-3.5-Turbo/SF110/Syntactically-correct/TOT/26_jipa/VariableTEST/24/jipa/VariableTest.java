
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VariableTest {
    
    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    // Test case for addVariable() method
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1")); // Trying to add duplicate variable
    }
    
    // Test case for removeVariable() method
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        
        Variable.removeVariable("var3"); // Trying to remove non-existing variable
        assertEquals(1, Variable.variableList.size());
    }
    
    // Test case for getVariableValue() method
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2")); // Trying to get non-existing variable
        
        Variable.setVariableValue("var1", 20);
        assertEquals(20, Variable.getVariableValue("var1"));
        
        assertEquals(20, Variable.getVariableValue("*var1")); // Testing reference variable
        assertEquals(0, Variable.getVariableValue("*var2")); // Reference to non-existing variable
    }
    
    // Test case for setVariableValue() method
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        Variable.setVariableValue("var1", 20);
        assertEquals(20, Variable.getVariableValue("var1"));
        
        Variable.setVariableValue("var2", 30); // Trying to set value for non-existing variable
        assertEquals(20, Variable.getVariableValue("var1"));
    }
    
    // Test case for findVariable() method
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        
        Variable.addVariable("&var3");
        Variable.setVariableValue("var3", 40);
        
        assertEquals(40, Variable.findVariable("*var3")); // Testing reference variable
        
        assertEquals(-1, Variable.findVariable("var4")); // Testing non-existing variable
    }
}

