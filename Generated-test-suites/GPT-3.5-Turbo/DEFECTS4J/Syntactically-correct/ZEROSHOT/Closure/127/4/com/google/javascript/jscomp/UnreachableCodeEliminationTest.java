import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.NodeTraversal;

public class UnreachableCodeEliminationTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
    
    Node externs = new Node();
    Node toplevel = new Node();
    uce.process(externs, toplevel);
  }
  
  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(compiler);
    
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node();
    Node parent = new Node();
    uce.new EliminationPass(null).visit(t, n, parent);
  }
  
  @Test
  public void testTryRemoveUnconditionalBranching() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
    Node n = new Node();
    uce.new EliminationPass(null).tryRemoveUnconditionalBranching(n);
  }
  
  @Test
  public void testComputeFollowing() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
    Node n = new Node();
    uce.new EliminationPass(null).computeFollowing(n);
  }
  
  @Test
  public void testRemoveDeadExprStatementSafely() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
    Node n = new Node();
    uce.new EliminationPass(null).removeDeadExprStatementSafely(n);
  }
  
  @Test
  public void testRemoveNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
    Node n = new Node();
    uce.new EliminationPass(null).removeNode(n);
  }
}