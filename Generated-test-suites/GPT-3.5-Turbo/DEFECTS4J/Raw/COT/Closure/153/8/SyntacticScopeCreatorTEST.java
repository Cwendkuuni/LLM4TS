
import junit.framework.TestCase;
import org.junit.Test;

public class SyntacticScopeCreatorTest extends TestCase {

    @Test
    public void testCreateScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.SCRIPT);
        Scope parent = null;
        Scope scope = scopeCreator.createScope(n, parent);
        assertNotNull(scope);
        assertEquals(scope.getParent(), parent);
        assertEquals(scope.getRootNode(), n);
    }

    @Test
    public void testScanRoot_functionNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        
        Node fnNode = new Node(Token.FUNCTION);
        fnNode.putProp(Node.SOURCENAME_PROP, "test.js");
        
        Node fnNameNode = new Node(Token.NAME, "myFunc");
        Node args = new Node(Token.LP);
        Node body = new Node(Token.BLOCK);

        fnNode.addChildToBack(fnNameNode);
        fnNode.addChildToBack(args);
        fnNode.addChildToBack(body);
        
        Scope parent = new Scope(null, new Node(Token.SCRIPT));
        scopeCreator.scanRoot(fnNode, parent);
        
        Scope scope = scopeCreator.createScope(fnNode, parent);
        
        assertNotNull(scope);
        assertEquals(scope.getParent(), parent);
        assertEquals(scope.getRootNode(), fnNode);
        
        Scope.Var fnVar = scope.getVar("myFunc");
        assertNotNull(fnVar);
        assertEquals(fnVar.getParentNode(), fnNode);
        assertEquals(fnVar.getInput().getName(), "test.js");
    }

    @Test
    public void testScanRoot_globalBlockNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.putProp(Node.SOURCENAME_PROP, "test.js");

        Scope scope = scopeCreator.createScope(scriptNode, null);

        assertNotNull(scope);
        assertNull(scope.getParent());
        assertEquals(scope.getRootNode(), scriptNode);
    }

    @Test
    public void testScanVars_varNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node varNode = new Node(Token.VAR);
        Node child1 = new Node(Token.NAME, "x");
        Node child2 = new Node(Token.NAME, "y");
        Node child3 = new Node(Token.NAME, "z");

        varNode.addChildToBack(child1);
        varNode.addChildToBack(child2);
        varNode.addChildToBack(child3);

        Node parent = new Node(Token.SCRIPT);
        Scope scope = new Scope(null, parent);
        scopeCreator.scope = scope;

        scopeCreator.scanVars(varNode, parent);

        Scope.Var xVar = scope.getVar("x");
        assertNotNull(xVar);
        assertEquals(xVar.getParentNode(), varNode);

        Scope.Var yVar = scope.getVar("y");
        assertNotNull(yVar);
        assertEquals(yVar.getParentNode(), varNode);

        Scope.Var zVar = scope.getVar("z");
        assertNotNull(zVar);
        assertEquals(zVar.getParentNode(), varNode);
    }

    @Test
    public void testScanVars_functionNodeDeclaration() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node fnNode = new Node(Token.FUNCTION);
        Node fnNameNode = new Node(Token.NAME, "myFunc");
        Node args = new Node(Token.LP);
        Node body = new Node(Token.BLOCK);

        fnNode.addChildToBack(fnNameNode);
        fnNode.addChildToBack(args);
        fnNode.addChildToBack(body);

        Node parent = new Node(Token.SCRIPT);
        Scope scope = new Scope(null, parent);
        scopeCreator.scope = scope;

        scopeCreator.scanVars(fnNode, parent);

        Scope.Var fnVar = scope.getVar("myFunc");
        assertNotNull(fnVar);
        assertEquals(fnVar.getParentNode(), fnNode);
    }

    @Test
    public void testScanVars_catchNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node catchNode = new Node(Token.CATCH);
        Node catchVarNode = new Node(Token.NAME, "e");
        Node blockNode = new Node(Token.BLOCK);

        catchNode.addChildToBack(catchVarNode);
        catchNode.addChildToBack(blockNode);

        Node parent = new Node(Token.SCRIPT);
        Scope scope = new Scope(null, parent);
        scopeCreator.scope = scope;

        scopeCreator.scanVars(catchNode, parent);

        Scope.Var eVar = scope.getVar("e");
        assertNotNull(eVar);
        assertEquals(eVar.getParentNode(), catchNode);
    }

    @Test
    public void testScanVars_controlStructureNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node ifNode = new Node(Token.IF);
        Node conditionNode = new Node(Token.TRUE);
        Node ifBlockNode = new Node(Token.BLOCK);
        Node elseBlockNode = new Node(Token.BLOCK);

        ifNode.addChildToBack(conditionNode);
        ifNode.addChildToBack(ifBlockNode);
        ifNode.addChildToBack(elseBlockNode);

        Node parent = new Node(Token.SCRIPT);
        Scope scope = new Scope(null, parent);
        scopeCreator.scope = scope;

        scopeCreator.scanVars(ifNode, parent);

        // Variables should not be declared inside the control structure
        assertNull(scope.getVar("x"));
        assertNull(scope.getVar("y"));
    }
}

