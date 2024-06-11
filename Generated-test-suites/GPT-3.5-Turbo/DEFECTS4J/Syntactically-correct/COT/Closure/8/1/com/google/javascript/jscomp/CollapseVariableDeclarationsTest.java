import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    // Test case 1: No variable declarations
    String input1 = "var a = 1;";
    String expectedOutput1 = "var a = 1;";
    assertEquals(expectedOutput1, collapseVariableDeclarations.collapse(input1));

    // Test case 2: Multiple variable declarations
    String input2 = "var a; var b = 2; var c = 3;";
    String expectedOutput2 = "var a, b = 2, c = 3;";
    assertEquals(expectedOutput2, collapseVariableDeclarations.collapse(input2));

    // Test case 3: Variable declarations with assignments
    String input3 = "var a = 1; b = 2; var c = 3;";
    String expectedOutput3 = "var a = 1, b = 2, c = 3;";
    assertEquals(expectedOutput3, collapseVariableDeclarations.collapse(input3));
  }

  @Test
  public void testExceptions() {
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    // Test case 1: Empty input
    String input1 = "";
    String expectedOutput1 = "";
    assertEquals(expectedOutput1, collapseVariableDeclarations.collapse(input1));

    // Test case 2: No variable declarations
    String input2 = "a = 1;";
    String expectedOutput2 = "a = 1;";
    assertEquals(expectedOutput2, collapseVariableDeclarations.collapse(input2));
  }
}