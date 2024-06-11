import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private UnreachableCodeElimination codeElimination;

  @Before
  public void setup() {
    codeElimination = new UnreachableCodeElimination(new AbstractCompiler(), true);
  }

  @Test
  public void testProcess() {
    // Setup
    Node externs = new Node();
    Node toplevel = new Node();

    // Execute
    codeElimination.process(externs, toplevel);

    // Verify
    // Add verification code here
  }

  @Test
  public void testEliminationPass_visit() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();

    // Execute
    codeElimination.new EliminationPass(null).visit(t, n, parent);

    // Verify
    // Add verification code here
  }

  @Test
  public void testEliminationPass_tryRemoveUnconditionalBranching() {
    // Setup
    Node n = new Node();

    // Execute
    codeElimination.new EliminationPass(null).tryRemoveUnconditionalBranching(n);

    // Verify
    // Add verification code here
  }

  @Test
  public void testEliminationPass_computeFollowing() {
    // Setup
    Node n = new Node();

    // Execute
    codeElimination.new EliminationPass(null).computeFollowing(n);

    // Verify
    // Add verification code here
  }

  @Test
  public void testEliminationPass_removeDeadExprStatementSafely() {
    // Setup
    Node n = new Node();

    // Execute
    codeElimination.new EliminationPass(null).removeDeadExprStatementSafely(n);

    // Verify
    // Add verification code here
  }

  @Test
  public void testEliminationPass_removeNode() {
    // Setup
    Node n = new Node();

    // Execute
    codeElimination.new EliminationPass(null).removeNode(n);

    // Verify
    // Add verification code here
  }
}