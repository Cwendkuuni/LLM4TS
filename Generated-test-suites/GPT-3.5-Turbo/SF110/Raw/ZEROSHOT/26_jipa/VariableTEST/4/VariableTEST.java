import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {
    
    @Before
    public void setUp() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);
    }
    
    @After
    public void tearDown() {
        Variable.removeVariable("var1");
        Variable.removeVariable("var2");
    }
    
    @Test
    public void testAddVariable() {
        assertFalse("Adding an existing variable should return false", Variable.addVariable("var1"));
        assertTrue("Adding a new variable should return true", Variable.addVariable("var3"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.removeVariable("var1");
        assertFalse("Removing a non-existent variable should return false", Variable.removeVariable("var4"));
        assertFalse("Removing an already removed variable should return false", Variable.removeVariable("var1"));
        assertNull("Removing a variable should remove it from the list", Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testGetVariableValue() {
        assertEquals("Getting the value of an existing variable should return the correct value", 10, Variable.getVariableValue("var1"));
        assertEquals("Getting the value of a non-existent variable should return 0", 0, Variable.getVariableValue("var3"));
        assertEquals("Getting the value of a reference to an existing variable should return the correct value", 10, Variable.getVariableValue("*var1"));
        assertEquals("Getting the value of a reference to a non-existent variable should return 0", 0, Variable.getVariableValue("*var3"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.setVariableValue("var1", 15);
        assertEquals("Setting the value of an existing variable should update the value", 15, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var3", 30);
        assertEquals("Setting the value of a non-existent variable should not create the variable", 0, Variable.getVariableValue("var3"));
        Variable.setVariableValue("*var2", 25);
        assertEquals("Setting the value of a reference to an existing variable should update the value", 25, Variable.getVariableValue("var2"));
        Variable.setVariableValue("*var4", 40);
        assertEquals("Setting the value of a reference to a non-existent variable should not create the variable", 0, Variable.getVariableValue("var4"));
    }
    
    @Test
    public void testFindVariable() {
        assertEquals("Finding an existing variable should return the index", 0, Variable.findVariable("var1"));
        assertEquals("Finding a non-existent variable should return -1", -1, Variable.findVariable("var3"));
        assertEquals("Finding a reference to an existing variable should return the value", 10, Variable.findVariable("*var1"));
        assertEquals("Finding a reference to a non-existent variable should return -1", -1, Variable.findVariable("*var3"));
    }
}