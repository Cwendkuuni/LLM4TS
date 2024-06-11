import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;

public class UnreachableCodeEliminationTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    Node externs = new Node();
    Node toplevel = new Node();
    pass.process(externs, toplevel);
  }

  @Test
  public void testEliminationPass() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    EliminationPass pass = new EliminationPass(cfg);
    pass.visit(t, n, null);
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    Node n = new Node();
    EliminationPass pass = new EliminationPass(null);
    pass.tryRemoveUnconditionalBranching(n);
  }

  @Test
  public void testComputeFollowing() {
    Node n = new Node();
    EliminationPass pass = new EliminationPass(null);
    pass.computeFollowing(n);
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    Node n = new Node();
    EliminationPass pass = new EliminationPass(null);
    pass.removeDeadExprStatementSafely(n);
  }

  @Test
  public void testRemoveNode() {
    Node n = new Node();
    EliminationPass pass = new EliminationPass(null);
    pass.removeNode(n);
  }
}