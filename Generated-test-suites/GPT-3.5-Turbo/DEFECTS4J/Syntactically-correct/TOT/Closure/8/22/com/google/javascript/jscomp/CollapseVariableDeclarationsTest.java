import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {
  
  private CollapseVariableDeclarations collapseVariableDeclarations;
  
  @Before
  public void setUp() {
    collapseVariableDeclarations = new CollapseVariableDeclarations();
  }
  
  @After
  public void tearDown() {
    collapseVariableDeclarations = null;
  }
  
  @Test
  public void testProcess_SingleVariableDeclaration_CorrectlyCollapsed() {
    // Arrange
    String inputCode = "var a; var b = 1; var c = 2;";
    String expectedOutputCode = "var a, b = 1, c = 2;";
    
    // Act
    String actualOutputCode = collapseVariableDeclarations.process(inputCode);
    
    // Assert
    assertEquals(expectedOutputCode, actualOutputCode);
  }
  
  @Test
  public void testProcess_MultipleVariableDeclarations_CorrectlyCollapsed() {
    // Arrange
    String inputCode = "var a; var b = 1; var c = 2; var d = 3; var e;";
    String expectedOutputCode = "var a, b = 1, c = 2, d = 3, e;";
    
    // Act
    String actualOutputCode = collapseVariableDeclarations.process(inputCode);
    
    // Assert
    assertEquals(expectedOutputCode, actualOutputCode);
  }
  
  @Test
  public void testProcess_VariableAssignmentsAndDeclarations_CorrectlyCollapsed() {
    // Arrange
    String inputCode = "a = true; b = true; var c = true;";
    String expectedOutputCode = "var c = b = a = true;";
    
    // Act
    String actualOutputCode = collapseVariableDeclarations.process(inputCode);
    
    // Assert
    assertEquals(expectedOutputCode, actualOutputCode);
  }
  
  @Test
  public void testProcess_NoVariableDeclarationsOrAssignments_CodeUnchanged() {
    // Arrange
    String inputCode = "function test() { return 'testing'; }";
    
    // Act
    String actualOutputCode = collapseVariableDeclarations.process(inputCode);
    
    // Assert
    assertEquals(inputCode, actualOutputCode);
  }
  
}