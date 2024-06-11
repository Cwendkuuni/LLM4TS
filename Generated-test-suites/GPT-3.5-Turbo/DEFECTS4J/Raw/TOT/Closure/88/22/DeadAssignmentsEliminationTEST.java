import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    deadAssignmentsElimination = new DeadAssignmentsElimination();
  }

  @Test
  public void testProcess_NoDeadAssignments() {
    // Arrange
    String input = "var a = 1; var b = 2; var c = a + b;";
    String expected = input;

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_DeadLocalAssignment_Removed() {
    // Arrange
    String input = "var a = 1; a = 2;";
    String expected = "var a = 1;";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_DeadLocalAssignment_ReadRightOfNode_NotRemoved() {
    // Arrange
    String input = "var a = 1; var b = a; a = 2;";
    String expected = input;

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_DeadLocalAssignment_WriteRightOfNode_Removed() {
    // Arrange
    String input = "var a = 1; a = 2; var b = a;";
    String expected = "var a = 1; var b = a;";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_DeadLocalAssignment_MultipleReadsBeforeAnyWrite_NotRemoved() {
    // Arrange
    String input = "var a = 1; var b = a + 1; var c = a + b; a = 2;";
    String expected = input;

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_DeadLocalAssignment_MultipleWritesBeforeAnyRead_Removed() {
    // Arrange
    String input = "var a = 1; a = 2; a = 3; var b = a;";
    String expected = "var a = 1; a = 3; var b = a;";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

}