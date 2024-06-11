import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VariableTest {
    
    @Test
    public void testAddVariable() {
        assertFalse(Variable.addVariable("var1")); // Adding a variable that already exists should return false
        assertTrue(Variable.addVariable("var2")); // Adding a new variable should return true
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var3");
        Variable.removeVariable("var3");
        assertEquals(-1, Variable.findVariable("var3")); // Removing a variable should set its index to -1
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var4");
        Variable.setVariableValue("var4", 5);
        assertEquals(5, Variable.getVariableValue("var4")); // Getting the value of a variable should return the correct value
        assertEquals(0, Variable.getVariableValue("var5")); // Getting the value of a non-existing variable should return 0
        assertEquals(5, Variable.getVariableValue("*var4")); // Getting the value of a reference variable should return the correct value
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var6");
        Variable.setVariableValue("var6", 10);
        assertEquals(10, Variable.getVariableValue("var6")); // Setting the value of a variable should update its value
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var7");
        assertEquals(0, Variable.findVariable("var7")); // Finding an existing variable should return its index
        assertEquals(-1, Variable.findVariable("var8")); // Finding a non-existing variable should return -1
        Variable.addVariable("&var9");
        assertEquals(0, Variable.findVariable("var9")); // Finding a reference variable should return its value
    }
}