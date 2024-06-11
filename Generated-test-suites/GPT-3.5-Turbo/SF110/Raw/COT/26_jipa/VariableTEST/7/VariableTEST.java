import org.junit.*;
import static org.junit.Assert.*;

public class VariableTest {

  @Test
  public void testAddVariable() {
    assertTrue(Variable.addVariable("var1"));
    assertFalse(Variable.addVariable("var1"));
  }
  
  @Test
  public void testRemoveVariable() {
    Variable.addVariable("var1");
    Variable.addVariable("var2");
    Variable.addVariable("var3");
    
    Variable.removeVariable("var2");
    assertFalse(Variable.variableList.contains("var2"));
    
    Variable.removeVariable("var4");
    assertFalse(Variable.variableList.contains("var4"));
  }
  
  @Test
  public void testGetVariableValue() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.getVariableValue("var1"));
    
    Variable.setVariableValue("var1", 20);
    assertEquals(20, Variable.getVariableValue("var1"));
    
    assertEquals(0, Variable.getVariableValue("var2"));
    
    Variable.addVariable("var3");
    assertEquals(0, Variable.getVariableValue("*var3")); 
  }
  
  @Test
  public void testSetVariableValue() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.getVariableValue("var1"));
    
    Variable.setVariableValue("var1", 20);
    assertEquals(20, Variable.getVariableValue("var1"));
    
    Variable.setVariableValue("var2", 30);
    assertEquals(0, Variable.getVariableValue("var2"));
  }
  
  @Test
  public void testFindVariable() {
    Variable.addVariable("var1");
    Variable.addVariable("var2");
    Variable.addVariable("var3");
    
    assertEquals(0, Variable.findVariable("var1"));
    assertEquals(1, Variable.findVariable("var2"));
    assertEquals(-1, Variable.findVariable("var4"));
    
    Variable.setVariableValue("var3", 10);
    assertEquals(10, Variable.findVariable("&var3"));
  }
}