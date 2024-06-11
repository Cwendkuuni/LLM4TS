
import org.junit.Assert;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {
  
  @Test
  public void testCollapseVariableDeclarations() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();
    
    // Test 1: Basic variable declarations
    String code1 = "var a; var b = 1; var c = 2;";
    String expected1 = "var a, b = 1, c = 2;";
    String actual1 = c.collapseVariableDeclarations(code1);
    Assert.assertEquals(expected1, actual1);
    
    // Test 2: Variable declarations with reassignment
    String code2 = "a = true; b = true; var c = true;";
    String expected2 = "var c = b = a = true;";
    String actual2 = c.collapseVariableDeclarations(code2);
    Assert.assertEquals(expected2, actual2);
  }
  
  @Test
  public void testCollapseVariableDeclarationsEdgeCases() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();
    
    // Test 1: Empty input
    String code1 = "";
    String expected1 = "";
    String actual1 = c.collapseVariableDeclarations(code1);
    Assert.assertEquals(expected1, actual1);
    
    // Test 2: Input with no variable declarations
    String code2 = "var a = 1; var b = 2;";
    String expected2 = "var a = 1; var b = 2;";
    String actual2 = c.collapseVariableDeclarations(code2);
    Assert.assertEquals(expected2, actual2);
    
    // Test 3: Input with only one variable declaration
    String code3 = "var a;";
    String expected3 = "var a;";
    String actual3 = c.collapseVariableDeclarations(code3);
    Assert.assertEquals(expected3, actual3);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCollapseVariableDeclarationsExceptions() {
    CollapseVariableDeclarations c = new CollapseVariableDeclarations();
    
    // Test 1: Null input
    String code1 = null;
    c.collapseVariableDeclarations(code1);
  }
}
