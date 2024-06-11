
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
  public void testProcess() {
    // TODO: implement test case
  }

  @Test
  public void testEnterScope() {
    // TODO: implement test case
  }

  @Test
  public void testExitScope() {
    // TODO: implement test case
  }

  @Test
  public void testVisit() {
    // TODO: implement test case
  }

  @Test
  public void testTryRemoveDeadAssignments() {
    // TODO: implement test case
  }

  @Test
  public void testTryRemoveAssignment() {
    // TODO: implement test case
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    // TODO: implement test case
  }

  @Test
  public void testIsVariableReadBeforeKill() {
    // TODO: implement test case
  }

  @Test
  public void testCheckHookBranchReadBeforeKill() {
    // TODO: implement test case
  }
}