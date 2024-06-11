import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope_FunctionDeclaration() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.FUNCTION);
        Scope parent = new Scope(null, null);

        Scope scope = creator.createScope(n, parent);

        assertEquals(parent, scope.getParent());
        assertEquals(n, scope.getRootNode());
    }

    @Test
    public void testCreateScope_FunctionExpression() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.FUNCTION);
        Scope parent = new Scope(null, null);

        Scope scope = creator.createScope(n, parent);

        assertEquals(parent, scope.getParent());
        assertEquals(n, scope.getRootNode());
    }

    @Test
    public void testCreateScope_GlobalBlock() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.BLOCK);
        Scope parent = null;

        Scope scope = creator.createScope(n, parent);

        assertNull(scope.getParent());
        assertEquals(n, scope.getRootNode());
    }

    @Test
    public void testScanVars_VarDeclaration() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);

        creator.scanVars(n, parent);

        // TODO: Verify result
    }

    @Test
    public void testScanVars_FunctionDeclaration() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);

        creator.scanVars(n, parent);

        // TODO: Verify result
    }

    @Test
    public void testScanVars_FunctionExpression() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);

        creator.scanVars(n, parent);

        // TODO: Verify result
    }

    @Test
    public void testScanVars_CatchBlock() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.CATCH);
        Node parent = new Node(Token.SCRIPT);

        creator.scanVars(n, parent);

        // TODO: Verify result
    }

    @Test
    public void testScanVars_ScriptBlock() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.SCRIPT);
        Node parent = null;

        creator.scanVars(n, parent);

        // TODO: Verify result
    }

    @Test
    public void testDeclareVar_UniqueName() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        String name = "testVar";
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.SCRIPT);
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node(Token.VAR);

        creator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        Scope scope = creator.createScope(parent, null);
        assertNotNull(scope.getVar(name));
    }

    @Test
    public void testDeclareVar_Redeclaration() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        String name = "testVar";
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.SCRIPT);
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node(Token.VAR);

        creator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);
        creator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        // TODO: Verify result
    }
}