import static org.junit.Assert.*;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    // Typical use case: multiple variable declarations are collapsed into one
    String input = "var a; var b = 1; var c = 2;";
    String expected = "var a, b = 1, c = 2;";
    assertEquals(expected, CollapseVariableDeclarations.collapseVariableDeclarations(input));

    // Edge case: only one variable declaration
    input = "var a;";
    expected = "var a;";
    assertEquals(expected, CollapseVariableDeclarations.collapseVariableDeclarations(input));

    // Edge case: multiple variable declarations with assignments are collapsed into one
    input = "a = true; b = true; var c = true;";
    expected = "var c = b = a = true;";
    assertEquals(expected, CollapseVariableDeclarations.collapseVariableDeclarations(input));

    // Error scenario: invalid input with syntax error
    input = "var a; var b = 1; var c = 2";
    assertNull(CollapseVariableDeclarations.collapseVariableDeclarations(input));
  }
}