
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class VariableTest {
    
    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @Test
    public void testAddVariable() {
        // Test case for adding a new variable
        assertTrue(Variable.addVariable("var1"));
        
        // Test case for adding an existing variable
        assertFalse(Variable.addVariable("var1"));
    }
    
    @Test
    public void testRemoveVariable() {
        // Test case for removing an existing variable
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
        
        // Test case for removing a non-existing variable
        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        // Test case for getting the value of an existing variable
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 100);
        assertEquals(100, Variable.getVariableValue("var1"));
        
        // Test case for getting the value of a non-existing variable
        assertEquals(0, Variable.getVariableValue("var2"));
        
        // Test case for getting the value of a referenced variable
        Variable.setVariableValue("var1", 200);
        assertEquals(200, Variable.getVariableValue("*var1"));
    }
    
    @Test
    public void testSetVariableValue() {
        // Test case for setting the value of an existing variable
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 100);
        assertEquals(100, Variable.getVariableValue("var1"));
        
        // Test case for setting the value of a non-existing variable
        Variable.setVariableValue("var2", 200);
        assertEquals(0, Variable.getVariableValue("var2"));
    }
    
    @Test
    public void testFindVariable() {
        // Test case for finding an existing variable
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
        
        // Test case for finding a non-existing variable
        assertEquals(-1, Variable.findVariable("var2"));
        
        // Test case for finding a referenced variable
        Variable.setVariableValue("var1", 100);
        assertEquals(100, Variable.findVariable("&var1"));
    }
    
}
