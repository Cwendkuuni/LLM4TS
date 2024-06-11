import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class VariableTest {
  
  @BeforeClass
  public static void setUp() {
    Variable.variableList.clear();
  }
  
  @Test
  public void testAddVariable() {
    assertEquals(true, Variable.addVariable("var1"));
    assertEquals(false, Variable.addVariable("var1"));
  }
  
  @Test
  public void testRemoveVariable() {
    Variable.addVariable("var1");
    Variable.addVariable("var2");
    
    Variable.removeVariable("var1");
    assertEquals(null, Variable.findVariable("var1"));   
  }
  
  @Test
  public void testGetVariableValue() {
    Variable.addVariable("var1");
    Variable variable = Variable.variableList.get(0);
    variable.setValue(5);
    
    assertEquals(0, Variable.getVariableValue("var2"));   
    assertEquals(0, Variable.getVariableValue("*var2"));
    assertEquals(5, Variable.getVariableValue("var1"));  
    assertEquals(5, Variable.getVariableValue("*var1"));  
  }
  
  @Test
  public void testSetVariableValue() {
    Variable.addVariable("var1");
    Variable variable = Variable.variableList.get(0);
    
    Variable.setVariableValue("var2", 10);
    assertEquals(0, variable.getValue());
    Variable.setVariableValue("var1", 5);
    assertEquals(5, variable.getValue());
  }
}