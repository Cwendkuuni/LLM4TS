import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {
  
  @Before
  public void setUp() {
    // setup code here
  }
  
  @After
  public void tearDown() {
    // cleanup code here
  }
  
  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();
    
    // Test case 1
    String input1 = "var a;\n" +
                    "var b = 1;\n" +
                    "var c = 2;\n";
    String expectedOutput1 = "var a, b = 1, c = 2;";
    String actualOutput1 = c.collapseVarDeclarations(input1);
    assertEquals(expectedOutput1, actualOutput1);
    
    // Test case 2
    String input2 = "a = true;\n" +
                    "b = true;\n" +
                    "var c = true;\n";
    String expectedOutput2 = "var c = b = a = true;";
    String actualOutput2 = c.collapseVarDeclarations(input2);
    assertEquals(expectedOutput2, actualOutput2);
    
    // Additional test cases ...
  }
}