import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class VariableTest {

  @Test
  public void testAddVariable() {
    // Test adding a variable that does not already exist
    assertTrue(Variable.addVariable("var1"));

    // Test adding a variable that already exists
    assertFalse(Variable.addVariable("var1"));
  }

  @Test
  public void testRemoveVariable() {
    // Test removing a variable that exists
    Variable.addVariable("var1");
    Variable.removeVariable("var1");
    assertEquals(0, Variable.getVariableValue("var1"));

    // Test removing a variable that does not exist
    Variable.removeVariable("var2");
    assertEquals(0, Variable.getVariableValue("var2"));
  }

  @Test
  public void testGetVariableValue() {
    // Test getting the value of a variable that exists
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.getVariableValue("var1"));

    // Test getting the value of a variable that does not exist
    assertEquals(0, Variable.getVariableValue("var2"));

    // Test getting the value of a reference variable
    Variable.setVariableValue("var2", 20);
    assertEquals(20, Variable.getVariableValue("*var2"));
  }

  @Test
  public void testSetVariableValue() {
    // Test setting the value of a variable that exists
    Variable.addVariable("var1");
    Variable.setVariableValue("var1", 10);
    assertEquals(10, Variable.getVariableValue("var1"));

    // Test setting the value of a variable that does not exist
    Variable.setVariableValue("var2", 20);
    assertEquals(0, Variable.getVariableValue("var2"));
  }

}