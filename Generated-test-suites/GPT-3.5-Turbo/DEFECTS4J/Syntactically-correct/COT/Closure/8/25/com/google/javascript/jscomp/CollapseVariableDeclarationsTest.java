
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    // Test basic functionality
    String input1 = "var a;\nvar b = 1;\nvar c = 2;\n";
    String expected1 = "var a, b = 1, c = 2;\n";
    assertEquals(expected1, collapseVariableDeclarations.collapse(input1));

    // Test collapsing assigns
    String input2 = "a = true;\nb = true;\nvar c = true;\n";
    String expected2 = "var c = b = a = true;\n";
    assertEquals(expected2, collapseVariableDeclarations.collapse(input2));

    // Test collapsing assigns with redeclaration
    String input3 = "var a;\na = true;\nb = true;\nvar c = true;\n";
    String expected3 = "var a, c = b = a = true;\n";
    assertEquals(expected3, collapseVariableDeclarations.collapse(input3));
  }
}
