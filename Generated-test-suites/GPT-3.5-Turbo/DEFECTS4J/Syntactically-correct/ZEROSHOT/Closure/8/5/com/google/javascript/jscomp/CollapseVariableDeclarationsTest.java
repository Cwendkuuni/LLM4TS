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
  public void testCollapseVariableDeclarations() {
    // Test case 1
    String code1 = "var a;\nvar b = 1;\nvar c = 2;\n";
    String expected1 = "var a, b = 1, c = 2;\n";
    String result1 = collapseVariableDeclarations.collapseVariableDeclarations(code1);
    assertEquals(expected1, result1);
    
    // Test case 2
    String code2 = "a = true;\nb = true;\nvar c = true;\n";
    String expected2 = "var c = b = a = true;\n";
    String result2 = collapseVariableDeclarations.collapseVariableAssignments(code2);
    assertEquals(expected2, result2);
  }
}