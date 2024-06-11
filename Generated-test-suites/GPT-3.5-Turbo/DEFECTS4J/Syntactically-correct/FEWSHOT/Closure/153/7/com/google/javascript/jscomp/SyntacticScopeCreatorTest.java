import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
    private SyntacticScopeCreator scopeCreator;

    @Before
    public void setUp() {
        scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }

    @Test
    public void testCreateScope_GlobalBlock() {
        Node n = new Node(Token.SCRIPT);
        Scope scope = scopeCreator.createScope(n, null);
        assertNull(scope.getParent());
        assertNull(scope.getRootNode());
        assertNull(scope.getParentScope());
        assertNull(scope.getVar("arguments"));
    }

    @Test
    public void testCreateScope_FunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.NAME, "fnName"));
        Scope parentScope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
        Scope scope = scopeCreator.createScope(n, parentScope);
        assertEquals(parentScope, scope.getParentScope());
        assertEquals(n, scope.getRootNode());
        assertEquals(n.getFirstChild(), scope.getVar("fnName").getNameNode());
    }

    @Test
    public void testCreateScope_FunctionExpression() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.EXPR_RESULT));
        n.getFirstChild().addChildToFront(new Node(Token.NAME, "fnName"));
        Scope parentScope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
        Scope scope = scopeCreator.createScope(n, parentScope);
        assertEquals(parentScope, scope.getParentScope());
        assertEquals(n, scope.getRootNode());
        assertNull(scope.getVar("fnName")); // function expression's name will not be declared
    }

    @Test
    public void testCreateScope_VariableDeclaration() {
        Node n = new Node(Token.VAR);
        n.addChildToFront(new Node(Token.NAME, "var1"));
        Scope parentScope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
        Scope scope = scopeCreator.createScope(n, parentScope);
        assertEquals(parentScope, scope.getParentScope());
        assertEquals(n, scope.getRootNode());
        assertEquals(n.getFirstChild(), scope.getVar("var1").getNameNode());
    }

    @Test
    public void testCreateScope_CatchBlock() {
        Node n = new Node(Token.CATCH);
        n.addChildToFront(new Node(Token.NAME, "exception"));
        Scope parentScope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
        Scope scope = scopeCreator.createScope(n, parentScope);
        assertEquals(parentScope, scope.getParentScope());
        assertEquals(n, scope.getRootNode());
        assertEquals(n.getFirstChild(), scope.getVar("exception").getNameNode());
    }

    @Test
    public void testCreateScope_CatchBlock_ShadowingArguments() {
        Node n = new Node(Token.CATCH);
        n.addChildToFront(new Node(Token.NAME, "arguments"));
        Scope parentScope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
        scopeCreator.createScope(new Node(Token.SCRIPT), parentScope);
        try {
            scopeCreator.createScope(n, parentScope);
        } catch (JSError ex) {
            assertEquals(SyntacticScopeCreator.VAR_ARGUMENTS_SHADOWED_ERROR, ex.getType());
        }
    }
}