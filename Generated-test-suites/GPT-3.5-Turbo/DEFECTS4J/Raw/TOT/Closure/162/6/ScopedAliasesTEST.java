import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.Node;



public class ScopedAliasesTest {
  
  ScopedAliases sa;
  
  @Before
  public void setUp() {
    sa = new ScopedAliases();
  }
  
  @Test
  public void testProcess() {
    Node externs = new Node(Token.EXPR_RESULT);
    Node root = new Node(Token.EXPR_RESULT);
    sa.process(externs, root);
  }
  
  @Test
  public void testHotSwapScript() {
    Node root = new Node(Token.EXPR_RESULT);
    Node originalRoot = new Node(Token.EXPR_RESULT);
    sa.hotSwapScript(root, originalRoot);
  }
  
  @Test
  public void testEnterScope() {
    NodeTraversal t = new NodeTraversal();
    sa.enterScope(t);
  }
  
  @Test
  public void testExitScope() {
    NodeTraversal t = new NodeTraversal();
    sa.exitScope(t);
  }
  
  @Test
  public void testShouldTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.EXPR_RESULT);
    sa.shouldTraverse(t, n, parent);
  }
  
  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.EXPR_RESULT);
    sa.visit(t, n, parent);
  }
  
  @Test
  public void testGetAliasDefinitionsInOrder() {
    assertEquals("Test case failed for getAliasDefinitionsInOrder() method.", 0, sa.getAliasDefinitionsInOrder().size());
  }
  
  @Test
  public void testGetAliasUsages() {
    assertEquals("Test case failed for getAliasUsages() method.", 0, sa.getAliasUsages().size());
  }
  
  @Test
  public void testGetScopeCalls() {
    assertEquals("Test case failed for getScopeCalls() method.", 0, sa.getScopeCalls().size());
  }
  
  @Test
  public void testHasErrors() {
    assertFalse("Test case failed for hasErrors() method.", sa.hasErrors());
  }
  
  @Test
  public void testIsCallToScopeMethod() {
    Node n = new Node(Token.EXPR_RESULT);
    assertFalse("Test case failed for isCallToScopeMethod() method.", sa.isCallToScopeMethod(n));
  }
  
  @Test
  public void testReport() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EXPR_RESULT);
    sa.report(t, n);
  }
  
  @Test
  public void testValidateScopeCall() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.EXPR_RESULT);
    sa.validateScopeCall(t, n, parent);
  }
  
  @Test
  public void testTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.EXPR_RESULT);
    sa.traverse(t, n, parent);
  }
  
  @Test
  public void testFixTypeNode() {
    Node typeNode = new Node(Token.STRING);
    sa.fixTypeNode(typeNode);
  }

}