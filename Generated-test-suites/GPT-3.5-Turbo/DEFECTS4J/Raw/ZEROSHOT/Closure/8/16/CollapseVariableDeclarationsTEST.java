import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  private CollapseVariableDeclarations collapseVariableDeclarations;

  @Before
  public void setUp() {
    collapseVariableDeclarations = new CollapseVariableDeclarations();
  }

  @Test
  public void testProcess_EmptyInput() {
    String input = "";
    String expectedOutput = "";
    String actualOutput = collapseVariableDeclarations.process(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcess_VariableDeclaration() {
    String input = "var a;\nvar b = 1;\nvar c = 2;\n";
    String expectedOutput = "var a, b = 1, c = 2;\n";
    String actualOutput = collapseVariableDeclarations.process(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcess_VariableAssignment() {
    String input = "a = true;\nb = true;\nvar c = true;\n";
    String expectedOutput = "var c = b = a = true;\n";
    String actualOutput = collapseVariableDeclarations.process(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcess_VariableDeclarationAndAssignment() {
    String input = "var a;\nvar b = 1;\na = true;\nvar c = 2;\nb = false;\n";
    String expectedOutput = "var a, b = 1, c = 2;\na = true;\nb = false;\n";
    String actualOutput = collapseVariableDeclarations.process(input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcess_NoCollapses() {
    String input = "var a;\nvar b = 1;\nvar c = 2;\nvar d = true;\n";
    String expectedOutput = "var a;\nvar b = 1;\nvar c = 2;\nvar d = true;\n";
    String actualOutput = collapseVariableDeclarations.process(input);
    assertEquals(expectedOutput, actualOutput);
  }

}