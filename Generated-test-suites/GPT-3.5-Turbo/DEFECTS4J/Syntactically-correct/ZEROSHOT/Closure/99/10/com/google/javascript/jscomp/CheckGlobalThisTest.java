import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CheckGlobalThisTest {
    private AbstractCompiler compiler;
    private CheckLevel level;
    private CheckGlobalThis checkGlobalThis;
    private Node node;
    private NodeTraversal.Callback callback;
    private NodeTraversal nodeTraversal;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
        node = mock(Node.class);
        callback = mock(NodeTraversal.Callback.class);
        nodeTraversal = mock(NodeTraversal.class);
    }

    @Test
    public void shouldTraverse_returnFalseForFunctionWithConstructorAnnotation() {
        // Arrange
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(checkGlobalThis.getFunctionJsDocInfo(node)).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(true);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);

        // Assert
        assertEquals(false, result);
        verify(jsDoc).isConstructor();
    }

    @Test
    public void shouldTraverse_returnFalseForFunctionWithThisTypeAnnotation() {
        // Arrange
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(checkGlobalThis.getFunctionJsDocInfo(node)).thenReturn(jsDoc);
        when(jsDoc.hasThisType()).thenReturn(true);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);

        // Assert
        assertEquals(false, result);
        verify(jsDoc).hasThisType();
    }

    @Test
    public void shouldTraverse_returnFalseForFunctionWithOverrideAnnotation() {
        // Arrange
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(checkGlobalThis.getFunctionJsDocInfo(node)).thenReturn(jsDoc);
        when(jsDoc.isOverride()).thenReturn(true);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, null);

        // Assert
        assertEquals(false, result);
        verify(jsDoc).isOverride();
    }

    @Test
    public void shouldTraverse_returnFalseForFunctionNotInBlockScriptNameOrAssign() {
        // Arrange
        Node parent = mock(Node.class);
        when(checkGlobalThis.getFunctionJsDocInfo(node)).thenReturn(null);
        when(parent.getType()).thenReturn(Token.DUMMY);
        when(node.getParent()).thenReturn(parent);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        // Assert
        assertEquals(false, result);
        verify(parent).getType();
        verify(node).getParent();
    }

    @Test
    public void shouldTraverse_returnTrueForFunctionInBlock() {
        // Arrange
        Node parent = mock(Node.class);
        when(checkGlobalThis.getFunctionJsDocInfo(node)).thenReturn(null);
        when(parent.getType()).thenReturn(Token.BLOCK);
        when(node.getParent()).thenReturn(parent);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        // Assert
        assertEquals(true, result);
        verify(parent).getType();
        verify(node).getParent();
    }

    @Test
    public void shouldTraverse_returnTrueForFunctionInScript() {
        // Arrange
        Node parent = mock(Node.class);
        when(checkGlobalThis.getFunctionJsDocInfo(node)).thenReturn(null);
        when(parent.getType()).thenReturn(Token.SCRIPT);
        when(node.getParent()).thenReturn(parent);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        // Assert
        assertEquals(true, result);
        verify(parent).getType();
        verify(node).getParent();
    }

    @Test
    public void shouldTraverse_returnTrueForFunctionInName() {
        // Arrange
        Node parent = mock(Node.class);
        when(checkGlobalThis.getFunctionJsDocInfo(node)).thenReturn(null);
        when(parent.getType()).thenReturn(Token.NAME);
        when(node.getParent()).thenReturn(parent);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        // Assert
        assertEquals(true, result);
        verify(parent).getType();
        verify(node).getParent();
    }

    @Test
    public void shouldTraverse_returnTrueForFunctionInAssign() {
        // Arrange
        Node parent = mock(Node.class);
        when(checkGlobalThis.getFunctionJsDocInfo(node)).thenReturn(null);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(node.getParent()).thenReturn(parent);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);

        // Assert
        assertEquals(true, result);
        verify(parent).getType();
        verify(node).getParent();
    }

    @Test
    public void shouldTraverse_returnFalseForAssignLhsChildNotNull() {
        // Arrange
        NodeTraversal t = mock(NodeTraversal.class);
        Node assignNode = mock(Node.class);
        Node lhsChild = mock(Node.class);
        Node rhsChild = mock(Node.class);
        Node parentNode = mock(Node.class);
        when(parentNode.getType()).thenReturn(Token.ASSIGN);
        when(t.makeError(node, level, CheckGlobalThis.GLOBAL_THIS)).thenReturn(mock(AbstractCompiler.class));
        when(assignNode.getFirstChild()).thenReturn(lhsChild);
        when(assignNode.getLastChild()).thenReturn(rhsChild);
        when(node.getType()).thenReturn(Token.THIS);
        when(node.getParent()).thenReturn(parentNode);
        checkGlobalThis.shouldTraverse(t, node, parentNode);
        reset(lhsChild, rhsChild, parentNode);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, lhsChild, assignNode);

        // Assert
        assertEquals(false, result);
        verify(lhsChild, never()).getType();
        verify(rhsChild, never()).getType();
        verify(parentNode, never()).getType();
        verify(node, never()).getType();
        verify(node, never()).getParent();
        verify(assignNode, never()).getFirstChild();
        verify(assignNode, never()).getLastChild();
        verify(t, never()).makeError(node, level, CheckGlobalThis.GLOBAL_THIS);
    }

    @Test
    public void visit_reportErrorWhenThisIsUnsafe() {
        // Arrange
        NodeTraversal t = mock(NodeTraversal.class);
        Node parent = mock(Node.class);
        Node lhsChild = mock(Node.class);
        Node rhsChild = mock(Node.class);
        Node parentNode = mock(Node.class);
        when(parentNode.getType()).thenReturn(Token.ASSIGN);
        when(assignNode.getFirstChild()).thenReturn(lhsChild);
        when(assignNode.getLastChild()).thenReturn(rhsChild);
        when(node.getType()).thenReturn(Token.THIS);
        when(node.getParent()).thenReturn(parentNode);
        checkGlobalThis.shouldTraverse(t, node, parentNode);
        reset(lhsChild, rhsChild, parentNode);
        when(checkGlobalThis.shouldReportThis(node, parent)).thenReturn(true);
        when(t.makeError(node, level, CheckGlobalThis.GLOBAL_THIS)).thenReturn(compiler);

        // Act
        checkGlobalThis.visit(t, node, parent);

        // Assert
        verify(t).makeError(node, level, CheckGlobalThis.GLOBAL_THIS);
        verify(compiler).report(compiler);
    }

    @Test
    public void visit_notReportErrorWhenThisIsNotUnsafe() {
        // Arrange
        NodeTraversal t = mock(NodeTraversal.class);
        Node parent = mock(Node.class);
        Node lhsChild = mock(Node.class);
        Node rhsChild = mock(Node.class);
        Node parentNode = mock(Node.class);
        when(parentNode.getType()).thenReturn(Token.ASSIGN);
        when(assignNode.getFirstChild()).thenReturn(lhsChild);
        when(assignNode.getLastChild()).thenReturn(rhsChild);
        when(node.getType()).thenReturn(Token.THIS);
        when(node.getParent()).thenReturn(parentNode);
        checkGlobalThis.shouldTraverse(t, node, parentNode);
        reset(lhsChild, rhsChild, parentNode);
        when(checkGlobalThis.shouldReportThis(node, parent)).thenReturn(false);

        // Act
        checkGlobalThis.visit(t, node, parent);

        // Assert
        verify(t, never()).makeError(node, level, CheckGlobalThis.GLOBAL_THIS);
        verify(compiler, never()).report(compiler);
    }

    @Test
    public void visit_resetAssignLhsChildWhenVisitingAssignLhsChild() {
        // Arrange
        NodeTraversal t = mock(NodeTraversal.class);
        Node parent = mock(Node.class);
        Node lhsChild = mock(Node.class);
        Node rhsChild = mock(Node.class);
        Node parentNode = mock(Node.class);
        when(parentNode.getType()).thenReturn(Token.ASSIGN);
        when(assignNode.getFirstChild()).thenReturn(lhsChild);
        when(assignNode.getLastChild()).thenReturn(rhsChild);
        when(node.getType()).thenReturn(Token.GETPROP);
        when(node.getLastChild()).thenReturn(mock(Node.class));
        when(node.getLastChild().getString()).thenReturn("prototype");
        when(node.getParent()).thenReturn(parentNode);
        checkGlobalThis.shouldTraverse(t, lhsChild, assignNode);
        reset(lhsChild, rhsChild, parentNode);
        when(lhsChild.getType()).thenReturn(Token.THIS);
        when(checkGlobalThis.shouldReportThis(lhsChild, assignNode)).thenReturn(true);
        when(t.makeError(lhsChild, level, CheckGlobalThis.GLOBAL_THIS)).thenReturn(compiler);

        // Act
        checkGlobalThis.visit(t, lhsChild, assignNode);

        // Assert
        verify(lhsChild, never()).getType();
        verify(rhsChild, never()).getType();
        verify(parentNode, never()).getType();
        verify(node, never()).getType();
        verify(node, never()).getLastChild();
        verify(node, never()).getParent();
        verify(assignNode, never()).getFirstChild();
        verify(assignNode, never()).getLastChild();
        verify(t).makeError(lhsChild, level, CheckGlobalThis.GLOBAL_THIS);
        verify(compiler).report(compiler);
        verify(lhsChild, never()).getNext();
        assertEquals(null, assignLhsChild);
    }
}