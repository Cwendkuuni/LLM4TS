import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    deadAssignmentsElimination = new DeadAssignmentsElimination();
  }

  @Test
  public void testProcess_noDeadAssignments_removeNoAssignments() {
    // Arrange
    String input = "var x = 1;";
    String expected = "var x = 1;";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_deadAssignments_removeAssignments() {
    // Arrange
    String input = "var x = 1; x = 2;";
    String expected = "var x = 1;";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_assignmentToSameVariable_removeAssignment() {
    // Arrange
    String input = "var x = 1; x = x + 1;";
    String expected = "var x = 1;";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_incrementOperator_removeAssignment() {
    // Arrange
    String input = "var x = 1; x++;";
    String expected = "var x = 1;";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_ifStatement_removeAssignment() {
    // Arrange
    String input = "var x = 1; if (x == 1) { x = 2; }";
    String expected = "var x = 1; if (x == 1) { }";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_whileLoop_removeAssignment() {
    // Arrange
    String input = "var x = 1; while (x < 3) { x = x + 1; }";
    String expected = "var x = 1; while (x < 3) { }";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_forLoop_removeAssignment() {
    // Arrange
    String input = "var x = 1; for (var i = 0; i < 3; i++) { x = i; }";
    String expected = "var x = 1; for (var i = 0; i < 3; i++) { }";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }

  @Test
  public void testProcess_switchStatement_removeAssignment() {
    // Arrange
    String input = "var x = 1; switch (x) { case 1: x = 2; break; }";
    String expected = "var x = 1; switch (x) { case 1: break; }";

    // Act
    String result = deadAssignmentsElimination.process(input);

    // Assert
    assertEquals(expected, result);
  }
}