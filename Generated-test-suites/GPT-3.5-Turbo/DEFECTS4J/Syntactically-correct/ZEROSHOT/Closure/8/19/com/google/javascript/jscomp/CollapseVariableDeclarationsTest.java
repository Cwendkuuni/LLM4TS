import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

  private CollapseVariableDeclarations testInstance;

  @Before
  public void setUp() {
    testInstance = new CollapseVariableDeclarations();
  }

  @Test
  public void testProcess_withNoCollapses_shouldNotChangeCode() {
    // Arrange
    String code = "var a; var b = 1; var c = 2;";
    String expected = "var a; var b = 1; var c = 2;";

    // Act
    String result = testInstance.process(code);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_withCollapsibleVariableDeclarations_shouldCollapseDeclarations() {
    // Arrange
    String code = "var a; var b = 1; var c = 2;";
    String expected = "var a, b = 1, c = 2;";

    // Act
    String result = testInstance.process(code);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_withCollapsibleAssignments_shouldCollapseAssignments() {
    // Arrange
    String code = "a = true; b = true; var c = true;";
    String expected = "var c = b = a = true;";

    // Act
    String result = testInstance.process(code);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_withMixedCollapses_shouldCollapseDeclarationsAndAssignments() {
    // Arrange
    String code = "var a = 1; a = 2; var b = 3; b = 4; var c = 5; c = 6;";
    String expected = "var a = 1, b = a = 2, c = b = 3; c = 4; var c = 5; c = 6;";

    // Act
    String result = testInstance.process(code);

    // Assert
    assertEquals(expected, result);
  }
}