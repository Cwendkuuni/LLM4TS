import org.junit.*;
import static org.junit.Assert.*;

public class ScopedAliasesTest {
  
  @Test
  public void testAliasUsage() {
    ScopedAliases.AliasedNode alias = new ScopedAliases.AliasedNode(new Node(Token.NAME, "aliasReference"), new Node(Token.NAME, "aliasDefinition"));
    alias.applyAlias();
    assertEquals("aliasDefinition", alias.aliasReference.getString());
  }
  
  @Test
  public void testAliasedTypeNode() {
    ScopedAliases.AliasedTypeNode alias = new ScopedAliases.AliasedTypeNode(new Node(Token.NAME, "aliasReference"), "correctedType");
    alias.applyAlias();
    assertEquals("correctedType", alias.aliasReference.getString());
  }
  
  @Test
  public void testTraversal() {
    ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
    assertFalse(traversal.hasErrors());
    
    Node node = new Node(Token.NAME, "aliasReference");
    Node definition = new Node(Token.NAME, "aliasDefinition");
    ScopedAliases.AliasedNode alias = new ScopedAliases.AliasedNode(node, definition);
    traversal.getAliasUsages().add(alias);
    assertEquals(alias, traversal.getAliasUsages().get(0));
    
    Node aliasDefinition = new Node(Token.NAME, "aliasDefinition");
    traversal.getAliasDefinitionsInOrder().add(aliasDefinition);
    assertEquals(aliasDefinition, traversal.getAliasDefinitionsInOrder().get(0));
    
    Node scopeCall = new Node(Token.NAME, "scopeCall");
    traversal.getScopeCalls().add(scopeCall);
    assertEquals(scopeCall, traversal.getScopeCalls().get(0));
    
    assertTrue(traversal.hasErrors());
  }
  
  @Test
  public void testValidateScopeCall() {
    ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
    NodeTraversal t = new NodeTraversal(null, null);
    Node node = new Node(Token.NAME, "goog.scope");
    Node parent = new Node(Token.NAME, "parent");
    traversal.validateScopeCall(t, node, parent);
    assertTrue(traversal.hasErrors());
  }
  
  @Test
  public void testVisit() {
    ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
    NodeTraversal t = new NodeTraversal(null, null);
    Node node = new Node(Token.NAME, "alias");
    Node parent = new Node(Token.NAME, "parent");
    traversal.visit(t, node, parent);
  }
  
  @Test
  public void testFixType() {
    ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
    Node typeNode = new Node(Token.NAME, "typeNode");
    traversal.fixTypeNode(typeNode);
  }
  
  @Test
  public void testHotSwapScript() {
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Node root = new Node(Token.NAME, "root");
    Node originalRoot = new Node(Token.NAME, "originalRoot");
    scopedAliases.hotSwapScript(root, originalRoot);
  }
  
  @Test
  public void testProcess() {
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Node externs = new Node(Token.NAME, "externs");
    Node root = new Node(Token.NAME, "root");
    scopedAliases.process(externs, root);
  }

}