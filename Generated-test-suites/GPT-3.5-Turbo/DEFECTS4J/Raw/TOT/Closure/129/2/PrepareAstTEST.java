import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
    
    private PrepareAst prepareAst;
    private AbstractCompiler compiler;

    @Before
    public void setup() {
        compiler = new AbstractCompiler();
        prepareAst = new PrepareAst(compiler);
    }

    // Test for process method
    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXPR_RESULT);
        Node root = new Node(Token.SCRIPT);
        
        // Test when checkOnly is false
        prepareAst.process(externs, root);
        // Assert that PrepareAnnotations is called for both externs and root
        // nodes
        assertTrue(compiler.getCalledMethods().contains("PrepareAnnotations"));
        assertEquals(2, compiler.getCalledMethods().size());
        
        // Test when checkOnly is true
        PrepareAst prepareAstCheckOnly = new PrepareAst(compiler, true);
        prepareAstCheckOnly.process(externs, root);
        // Assert that normalizeNodeTypes is called for the root node
        assertTrue(compiler.getCalledMethods().contains("normalizeNodeTypes"));
        assertEquals(3, compiler.getCalledMethods().size());
    }

    // Test for normalizeNodeTypes method
    @Test
    public void testNormalizeNodeTypes() {
        Node n = new Node(Token.SCRIPT);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.STRING);
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        
        // Test when the parent pointer check fails
        Node invalidChild = new Node(Token.DELPROP);
        n.addChildToFront(invalidChild);
        assertThrows(IllegalStateException.class, () -> {
            prepareAst.normalizeNodeTypes(n);
        });
        
        // Test when the parent pointer check passes
        Node validChild = new Node(Token.FOR);
        n.addChildToFront(validChild);
        prepareAst.normalizeNodeTypes(n);
        
        // Assert that normalizeBlocks is called for both invalidChild and validChild
        assertTrue(compiler.getCalledMethods().contains("normalizeBlocks"));
        assertEquals(2, compiler.getCalledMethods().size());
    }

    // Test for normalizeBlocks method
    @Test
    public void testNormalizeBlocks() {
        Node n = new Node(Token.IF);
        Node child1 = new Node(Token.STRING);
        n.addChildToBack(child1);
        
        // Test when the node is not a control structure or label or switch
        prepareAst.normalizeBlocks(n);
        // Assert that reportChange is called
        assertTrue(compiler.getCalledMethods().contains("reportChange"));
        
        // Test when the node is a control structure code block
        Node child2 = new Node(Token.BLOCK);
        n.replaceChild(child1, child2);
        prepareAst.normalizeBlocks(n);
        // Assert that reportChange is called and the child node is added to the new block
        assertTrue(compiler.getCalledMethods().contains("reportChange"));
        assertEquals(2, compiler.getCalledMethods().size());
    }

    // Test for PrepareAnnotations class
    @Test
    public void testPrepareAnnotations() {
        PrepareAst.PrepareAnnotations prepareAnnotations = prepareAst.new PrepareAnnotations();
        
        // Test for shouldTraverse method
        NodeTraversal t = new NodeTraversal();
        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING);
        Node value = new Node(Token.STRING);
        objlit.addChildToBack(key);
        key.addChildToBack(value);
        value.addChildToBack(new Node(Token.BLOCK));
        
        prepareAnnotations.shouldTraverse(t, objlit, null);
        
        // Assert that normalizeObjectLiteralAnnotations is called
        assertTrue(compiler.getCalledMethods().contains("normalizeObjectLiteralAnnotations"));
        assertEquals(1, compiler.getCalledMethods().size());
        
        // Test for visit method with Token.CALL
        Node n = new Node(Token.CALL);
        prepareAnnotations.visit(t, n, null);
        // Assert that annotateCalls is called
        assertTrue(compiler.getCalledMethods().contains("annotateCalls"));
        assertEquals(2, compiler.getCalledMethods().size());
        
        // Test for visit method with Token.FUNCTION
        n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.VAR);
        parent.addChildToBack(n);
        prepareAnnotations.visit(t, n, parent);
        // Assert that annotateDispatchers is called
        assertTrue(compiler.getCalledMethods().contains("annotateDispatchers"));
        assertEquals(3, compiler.getCalledMethods().size());
    }

    // Test for normalizeObjectLiteralAnnotations method
    @Test
    public void testNormalizeObjectLiteralAnnotations() {
        Node objlit = new Node(Token.OBJECTLIT);
        Node key1 = new Node(Token.STRING);
        Node function = new Node(Token.FUNCTION);
        key1.addChildToBack(function);
        Node key2 = new Node(Token.STRING);
        Node value2 = new Node(Token.STRING);
        key2.addChildToBack(value2);
        objlit.addChildToBack(key1);
        objlit.addChildToBack(key2);
        
        prepareAst.normalizeObjectLiteralAnnotations(objlit);
        
        // Assert that normalizeObjectLiteralKeyAnnotations is called
        assertTrue(compiler.getCalledMethods().contains("normalizeObjectLiteralKeyAnnotations"));
        assertEquals(1, compiler.getCalledMethods().size());
        // Assert that the function node's JSDocInfo is set to the JSDocInfo of key1
        assertEquals(key1.getJSDocInfo(), function.getJSDocInfo());
    }

    // Test for annotateCalls method
    @Test
    public void testAnnotateCalls() {
        Node n = new Node(Token.CALL);
        Node first = new Node(Token.FUNCTION);
        n.addChildToBack(first);
        
        prepareAst.annotateCalls(n);
        
        // Assert that the FREE_CALL property is set to true for the node n
        assertTrue(n.getBooleanProp(Node.FREE_CALL));
    }

    // Test for annotateDispatchers method
    @Test
    public void testAnnotateDispatchers() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.VAR);
        parent.addChildToBack(n);
        parent.setJSDocInfo(new JSDocInfo());
        
        prepareAst.annotateDispatchers(n, parent);
        
        // Assert that the IS_DISPATCHER property is set to true for the node n
        assertTrue(n.getBooleanProp(Node.IS_DISPATCHER));
    }

    // Test for normalizeObjectLiteralKeyAnnotations method
    @Test
    public void testNormalizeObjectLiteralKeyAnnotations() {
        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING);
        Node value = new Node(Token.FUNCTION);
        key.addChildToBack(value);
        objlit.addChildToBack(key);
        
        prepareAst.normalizeObjectLiteralKeyAnnotations(objlit, key, value);
        
        // Assert that the JSDocInfo of the key node is set to the JSDocInfo of the value node
        assertEquals(value.getJSDocInfo(), key.getJSDocInfo());
    }

}