import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    // Test for CheckGlobalThis constructor
    @Test
    public void testConstructor() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
        assertNotNull(checkGlobalThis);
    }

    // Test shouldTraverse method when given valid parameters
    @Test
    public void testShouldTraverseValid() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal t = new NodeTraversal(compiler, new Callback()); 
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);

        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    // Test shouldTraverse method when given invalid parameters
    @Test
    public void testShouldTraverseInvalid() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal t = new NodeTraversal(compiler, new Callback());
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ADD);

        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    // Test visit method for reporting global this
    @Test
    public void testVisitGlobalThisError() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal t = new NodeTraversal(compiler, new Callback());
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);

        checkGlobalThis.visit(t, n, parent);

        assertTrue(t.getErrorCount() > 0);
    }

    // Test visit method for not reporting global this
    @Test
    public void testVisitNoGlobalThisError() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        NodeTraversal t = new NodeTraversal(compiler, new Callback());
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);

        checkGlobalThis.visit(t, n, parent);

        assertEquals(0, t.getErrorCount());
    }

    // Test getFunctionJsDocInfo method with function and JSDoc
    @Test
    public void testGetFunctionJsDocInfoWithJSDoc() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node function = new Node(Token.FUNCTION);
        function.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder(true).build());

        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(function);

        assertNotNull(jsDocInfo);
    }

    // Test getFunctionJsDocInfo method with function but no JSDoc
    @Test
    public void testGetFunctionJsDocInfoWithoutJSDoc() {
        AbstractCompiler compiler = new Compiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

        Node function = new Node(Token.FUNCTION);

        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(function);

        assertNull(jsDocInfo);
    }

}