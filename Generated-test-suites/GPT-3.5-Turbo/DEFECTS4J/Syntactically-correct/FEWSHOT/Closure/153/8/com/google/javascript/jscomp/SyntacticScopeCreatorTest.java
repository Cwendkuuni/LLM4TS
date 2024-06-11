import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
    private ScopeCreator scopeCreator;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler();
        RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
        scopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
    }

    @After
    public void tearDown() {
        scopeCreator = null;
    }

    @Test
    public void testCreateScopeForFunction() {
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.SOURCENAME_PROP, "test.js");
        Scope parent = new Scope(null, new Node(Token.SCRIPT));

        Scope scope = scopeCreator.createScope(n, parent);

        assertEquals(n, scope.getRootNode());
        assertNull(scope.getParent());
        assertEquals("test.js", scope.getSourceName());
        assertEquals(parent.getDepth() + 1, scope.getDepth());
    }

    @Test
    public void testCreateScopeForGlobalBlock() {
        Node n = new Node(Token.SCRIPT);
        n.putProp(Node.SOURCENAME_PROP, "test.js");

        Scope scope = scopeCreator.createScope(n, null);

        assertEquals(n, scope.getRootNode());
        assertNull(scope.getParent());
        assertEquals("test.js", scope.getSourceName());
        assertEquals(0, scope.getDepth());
    }

    @Test
    public void testScanVarsForVar() {
        Node n = new Node(Token.VAR);
        n.addChildToBack(new Node(Token.NAME, "x"));
        n.getSecondChild().addChildToBack(new Node(Token.NAME, "y"));
        n.getSecondChild().addChildToBack(new Node(Token.NAME, "z"));

        Node parent = new Node(Token.FUNCTION);
        parent.putProp(Node.SOURCENAME_PROP, "test.js");

        Scope scope = new Scope(parent, new Node(Token.SCRIPT));
        scopeCreator.scanVars(n, parent);

        assertEquals(3, scope.getVarCount());
        assertEquals("x", scope.getSlot(0).getName());
        assertEquals("y", scope.getSlot(1).getName());
        assertEquals("z", scope.getSlot(2).getName());
    }

    @Test
    public void testScanVarsForFunctionExpression() {
        Node n = new Node(Token.FUNCTION);
        n.putBooleanProp(Node.FUNCTION_EXPRESSION, true);
        n.addChildToBack(new Node(Token.NAME, "foo"));

        Node parent = new Node(Token.SCRIPT);
        parent.putProp(Node.SOURCENAME_PROP, "test.js");

        Scope scope = new Scope(parent, new Node(Token.SCRIPT));
        scopeCreator.scanVars(n, parent);

        assertEquals(1, scope.getVarCount());
        assertEquals("foo", scope.getSlot(0).getName());
    }

    @Test
    public void testScanVarsForCatch() {
        Node n = new Node(Token.CATCH);
        n.addChildToBack(new Node(Token.NAME, "e"));
        n.addChildToBack(new Node(Token.EMPTY));

        Node parent = new Node(Token.FUNCTION);
        parent.putProp(Node.SOURCENAME_PROP, "test.js");

        Scope scope = new Scope(parent, new Node(Token.SCRIPT));
        scopeCreator.scanVars(n, parent);

        assertEquals(1, scope.getVarCount());
        assertEquals("e", scope.getSlot(0).getName());
    }

    @Test
    public void testScanVarsForScript() {
        Node n = new Node(Token.SCRIPT);
        n.putProp(Node.SOURCENAME_PROP, "test.js");

        Scope scope = new Scope(null, new Node(Token.SCRIPT));
        scopeCreator.scanVars(n, null);

        assertEquals("test.js", scope.getSourceName());
    }
}