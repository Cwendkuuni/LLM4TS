import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    checkSideEffects = new CheckSideEffects();
  }

  @Test
  public void testProcess_ValidCode_ReturnsNoWarnings() {
    // Arrange
    String code = "var a = 5;";
    // Act
    checkSideEffects.process(code);
    // Assert
    assertTrue(checkSideEffects.getProblemNodes().isEmpty());
  }

  @Test
  public void testProcess_SuspiciousCode_ReturnsWarning() {
    // Arrange
    String code = "var a = 'this is ' 'not a bug';";
    // Act
    checkSideEffects.process(code);
    // Assert
    assertFalse(checkSideEffects.getProblemNodes().isEmpty());
    // Additional assertion for the warning message
    String warningMessage = checkSideEffects.getProblemNodes().get(0).getWarningMessage();
    assertTrue(warningMessage.contains("Is there a missing '+' on the previous line?"));
  }

  @Test
  public void testHotSwapScript_ValidCode_ReturnsNoWarnings() {
    // Arrange
    String code = "var a = 5;";
    // Act
    checkSideEffects.hotSwapScript(code);
    // Assert
    assertTrue(checkSideEffects.getProblemNodes().isEmpty());
  }

  @Test
  public void testHotSwapScript_SuspiciousCode_ReturnsWarning() {
    // Arrange
    String code = "var a = 'this is ' 'not a bug';";
    // Act
    checkSideEffects.hotSwapScript(code);
    // Assert
    assertFalse(checkSideEffects.getProblemNodes().isEmpty());
    // Additional assertion for the warning message
    String warningMessage = checkSideEffects.getProblemNodes().get(0).getWarningMessage();
    assertTrue(warningMessage.contains("Is there a missing '+' on the previous line?"));
  }

}