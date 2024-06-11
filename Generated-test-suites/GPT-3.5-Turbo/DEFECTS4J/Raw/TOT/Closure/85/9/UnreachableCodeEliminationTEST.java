import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private UnreachableCodeElimination uce;

  @Before
  public void setUp() {
    uce = new UnreachableCodeElimination(new AbstractCompiler(), false);
  }

  @Test
  public void testEnterScope() {
    // Test typical use case
    Scope scope = new Scope(null);
    Node rootNode = new Node(Token.SCRIPT);
    scope.setRootNode(rootNode);

    uce.enterScope(new NodeTraversal(null, rootNode, null, scope));

    // Test edge case: null scope
    uce.enterScope(new NodeTraversal(null, rootNode, null, null));
  }

  @Test
  public void testExitScope() {
    // Test typical use case
    Scope scope = new Scope(null);
    Node rootNode = new Node(Token.SCRIPT);
    scope.setRootNode(rootNode);

    uce.enterScope(new NodeTraversal(null, rootNode, null, scope));
    uce.exitScope(new NodeTraversal(null, rootNode, null, scope));

    // Test edge case: empty stack
    uce.exitScope(new NodeTraversal(null, rootNode, null, scope));
  }

  @Test
  public void testProcess() {
    // Test typical use case
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);

    uce.process(externs, root);

    // Test edge case: null externs
    uce.process(null, root);

    // Test edge case: null root
    uce.process(externs, null);
  }

  @Test
  public void testVisit() {
    // Test typical use case
    NodeTraversal t = new NodeTraversal(null, null, null, null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);

    uce.visit(t, n, parent);

    // Test edge case: null parent
    uce.visit(t, n, null);

    // Test edge case: n is FUNCTION
    n.setType(Token.FUNCTION);
    uce.visit(t, n, parent);

    // Test edge case: n is SCRIPT
    n.setType(Token.SCRIPT);
    uce.visit(t, n, parent);
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    // Test typical use case
    Node n = new Node(Token.RETURN);
    Node result = uce.tryRemoveUnconditionalBranching(n);
    assertNull(result);

    // Test edge case: n is null
    result = uce.tryRemoveUnconditionalBranching(null);
    assertNull(result);

    // Test edge case: outEdges.size() > 1
    n = new Node(Token.BLOCK);
    result = uce.tryRemoveUnconditionalBranching(n);
    assertNull(result);
  }

  @Test
  public void testComputeFollowing() {
    // Test typical use case
    Node n = new Node(Token.RETURN);
    Node next = uce.computeFollowing(n);
    assertNull(next);

    // Test edge case: null n
    next = uce.computeFollowing(null);
    assertNull(next);
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    // Test typical use case
    Node n = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    uce.removeDeadExprStatementSafely(n);
    assertNull(n.getParent());

    // Test special case: n is empty
    n = new Node(Token.EMPTY);
    parent.addChildToFront(n);
    uce.removeDeadExprStatementSafely(n);
    assertNull(n.getParent());

    // Test edge case: parent is TRY
    Node tryNode = new Node(Token.TRY);
    parent.addChildToFront(tryNode);
    n = new Node(Token.CATCH);
    tryNode.addChildToFront(n);
    uce.removeDeadExprStatementSafely(n);
    assertNotNull(n.getParent());
  }


}