import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ScopedAliasesTest {
  
  @Test
  public void testAliasReferences() {
    Node aliasReference = new Node(Token.NAME, "aliasReference");
    Node aliasDefinition = new Node(Token.NAME, "aliasDefinition");
    AliasedNode aliasedNode = new AliasedNode(aliasReference, aliasDefinition);
    aliasedNode.applyAlias();
    assertEquals(aliasDefinition, aliasReference.getParent());
  }
  
  @Test
  public void testAliasedTypeNode() {
    Node aliasReference = new Node(Token.NAME, "aliasReference");
    String correctedType = "correctedType";
    AliasedTypeNode aliasedTypeNode = new AliasedTypeNode(aliasReference, correctedType);
    aliasedTypeNode.applyAlias();
    assertEquals(correctedType, aliasReference.getString());
  }
  
  @Test
  public void testValidateScopeCall() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME, "goog.scope");
    Node parent = new Node(Token.SCRIPT);
    Traversal traversal = new Traversal();
    traversal.validateScopeCall(t, n, parent);
    assertEquals(1, traversal.getScopeCalls().size());
  }
  
  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME, "aliasName");
    Node parent = new Node(Token.VAR);
    Traversal traversal = new Traversal();
    traversal.getAliases().put("aliasName", new Var());
    traversal.visit(t, n, parent);
    assertEquals(1, traversal.getAliasUsages().size());
  }
}