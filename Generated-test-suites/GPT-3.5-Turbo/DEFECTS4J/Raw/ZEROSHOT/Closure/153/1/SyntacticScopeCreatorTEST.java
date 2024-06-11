import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
    private AbstractCompiler compiler;
    private SyntacticScopeCreator scopeCreator;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
    }

    @Test
    public void testCreateScope_FunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.SOURCENAME_PROP, "test.js");

        Scope parent = new Scope(null, null);
        Scope result = scopeCreator.createScope(n, parent);

        assertNotNull(result);
        assertEquals("test.js", result.getSourceFile().getName());
        assertTrue(result.isDeclared("arguments", false));
    }

    @Test
    public void testCreateScope_GlobalBlock() {
        Node n = new Node(Token.SCRIPT);
        n.putProp(Node.SOURCENAME_PROP, "test.js");

        Scope result = scopeCreator.createScope(n, null);

        assertNotNull(result);
        assertEquals("test.js", result.getSourceFile().getName());
        assertNull(result.getParent());
    }

    @Test
    public void testScanVars_VarDeclaration() {
        Node n = new Node(Token.VAR);
        n.addChildToBack(new Node(Token.NAME, new Node("x")));
        n.addChildToBack(new Node(Token.NAME, new Node("y")));
        n.addChildToBack(new Node(Token.NAME, new Node("z")));

        Node parent = new Node(Token.SCRIPT);
        scopeCreator.scanVars(n, parent);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertTrue(scope.isDeclared("x", false));
        assertTrue(scope.isDeclared("y", false));
        assertTrue(scope.isDeclared("z", false));
    }

    @Test
    public void testScanVars_FunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToBack(new Node(Token.NAME, new Node("foo")));
        n.addChildToBack(new Node(Token.LP));
        n.addChildToBack(new Node(Token.BLOCK));

        Node parent = new Node(Token.SCRIPT);
        scopeCreator.scanVars(n, parent);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertTrue(scope.isDeclared("foo", false));
    }

    @Test
    public void testScanVars_CatchBlock() {
        Node n = new Node(Token.CATCH);
        n.addChildToFront(new Node(Token.NAME, new Node("e")));
        n.addChildToBack(new Node(Token.BLOCK));

        Node parent = new Node(Token.SCRIPT);
        scopeCreator.scanVars(n, parent);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertTrue(scope.isDeclared("e", false));
    }

    @Test
    public void testScanVars_ControlStructure() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.VAR));

        Node parent = new Node(Token.SCRIPT);
        scopeCreator.scanVars(n, parent);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertFalse(scope.isDeclared("x", false));
    }

    @Test
    public void testScanVars_StatementBlock() {
        Node n = new Node(Token.BLOCK);
        n.addChildToBack(new Node(Token.VAR));
        n.addChildToBack(new Node(Token.EXPR_RESULT));

        Node parent = new Node(Token.SCRIPT);
        scopeCreator.scanVars(n, parent);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertFalse(scope.isDeclared("x", false));
    }

    @Test
    public void testDeclareVar() {
        Node n = new Node(Token.NAME, new Node("x"));
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.SCRIPT);
        Node nodeWithLineNumber = new Node();

        scopeCreator.declareVar("x", n, parent, gramps, null, nodeWithLineNumber);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertTrue(scope.isDeclared("x", false));
    }
}