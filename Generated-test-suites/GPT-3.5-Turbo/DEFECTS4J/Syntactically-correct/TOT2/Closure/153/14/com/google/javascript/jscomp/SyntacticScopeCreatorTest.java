import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator scopeCreator;

    @Before
    public void setUp() {
        scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }

    @Test
    public void testCreateScope() {
        // Typical use case
        Scope scope = scopeCreator.createScope(new Node(Token.SCRIPT), null);
        assertNotNull(scope);

        // Edge case: Empty node
        scope = scopeCreator.createScope(new Node(Token.EMPTY), null);
        assertNotNull(scope);

        // Error scenario: Null parent
        try {
            scope = scopeCreator.createScope(new Node(Token.SCRIPT), null);
            assertNotNull(scope);
        } catch (NullPointerException e) {
            fail("NullPointerException should not be thrown");
        }
    }

    @Test
    public void testScanVars() {
        // Typical use case
        Node scriptNode = new Node(Token.SCRIPT);
        scopeCreator.scanVars(scriptNode, null);

        // Edge case: Var declaration within for loop
        Node forNode = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        scopeCreator.scanVars(forNode, scriptNode);

        // Error scenario: Duplicate variable declaration
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "x"), new Node(Token.NAME, "x"));
        try {
            scopeCreator.scanVars(varNode, scriptNode);
        } catch (JSError e) {
            assertEquals("Variable x first declared in ??", e.getDescription());
        }
    }

    @Test
    public void testRedeclarationHandler() {
        // Typical use case
        Scope scope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
        SyntacticScopeCreator.RedeclarationHandler handler = scopeCreator.new DefaultRedeclarationHandler();
        handler.onRedeclaration(scope, "x", new Node(Token.NAME, "x"), new Node(Token.SCRIPT), null, new Node(Token.SCRIPT));

        // Edge case: Allow duplicate declaration with suppression
        scope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "y"), new Node(Token.NAME, "y"));
        varNode.setJSDocInfo(new JSDocInfoBuilder(false).addSuppression("duplicate").build());
        handler.onRedeclaration(scope, "y", new Node(Token.NAME, "y"), new Node(Token.SCRIPT), null, new Node(Token.SCRIPT));

        // Error scenario: Redeclaration of arguments variable
        scope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
        try {
            handler.onRedeclaration(scope, "arguments", new Node(Token.NAME, "arguments"), new Node(Token.SCRIPT), null, new Node(Token.SCRIPT));
        } catch (JSError e) {
            assertEquals("Shadowing \"arguments\" is not allowed", e.getDescription());
        }
    }

}