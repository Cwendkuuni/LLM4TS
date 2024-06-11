import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope_WithNullParent() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(0, null);
        Scope scope = creator.createScope(n, null);

        assertNull(scope.getParent());
        assertEquals(n, scope.getRootNode());
        assertEquals(compiler, scope.getCompiler());
        assertNull(scope.getVar(ARGUMENTS));
    }

    @Test
    public void testCreateScope_WithNonNullParent() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n1 = new Node(0, null);
        Node n2 = new Node(1, null);
        Scope parent = new Scope(n1, compiler);
        Scope scope = creator.createScope(n2, parent);

        assertEquals(parent, scope.getParent());
        assertEquals(n2, scope.getRootNode());
        assertEquals(compiler, scope.getCompiler());
        assertNull(scope.getVar(ARGUMENTS));
    }

    @Test
    public void testCreateScope_WithFunctionNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.FUNCTION, null);
        n.putProp(Node.SOURCENAME_PROP, "test.js");
        Node fnName = new Node(Token.NAME, "testFunction");
        Node args = new Node(Token.LP);
        Node body = new Node(Token.BLOCK);

        n.addChildToFront(fnName);
        n.addChildToBack(args);
        n.addChildToBack(body);

        Scope parent = new Scope(new Node(0, null), compiler);
        Scope scope = creator.createScope(n, parent);

        assertNull(scope.getParent());
        assertEquals(n, scope.getRootNode());
        assertEquals(compiler, scope.getCompiler());
        assertNotNull(scope.getVar("testFunction"));
    }

    @Test
    public void testCreateScope_WithCatchNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.CATCH, null);
        Node var = new Node(Token.NAME, "error");
        Node block = new Node(Token.BLOCK);

        n.addChildToFront(var);
        n.addChildToBack(block);

        Scope parent = new Scope(new Node(0, null), compiler);
        Scope scope = creator.createScope(n, parent);

        assertNull(scope.getParent());
        assertEquals(n, scope.getRootNode());
        assertEquals(compiler, scope.getCompiler());
        assertNotNull(scope.getVar("error"));
    }

    @Test
    public void testCreateScope_WithScriptNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.SCRIPT, null);
        n.putProp(Node.SOURCENAME_PROP, "test.js");

        Scope parent = new Scope(new Node(0, null), compiler);
        Scope scope = creator.createScope(n, parent);

        assertNull(scope.getParent());
        assertEquals(n, scope.getRootNode());
        assertEquals(compiler, scope.getCompiler());
        assertNull(scope.getVar(ARGUMENTS));
    }
}