import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;


public class CheckGlobalThisTest {

    @InjectMocks
    private CheckGlobalThis checkGlobalThis;

    @Mock
    private AbstractCompiler compiler;

    @Test
    public void testShouldTraverse_FunctionIsConstructor_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.isFunction()).thenReturn(true);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasThisTypeAnnotation_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.isFunction()).thenReturn(true);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.hasThisType()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasOverrideAnnotation_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.isFunction()).thenReturn(true);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.isOverride()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ParentIsObjectLitKey_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.isFunction()).thenReturn(true);
        when(n.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.STRING_KEY);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.getLendsName()).thenReturn("SomeName.prototype");

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ParentIsAssign_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node grandParent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.isFunction()).thenReturn(true);
        when(n.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getFirstChild()).thenReturn(n);
        when(parent.getParent()).thenReturn(grandParent);
        when(grandParent.getType()).thenReturn(Token.BLOCK);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ParentIsNotTraversable_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.isFunction()).thenReturn(true);
        when(n.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.NUMBER);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ValidFunction_ReturnsTrue() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.isFunction()).thenReturn(true);
        when(n.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.BLOCK);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testVisit_ThisIsGlobal_ReturnsDiagnosticType() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.isThis()).thenReturn(true);
        when(checkGlobalThis.shouldReportThis(n)).thenReturn(true);
        when(t.makeError(n, CheckGlobalThis.GLOBAL_THIS)).thenReturn(mock(JSError.class));

        checkGlobalThis.visit(t, n, parent);

        verify(compiler).report(any(JSError.class));
    }

    @Test
    public void testVisit_ThisIsNotGlobal_DoesNotReturnDiagnosticType() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.isThis()).thenReturn(true);
        when(checkGlobalThis.shouldReportThis(n)).thenReturn(false);

        checkGlobalThis.visit(t, n, parent);

        verify(compiler, never()).report(any(JSError.class));
    }

    @Test
    public void testVisit_NodeIsNotAssign_DoesNotResetAssignLhsChild() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.isThis()).thenReturn(false);
        when(checkGlobalThis.shouldReportThis(n)).thenReturn(false);

        checkGlobalThis.visit(t, n, parent);

        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testVisit_NodeIsAssign_ResetsAssignLhsChild() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.isThis()).thenReturn(false);
        when(checkGlobalThis.shouldReportThis(n)).thenReturn(false);
        FieldSetter.setField(checkGlobalThis, CheckGlobalThis.class.getDeclaredField("assignLhsChild"), n);

        checkGlobalThis.visit(t, n, parent);

        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testShouldReportThis_AssignLhsChildIsNull_ReturnsFalse() {
        Node n = mock(Node.class);
        FieldSetter.setField(checkGlobalThis, CheckGlobalThis.class.getDeclaredField("assignLhsChild"), null);

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertFalse(result);
    }

    @Test
    public void testShouldReportThis_AssignLhsChildIsNotNull_ReturnsTrue() {
        Node n = mock(Node.class);
        FieldSetter.setField(checkGlobalThis, CheckGlobalThis.class.getDeclaredField("assignLhsChild"), n);

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertTrue(result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NHasJSDocInfo_ReturnsJSDocInfo() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        FieldSetter.setField(checkGlobalThis, CheckGlobalThis.class.getDeclaredField("assignLhsChild"), parent);

        when(n.getJSDocInfo()).thenReturn(null);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getJSDocInfo()).thenReturn(jsDoc);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NHasNoJSDocInfoButParentHasJSDocInfo_ReturnsParentJSDocInfo() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo parentJsDoc = mock(JSDocInfo.class);
        FieldSetter.setField(checkGlobalThis, CheckGlobalThis.class.getDeclaredField("assignLhsChild"), parent);

        when(n.getJSDocInfo()).thenReturn(null);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getJSDocInfo()).thenReturn(null);
        when(parent.getParent()).thenReturn(mock(Node.class));
        when(parent.getParent().isVar()).thenReturn(true);
        when(parent.getParent().getJSDocInfo()).thenReturn(parentJsDoc);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(parentJsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NHasNoJSDocInfoButParentHasNoJSDocInfo_ReturnsNull() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        FieldSetter.setField(checkGlobalThis, CheckGlobalThis.class.getDeclaredField("assignLhsChild"), parent);

        when(n.getJSDocInfo()).thenReturn(null);
        when(parent.getType()).thenReturn(Token.NUMBER);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertNull(result);
    }
}