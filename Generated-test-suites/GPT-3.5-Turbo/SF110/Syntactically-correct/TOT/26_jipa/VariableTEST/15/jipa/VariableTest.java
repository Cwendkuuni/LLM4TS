
package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class VariableTest {
  
  @Before
  public void setUp() {
    Variable.variableList = new ArrayList<Variable>();
  }
  
  @Test
  public void testAddVariable_typicalCase() {
    boolean result = Variable.addVariable("var1");
    assertTrue(result);
    assertEquals(1, Variable.variableList.size());
    assertEquals("var1", Variable.variableList.get(0).getName());
    assertEquals(0, Variable.variableList.get(0).getValue());
  }
  
  @Test
  public void testAddVariable_alreadyExists() {
    Variable.addVariable("var1");
    boolean result = Variable.addVariable("var1");
    assertFalse(result);
    assertEquals(1, Variable.variableList.size());
  }
  
  @Test
  public void testRemoveVariable_typicalCase() {
    Variable.addVariable("var1");
    Variable.addVariable("var2");
    Variable.removeVariable("var1");
    assertEquals(1, Variable.variableList.size());
    assertEquals("var2", Variable.variableList.get(0).getName());
  }
  
  @Test
  public void testRemoveVariable_doesNotExist() {
    Variable.addVariable("var1");
    Variable.removeVariable("var2");
    assertEquals(1, Variable.variableList.size());
    assertEquals("var1", Variable.variableList.get(0).getName());
  }
  
  @Test
  public void testGetVariableValue_typicalCase() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    int result = Variable.getVariableValue("var1");
    assertEquals(10, result);
  }
  
  @Test
  public void testGetVariableValue_variableNotFound() {
    int result = Variable.getVariableValue("var1");
    assertEquals(0, result);
  }
  
  @Test
  public void testGetVariableValue_referenceVariable() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    int result = Variable.getVariableValue("*var1");
    assertEquals(10, result);
  }
  
  @Test
  public void testSetVariableValue_typicalCase() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    int result = Variable.getVariableValue("var1");
    assertEquals(10, result);
  }
  
  @Test
  public void testSetVariableValue_variableNotFound() {
    Variable.setVariableValue("var1", 10);
    assertEquals(0, Variable.variableList.size());
  }
  
  @Test
  public void testFindVariable_typicalCase() {
    Variable.addVariable("var1");
    int result = Variable.findVariable("var1");
    assertEquals(0, result);
  }
  
  @Test
  public void testFindVariable_variableNotFound() {
    int result = Variable.findVariable("var1");
    assertEquals(-1, result);
  }
  
  @Test
  public void testFindVariable_referenceVariable() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    int result = Variable.findVariable("&var1");
    assertEquals(10, result);
  }
  
}
