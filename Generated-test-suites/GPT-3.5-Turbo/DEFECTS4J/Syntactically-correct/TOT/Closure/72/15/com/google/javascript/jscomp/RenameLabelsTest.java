import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {
  
  private RenameLabels renameLabels;
  
  @Before
  public void setUp() {
    renameLabels = new RenameLabels();
  }

  @Test
  public void process_ValidInput_CorrectRenameLabels() {
    // Arrange
    String input = "function foo() { a: { break a; } }";
    String expectedOutput = "function foo() { b: { break b; } }";
    
    // Act
    String actualOutput = renameLabels.process(input);
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void process_EmptyInput_ReturnsEmptyString() {
    // Arrange
    String input = "";
    String expectedOutput = "";
    
    // Act
    String actualOutput = renameLabels.process(input);
    
    // Assert
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void process_InvalidInput_ThrowsException() {
    // Arrange
    String input = "invalid input";
    
    // Act & Assert
    assertThrows(Exception.class, () -> {
      renameLabels.process(input);
    });
  }
  
}