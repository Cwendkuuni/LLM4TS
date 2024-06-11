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
    public void testCreateScope_FunctionNode_ReturnsScope() {
        Node n = new Node(Token.FUNCTION);
        Scope parent = new Scope();
        Scope scope = scopeCreator.createScope(n, parent);
        assertNotNull(scope);
    }

    @Test
    public void testCreateScope_GlobalBlock_ReturnsScope() {
        Node n = new Node(Token.SCRIPT);
        Scope scope = scopeCreator.createScope(n, null);
        assertNotNull(scope);
    }

    @Test
    public void testScanVars_VarNode_DeclaresVariables() {
        Node n = new Node(Token.VAR);
        Node child1 = new Node(Token.NAME);
        child1.setString("x");
        Node child2 = new Node(Token.NAME);
        child2.setString("y");
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        scopeCreator.scanVars(n, null);
        assertTrue(scopeCreator.getScope().isDeclared("x", false));
        assertTrue(scopeCreator.getScope().isDeclared("y", false));
    }

    @Test
    public void testScanVars_FunctionNode_DeclaresVariable() {
        Node n = new Node(Token.FUNCTION);
        Node child1 = new Node(Token.NAME);
        child1.setString("func");
        n.addChildToBack(child1);
        scopeCreator.scanVars(n, null);
        assertTrue(scopeCreator.getScope().isDeclared("func", false));
    }

    @Test
    public void testScanVars_CatchNode_DeclaresVariable() {
        Node n = new Node(Token.CATCH);
        Node child1 = new Node(Token.NAME);
        child1.setString("err");
        Node child2 = new Node(Token.BLOCK);
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        scopeCreator.scanVars(n, null);
        assertTrue(scopeCreator.getScope().isDeclared("err", false));
    }

    @Test
    public void testScanVars_ScriptNode_SetsSourceName() {
        Node n = new Node(Token.SCRIPT);
        n.putProp(Node.SOURCENAME_PROP, "source.js");
        scopeCreator.scanVars(n, null);
        assertEquals("source.js", scopeCreator.getSourceName());
    }

}