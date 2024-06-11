import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.Node;
import junit.framework.TestCase;

public class UnreachableCodeEliminationTest extends TestCase {

  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = false;
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);

    // Test case for process() method
    Node externs = null;
    Node toplevel = new Node(Token.SCRIPT);
    elimination.process(externs, toplevel);
  }

  public void testEliminationPass() {
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    UnreachableCodeElimination.EliminationPass pass = new UnreachableCodeElimination(compiler, false).new EliminationPass(cfg);

    // Test case for visit() method
    NodeTraversal t = null;
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.SCRIPT);
    pass.visit(t, n, parent);

    // Test case for tryRemoveUnconditionalBranching() method
    pass.tryRemoveUnconditionalBranching(n);

    // Test case for computeFollowing() method
    pass.computeFollowing(n);

    // Test case for removeDeadExprStatementSafely() method
    pass.removeDeadExprStatementSafely(n);

    // Test case for removeNode() method
    pass.removeNode(n);
  }
}