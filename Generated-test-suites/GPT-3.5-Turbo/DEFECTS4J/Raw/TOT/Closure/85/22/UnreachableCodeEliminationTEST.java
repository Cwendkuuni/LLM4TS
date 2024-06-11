import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testEnterScope_removeNoOpStatements() {
    // Typical use case: Remove no-op statements
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node node1 = new Node(Token.VAR);
    Node node2 = new Node(Token.RETURN);
    node2.addChildToFront(new Node(Token.TRUE));

    // Act
    uce.enterScope(node1);
    uce.exitScope(node2);

    // Assert
    assertNull(node2.getParent());
  }

  @Test
  public void testEnterScope_notRemoveNoOpStatements() {
    // Edge case: Not remove no-op statements
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = false;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node node1 = new Node(Token.VAR);
    Node node2 = new Node(Token.RETURN);
    node2.addChildToFront(new Node(Token.TRUE));

    // Act
    uce.enterScope(node1);
    uce.exitScope(node2);

    // Assert
    assertNotNull(node2.getParent());
  }

  @Test
  public void testProcess_removeDeadExprStatementSafely() {
    // Typical use case: Remove a dead expression statement
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.EXPR_RESULT);
    Node child = new Node(Token.NULL);
    n.addChildToFit(child);
    parent.addChildToBack(n);

    // Act
    uce.visit(null, n, parent);

    // Assert
    assertEquals(0, parent.getChildCount());
  }

  @Test
  public void testTryRemoveUnconditionalBranching_removeBranch() {
    // Typical use case: Remove an unconditional branch
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node parent = new Node(Token.BLOCK);
    Node n = new Node(Token.BREAK);
    Node child = new Node(Token.STRING);
    child.setString("label");
    n.addChildToFront(child);
    parent.addChildToBack(n);

    // Act
    Node result = uce.tryRemoveUnconditionalBranching(n);

    // Assert
    assertNull(parent.getParent());
    assertEquals(null, result);
  }

  @Test
  public void testComputeFollowing_returnStatement() {
    // Edge case: Compute following node for a return statement
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    boolean removeNoOpStatements = true;
    UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node n = new Node(Token.RETURN);
    Node followNode = new Node(Token.TRUE);
    n.addChildToFront(followNode);

    // Act
    Node result = uce.computeFollowing(n);

    // Assert
    assertEquals(followNode, result);
  }

}