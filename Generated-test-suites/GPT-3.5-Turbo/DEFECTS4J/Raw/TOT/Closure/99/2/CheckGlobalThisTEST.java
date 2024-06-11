import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(null, null);
    }

    @Test
    public void testShouldTraverse() {
        // Typical use case with function annotated with @this
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION, null);
        Node parent = new Node(Token.NAME, null);
        t.traverse(n, parent);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));

        // Typical use case with function annotated with @constructor
        n = new Node(Token.FUNCTION, null);
        parent = new Node(Token.NAME, null);
        n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().annotateConstructor().build());
        t.traverse(n, parent);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));

        // Typical use case with function annotated with @override
        n = new Node(Token.FUNCTION, null);
        parent = new Node(Token.NAME, null);
        n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().annotateOverride().build());
        t.traverse(n, parent);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));

        // Typical use case with function nested in a block
        n = new Node(Token.FUNCTION, null);
        parent = new Node(Token.BLOCK, null);
        t.traverse(n, parent);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));

        // Edge case with function nested in a script
        n = new Node(Token.FUNCTION, null);
        parent = new Node(Token.SCRIPT, null);
        t.traverse(n, parent);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));

        // Edge case with function nested in an assignment
        n = new Node(Token.FUNCTION, null);
        parent = new Node(Token.ASSIGN, null);
        t.traverse(n, parent);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));

        // Edge case with no parent
        n = new Node(Token.FUNCTION, null);
        parent = null;
        t.traverse(n, parent);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));

        // Error scenario with non-function node
        n = new Node(Token.NAME, null);
        parent = new Node(Token.NAME, null);
        t.traverse(n, parent);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit() {
        // Typical use case with THIS on the left side of an assign
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS, null);
        Node parent = new Node(Token.ASSIGN, null);
        t.traverse(n, parent);
        Compiler compiler = new Compiler();
        checkGlobalThis.visit(t, n, parent);
        assertEquals(1, compiler.getWarnings().length);

        // Typical use case with THIS property access
        n = new Node(Token.THIS, null);
        parent = new Node(Token.GETPROP, null);
        t.traverse(n, parent);
        compiler = new Compiler();
        checkGlobalThis.visit(t, n, parent);
        assertEquals(1, compiler.getWarnings().length);

        // Edge case with THIS not on the left side of an assign or property access
        n = new Node(Token.THIS, null);
        parent = new Node(Token.ADD, null);
        t.traverse(n, parent);
        compiler = new Compiler();
        checkGlobalThis.visit(t, n, parent);
        assertEquals(0, compiler.getWarnings().length);
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        // Typical use case with JSDoc info directly on the function node
        Node n = new Node(Token.FUNCTION, null);
        n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
        Node parent = new Node(Token.BLOCK, null);
        assertEquals(n.getJSDocInfo(), checkGlobalThis.getFunctionJsDocInfo(n));

        // Typical use case with JSDoc info on the parent node
        parent = new Node(Token.FUNCTION, null);
        parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
        n = new Node(Token.NAME, null);
        parent.addChildToBack(n);
        assertEquals(parent.getJSDocInfo(), checkGlobalThis.getFunctionJsDocInfo(n));

        // Edge case with no JSDoc info on the function or the parent node
        n = new Node(Token.FUNCTION, null);
        parent = new Node(Token.BLOCK, null);
        assertEquals(null, checkGlobalThis.getFunctionJsDocInfo(n));
    }

}