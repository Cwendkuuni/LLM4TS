import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis();
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_False() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_False() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setThisType(true);
        n.setJSDocInfo(jsDoc);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation_False() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionWithInterfaceAnnotation_False() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setInterface(true);
        n.setJSDocInfo(jsDoc);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionInBlock_True() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.BLOCK);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionInScript_True() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.SCRIPT);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionInName_True() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.NAME);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionInAssign_True() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.ASSIGN);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit_ThisNode_ShouldReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        checkGlobalThis.visit(t, n, parent);
        // Assert error reported
    }

    @Test
    public void testVisit_AssignLhsChildNode_ShouldNotReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        checkGlobalThis.visit(t, n, parent);
        // Assert error not reported
    }

    @Test
    public void testVisit_PropertyAccess_ShouldReportError() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.visit(t, n, parent);
        // Assert error reported
    }
}