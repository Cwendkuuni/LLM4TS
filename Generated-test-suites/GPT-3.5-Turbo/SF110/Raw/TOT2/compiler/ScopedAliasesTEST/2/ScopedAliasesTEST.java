import org.junit.Test;
import static org.junit.Assert.*;

public class ScopedAliasesTest {
  
  @Test
  public void testAliasedNode() {
    // Test for applying alias on an aliased node
    ScopedAliases scopedAliases = new ScopedAliases();
    
    // Add alias definition
    Node aliasDefinition = new Node(Token.NAME, new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "goog.x")));
    
    // Add alias reference
    Node aliasReference = new Node(Token.NAME, "x");
    
    AliasedNode aliasedNode = new AliasedNode(aliasReference, aliasDefinition);
    
    aliasedNode.applyAlias();
    // Assert the alias is successfully applied
    assertEquals("goog.x", aliasReference.getString());
  }
  
  @Test
  public void testAliasedTypeNode() {
    // Test for applying alias on a type node
    ScopedAliases scopedAliases = new ScopedAliases();
    
    // Add alias definition
    Node aliasDefinition = new Node(Token.NAME, new Node(Token.ASSIGN, new Node(Token.NAME, "g"), new Node(Token.NAME, "goog")));
    
    // Add type node
    Node typeNode = new Node("typeName");
    typeNode.setString("g.Type");
    
    AliasedTypeNode aliasedTypeNode = new AliasedTypeNode(typeNode, aliasDefinition, "g");
    
    aliasedTypeNode.applyAlias();
    // Assert the type node is successfully aliased
    assertEquals("goog.Type", typeNode.getString());
  }
  
  @Test
  public void testScopeCallValidation() {
    // Test for validating the goog.scope call
    ScopedAliases scopedAliases = new ScopedAliases();
    
    Node scopeCall = new Node(Token.CALL, new Node(Token.NAME, "goog.scope"));
    scopeCall.addChildToBack(new Node(Token.FUNCTION));
    
    Traversal traversal = new Traversal();
    traversal.validateScopeCall(new NodeTraversal(), scopeCall, scopeCall.getParent());
    
    // Assert that the validation is successful
    assertFalse(traversal.hasErrors());
  }
}