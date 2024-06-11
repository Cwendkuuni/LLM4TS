import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope_NullNodeAndParent_ReturnsScopeWithNullSourceName() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
        Scope scope = scopeCreator.createScope(null, null);
        
        assertNull(scope.getParent());
        assertNull(scope.getSourceName());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateScope_NullCompiler_ThrowsNullPointerException() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(null);
        scopeCreator.createScope(new Node(Token.SCRIPT), null);
    }

    @Test
    public void testDeclareVar_VarNameAlreadyDeclaredInScope_ReportError() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
        Scope scope = new Scope(new Node(Token.SCRIPT), new AbstractCompiler());
        scope.declare("x", new Node(Token.NAME), null, new CompilerInput("test_input"));
        
        scopeCreator.declareVar("x", new Node(Token.NAME), new Node(Token.VAR),
                                new Node(Token.SCRIPT), null, new Node(Token.SCRIPT));
        
        assertEquals(1, scope.getVarCount());
        assertNotNull(scope.getVar("x"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeclareVar_NullName_ThrowsIllegalArgumentException() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
        scopeCreator.declareVar(null, new Node(Token.NAME), new Node(Token.VAR),
                                new Node(Token.SCRIPT), null, new Node(Token.SCRIPT));
    }

    @Test
    public void testCreateScope_FunctionNodeWithEmptyFunctionName_AddFunctionNameToScope() {
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME, "testFunc");
        functionNode.addChildToBack(nameNode);

        Scope scope = scopeCreator.createScope(functionNode, null);

        assertNotNull(scope.getVar("testFunc"));
    }
}