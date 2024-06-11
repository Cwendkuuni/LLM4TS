import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    
    Node externs = new Node();
    Node toplevel = new Node();
    
    uce.process(externs, toplevel);
    
    // Verify results here
  }

  @Test
  public void testEliminationPass() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    NodeTraversal t = new NodeTraversal(cfg);
    Node n = new Node();
    Node parent = new Node();
    
    UnreachableCodeElimination elim = new UnreachableCodeElimination(null, true);
    UnreachableCodeElimination.EliminationPass pass = elim.new EliminationPass(cfg);
    
    pass.visit(t, n, parent);
    
    // Verify results here
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    
    UnreachableCodeElimination elim = new UnreachableCodeElimination(null, true);
    UnreachableCodeElimination.EliminationPass pass = elim.new EliminationPass(cfg);
    
    Node n = new Node();
    
    pass.tryRemoveUnconditionalBranching(n);
    
    // Verify results here
  }

  @Test
  public void testComputeFollowing() {
    UnreachableCodeElimination elim = new UnreachableCodeElimination(null, true);
    UnreachableCodeElimination.EliminationPass pass = elim.new EliminationPass(null);
    
    Node n = new Node();
    Node result = pass.computeFollowing(n);
    
    // Verify results here
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    UnreachableCodeElimination elim = new UnreachableCodeElimination(null, true);
    UnreachableCodeElimination.EliminationPass pass = elim.new EliminationPass(null);
    
    Node n = new Node();
    pass.removeDeadExprStatementSafely(n);
    
    // Verify results here
  }

  @Test
  public void testRemoveNode() {
    UnreachableCodeElimination elim = new UnreachableCodeElimination(null, true);
    UnreachableCodeElimination.EliminationPass pass = elim.new EliminationPass(null);
    
    Node n = new Node();
    pass.removeNode(n);
    
    // Verify results here
  }
}