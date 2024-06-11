import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases();
  }

  @Test
  public void testEnterScope() {
    Node root = new Node();
    NodeTraversal traversal = new NodeTraversal();
    scopedAliases.enterScope(traversal, root);
    assertEquals(traversal.getScope(), scopedAliases.getScope());
  }

  @Test
  public void testExitScope() {
    Node root = new Node();
    NodeTraversal traversal = new NodeTraversal();
    scopedAliases.exitScope(traversal, root);
    assertEquals(traversal.getScope(), scopedAliases.getScope());
  }

  @Test
  public void testIsValidScopeCall() {
    Node node = new Node();
    assertEquals(true, scopedAliases.isValidScopeCall(node));
  }

  @Test
  public void testIsCallToScopeMethod() {
    Node node = new Node();
    assertEquals(true, scopedAliases.isCallToScopeMethod(node));
  }

  @Test
  public void testShouldTraverse() {
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node();
    Node parent = new Node();
    assertEquals(true, scopedAliases.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testVisit() {
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node();
    Node parent = new Node();
    scopedAliases.visit(traversal, node, parent);
    assertEquals(traversal.getScope(), scopedAliases.getScope());
  }
}