import static org.junit.Assert.*;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

    // Test case 1
    String input1 = "var a; var b = 1; var c = 2;";
    String expectedOutput1 = "var a, b = 1, c = 2;";
    String actualOutput1 = collapseVariableDeclarations.collapseVariableDeclarations(input1);
    assertEquals(expectedOutput1, actualOutput1);

    // Test case 2
    String input2 = "a = true; b = true; var c = true;";
    String expectedOutput2 = "var c = b = a = true;";
    String actualOutput2 = collapseVariableDeclarations.collapseVariableDeclarations(input2);
    assertEquals(expectedOutput2, actualOutput2);
  }
}