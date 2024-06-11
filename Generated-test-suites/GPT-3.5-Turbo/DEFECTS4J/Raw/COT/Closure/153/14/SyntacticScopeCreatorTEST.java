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
    public void testCreateScope_withFunctionNode_createsScopeWithFunctionVariables() {
        Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME, "test"), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Scope parentScope = new Scope(new Node(Token.EMPTY), new AbstractCompiler());
        
        Scope scope = scopeCreator.createScope(functionNode, parentScope);
        
        assertEquals(functionNode, scope.getScopeRoot());
        // Assert declarations of function variables
        assertTrue(scope.isDeclared("test", false));
    }

    @Test
    public void testCreateScope_withGlobalBlockNode_createsScopeWithGlobalVariables() {
        Node globalBlockNode = new Node(Token.SCRIPT);
        Scope parentScope = null;
        
        Scope scope = scopeCreator.createScope(globalBlockNode, parentScope);
        
        assertEquals(globalBlockNode, scope.getScopeRoot());
    }

    @Test
    public void testCreateScope_withCatchNode_createsScopeWithCatchVariables() {
        Node catchNode = new Node(Token.CATCH, new Node(Token.NAME, "exception"), new Node(Token.BLOCK));
        Scope parentScope = new Scope(new Node(Token.EMPTY), new AbstractCompiler());
        
        Scope scope = scopeCreator.createScope(catchNode, parentScope);
        
        assertEquals(catchNode, scope.getScopeRoot());
        // Assert declarations of catch variables
        assertTrue(scope.isDeclared("exception", false));
    }

    @Test
    public void testCreateScope_withVarNode_declaresVariables() {
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "x", new Node(Token.NUMBER, 1)), new Node(Token.NAME, "y"), new Node(Token.NAME, "z"));
        Scope parentScope = new Scope(new Node(Token.EMPTY), new AbstractCompiler());
        
        Scope scope = scopeCreator.createScope(varNode, parentScope);
        
        assertEquals(varNode, scope.getScopeRoot());
        // Assert declarations of variables
        assertTrue(scope.isDeclared("x", false));
        assertTrue(scope.isDeclared("y", false));
        assertTrue(scope.isDeclared("z", false));
    }
}