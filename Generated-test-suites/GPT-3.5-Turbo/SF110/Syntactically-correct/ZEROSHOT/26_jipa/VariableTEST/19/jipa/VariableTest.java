import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {
    
    @BeforeClass
    public static void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
        
        assertTrue(Variable.addVariable("var2"));
        assertTrue(Variable.addVariable("var3"));
        assertTrue(Variable.addVariable("var4"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        
        Variable.addVariable("var2");
        
        Variable.addVariable("var3");
        
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        
        Variable.removeVariable("var3");
        assertFalse(Variable.variableList.contains("var3"));
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 10);
        assertEquals(10, Variable.getVariableValue("var2"));
        
        Variable.addVariable("var3");
        Variable.setVariableValue("var3", 15);
        assertEquals(15, Variable.getVariableValue("var3"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 10);
        assertEquals(10, Variable.getVariableValue("var2"));
        
        Variable.addVariable("var3");
        Variable.setVariableValue("var3", 15);
        assertEquals(15, Variable.getVariableValue("var3"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(0, Variable.findVariable("var1"));
        
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 10);
        assertEquals(1, Variable.findVariable("var2"));
        
        Variable.addVariable("var3");
        Variable.setVariableValue("var3", 15);
        assertEquals(2, Variable.findVariable("var3"));
    }
    
    @AfterClass
    public static void tearDown() {
        Variable.variableList = null;
    }
}