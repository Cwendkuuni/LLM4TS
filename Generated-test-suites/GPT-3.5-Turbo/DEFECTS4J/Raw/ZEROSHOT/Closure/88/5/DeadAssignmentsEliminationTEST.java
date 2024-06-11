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
  public void testProcess() {
    // TODO: Implement test for process method
    fail("Not yet implemented");
  }

  @Test
  public void testEnterScope() {
    // TODO: Implement test for enterScope method
    fail("Not yet implemented");
  }

  @Test
  public void testExitScope() {
    // TODO: Implement test for exitScope method
    fail("Not yet implemented");
  }

  @Test
  public void testVisit() {
    // TODO: Implement test for visit method
    fail("Not yet implemented");
  }

  @Test
  public void testTryRemoveDeadAssignments() {
    // TODO: Implement test for tryRemoveDeadAssignments method
    fail("Not yet implemented");
  }

  @Test
  public void testTryRemoveAssignment() {
    // TODO: Implement test for tryRemoveAssignment method
    fail("Not yet implemented");
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    // TODO: Implement test for isVariableStillLiveWithinExpression method
    fail("Not yet implemented");
  }

  @Test
  public void testIsVariableReadBeforeKill() {
    // TODO: Implement test for isVariableReadBeforeKill method
    fail("Not yet implemented");
  }
}