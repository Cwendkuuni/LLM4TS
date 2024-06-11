import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    }

    @Test
    public void shouldTraverse_shouldReturnFalse_whenFunctionIsConstructor() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(parent.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(mock(JSDocInfo.class));
        when(n.getJSDocInfo().isConstructor()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertEquals(false, result);
        verifyZeroInteractions(compiler);
    }

    @Test
    public void shouldTraverse_shouldReturnFalse_whenFunctionHasThisTypeAnnotation() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(parent.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(mock(JSDocInfo.class));
        when(n.getJSDocInfo().isConstructor()).thenReturn(false);
        when(n.getJSDocInfo().hasThisType()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertEquals(false, result);
        verifyZeroInteractions(compiler);
    }

    @Test
    public void shouldTraverse_shouldReturnFalse_whenParentIsAssignAndChildIsLeftSide() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node grandparent = mock(Node.class);
        Node assignNode = mock(Node.class);
        Node lhs = mock(Node.class);

        when(n.getType()).thenReturn(Token.NAME);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(grandparent.getType()).thenReturn(Token.GETPROP);
        when(assignNode.getType()).thenReturn(Token.ASSIGN);
        when(lhs.getType()).thenReturn(Token.NAME);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(parent.getLastChild()).thenReturn(assignNode);
        when(grandparent.getFirstChild()).thenReturn(parent);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertEquals(true, result);
        verifyZeroInteractions(compiler);
    }

    @Test
    public void shouldTraverse_shouldReturnFalse_whenParentIsAssignAndChildIsRightSide_andLeftSideIsPrototype() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node grandparent = mock(Node.class);
        Node assignNode = mock(Node.class);
        Node lhs = mock(Node.class);
        Node rhs = mock(Node.class);
        Node prototype = mock(Node.class);
        Node lastChild = mock(Node.class);

        when(n.getType()).thenReturn(Token.SUB);
        when(n.getString()).thenReturn("prototype");
        when(parent.getType()).thenReturn(Token.GETPROP);
        when(grandparent.getType()).thenReturn(Token.ASSIGN);
        when(assignNode.getType()).thenReturn(Token.ASSIGN);
        when(lhs.getType()).thenReturn(Token.NAME);
        when(rhs.getType()).thenReturn(Token.VALUE);
        when(prototype.getType()).thenReturn(Token.NAME);
        when(lastChild.getString()).thenReturn("prototype");
        when(parent.getFirstChild()).thenReturn(lhs);
        when(parent.getLastChild()).thenReturn(prototype);
        when(grandparent.getFirstChild()).thenReturn(parent);
        when(grandparent.getLastChild()).thenReturn(rhs);
        when(lhs.getNext()).thenReturn(rhs);
        when(rhs.getNext()).thenReturn(lastChild);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertEquals(false, result);
        verifyZeroInteractions(compiler);
    }

    @Test
    public void shouldTraverse_shouldReturnFalse_whenParentIsAssignAndChildIsRightSide_andLeftSideContainsPrototype() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node grandparent = mock(Node.class);
        Node assignNode = mock(Node.class);
        Node lhs = mock(Node.class);
        Node rhs = mock(Node.class);
        Node prototype = mock(Node.class);
        Node lastChild = mock(Node.class);

        when(n.getType()).thenReturn(Token.ADD);
        when(n.getQualifiedName()).thenReturn("x.prototype.y");
        when(parent.getType()).thenReturn(Token.GETPROP);
        when(grandparent.getType()).thenReturn(Token.ASSIGN);
        when(assignNode.getType()).thenReturn(Token.ASSIGN);
        when(lhs.getType()).thenReturn(Token.NAME);
        when(rhs.getType()).thenReturn(Token.VALUE);
        when(prototype.getType()).thenReturn(Token.NAME);
        when(lastChild.getString()).thenReturn("y");
        when(parent.getFirstChild()).thenReturn(lhs);
        when(parent.getLastChild()).thenReturn(prototype);
        when(grandparent.getFirstChild()).thenReturn(parent);
        when(grandparent.getLastChild()).thenReturn(rhs);
        when(lhs.getNext()).thenReturn(rhs);
        when(rhs.getNext()).thenReturn(lastChild);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertEquals(false, result);
        verifyZeroInteractions(compiler);
    }

    @Test
    public void shouldTraverse_shouldReturnTrue_whenParentIsAssignAndChildIsRightSide_andLeftSideIsNotPrototype() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node grandparent = mock(Node.class);
        Node assignNode = mock(Node.class);
        Node lhs = mock(Node.class);
        Node rhs = mock(Node.class);
        Node prototype = mock(Node.class);
        Node lastChild = mock(Node.class);

        when(n.getType()).thenReturn(Token.SUB);
        when(n.getString()).thenReturn("this");
        when(parent.getType()).thenReturn(Token.GETPROP);
        when(grandparent.getType()).thenReturn(Token.ASSIGN);
        when(assignNode.getType()).thenReturn(Token.ASSIGN);
        when(lhs.getType()).thenReturn(Token.NAME);
        when(rhs.getType()).thenReturn(Token.VALUE);
        when(prototype.getType()).thenReturn(Token.NAME);
        when(lastChild.getString()).thenReturn("prototype");
        when(parent.getFirstChild()).thenReturn(lhs);
        when(parent.getLastChild()).thenReturn(prototype);
        when(grandparent.getFirstChild()).thenReturn(parent);
        when(grandparent.getLastChild()).thenReturn(rhs);
        when(lhs.getNext()).thenReturn(rhs);
        when(rhs.getNext()).thenReturn(lastChild);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertEquals(true, result);
        verifyZeroInteractions(compiler);
    }

    @Test
    public void visit_shouldReportError_whenNodeIsTHIS_andParentShouldReportThis() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.THIS);
        when(parent.getType()).thenReturn(Token.FUNCTION);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        verify(compiler).report(nodeTraversal.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
    }

    @Test
    public void visit_shouldNotReportError_whenNodeIsTHIS_andParentShouldNotReportThis() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.THIS);
        when(parent.getType()).thenReturn(Token.FUNCTION);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        verifyZeroInteractions(compiler);
    }

    @Test
    public void visit_shouldNotReportError_whenNodeIsNotTHIS() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.NAME);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        verifyZeroInteractions(compiler);
    }

    @Test
    public void visit_shouldResetAssignLhsChild_whenNodeIsAssignLhsChild() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.NAME);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        assertEquals(null, checkGlobalThis.getAssignLhsChild());
    }

    @Test
    public void visit_shouldNotResetAssignLhsChild_whenNodeIsNotAssignLhsChild() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.NAME);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        assertEquals(null, checkGlobalThis.getAssignLhsChild());
    }

    @Test
    public void getFunctionJsDocInfo_shouldReturnJSDocInfo_whenFunctionHasJSDocInfo() {
        Node n = mock(Node.class);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(n.getJSDocInfo(), result);
    }

    @Test
    public void getFunctionJsDocInfo_shouldReturnNull_whenFunctionDoesNotHaveJSDocInfo() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getJSDocInfo()).thenReturn(null);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getJSDocInfo()).thenReturn(null);
        when(parent.getParent().getType()).thenReturn(Token.VAR);
        when(parent.getParent().getJSDocInfo()).thenReturn(null);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(null, result);
    }
}