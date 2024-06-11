import org.junit.*;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations_singleDeclaration() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();
    String input = "var a;";
    String expectedOutput = "var a;";
    String actualOutput = c.collapseDeclarations(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testCollapseVariableDeclarations_multipleDeclarations() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();
    String input = "var a;\nvar b = 1;\nvar c = 2;\n";
    String expectedOutput = "var a, b = 1, c = 2;";
    String actualOutput = c.collapseDeclarations(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testCollapseVariableDeclarations_multipleAssignments() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();
    String input = "a = true;\nb = true;\nvar c = true;\n";
    String expectedOutput = "var c = b = a = true;";
    String actualOutput = c.collapseDeclarations(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testCollapseVariableDeclarations_mixDeclarationsAndAssignments() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();
    String input = "var a;\na = true;\nvar b = 1;\nvar c = 2;\na = false;\n";
    String expectedOutput = "var a, b = 1, c = 2;\na = false;";
    String actualOutput = c.collapseDeclarations(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testCollapseVariableDeclarations_noDeclarations() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();
    String input = "a = true;\nb = true;\nc = true;\n";
    String expectedOutput = "a = true;\nb = true;\nc = true;";
    String actualOutput = c.collapseDeclarations(input);
    assertEquals(expectedOutput, actualOutput);
  }

}