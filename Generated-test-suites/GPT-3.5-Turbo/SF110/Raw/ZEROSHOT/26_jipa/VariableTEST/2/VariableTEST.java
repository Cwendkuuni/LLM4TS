import static org.junit.Assert.*;
import org.junit.*;

public class VariableTest {
    
    @BeforeClass
    public static void setup() {
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable_success() {
        assertTrue(Variable.addVariable("var1"));
    }
    
    @Test
    public void testAddVariable_duplicate() {
        Variable.addVariable("var1");
        assertFalse(Variable.addVariable("var1"));
    }
    
    @Test
    public void testRemoveVariable_exist() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testRemoveVariable_nonExist() {
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue_found() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testGetVariableValue_notFound() {
        assertEquals(0, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testGetVariableValue_reference() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("*var1"));
    }
    
    @Test
    public void testSetVariableValue_found() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testSetVariableValue_notFound() {
        Variable.setVariableValue("var1", 10);
        assertEquals(0, Variable.getVariableValue("var1"));
    }
    
    @Test
    public void testFindVariable_exist() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
    }
    
    @Test
    public void testFindVariable_nonExist() {
        assertEquals(-1, Variable.findVariable("var1"));
    }
    
    @Test
    public void testFindVariable_reference() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.findVariable("&var1"));
    }
}