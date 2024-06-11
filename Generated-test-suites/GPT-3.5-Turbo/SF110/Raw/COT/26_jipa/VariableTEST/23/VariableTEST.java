
package jipa;

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
  }
  
  @Test
  public void testRemoveVariable() {
    Variable.addVariable("var1");
    Variable.removeVariable("var1");
    assertTrue(Variable.variableList.isEmpty());
  }
  
  @Test
  public void testGetVariableValue() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.getVariableValue("var1"));
    assertEquals(0, Variable.getVariableValue("var2"));
  }
  
  @Test
  public void testSetVariableValue() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.variableList.get(0).getValue());
    Variable.setVariableValue("var2", 20);
    assertEquals(0, Variable.variableList.size());
  }

  @Test
  public void testFindVariable() {
    Variable.addVariable("var1");
    assertEquals(0, Variable.findVariable("var1"));
    assertEquals(-1, Variable.findVariable("var2"));
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.findVariable("*var1"));
    assertEquals(-1, Variable.findVariable("*var2"));
    Variable.setVariableValue("var1", 20);
    assertEquals(20, Variable.findVariable("*&var1"));
  }
  
  @AfterClass
  public static void tearDown() {
    Variable.variableList = null;
  }

}
