
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

  @Test
  public void testAddVariable() {
    // Test adding a new variable
    assertTrue(Variable.addVariable("x"));
    assertEquals(1, Variable.variableList.size());

    // Test adding an existing variable
    assertFalse(Variable.addVariable("x"));
    assertEquals(1, Variable.variableList.size());
  }

  @Test
  public void testRemoveVariable() {
    // Test removing an existing variable
    Variable.addVariable("x");
    Variable.removeVariable("x");
    assertEquals(0, Variable.variableList.size());

    // Test removing a non-existing variable
    Variable.removeVariable("y");
    assertEquals(0, Variable.variableList.size());
  }

  @Test
  public void testGetVariableValue() {
    // Test getting the value of an existing variable
    Variable.addVariable("x");
    Variable.setVariableValue("x", 5);
    assertEquals(5, Variable.getVariableValue("x"));

    // Test getting the value of a non-existing variable
    assertEquals(0, Variable.getVariableValue("y"));

    // Test getting the value of a referenced variable
    Variable.setVariableValue("x", 10);
    assertEquals(10, Variable.getVariableValue("*x"));
  }

  @Test
  public void testSetVariableValue() {
    // Test setting the value of an existing variable
    Variable.addVariable("x");
    Variable.setVariableValue("x", 5);
    assertEquals(5, Variable.getVariableValue("x"));

    // Test setting the value of a non-existing variable
    Variable.setVariableValue("y", 10);
    assertEquals(0, Variable.getVariableValue("y"));
  }
}
