import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.jscomp.Scope;

public class SyntacticScopeCreatorTest {
    private AbstractCompiler compiler;
    private SyntacticScopeCreator scopeCreator;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
    }

    @Test
    public void testCreateScope_GlobalBlock() {
        Node n = new Node(Token.SCRIPT);
        Scope parent = null;

        Scope scope = scopeCreator.createScope(n, parent);

        assertEquals(n, scope.getRootNode());
        assertNull(scope.getParent());
    }

    @Test
    public void testCreateScope_FunctionBlock() {
        Node n = new Node(Token.FUNCTION);
        Scope parent = new Scope(new Node(Token.SCRIPT), compiler);

        Scope scope = scopeCreator.createScope(n, parent);

        assertEquals(n, scope.getRootNode());
        assertEquals(parent, scope.getParent());
    }

    @Test
    public void testCreateScope_FunctionExpression() {
        Node n = new Node(Token.FUNCTION);
        n.putProp(Node.SOURCENAME_PROP, "test.js");
        Node fnNameNode = new Node(Token.NAME, "fnName");
        Node args = new Node(Token.LP);
        Node body = new Node(Token.BLOCK);
        n.addChildToFront(fnNameNode);
        n.addChildAfter(args, fnNameNode);
        n.addChildToBack(body);
        Scope parent = new Scope(new Node(Token.SCRIPT), compiler);

        Scope scope = scopeCreator.createScope(n, parent);

        assertEquals(n, scope.getRootNode());
        assertEquals("test.js", scope.getSourceName());
        assertEquals(parent, scope.getParent());
        assertTrue(scope.isDeclared("fnName", false));
        assertTrue(scope.isDeclared("arguments", false));
        assertEquals(body, scope.getRootNode().getLastChild());
    }

    @Test
    public void testCreateScope_CatchBlock() {
        Node n = new Node(Token.CATCH);
        Node var = new Node(Token.NAME, "error");
        Node block = new Node(Token.BLOCK);
        n.addChildToFront(var);
        n.addChildToBack(block);
        Scope parent = new Scope(new Node(Token.SCRIPT), compiler);

        Scope scope = scopeCreator.createScope(n, parent);

        assertEquals(n, scope.getRootNode());
        assertEquals(parent, scope.getParent());
        assertTrue(scope.isDeclared("error", false));
        assertEquals(block, scope.getRootNode().getLastChild());
    }

    @Test
    public void testCreateScope_VariableDeclaration() {
        Node n = new Node(Token.VAR);
        Node var1 = new Node(Token.NAME, "x");
        Node var2 = new Node(Token.NAME, "y");
        Node var3 = new Node(Token.NAME, "z");
        n.addChildToFront(var1);
        n.addChildAfter(var2, var1);
        n.addChildToBack(var3);
        Scope parent = new Scope(new Node(Token.SCRIPT), compiler);

        Scope scope = scopeCreator.createScope(n, parent);

        assertEquals(n, scope.getRootNode());
        assertEquals(parent, scope.getParent());
        assertTrue(scope.isDeclared("x", false));
        assertTrue(scope.isDeclared("y", false));
        assertTrue(scope.isDeclared("z", false));
    }

    @Test
    public void testScanVars_GlobalBlock() {
        Node n = new Node(Token.SCRIPT);
        Scope parent = null;

        scopeCreator.createScope(n, parent);
        scopeCreator.scanVars(n, null);

        assertEquals(n, scopeCreator.getScope().getRootNode());
        assertNull(scopeCreator.getScope().getParent());
    }

    @Test
    public void testScanVars_FunctionBlock() {
        Node n = new Node(Token.FUNCTION);
        Scope parent = new Scope(new Node(Token.SCRIPT), compiler);

        scopeCreator.createScope(n, parent);
        scopeCreator.scanVars(n, parent.getRootNode());

        assertEquals(n, scopeCreator.getScope().getRootNode());
        assertEquals(parent, scopeCreator.getScope().getParent());
    }

    @Test
    public void testScanVars_FunctionExpression() {
        Node n = new Node(Token.FUNCTION);
        Scope parent = new Scope(new Node(Token.SCRIPT), compiler);
        Node fnNameNode = new Node(Token.NAME, "fnName");
        Node args = new Node(Token.LP);
        Node body = new Node(Token.BLOCK);
        n.addChildToFront(fnNameNode);
        n.addChildAfter(args, fnNameNode);
        n.addChildToBack(body);

        scopeCreator.createScope(n, parent);
        scopeCreator.scanVars(n, parent.getRootNode());

        assertEquals(n, scopeCreator.getScope().getRootNode());
        assertEquals(parent, scopeCreator.getScope().getParent());
        assertTrue(scopeCreator.getScope().isDeclared("fnName", false));
        assertTrue(scopeCreator.getScope().isDeclared("arguments", false));
        assertEquals(body, scopeCreator.getScope().getRootNode().getLastChild());
    }

    @Test
    public void testScanVars_CatchBlock() {
        Node n = new Node(Token.CATCH);
        Scope parent = new Scope(new Node(Token.SCRIPT), compiler);
        Node var = new Node(Token.NAME, "error");
        Node block = new Node(Token.BLOCK);
        n.addChildToFront(var);
        n.addChildToBack(block);

        scopeCreator.createScope(n, parent);
        scopeCreator.scanVars(n, parent.getRootNode());

        assertEquals(n, scopeCreator.getScope().getRootNode());
        assertEquals(parent, scopeCreator.getScope().getParent());
        assertTrue(scopeCreator.getScope().isDeclared("error", false));
        assertEquals(block, scopeCreator.getScope().getRootNode().getLastChild());
    }

    @Test
    public void testScanVars_VariableDeclaration() {
        Node n = new Node(Token.VAR);
        Scope parent = new Scope(new Node(Token.SCRIPT), compiler);
        Node var1 = new Node(Token.NAME, "x");
        Node var2 = new Node(Token.NAME, "y");
        Node var3 = new Node(Token.NAME, "z");
        n.addChildToFront(var1);
        n.addChildAfter(var2, var1);
        n.addChildToBack(var3);

        scopeCreator.createScope(n, parent);
        scopeCreator.scanVars(n, parent.getRootNode());

        assertEquals(n, scopeCreator.getScope().getRootNode());
        assertEquals(parent, scopeCreator.getScope().getParent());
        assertTrue(scopeCreator.getScope().isDeclared("x", false));
        assertTrue(scopeCreator.getScope().isDeclared("y", false));
        assertTrue(scopeCreator.getScope().isDeclared("z", false));
    }

    @Test
    public void testDeclareVar_FirstDeclaration() {
        String name = "x";
        Node n = new Node(Token.NAME, name);
        Node parent = new Node(Token.SCRIPT);
        Node gramps = null;
        JSType declaredType = null;
        Node nodeWithLineNumber = n;

        Scope scope = new Scope(parent, compiler);
        scopeCreator.setScope(scope);
        scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        assertTrue(scope.isDeclared(name, false));
        assertEquals(n, scope.getVar(name).getNode());
    }

    @Test
    public void testDeclareVar_MultipleDeclarations() {
        String name = "x";
        Node n = new Node(Token.NAME, name);
        Node parent = new Node(Token.SCRIPT);
        Node gramps = null;
        JSType declaredType = null;
        Node nodeWithLineNumber = n;

        Scope scope = new Scope(parent, compiler);
        scope.declare(name, new Node(Token.NAME), null, compiler.getInput(""));
        scopeCreator.setScope(scope);
        scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        assertFalse(scope.isDeclared(name, false));
        assertEquals(1, compiler.getErrorCount());
    }

    @Test
    public void testDeclareVar_GlobalScope() {
        String name = "x";
        Node n = new Node(Token.NAME, name);
        Node parent = null;
        Node gramps = null;
        JSType declaredType = null;
        Node nodeWithLineNumber = n;

        Scope scope = new Scope(new Node(Token.SCRIPT), compiler);
        scopeCreator.setScope(scope);
        scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

        assertTrue(scope.isDeclared(name, false));
        assertEquals(n, scope.getVar(name).getNode());
    }
}