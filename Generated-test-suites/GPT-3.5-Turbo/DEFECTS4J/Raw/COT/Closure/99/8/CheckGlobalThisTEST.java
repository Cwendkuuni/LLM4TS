
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldNotTraverse_constructorFunction() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldNotTraverse_functionWithThisAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldNotTraverse_functionWithOverrideAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldNotTraverse_nestedFunction() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.LET);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_assignment_lhsChildNotNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        Node assignLhsChild = new Node(Token.NAME);
        checkGlobalThis.assignLhsChild = assignLhsChild;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldNotTraverse_assignment_prototypeProperty() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal();
        Node lhs = new Node(Token.GETPROP);
        lhs.getLastChild().setString("prototype");
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(lhs);
        boolean result = checkGlobalThis.shouldTraverse(t, lhs, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldNotTraverse_assignment_nestedPrototypeProperty() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal();
        Node lhs = new Node(Token.GETPROP);
        lhs.setQualifiedName("a.prototype.b");
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(lhs);
        boolean result = checkGlobalThis.shouldTraverse(t, lhs, parent);
        assertFalse(result);
    }

    @Test
    public void testVisit_globalThisReported() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        // Check that a warning is reported
        assertEquals(1, t.getErrors().size());
    }

    @Test
    public void testVisit_assignLhsChildNotNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        Node assignLhsChild = new Node(Token.NAME);
        checkGlobalThis.assignLhsChild = assignLhsChild;
        checkGlobalThis.visit(t, n, parent);
        // Check that assignLhsChild is set to null after visit
        assertNull(checkGlobalThis.assignLhsChild);
    }
}
