import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    deadAssignmentsElimination = new DeadAssignmentsElimination(new AbstractCompiler());
  }

  @Test
  public void testProcess() {
    // TODO: Implement test case
  }

  @Test
  public void testEnterScope() {
    // TODO: Implement test case
  }

  @Test
  public void testExitScope() {
    // TODO: Implement test case
  }

  @Test
  public void testVisit() {
    // TODO: Implement test case
  }

  @Test
  public void testTryRemoveDeadAssignments() {
    // TODO: Implement test case
  }

  @Test
  public void testTryRemoveAssignment() {
    // TODO: Implement test case
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    // TODO: Implement test case
  }

  @Test
  public void testIsVariableReadBeforeKill() {
    // TODO: Implement test case
  }
}