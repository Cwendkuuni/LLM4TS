import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {
    
    @BeforeClass
    public static void setup() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");
    }
    
    @AfterClass
    public static void cleanup() {
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable() {
        assertFalse(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var4"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.removeVariable("var2");
        assertNull(Variable.getVariableValue("var2"));
    }
    
    @Test
    public void testGetVariableValue() {
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var3"));
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var3", 20);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(20, Variable.getVariableValue("var3"));
        assertEquals(10, Variable.getVariableValue("*var1"));
        assertEquals(0, Variable.getVariableValue("*var4"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.setVariableValue("var2", 30);
        assertEquals(30, Variable.getVariableValue("var2"));
    }
    
    @Test
    public void testFindVariable() {
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(-1, Variable.findVariable("var4"));
        assertEquals(0, Variable.findVariable("&var1"));
        assertEquals(10, Variable.findVariable("&var1"));
        assertEquals(-1, Variable.findVariable("var5"));
        assertEquals(-1, Variable.findVariable("&var5"));
    }
}