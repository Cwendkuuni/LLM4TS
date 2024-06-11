import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    SyntacticScopeCreator syntacticScopeCreator;

    @Before
    public void setUp() {
        syntacticScopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }

    @Test
    public void testCreateScope_GlobalScope() {
        Scope scope = syntacticScopeCreator.createScope(new Node(Token.SCRIPT), null);
        assertNotNull(scope);
        assertTrue(scope.isGlobal());
    }

    @Test
    public void testCreateScope_FunctionScope() {
        Node functionNode = new Node(Token.FUNCTION);
        Scope parentScope = syntacticScopeCreator.createScope(new Node(Token.SCRIPT), null);
        Scope scope = syntacticScopeCreator.createScope(functionNode, parentScope);
        assertNotNull(scope);
        assertFalse(scope.isGlobal());
        assertEquals(parentScope, scope.getParent());
    }

    @Test
    public void testCreateScope_VarDeclaration() {
        Node varNode = new Node(Token.VAR);
        Scope parentScope = syntacticScopeCreator.createScope(new Node(Token.SCRIPT), null);
        Scope scope = syntacticScopeCreator.createScope(varNode, parentScope);
        assertNotNull(scope);
    }

    @Test
    public void testCreateScope_FunctionExpression() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.FLAG_FUNCTION_EXPRESSION, true);
        Scope parentScope = syntacticScopeCreator.createScope(new Node(Token.SCRIPT), null);
        Scope scope = syntacticScopeCreator.createScope(functionNode, parentScope);
        assertNull(syntacticScopeCreator.createScope(new Node(Token.FUNCTION), null));
    }

    @Test
    public void testCreateScope_CatchScope() {
        Node catchNode = new Node(Token.CATCH);
        Scope parentScope = syntacticScopeCreator.createScope(new Node(Token.SCRIPT), null);
        Scope scope = syntacticScopeCreator.createScope(catchNode, parentScope);
        assertNotNull(scope);
        assertFalse(scope.isGlobal());
        assertEquals(parentScope, scope.getParent());
    }
}