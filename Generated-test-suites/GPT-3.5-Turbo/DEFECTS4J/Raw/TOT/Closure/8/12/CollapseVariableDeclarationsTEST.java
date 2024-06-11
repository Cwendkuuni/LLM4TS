import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();

    // Test case for collapsing multiple variable declarations
    String input1 = "var a;\nvar b = 1;\nvar c = 2;";
    String expected1 = "var a, b = 1, c = 2;";
    assertEquals(expected1, c.collapseVariableDeclarations(input1));

    // Test case for collapsing assigns with variable declarations
    String input2 = "a = true;\nb = true;\nvar c = true;";
    String expected2 = "var c = b = a = true;";
    assertEquals(expected2, c.collapseVariableDeclarations(input2));
  }

  @Test
  public void testCollapseVariableDeclarationsWithEdgeCases() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();

    // Test case with empty input
    String input1 = "";
    String expected1 = "";
    assertEquals(expected1, c.collapseVariableDeclarations(input1));

    // Test case with single variable declaration
    String input2 = "var a;";
    String expected2 = "var a;";
    assertEquals(expected2, c.collapseVariableDeclarations(input2));

    // Test case with single assign
    String input3 = "a = true;";
    String expected3 = "a = true;";
    assertEquals(expected3, c.collapseVariableDeclarations(input3));

    // Test case with variable declaration and assign
    String input4 = "var a;\na = true;";
    String expected4 = "var a = true;";
    assertEquals(expected4, c.collapseVariableDeclarations(input4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCollapseVariableDeclarationsWithInvalidInput() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();

    // Test case with invalid input (missing semicolon)
    String input1 = "var a\nvar b = 1;\nvar c = 2;";
    c.collapseVariableDeclarations(input1);
  }
}