package jipa;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class VariableTest {
  
  @Test
  public void testAddVariable() {
    // Test adding a new variable
    assertTrue(Variable.addVariable("var1"));
    
    // Test adding an existing variable
    assertFalse(Variable.addVariable("var1"));
  }
  
  @Test
  public void testRemoveVariable() {
    // Test removing an existing variable
    Variable.addVariable("var1");
    Variable.removeVariable("var1");
    
    // Test removing a non-existing variable
    assertNull(Variable.variableList);
  }
  
  @Test
  public void testGetVariableValue() {
    // Test getting the value of an existing variable
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.getVariableValue("var1"));
    
    // Test getting the value of a non-existing variable
    assertEquals(0, Variable.getVariableValue("var2"));
    
    // Test getting the value of a referenced variable
    assertEquals(10, Variable.getVariableValue("*var1"));
  }
  
  @Test
  public void testSetVariableValue() {
    // Test setting the value of an existing variable
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.getVariableValue("var1"));
    
    // Test setting the value of a non-existing variable
    Variable.setVariableValue("var2", 20);
    assertNull(Variable.variableList);
  }
  
  @Test
  public void testFindVariable() {
    // Test finding an existing variable
    Variable.addVariable("var1");
    assertEquals(0, Variable.findVariable("var1"));
    
    // Test finding a non-existing variable
    assertEquals(-1, Variable.findVariable("var2"));
    
    // Test finding a referenced variable
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.findVariable("&var1"));
  }
}