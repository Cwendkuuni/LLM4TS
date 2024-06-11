import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseSingleVariableDeclaration() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();
    // Test case for collapsing single variable declaration
    // var a;
    String input = "var a;";
    assertEquals("var a;", collapse.collapseVariableDeclarations(input));
  }

  @Test
  public void testCollapseMultipleVariableDeclarations() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();
    // Test case for collapsing multiple variable declarations
    // var a;
    // var b = 1;
    // var c = 2;
    String input = "var a;\n var b = 1;\n var c = 2;";
    assertEquals("var a, b = 1, c = 2;", collapse.collapseVariableDeclarations(input));
  }

  @Test
  public void testCollapseVariableAssignments() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();
    // Test case for collapsing variable assignments
    // a = true;
    // b = true;
    // var c = true;
    String input = "a = true;\n b = true;\n var c = true;";
    assertEquals("var c = b = a = true;", collapse.collapseVariableDeclarations(input));
  }

  @Test
  public void testCollapseMixedVariableDeclarations() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();
    // Test case for collapsing mixed variable declarations and assignments
    // var a;
    // b = 1;
    // a = true;
    String input = "var a;\n b = 1;\n a = true;";
    assertEquals("var a, b = 1; a = true;", collapse.collapseVariableDeclarations(input));
  }

  @Test
  public void testCollapseNoVariableDeclarations() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();
    // Test case for empty input
    String input = "";
    assertEquals("", collapse.collapseVariableDeclarations(input));
  }

  @Test
  public void testCollapseNestedVariableDeclarations() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();
    // Test case for nested variable declarations
    // var a;
    // if (condition) {
    //   var b;
    // }
    // var c;
    String input = "var a;\n if (condition) {\n var b;\n }\n var c;";
    assertEquals("var a;\n if (condition) {\n var b;\n }\n var c;", collapse.collapseVariableDeclarations(input));
  }

  @Test
  public void testCollapseAdjacentVariableDeclarations() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();
    // Test case for adjacent variable declarations
    // var a;
    // var b;
    // var c;
    String input = "var a;\n var b;\n var c;";
    assertEquals("var a, b, c;", collapse.collapseVariableDeclarations(input));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCollapseInvalidInput() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();
    // Test case for invalid input
    String input = "var a = 1;\n b = 2;";
    collapse.collapseVariableDeclarations(input);
  }
}