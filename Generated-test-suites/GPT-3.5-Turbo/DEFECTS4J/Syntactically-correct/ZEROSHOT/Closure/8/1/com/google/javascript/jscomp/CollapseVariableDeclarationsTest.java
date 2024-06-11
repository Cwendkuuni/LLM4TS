import static org.junit.Assert.*;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {
  
  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations collapse = new CollapseVariableDeclarations();
    
    // Test case 1
    String code1 = "var a;\n" +
                   "var b = 1;\n" +
                   "var c = 2;";
    String expected1 = "var a, b = 1, c = 2;";
    String result1 = collapse.collapseVariableDeclarations(code1);
    assertEquals(expected1, result1);
    
    // Test case 2
    String code2 = "a = true;\n" +
                   "b = true;\n" +
                   "var c = true;";
    String expected2 = "var c = b = a = true;";
    String result2 = collapse.collapseVariableDeclarations(code2);
    assertEquals(expected2, result2);
    
    // Test case 3
    String code3 = "var a;\n" +
                   "var b = 1;\n" +
                   "var c = 2;\n" +
                   "a = true;\n" +
                   "b = true;\n" +
                   "var d = true;";
    String expected3 = "var a, b = 1, c = 2, d = b = a = true;";
    String result3 = collapse.collapseVariableDeclarations(code3);
    assertEquals(expected3, result3);
  }
  
}