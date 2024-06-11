import static org.junit.Assert.*;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  
  @Test
  public void testEnterScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    uce.process(externs, root);
  }
  
  @Test
  public void testExitScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    NodeTraversal t = new NodeTraversal(compiler, null);
    uce.exitScope(t);
  }
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    uce.process(externs, root);
  }
  
  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    NodeTraversal t = new NodeTraversal(compiler, null);
    Node n = new Node(Token.NEW);
    Node parent = new Node(Token.PARENT);
    uce.visit(t, n, parent);
  }
  
  @Test
  public void testTryRemoveUnconditionalBranching() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node n = new Node(Token.NODE);
    uce.tryRemoveUnconditionalBranching(n);
  }
  
  @Test
  public void testComputeFollowing() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node n = new Node(Token.NODE);
    Node following = uce.computeFollowing(n);
    assertNull(following);
  }
  
  @Test
  public void testRemoveDeadExprStatementSafely() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node n = new Node(Token.NODE);
    uce.removeDeadExprStatementSafely(n);
  }
  
}