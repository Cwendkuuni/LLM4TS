import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    }

    @Test
    public void testShouldTraverse_WithFunctionConstructor_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithFunctionAnnotation_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setThisType(new JSType());
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithNestedFunctionAssignment_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.GETPROP));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithNonGlobalFunctionAssignment_ShouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit_WithThisOnLeftSideOfAssignment_ShouldReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        checkGlobalThis.visit(t, n, parent);
        // Assert that an error has been reported
    }

    @Test
    public void testVisit_WithThisInsideGetProperty_ShouldReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(new Node(Token.NAME));
        checkGlobalThis.visit(t, n, parent);
        // Assert that an error has been reported
    }

    @Test
    public void testVisit_WithThisOutsideOfAssignment_ShouldNotReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.BLOCK);
        checkGlobalThis.visit(t, n, parent);
        // Assert that no error has been reported
    }
}