import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testAssignLhsChildIsNull() {
        CheckGlobalThis check = new CheckGlobalThis(null);
        check.assignLhsChild = null;
        check.shouldReportThis(null);

        assertNull(check.assignLhsChild);
    }

    @Test
    public void testAssignLhsChildIsNotNull() {
        CheckGlobalThis check = new CheckGlobalThis(null);
        check.assignLhsChild = new Node();
        check.shouldReportThis(null);

        assertNull(check.assignLhsChild);
    }

    @Test
    public void testShouldReportThis() {
        CheckGlobalThis check = new CheckGlobalThis(null);
        Node node = new Node();
        Node parent = new Node();

        boolean result = check.shouldReportThis(node);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverseWithFunction() {
        CheckGlobalThis check = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node();
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        Node gramps = new Node();

        boolean result = check.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverseWithParentNotObjectLiteralKeys() {
        CheckGlobalThis check = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node();
        Node parent = new Node(Token.BLOCK);

        boolean result = check.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverseWithParentIsAssign() {
        CheckGlobalThis check = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node();
        Node parent = new Node(Token.ASSIGN);

        boolean result = check.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverseWithParentIsObjectLitKey() {
        CheckGlobalThis check = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node();
        Node parent = new Node(Token.STRING_KEY);
        Node gramps = new Node();
        JSDocInfo maybeLends = new JSDocInfo();
        gramps.setJSDocInfo(maybeLends);

        boolean result = check.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverseWithParentNotObjectLitKey() {
        CheckGlobalThis check = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node();
        Node parent = new Node(Token.IMPORT);

        boolean result = check.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testVisitIsThis() {
        CheckGlobalThis check = new CheckGlobalThis(null);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node();
        Node parent = new Node(Token.IMPORT);

        check.visit(t, n, parent);

        // Assert something here
    }
}