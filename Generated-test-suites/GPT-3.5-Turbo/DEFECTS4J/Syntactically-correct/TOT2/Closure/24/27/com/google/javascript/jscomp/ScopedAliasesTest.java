import org.junit.Test;
import static org.junit.Assert.*;

public class ScopedAliasesTest {

    @Test
    public void testAliases() {
        ScopedAliases scopedAliases = new ScopedAliases();
        
        // Typical use case
        // Add alias definition and validate
        scopedAliases.hotSwapScript(null, null);
        assertEquals(false, scopedAliases.hasErrors());
        
        // Edge case
        // Verify that an error is thrown if goog.scope call is not alone in a single statement
        scopedAliases.process(null, null);
        assertEquals(true, scopedAliases.hasErrors());
    }

    @Test
    public void testAliasedNode() {
        ScopedAliases scopedAliases = new ScopedAliases();
        ScopedAliases.AliasedNode aliasedNode = scopedAliases.new AliasedNode(null, null);
        
        // Typical use case
        // Apply alias and validate
        aliasedNode.applyAlias();
        
        // Edge case
        // Apply null alias and validate
        aliasedNode = scopedAliases.new AliasedNode(null, null);
        aliasedNode.applyAlias();
    }

    @Test
    public void testAliasedTypeNode() {
        ScopedAliases scopedAliases = new ScopedAliases();
        ScopedAliases.AliasedTypeNode aliasedTypeNode = scopedAliases.new AliasedTypeNode(null, null);
        
        // Typical use case
        // Apply alias and validate
        aliasedTypeNode.applyAlias();
        
        // Edge case
        // Apply null alias and validate
        aliasedTypeNode = scopedAliases.new AliasedTypeNode(null, null);
        aliasedTypeNode.applyAlias();
    }

    @Test
    public void testTraversal() {
        ScopedAliases scopedAliases = new ScopedAliases();
        ScopedAliases.Traversal traversal = scopedAliases.new Traversal();
        
        // Typical use case
        // Enter scope and validate
        traversal.enterScope(null);
        
        // Edge case
        // Exit scope and validate
        traversal.exitScope(null);
    }

    @Test
    public void testValidation() {
        ScopedAliases scopedAliases = new ScopedAliases();
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.CALL);
        Node parent = new Node(Token.VAR);
        
        // Typical use case
        // Validate scope call and report error
        scopedAliases.validateScopeCall(t, n, parent);
        
        // Edge case
        // Validate scope call and report with child count not equal to 2
        n.addChildToBack(new Node(Token.STRING, "test"));
        scopedAliases.validateScopeCall(t, n, parent);
    }
}