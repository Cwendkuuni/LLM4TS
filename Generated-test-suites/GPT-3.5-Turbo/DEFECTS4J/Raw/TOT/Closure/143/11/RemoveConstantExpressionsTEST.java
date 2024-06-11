import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setup() {
    removeConstantExpressions = new RemoveConstantExpressions();
  }

  @Test
  public void testProcess_WhenGivenValidExpression_ShouldReplaceWithSideEffectNodes() {
    // Arrange
    String inputJS = "1 + foo() + bar()";
    String expectedJS = "foo();bar()";
    
    // Act
    String actualJS = removeConstantExpressions.process(inputJS);

    // Assert
    assertEquals(expectedJS, actualJS);
  }

  @Test
  public void testProcess_WhenGivenEmptyExpression_ShouldReturnEmptyString() {
    // Arrange
    String inputJS = "";
    String expectedJS = "";
    
    // Act
    String actualJS = removeConstantExpressions.process(inputJS);

    // Assert
    assertEquals(expectedJS, actualJS);
  }

  @Test
  public void testProcess_WhenGivenExpressionWithNoConstants_ShouldReturnInputExpression() {
    // Arrange
    String inputJS = "x + y";
    String expectedJS = "x + y";
    
    // Act
    String actualJS = removeConstantExpressions.process(inputJS);

    // Assert
    assertEquals(expectedJS, actualJS);
  }

  @Test
  public void testProcess_WhenGivenExpressionWithOnlyConstants_ShouldReturnEmptyString() {
    // Arrange
    String inputJS = "1 + 2 + 3";
    String expectedJS = "";
    
    // Act
    String actualJS = removeConstantExpressions.process(inputJS);

    // Assert
    assertEquals(expectedJS, actualJS);
  }

  @Test
  public void testProcess_WhenGivenExpressionWithNestedFunctions_ShouldReplaceWithSideEffectNodes() {
    // Arrange
    String inputJS = "foo(bar(), baz(qux()))";
    String expectedJS = "foo(bar(), qux())";
    
    // Act
    String actualJS = removeConstantExpressions.process(inputJS);

    // Assert
    assertEquals(expectedJS, actualJS);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_WhenGivenNullExpression_ShouldThrowIllegalArgumentException() {
    // Arrange
    String inputJS = null;
    
    // Act
    removeConstantExpressions.process(inputJS);

    // Assert
    // Expecting an exception to be thrown
  }

}