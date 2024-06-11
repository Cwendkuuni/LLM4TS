import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private UnreachableCodeElimination unreachableCodeElimination;

  @Before
  public void setUp() {
    unreachableCodeElimination = new UnreachableCodeElimination(new AbstractCompiler(), true);
  }

  @Test
  public void enterScope_shouldPushCfgToStack() {
    // TODO: Implement test case
  }

  @Test
  public void exitScope_shouldPopCfgFromStack() {
    // TODO: Implement test case
  }

  @Test
  public void process_shouldTraverseTheAst() {
    // TODO: Implement test case
  }

  @Test
  public void visit_shouldHandleFunctionAndScriptNodes() {
    // TODO: Implement test case
  }

  @Test
  public void visit_shouldRemoveDeadExprStatements() {
    // TODO: Implement test case
  }

  @Test
  public void tryRemoveUnconditionalBranching_shouldRemoveUnreachableBreakStatements() {
    // TODO: Implement test case
  }

  @Test
  public void tryRemoveUnconditionalBranching_shouldRemoveUnreachableContinueStatements() {
    // TODO: Implement test case
  }

  @Test
  public void tryRemoveUnconditionalBranching_shouldRemoveUnreachableReturnStatements() {
    // TODO: Implement test case
  }

  @Test
  public void tryRemoveUnconditionalBranching_shouldRemoveUnreachableBlockStatements() {
    // TODO: Implement test case
  }

  @Test
  public void tryRemoveUnconditionalBranching_shouldNotRemoveReachableBreakStatements() {
    // TODO: Implement test case
  }

  @Test
  public void tryRemoveUnconditionalBranching_shouldNotRemoveReachableContinueStatements() {
    // TODO: Implement test case
  }

  @Test
  public void tryRemoveUnconditionalBranching_shouldNotRemoveReachableReturnStatements() {
    // TODO: Implement test case
  }

  @Test
  public void tryRemoveUnconditionalBranching_shouldNotRemoveReachableBlockStatements() {
    // TODO: Implement test case
  }

  @Test
  public void computeFollowing_shouldReturnFollowingNode() {
    // TODO: Implement test case
  }

  @Test
  public void removeDeadExprStatementSafely_shouldNotRemoveEmptyStatements() {
    // TODO: Implement test case
  }

  @Test
  public void removeDeadExprStatementSafely_shouldNotRemoveBlocksWithoutChildren() {
    // TODO: Implement test case
  }

  @Test
  public void removeDeadExprStatementSafely_shouldNotRemoveBlocksWithCatchParents() {
    // TODO: Implement test case
  }

  @Test
  public void removeDeadExprStatementSafely_shouldRemoveUnreachableDoNodes() {
    // TODO: Implement test case
  }

  @Test
  public void removeDeadExprStatementSafely_shouldRemoveDeadExprStatements() {
    // TODO: Implement test case
  }

}