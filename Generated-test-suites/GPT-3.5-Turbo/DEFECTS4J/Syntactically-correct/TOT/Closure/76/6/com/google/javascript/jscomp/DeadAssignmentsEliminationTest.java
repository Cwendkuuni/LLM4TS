
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

  private DeadAssignmentsElimination eliminator;

  @Before
  public void setUp() {
    eliminator = new DeadAssignmentsElimination();
  }

  @Test
  public void testProcess_RemovesDeadAssignments() {
    // Prepare
    String jsCode = "var x = 5; x = 10;";
    String expectedResult = "var x = 5;";

    // Act
    String actualResult = eliminator.process(jsCode);

    // Assert
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testProcess_KeepsLiveAssignments() {
    // Prepare
    String jsCode = "var x = 5; x = 10; var y = x;";
    String expectedResult = "var x = 5; x = 10; var y = x;";

    // Act
    String actualResult = eliminator.process(jsCode);

    // Assert
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testProcess_RemovesIncrementAndDecrementOperations() {
    // Prepare
    String jsCode = "var x = 5; x++;";
    String expectedResult = "var x = 5;";

    // Act
    String actualResult = eliminator.process(jsCode);

    // Assert
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testProcess_RemovesIdentityAssignments() {
    // Prepare
    String jsCode = "var x = 5; x = x;";
    String expectedResult = "var x = 5;";

    // Act
    String actualResult = eliminator.process(jsCode);

    // Assert
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testProcess_KeepsLiveAssignmentsWithinExpression() {
    // Prepare
    String jsCode = "var x = 5; var y = x + x; x = 10;";
    String expectedResult = "var x = 5; var y = x + x; x = 10;";

    // Act
    String actualResult = eliminator.process(jsCode);

    // Assert
    assertEquals(expectedResult, actualResult);
  }
}

