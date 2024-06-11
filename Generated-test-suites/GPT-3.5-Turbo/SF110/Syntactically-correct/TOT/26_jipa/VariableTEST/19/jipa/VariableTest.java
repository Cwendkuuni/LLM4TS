
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {
    
    @Before
    public void setUp() {
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable_withValidName_shouldAddVariableToList() {
        assertTrue(Variable.addVariable("variable1"));
        assertEquals(1, Variable.variableList.size());
        assertEquals("variable1", Variable.variableList.get(0).getName());
        assertEquals(0, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testAddVariable_withExistingName_shouldNotAddVariableToList() {
        Variable.variableList.add(new Variable("variable1"));
        assertFalse(Variable.addVariable("variable1"));
        assertEquals(1, Variable.variableList.size());
    }
    
    @Test
    public void testRemoveVariable_withExistingName_shouldRemoveVariableFromList() {
        Variable.variableList.add(new Variable("variable1"));
        assertEquals(1, Variable.variableList.size());
        
        Variable.removeVariable("variable1");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testRemoveVariable_withNonExistingName_shouldNotRemoveVariableFromList() {
        Variable.variableList.add(new Variable("variable1"));
        assertEquals(1, Variable.variableList.size());
        
        Variable.removeVariable("variable2");
        assertEquals(1, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue_withExistingName_shouldReturnValueOfVariable() {
        Variable.variableList.add(new Variable("variable1"));
        Variable.variableList.get(0).setValue(10);
        
        int result = Variable.getVariableValue("variable1");
        assertEquals(10, result);
    }
    
    @Test
    public void testGetVariableValue_withNonExistingName_shouldReturnDefaultValue() {
        int result = Variable.getVariableValue("variable1");
        assertEquals(0, result);
    }
    
    @Test
    public void testGetVariableValue_withReferenceName_shouldReturnReferencedVariableValue() {
        Variable.variableList.add(new Variable("variable1"));
        Variable.variableList.get(0).setValue(10);
        
        int result = Variable.getVariableValue("*variable1");
        assertEquals(10, result);
    }
    
    @Test
    public void testSetVariableValue_withExistingName_shouldSetVariableValue() {
        Variable.variableList.add(new Variable("variable1"));
        
        Variable.setVariableValue("variable1", 10);
        assertEquals(10, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testSetVariableValue_withNonExistingName_shouldNotSetVariableValue() {
        Variable.variableList.add(new Variable("variable1"));
        
        Variable.setVariableValue("variable2", 10);
        assertEquals(0, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testFindVariable_withExistingName_shouldReturnIndex() {
        Variable.variableList.add(new Variable("variable1"));
        Variable.variableList.add(new Variable("variable2"));
        
        int result = Variable.findVariable("variable2");
        assertEquals(1, result);
    }
    
    @Test
    public void testFindVariable_withNonExistingName_shouldReturnNegativeOne() {
        Variable.variableList.add(new Variable("variable1"));
        Variable.variableList.add(new Variable("variable2"));
        
        int result = Variable.findVariable("variable3");
        assertEquals(-1, result);
    }
    
    @Test
    public void testFindVariable_withReferenceName_shouldReturnValueOfReferencedVariable() {
        Variable.variableList.add(new Variable("variable1"));
        Variable.variableList.get(0).setValue(10);
        
        int result = Variable.findVariable("&variable1");
        assertEquals(10, result);
    }
}
