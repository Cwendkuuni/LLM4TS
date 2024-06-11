import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    // Test case 1
    String input1 = "var a;\nvar b = 1;\nvar c = 2;";
    String expected1 = "var a, b = 1, c = 2;";
    assertEquals(expected1, CollapseVariableDeclarations.collapseVariableDeclarations(input1));

    // Test case 2
    String input2 = "a = true;\nb = true;\nvar c = true;";
    String expected2 = "var c = b = a = true;";
    assertEquals(expected2, CollapseVariableDeclarations.collapseVariableDeclarations(input2));
  }

  @Test
  public void testCollapseVariableDeclarations_emptyInput() {
    String input = "";
    String expected = "";
    assertEquals(expected, CollapseVariableDeclarations.collapseVariableDeclarations(input));
  }

  @Test
  public void testCollapseVariableDeclarations_singleDeclaration() {
    // Test case 1
    String input1 = "var a;";
    String expected1 = "var a;";
    assertEquals(expected1, CollapseVariableDeclarations.collapseVariableDeclarations(input1));

    // Test case 2
    String input2 = "var b = 1;";
    String expected2 = "var b = 1;";
    assertEquals(expected2, CollapseVariableDeclarations.collapseVariableDeclarations(input2));
  }

  @Test
  public void testCollapseVariableDeclarations_singleAssignment() {
    // Test case 1
    String input1 = "a = true;";
    String expected1 = "a = true;";
    assertEquals(expected1, CollapseVariableDeclarations.collapseVariableDeclarations(input1));

    // Test case 2
    String input2 = "b = true;";
    String expected2 = "b = true;";
    assertEquals(expected2, CollapseVariableDeclarations.collapseVariableDeclarations(input2));
  }

  @Test
  public void testCollapseVariableDeclarations_mixedDeclarationAndAssignment() {
    // Test case 1
    String input1 = "var a;\nb = true;\nvar c = 2;";
    String expected1 = "var a, c = 2;\nb = true;";
    assertEquals(expected1, CollapseVariableDeclarations.collapseVariableDeclarations(input1));

    // Test case 2
    String input2 = "a = true;\nvar b = 1;\nb = false;";
    String expected2 = "var b = 1;\na = true;\nb = false;";
    assertEquals(expected2, CollapseVariableDeclarations.collapseVariableDeclarations(input2));
  }
}