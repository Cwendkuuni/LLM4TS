import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {
    @Test
    public void addVariable_test() {
        Variable.addVariable("var1");
        assertTrue(Variable.variableList.size() == 1);
        
        Variable.addVariable("var2");
        assertTrue(Variable.variableList.size() == 2);
        
        assertFalse(Variable.addVariable("var1")); //duplicate variable name
        
        assertTrue(Variable.addVariable("var3")); //adding a new variable
        
        assertFalse(Variable.addVariable(null)); //null variable name
    }
    
    @Test
    public void removeVariable_test() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");
        
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        
        Variable.removeVariable("var4"); //removing a non-existing variable
        
        assertTrue(Variable.variableList.size() == 2);
    }
    
    @Test
    public void getVariableValue_test() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);
        
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(20, Variable.getVariableValue("*var2"));
        
        assertEquals(0, Variable.getVariableValue("var3")); //non-existing variable
        
        Variable.setVariableValue("var2", -10);
        assertEquals(-10, Variable.getVariableValue("var2"));
    }
    
    @Test
    public void setVariableValue_test() {
        Variable.addVariable("var1");
        
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        
        Variable.setVariableValue("var2", 10); //setting value for non-existing variable
        
        Variable.setVariableValue("*var1", -5);
        assertEquals(-5, Variable.getVariableValue("var1")); //setting referenced value
    }
    
    @Test
    public void findVariable_test() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        assertEquals(0, Variable.findVariable("var1"));
        
        Variable.setVariableValue("var2", 10);
        assertEquals(10, Variable.findVariable("*var2"));
        
        assertEquals(-1, Variable.findVariable("var3")); //non-existing variable
    }
}