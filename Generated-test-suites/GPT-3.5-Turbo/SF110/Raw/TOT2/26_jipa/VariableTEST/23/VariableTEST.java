import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VariableTest {
    
    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        assertTrue(Variable.addVariable("testVariable"));
        assertFalse(Variable.addVariable("testVariable"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVariable");
        Variable.removeVariable("testVariable");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVariable");
        assertEquals(0, Variable.getVariableValue("testVariable"));
        Variable.setVariableValue("testVariable", 10);
        assertEquals(10, Variable.getVariableValue("testVariable"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 20);
        assertEquals(20, Variable.getVariableValue("testVariable"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("testVariable");
        assertEquals(0, Variable.findVariable("testVariable"));
        assertEquals(-1, Variable.findVariable("nonExistentVariable"));
    }
}