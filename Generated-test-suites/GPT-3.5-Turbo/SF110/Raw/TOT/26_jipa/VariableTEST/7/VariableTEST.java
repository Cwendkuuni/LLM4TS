import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VariableTest {

    @Before
    public void setup() {
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable_typical() {
        assertTrue(Variable.addVariable("var1"));
    }
    
    @Test
    public void testAddVariable_alreadyExists() {
        Variable.addVariable("var1");
        assertFalse(Variable.addVariable("var1"));
    }
    
    @Test
    public void testRemoveVariable_typical() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertTrue(Variable.variableList.isEmpty());
    }
    
    @Test
    public void testRemoveVariable_notExists() {
        Variable.removeVariable("var1");
        assertTrue(Variable.variableList.isEmpty());
    }
    
    @Test
    public void testGetVariableValue_typical() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testGetVariableValue_notExists() {
        assertEquals(0, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testGetVariableValue_reference() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("*var1"));
    }
    
    @Test
    public void testSetVariableValue_typical() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testSetVariableValue_notExists() {
        Variable.setVariableValue("var1", 10);
        assertEquals(0, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testFindVariable_typical() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(0, Variable.findVariable("var1"));
    }
    
    @Test
    public void testFindVariable_notExists() {
        assertEquals(-1, Variable.findVariable("var1"));
    }
    
    @Test
    public void testFindVariable_reference() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.findVariable("&var1"));
    }
    
    @Test
    public void testFindVariable_multipleInstances() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");
        assertEquals(2, Variable.findVariable("var3"));
    }
}