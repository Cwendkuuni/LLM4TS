import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testEnterScope() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    unreachableCodeElimination.enterScope(null);
  }

  @Test
  public void testExitScope() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    unreachableCodeElimination.exitScope(null);
  }

  @Test
  public void testProcess() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    unreachableCodeElimination.process(null, null);
  }

  @Test
  public void testVisit() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    unreachableCodeElimination.visit(null, null, null);
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    unreachableCodeElimination.tryRemoveUnconditionalBranching(null);
  }

  @Test
  public void testComputeFollowing() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    unreachableCodeElimination.computeFollowing(null);
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    unreachableCodeElimination.removeDeadExprStatementSafely(null);
  }
}