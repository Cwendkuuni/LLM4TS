import static org.junit.Assert.*;
import org.junit.Test;

public class VariableTest {

  @Test
  public void testAddVariable() {
    assertTrue(Variable.addVariable("var1")); // Add a new variable
    assertFalse(Variable.addVariable("var1")); // Try adding the same variable again
  }

  @Test
  public void testRemoveVariable() {
    Variable.addVariable("var1");
    Variable.removeVariable("var1"); // Remove an existing variable
    assertFalse(Variable.variableList.contains("var1")); // Check if variable is removed
  }

  @Test
  public void testGetVariableValue() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.getVariableValue("var1")); // Get value of an existing variable
    assertEquals(0, Variable.getVariableValue("var2")); // Get value of a non-existing variable
    assertEquals(10, Variable.getVariableValue("*var1")); // Get the value using a reference variable
  }

  @Test
  public void testSetVariableValue() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10); // Set value of an existing variable
    assertEquals(10, Variable.getVariableValue("var1"));
    Variable.setVariableValue("var2", 20); // Set value of a non-existing variable
    assertEquals(0, Variable.getVariableValue("var2"));
  }

  @Test
  public void testFindVariable() {
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    assertEquals(0, Variable.findVariable("var1")); // Check index of an existing variable
    assertEquals(-1, Variable.findVariable("var2")); // Check index of a non-existing variable
    assertEquals(10, Variable.findVariable("&var1")); // Check value of a reference variable
  }
}
