import org.junit.*;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination codeElimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node externs = null;
    Node toplevel = null;
    codeElimination.process(externs, toplevel);
    // Assert statements here
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination codeElimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal t = new NodeTraversal();
    Node n = null;
    Node parent = null;
    codeElimination.visit(traversal, n, parent);
    // Assert statements here
  }

  @Test
  public void testEliminationPass() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination().new EliminationPass(cfg);
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal t = new NodeTraversal();
    Node n = null;
    Node parent = null;
    eliminationPass.visit(traversal, n, parent);
    // Assert statements here
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    Node n = null;
    UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination().new EliminationPass(null);
    eliminationPass.tryRemoveUnconditionalBranching(n);
    // Assert statements here
  }

  @Test
  public void testComputeFollowing() {
    Node n = null;
    UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination().new EliminationPass(null);
    Node result = eliminationPass.computeFollowing(n);
    // Assert statements here
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    Node n = null;
    UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination().new EliminationPass(null);
    eliminationPass.removeDeadExprStatementSafely(n);
    // Assert statements here
  }

  @Test
  public void testRemoveNode() {
    Node n = null;
    UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination().new EliminationPass(null);
    eliminationPass.removeNode(n);
    // Assert statements here
  }
}