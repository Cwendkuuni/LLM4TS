import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.WarningLevel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(null, WarningLevel.WARNING);
    }

    @Test
    public void shouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo().setHasThisType(true));
        assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
    }

    @Test
    public void shouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo().setConstructor(true));
        assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
    }

    @Test
    public void shouldTraverse_FunctionInsideBlock_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.BLOCK);
        Node n = new Node(Token.FUNCTION);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void shouldTraverse_FunctionInsideScript_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void shouldTraverse_FunctionAssignedToVariable_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.NAME);
        Node n = new Node(Token.FUNCTION);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void shouldTraverse_FunctionAssignedToProperty_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.GETPROP);
        Node n = new Node(Token.FUNCTION);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void shouldTraverse_FunctionInsideObject_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.ASSIGN);
        Node n = new Node(Token.FUNCTION);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void shouldTraverse_FunctionAssignedToPrototype_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToFront(new Node(Token.NAME, "prototype"));
        parent.addChildToBack(lhs);
        Node n = new Node(Token.FUNCTION);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void shouldTraverse_NestedAssignmentWithThis_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToFront(new Node(Token.NAME, "this"));
        parent.addChildToBack(lhs);
        Node n = new Node(Token.NAME, "property");
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void visit_ThisOnLeftSideOfAssignment_ReportGlobalThisWarning() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(n);
        DiagnosticType expected = DiagnosticType.warning(
                "JSC_USED_GLOBAL_THIS",
                "dangerous use of the global 'this' object");
        assertEquals(expected, checkGlobalThis.visit(t, n, parent));
    }

    @Test
    public void visit_ThisWithPropertyAccess_ReportGlobalThisWarning() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(n);
        DiagnosticType expected = DiagnosticType.warning(
                "JSC_USED_GLOBAL_THIS",
                "dangerous use of the global 'this' object");
        assertEquals(expected, checkGlobalThis.visit(t, n, parent));
    }

    @Test
    public void visit_NotThisNode_ReturnsNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME, "variable");
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(n);
        assertNull(checkGlobalThis.visit(t, n, parent));
    }

    @Test
    public void visit_ThisNodeOnLeftSideOfAssignment_SetAssignLhsChildToNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(n);
        checkGlobalThis.visit(t, n, parent);
        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void shouldReportThis_ThisOnLeftSideOfAssignment_ReturnsTrue() {
        Node n = new Node(Token.NAME, "this");
        Node parent = new Node(Token.ASSIGN);
        assertTrue(checkGlobalThis.shouldReportThis(n, parent));
    }

    @Test
    public void shouldReportThis_ThisWithPropertyAccess_ReturnsTrue() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        assertTrue(checkGlobalThis.shouldReportThis(n, parent));
    }

    @Test
    public void shouldReportThis_NotThisNode_ReturnsFalse() {
        Node n = new Node(Token.NAME, "variable");
        Node parent = new Node(Token.GETPROP);
        assertFalse(checkGlobalThis.shouldReportThis(n, parent));
    }

    @Test
    public void visit_ThisNode_ResetAssignLhsChildToNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.visit(t, n, parent);
        assertNull(checkGlobalThis.assignLhsChild);
    }


}