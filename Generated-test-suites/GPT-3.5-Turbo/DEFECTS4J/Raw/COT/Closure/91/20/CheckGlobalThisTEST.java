
import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_shouldReturnTrueForGlobalContexts() {
        CheckGlobalThis checker = new CheckGlobalThis();
        boolean result = checker.shouldTraverse(null, null, null);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForNonGlobalContexts() {
        CheckGlobalThis checker = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        boolean result = checker.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testVisit_shouldReportErrorIfThisIsOnLeftSideOfAssignment() {
        CheckGlobalThis checker = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        DiagnosticType expected = DiagnosticType.warning(
            "JSC_USED_GLOBAL_THIS",
            "dangerous use of the global 'this' object");
        compiler.setErrorLevel(DiagnosticType.warning, CheckLevel.WARNING);
        checker.visit(t, n, parent);
        assertTrue(t.hasErrors());
        DiagnosticType actual = t.getError(n);
        assertEquals(expected, actual);
    }

    @Test
    public void testVisit_shouldReportErrorIfThisIsPropertyAccess() {
        CheckGlobalThis checker = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        DiagnosticType expected = DiagnosticType.warning(
            "JSC_USED_GLOBAL_THIS",
            "dangerous use of the global 'this' object");
        compiler.setErrorLevel(DiagnosticType.warning, CheckLevel.WARNING);
        checker.visit(t, n, parent);
        assertTrue(t.hasErrors());
        DiagnosticType actual = t.getError(n);
        assertEquals(expected, actual);
    }

    @Test
    public void testVisit_shouldNotReportErrorIfThisIsNotOnLeftSideOfAssignmentOrPropertyAccess() {
        CheckGlobalThis checker = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        DiagnosticType expected = null;
        compiler.setErrorLevel(DiagnosticType.warning, CheckLevel.WARNING);
        checker.visit(t, n, parent);
        assertFalse(t.hasErrors());
        DiagnosticType actual = t.getError(n);
        assertEquals(expected, actual);
    }
}
