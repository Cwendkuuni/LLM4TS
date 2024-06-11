
import org.junit.*;
import java.util.*;

public class VariableTest {
    
    @BeforeClass
    public static void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @Test
    public void testAddVariable_withNonExistingVarName_returnsTrue() {
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
        assertEquals("var1", Variable.variableList.get(0).getName());
        assertSame(0, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testAddVariable_withExistingVarName_returnsFalse() {
        Variable.addVariable("var2");
        
        assertFalse(Variable.addVariable("var2"));
        assertEquals(1, Variable.variableList.size());
        assertEquals("var2", Variable.variableList.get(0).getName());
        assertSame(0, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testRemoveVariable_withExistingVarName_removesVariable() {
        Variable.addVariable("var3");
        Variable.removeVariable("var3");
        
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testRemoveVariable_withNonExistingVarName_doesNothing() {
        Variable.removeVariable("var4");
        
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue_withExistingVarName_returnsValue() {
        Variable.addVariable("var5");
        Variable.setVariableValue("var5", 10);
        
        assertEquals(10, Variable.getVariableValue("var5"));
    }
    
    @Test
    public void testGetVariableValue_withNonExistingVarName_returnsZero() {
        assertEquals(0, Variable.getVariableValue("var6"));
    }
    
    @Test
    public void testGetVariableValue_withReferenceVarName_returnsReferencedValue() {
        Variable.addVariable("var7");
        Variable.setVariableValue("var7", 20);
        
        assertEquals(20, Variable.getVariableValue("&var7"));
    }
    
    @Test
    public void testSetVariableValue_withExistingVarName_setsValue() {
        Variable.addVariable("var8");
        Variable.setVariableValue("var8", 30);
        
        assertEquals(30, Variable.getVariableValue("var8"));
    }
    
    @Test
    public void testSetVariableValue_withNonExistingVarName_doesNothing() {
        Variable.setVariableValue("var9", 40);
        
        assertEquals(0, Variable.variableList.size());
    }
    
    @AfterClass
    public static void tearDown() {
        Variable.variableList = null;
    }
}
