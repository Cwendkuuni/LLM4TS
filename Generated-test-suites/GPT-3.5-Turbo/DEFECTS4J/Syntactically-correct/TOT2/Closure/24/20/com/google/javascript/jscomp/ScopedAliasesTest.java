import org.junit.Test;
import static org.junit.Assert.*;

public class ScopedAliasesTest {

    @Test
    public void testAssociateAliasWithVariable() {
        // Test associating an alias with a variable
        // Given
        ScopedAliases scopedAliases = new ScopedAliases();
        Node aliasReference = new Node(Token.NAME, "alias", new Node(Token.EMPTY));
        Node aliasDefinition = new Node(Token.NAME, "variable", new Node(Token.EMPTY));

        // When
        scopedAliases.new AliasedNode(aliasReference, aliasDefinition).applyAlias();

        // Then
        assertEquals(aliasDefinition, aliasReference.getParent());
    }

    @Test
    public void testApplyAliasWithTypeNode() {
        // Test applying alias to a type node
        // Given
        ScopedAliases scopedAliases = new ScopedAliases();
        Node typeNode = new Node(Token.STRING, "Type");
        scopedAliases.aliases.put("Type", new Var(new Node(Token.NAME, "variable", new Node(Token.EMPTY))));

        // When
        scopedAliases.new AliasedTypeNode(typeNode, "variableType").applyAlias();

        // Then
        assertEquals("variableType", typeNode.getString());
    }

    @Test
    public void testScopeCallWithProperParameters() {
        // Test proper parameters in a scope call
        // Given
        ScopedAliases scopedAliases = new ScopedAliases();
        Node scopeCall = new Node(Token.CALL, new Node(Token.NAME, "goog.scope"), new Node(Token.FUNCTION));
        scopedAliases.preprocessorSymbolTable = new PreprocessorSymbolTable();

        // When
        scopedAliases.validateScopeCall(null, scopeCall, scopeCall.getParent());

        // Then
        assertFalse(scopedAliases.hasErrors());
        assertTrue(scopedAliases.scopeCalls.contains(scopeCall));
    }

    // Add more test methods for other public methods in ScopedAliases class

}