import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator scopeCreator;

    @Before
    public void setup() {
        scopeCreator = new SyntacticScopeCreator(null);
    }

    @Test
    public void testCreateScope_FunctionDeclaration() {
        Node functionNode = NodeUtil.newName("foo");
        Node argumentsNode = NodeUtil.newName("args");
        Node bodyNode = NodeUtil.newBlock();

        functionNode.addChildToFront(argumentsNode);
        functionNode.addChildToBack(bodyNode);

        Scope parentScope = new Scope(null, null);
        Scope scope = scopeCreator.createScope(functionNode, parentScope);

        assertNotNull(scope);
        assertEquals(parentScope, scope.getParent());
        assertEquals(functionNode, scope.getRootNode());
        assertEquals("foo", scope.getRootVar().getName());
        assertEquals(Token.FUNCTION, scope.getRootNode().getType());
        assertEquals(Token.LP, scope.getRootNode().getFirstChild().getType());
        assertEquals(0, scope.getRootNode().getChildCount());
    }

    @Test
    public void testCreateScope_FunctionExpression() {
        Node functionNode = NodeUtil.newName("");
        Node argumentsNode = NodeUtil.newName("args");
        Node bodyNode = NodeUtil.newBlock();

        functionNode.addChildToFront(argumentsNode);
        functionNode.addChildToBack(bodyNode);

        Scope parentScope = new Scope(null, null);
        Scope scope = scopeCreator.createScope(functionNode, parentScope);

        assertNotNull(scope);
        assertEquals(parentScope, scope.getParent());
        assertEquals(functionNode, scope.getRootNode());
        assertEquals("", scope.getRootVar().getName());
        assertEquals(Token.FUNCTION, scope.getRootNode().getType());
        assertEquals(Token.LP, scope.getRootNode().getFirstChild().getType());
        assertEquals(0, scope.getRootNode().getChildCount());
    }

    @Test
    public void testCreateScope_GlobalBlock() {
        Node globalNode = NodeUtil.newBlock();

        Scope scope = scopeCreator.createScope(globalNode, null);

        assertNotNull(scope);
        assertNull(scope.getParent());
        assertEquals(globalNode, scope.getRootNode());
        assertNull(scope.getRootVar());
        assertEquals(Token.BLOCK, scope.getRootNode().getType());
        assertEquals(0, scope.getRootNode().getChildCount());
    }

    @Test
    public void testScanRoot_FunctionDeclaration() {
        Node functionNode = NodeUtil.newName("foo");
        Node argumentsNode = NodeUtil.newName("args");
        Node bodyNode = NodeUtil.newBlock();
        Node varNode = NodeUtil.newVar(NodeUtil.newName("x"), NodeUtil.newNumber(10));

        functionNode.addChildToFront(argumentsNode);
        functionNode.addChildToBack(bodyNode);
        bodyNode.addChildToFront(varNode);

        scopeCreator.scanRoot(functionNode, null);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertNull(scope.getParent());
        assertEquals(functionNode, scope.getRootNode());
        assertEquals("foo", scope.getRootVar().getName());
        assertEquals(Token.FUNCTION, scope.getRootNode().getType());
        assertEquals(1, scope.getRootNode().getChildCount());
        assertEquals("x", scope.getVar("x").getName());
        assertEquals(10, scope.getVar("x").getNode().getFirstChild().getDouble(), 0.0);
    }

    @Test
    public void testScanRoot_FunctionExpression() {
        Node functionNode = NodeUtil.newName("");
        Node argumentsNode = NodeUtil.newName("args");
        Node bodyNode = NodeUtil.newBlock();
        Node varNode = NodeUtil.newVar(NodeUtil.newName("x"), NodeUtil.newNumber(10));

        functionNode.addChildToFront(argumentsNode);
        functionNode.addChildToBack(bodyNode);
        bodyNode.addChildToFront(varNode);

        scopeCreator.scanRoot(functionNode, null);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertNull(scope.getParent());
        assertEquals(functionNode, scope.getRootNode());
        assertEquals("", scope.getRootVar().getName());
        assertEquals(Token.FUNCTION, scope.getRootNode().getType());
        assertEquals(1, scope.getRootNode().getChildCount());
        assertEquals("x", scope.getVar("x").getName());
        assertEquals(10, scope.getVar("x").getNode().getFirstChild().getDouble(), 0.0);
    }

    @Test
    public void testScanRoot_GlobalBlock() {
        Node globalNode = NodeUtil.newBlock();
        Node varNode = NodeUtil.newVar(NodeUtil.newName("x"), NodeUtil.newNumber(10));

        globalNode.addChildToFront(varNode);

        scopeCreator.scanRoot(globalNode, null);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertNull(scope.getParent());
        assertEquals(globalNode, scope.getRootNode());
        assertNull(scope.getRootVar());
        assertEquals(Token.BLOCK, scope.getRootNode().getType());
        assertEquals(1, scope.getRootNode().getChildCount());
        assertEquals("x", scope.getVar("x").getName());
        assertEquals(10, scope.getVar("x").getNode().getFirstChild().getDouble(), 0.0);
    }

    @Test
    public void testScanVars_VarDeclaration() {
        Node varNode = NodeUtil.newVar(NodeUtil.newName("x"), NodeUtil.newNumber(10));
        Node bodyNode = NodeUtil.newBlock();

        bodyNode.addChildToFront(varNode);

        scopeCreator.scanVars(bodyNode, null);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertNull(scope.getParent());
        assertNull(scope.getRootVar());
        assertEquals(bodyNode, scope.getRootNode());
        assertEquals(Token.BLOCK, scope.getRootNode().getType());
        assertEquals(1, scope.getRootNode().getChildCount());
        assertEquals("x", scope.getVar("x").getName());
        assertEquals(10, scope.getVar("x").getNode().getFirstChild().getDouble(), 0.0);
    }

    @Test
    public void testScanVars_FunctionDeclaration() {
        Node functionNode = NodeUtil.newName("foo");
        Node argumentsNode = NodeUtil.newName("args");
        Node bodyNode = NodeUtil.newBlock();

        functionNode.addChildToFront(argumentsNode);
        functionNode.addChildToBack(bodyNode);

        scopeCreator.scanVars(functionNode, null);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertNull(scope.getParent());
        assertEquals(functionNode, scope.getRootNode());
        assertEquals("foo", scope.getRootVar().getName());
        assertEquals(Token.FUNCTION, scope.getRootNode().getType());
        assertEquals(2, scope.getRootNode().getChildCount());
        assertEquals("foo", scope.getVar("foo").getName());
        assertEquals(argumentsNode, scope.getVar("args").getNode());
    }

    @Test
    public void testScanVars_ScriptBlock() {
        Node globalNode = NodeUtil.newBlock();
        Node varNode = NodeUtil.newVar(NodeUtil.newName("x"), NodeUtil.newNumber(10));

        globalNode.addChildToFront(varNode);

        scopeCreator.scanVars(globalNode, null);

        Scope scope = scopeCreator.getScope();
        assertNotNull(scope);
        assertNull(scope.getParent());
        assertNull(scope.getRootVar());
        assertEquals(globalNode, scope.getRootNode());
        assertEquals(Token.BLOCK, scope.getRootNode().getType());
        assertEquals(1, scope.getRootNode().getChildCount());
        assertEquals("x", scope.getVar("x").getName());
        assertEquals(10, scope.getVar("x").getNode().getFirstChild().getDouble(), 0.0);
    }
}