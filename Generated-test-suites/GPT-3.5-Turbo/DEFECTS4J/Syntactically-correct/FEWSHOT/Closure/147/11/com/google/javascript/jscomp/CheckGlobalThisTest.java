import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler compiler;
    private CheckLevel level;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void shouldTraverse_ValidFunction_ReturnTrue() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(null);
        when(parent.getType()).thenReturn(Token.BLOCK);
        when(parent.getJSDocInfo()).thenReturn(null);
        when(jsDoc != null &&
                (jsDoc.isConstructor() ||
                jsDoc.isInterface() ||
                jsDoc.hasThisType() ||
                jsDoc.isOverride())).thenReturn(false);
        when(parent.getType()).thenReturn(Token.BLOCK);
        when(parent.getType()).thenReturn(Token.SCRIPT);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getType()).thenReturn(Token.ASSIGN);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void shouldTraverse_ConstructorFunction_ReturnFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_ThisTypeFunction_ReturnFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.hasThisType()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_OverrideFunction_ReturnFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isOverride()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_NonGlobalContextFunction_ReturnFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jjoc);
        when(jsDoc != null &&
                (jsDoc.isConstructor() ||
                jsDoc.isInterface() ||
                jsDoc.hasThisType() ||
                jsDoc.isOverride())).thenReturn(false);
        when(parent.getType()).thenReturn(Token.IF);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_RightSideAssignmentToPrototype_ReturnFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node rhs = mock(Node.class);
        Node lhs = mock(Node.class);

        when(n.getType()).thenReturn(Token.THIS);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(lhs.getNext()).thenReturn(rhs);
        when(NodeUtil.isGet(lhs)).thenReturn(true);
        when(lhs.getType()).thenReturn(Token.GETPROP);
        when(lhs.getLastChild().getString()).thenReturn("prototype");

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void visit_ThisOnAssignmentLeftSide_ReportError() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.THIS);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);

        checkGlobalThis.visit(t, n, parent);

        verify(compiler).report(t.makeError(n, level, GLOBAL_THIS));
    }

    @Test
    public void visit_AssignLhsChildSet_CleanAssignLhsChild() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        checkGlobalThis.assignLhsChild = n;

        checkGlobalThis.visit(t, n, parent);

        assertNull(checkGlobalThis.assignLhsChild);
    }

}