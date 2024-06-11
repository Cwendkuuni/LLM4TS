import static org.junit.Assert.*;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();

    // Test case 1
    String expected1 = "var a, b = 1, c = 2;";
    String input1 = "var a;\nvar b = 1;\nvar c = 2;";
    String output1 = collapse.collapseVariableDeclarations(input1);
    assertEquals(expected1, output1);

    // Test case 2
    String expected2 = "var c = b = a = true;";
    String input2 = "a = true;\nb = true;\nvar c = true;";
    String output2 = collapse.collapseVariableDeclarations(input2);
    assertEquals(expected2, output2);
  }
}