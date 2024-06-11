import org.junit.*;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope_functionDeclaration() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        
        Node node = new Node(Token.FUNCTION);
        node.putProp(Node.SOURCENAME_PROP, "test.js");
        Scope scope = creator.createScope(node, null);
        
        assertNull(scope.getParent());
        assertEquals("test.js", creator.sourceName);
        assertNotNull(creator.scope);
    }

    @Test
    public void testCreateScope_globalBlock() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        
        Node node = new Node(Token.SCRIPT);
        node.putProp(Node.SOURCENAME_PROP, "test.js");
        Scope scope = creator.createScope(node, null);
        
        assertNull(scope.getParent());
        assertEquals("test.js", creator.sourceName);
        assertNotNull(creator.scope);
    }

    @Test
    public void testScanRoot_function() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        
        Node node = new Node(Token.FUNCTION);
        node.putProp(Node.SOURCENAME_PROP, "test.js");
        
        Node fnNameNode = new Node(Token.NAME, "testFunc");
        Node args = new Node(Token.LP);
        Node body = new Node(Token.BLOCK);
        
        node.addChildToBack(fnNameNode);
        node.addChildToBack(args);
        node.addChildToBack(body);
        
        creator.sourceName = "test.js";
        Scope parentScope = new Scope(node, compiler);
        creator.scope = parentScope;
        creator.scanRoot(node, parentScope);
        
        assertNotNull(parentScope.getVar("testFunc"));
        assertNotNull(parentScope.getVar("arguments"));
    }

    @Test
    public void testScanRoot_globalBlock() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        
        Node node = new Node(Token.SCRIPT);
        node.putProp(Node.SOURCENAME_PROP, "test.js");
        
        creator.sourceName = "test.js";
        creator.scope = new Scope(node, compiler);
        creator.scanRoot(node, null);
        
        assertNull(creator.scope.getParent());
        assertNotNull(creator.scope.getVar("arguments"));
    }

    @Test
    public void testScanVars_varDeclare() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        
        Node node = new Node(Token.VAR);
        Node child1 = new Node(Token.NAME, "x");
        Node child2 = new Node(Token.NAME, "y");
        Node child3 = new Node(Token.NAME, "z");
        
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        
        Scope parentScope = new Scope(node, compiler);
        creator.scope = parentScope;
        
        creator.scanVars(node, null);
        
        assertNotNull(parentScope.getVar("x"));
        assertNotNull(parentScope.getVar("y"));
        assertNotNull(parentScope.getVar("z"));
    }

    @Test
    public void testScanVars_functionExpression() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        
        Node node = createFunctionExpressionNode();
        
        Scope parentScope = new Scope(node, compiler);
        creator.scope = parentScope;
        
        creator.scanVars(node, null);
        
        assertNull(parentScope.getVar("testFunc"));
    }

    @Test
    public void testScanVars_catchBlock() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        
        Node node = createCatchNode();
        
        Scope parentScope = new Scope(node, compiler);
        creator.scope = parentScope;
        
        creator.scanVars(node, null);
        
        assertNotNull(parentScope.getVar("x"));
    }

    @Test
    public void testScanVars_statementBlock() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        
        Node node = new Node(Token.BLOCK);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.FUNCTION);
        
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        
        Scope parentScope = new Scope(node, compiler);
        creator.scope = parentScope;
        
        creator.scanVars(node, null);
        
        assertNotNull(parentScope.getVar("x"));
        assertNull(parentScope.getVar("testFunc"));
    }

    private Node createFunctionExpressionNode() {
        Node node = new Node(Token.FUNCTION);
        
        Node fnNameNode = new Node(Token.EMPTY);
        Node args = new Node(Token.LP);
        Node body = new Node(Token.BLOCK);
        
        node.addChildToBack(fnNameNode);
        node.addChildToBack(args);
        node.addChildToBack(body);
        
        return node;
    }

    private Node createCatchNode() {
        Node node = new Node(Token.CATCH);
        
        Node var = new Node(Token.NAME, "x");
        Node block = new Node(Token.BLOCK);
        
        node.addChildToBack(var);
        node.addChildToBack(block);
        
        return node;
    }
}