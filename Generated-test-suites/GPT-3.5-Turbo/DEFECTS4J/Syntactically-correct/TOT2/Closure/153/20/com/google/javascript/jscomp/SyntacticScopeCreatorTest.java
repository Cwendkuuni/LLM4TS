import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope() {
        // Test creating a scope with a valid input Node
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.FUNCTION);
        Scope parent = null;

        Scope scope = scopeCreator.createScope(n, parent);

        assertNotNull(scope);
    }

    @Test
    public void testVarMultiplyDeclaredError() {
        // Test for VAR_MULTIPLY_DECLARED_ERROR exception
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.VAR, new Node(Token.NAME, "x"), new Node(Token.NAME, "x"));
        Scope parent = null;

        try {
            scopeCreator.createScope(n, parent);
            fail("Expected VAR_MULTIPLY_DECLARED_ERROR exception");
        } catch (Exception e) {
            assertTrue(e instanceof JSError);
            assertEquals("Variable x first declared in null", e.getMessage());
        }
    }

    @Test
    public void testVarArgumentsShadowedError() {
        // Test for VAR_ARGUMENTS_SHADOWED_ERROR exception
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.SCRIPT);

        Scope scope = scopeCreator.createScope(n, null);
        Node catchNode = new Node(Token.CATCH, new Node(Token.NAME, "arguments"), new Node(Token.BLOCK));
        n.addChildToBack(catchNode);

        try {
            scopeCreator.createScope(n, scope);
            fail("Expected VAR_ARGUMENTS_SHADOWED_ERROR exception");
        } catch (Exception e) {
            assertTrue(e instanceof JSError);
            assertEquals("Shadowing \"arguments\" is not allowed", e.getMessage());
        }
    }

    @Test
    public void testDefaultRedeclarationHandler() {
        // Test default redeclaration handler
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        SyntacticScopeCreator.DefaultRedeclarationHandler handler = scopeCreator.new DefaultRedeclarationHandler();
        Scope scope = new Scope(new Node(Token.SCRIPT), compiler);
        String name = "x";
        Node n = new Node(Token.NAME, "x");
        Node parent = new Node(Token.SCRIPT);
        Node gramps = null;
        Node nodeWithLineNumber = new Node(Token.NAME, "x");

        try {
            handler.onRedeclaration(scope, name, n, parent, gramps, nodeWithLineNumber);
            fail("Expected VAR_MULTIPLY_DECLARED_ERROR exception");
        } catch (JSError e) {
            assertEquals("Variable x first declared in null", e.getMessage());
        }
    }

}