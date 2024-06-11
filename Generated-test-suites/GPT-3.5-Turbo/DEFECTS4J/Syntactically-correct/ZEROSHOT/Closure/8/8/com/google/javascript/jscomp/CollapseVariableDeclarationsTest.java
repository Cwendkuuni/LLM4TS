import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations test = new CollapseVariableDeclarations();
    
    // Test case 1: Collapse multiple variable declarations
    String input1 = "var a;\nvar b = 1;\nvar c = 2;\n";
    String expected1 = "var a, b = 1, c = 2;\n";
    String result1 = test.collapseVariableDeclarations(input1);
    assertEquals(expected1, result1);
    
    // Test case 2: Collapse assigns
    String input2 = "a = true;\nb = true;\nvar c = true;\n";
    String expected2 = "var c = b = a = true;\n";
    String result2 = test.collapseVariableDeclarations(input2);
    assertEquals(expected2, result2);
  }
}