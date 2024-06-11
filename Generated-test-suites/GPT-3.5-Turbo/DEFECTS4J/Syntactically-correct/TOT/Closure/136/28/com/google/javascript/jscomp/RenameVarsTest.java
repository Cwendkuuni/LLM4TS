import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameVarsTest {

  private RenameVars renameVars;

  @Before
  public void setUp() {
    renameVars = new RenameVars();
  }

  @Test
  public void testProcess_LocalVariableAssignment_ShouldRenameLocalVariable() {
    // Arrange
    String input = "int a = 1;";
    String expectedOutput = "int b = 1;";

    // Act
    String output = renameVars.process(input);

    // Assert
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcess_GlobalVariableAssignment_ShouldRenameGlobalVariable() {
    // Arrange
    String input = "int x = 2;";
    String expectedOutput = "int y = 2;";

    // Act
    String output = renameVars.process(input);

    // Assert
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcess_LocalVariableAssignment_TooManyReferences_ShouldNotRenameVariable() {
    // Arrange
    String input = "int a = 3; int b = a;";
    String expectedOutput = "int a = 3; int b = a;";

    // Act
    String output = renameVars.process(input);

    // Assert
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcess_GlobalVariableAssignment_AlreadyRenamed_ShouldNotRenameVariable() {
    // Arrange
    String input = "int x = 4;";
    String expectedOutput = "int x = 4;";

    // Act
    String output = renameVars.process(input);

    // Assert
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcess_LocalVariableAssignment_PreserveAnonymousFunctionNames_ShouldNotRenameVariable() {
    // Arrange
    String input = "() -> { int a = 5; };";
    String expectedOutput = "() -> { int a = 5; };";

    // Act
    String output = renameVars.process(input);

    // Assert
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcess_LocalVariableAssignment_InvalidName_ShouldNotRenameVariable() {
    // Arrange
    String input = "int 123 = 6;";
    String expectedOutput = "int 123 = 6;";

    // Act
    String output = renameVars.process(input);

    // Assert
    assertEquals(expectedOutput, output);
  }

}