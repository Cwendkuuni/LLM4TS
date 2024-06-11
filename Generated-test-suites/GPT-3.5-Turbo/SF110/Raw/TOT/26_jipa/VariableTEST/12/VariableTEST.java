import org.junit.Test;
import java.util.*;

public class VariableTest {

    @Test
    public void testAddVariable_typical() {
        String varName = "var1";
        
        boolean result = Variable.addVariable(varName);
        
        assertTrue(result);
    }
    
    @Test
    public void testAddVariable_alreadyExists() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        String varName = "var1";
        
        boolean result = Variable.addVariable(varName);
        
        assertFalse(result);
    }
    
    @Test
    public void testRemoveVariable_typical() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        String varName = "var1";
        
        Variable.removeVariable(varName);
        
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testRemoveVariable_notFound() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        String varName = "var2";
        
        Variable.removeVariable(varName);
        
        assertEquals(1, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue_typical() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        Variable.variableList.get(0).setValue(5);
        String varName = "var1";
        
        int result = Variable.getVariableValue(varName);
        
        assertEquals(5, result);
    }
    
    @Test
    public void testGetVariableValue_notFound() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        String varName = "var2";
        
        int result = Variable.getVariableValue(varName);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testGetVariableValue_byReference() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        Variable.variableList.get(0).setValue(5);
        String varName = "*var1";
        
        int result = Variable.getVariableValue(varName);
        
        assertEquals(5, result);
    }
    
    @Test
    public void testSetVariableValue_typical() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        String varName = "var1";
        int varValue = 5;
        
        Variable.setVariableValue(varName, varValue);
        
        assertEquals(5, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testSetVariableValue_notFound() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        String varName = "var2";
        int varValue = 5;
        
        Variable.setVariableValue(varName, varValue);
        
        assertEquals(0, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testFindVariable_typical() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        String varName = "var1";
        
        int result = Variable.findVariable(varName);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testFindVariable_notFound() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        String varName = "var2";
        
        int result = Variable.findVariable(varName);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testFindVariable_byReference() {
        Variable.variableList = new ArrayList<Variable>();
        Variable.variableList.add(new Variable("var1"));
        Variable.variableList.get(0).setValue(5);
        String varName = "&var1";
        
        int result = Variable.findVariable(varName);
        
        assertEquals(5, result);
    }
}