import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {

    private AbstractCompiler compilerMock;
    private CheckLevel levelMock;
    private CheckGlobalThis checkGlobalThis;
    private NodeTraversal.Callback callbackMock;

    @Before
    public void setUp() {
        compilerMock = mock(AbstractCompiler.class);
        levelMock = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compilerMock, levelMock);
        callbackMock = mock(NodeTraversal.Callback.class);
    }

    @Test
    public void shouldTraverse_ReturnsTrueForNonGlobalContext_ShouldReturnTrue() {
        NodeTraversal tMock = mock(NodeTraversal.class);
        Node nMock = mock(Node.class);
        Node parentMock = mock(Node.class);
        when(nMock.getType()).thenReturn(Token.NUMBER);
        boolean result = checkGlobalThis.shouldTraverse(tMock, nMock, parentMock);
        assertEquals(true, result);
    }

    @Test
    public void shouldTraverse_ReturnsFalseForFunctionWithConstructorAnnotation_ShouldReturnFalse() {
        NodeTraversal tMock = mock(NodeTraversal.class);
        Node nMock = mock(Node.class);
        Node parentMock = mock(Node.class);
        when(nMock.getType()).thenReturn(Token.FUNCTION);
        when(parentMock.getType()).thenReturn(Token.NAME);
        JSDocInfo jsDocMock = mock(JSDocInfo.class);
        when(checkGlobalThis.getFunctionJsDocInfo(nMock)).thenReturn(jsDocMock);
        when(jsDocMock.isConstructor()).thenReturn(true);
        boolean result = checkGlobalThis.shouldTraverse(tMock, nMock, parentMock);
        assertEquals(false, result);
    }

    @Test
    public void shouldTraverse_ReturnsTrueForFunctionWithoutConstructorAnnotation_ShouldReturnTrue() {
        NodeTraversal tMock = mock(NodeTraversal.class);
        Node nMock = mock(Node.class);
        Node parentMock = mock(Node.class);
        when(nMock.getType()).thenReturn(Token.FUNCTION);
        when(parentMock.getType()).thenReturn(Token.ASSIGN);
        JSDocInfo jsDocMock = mock(JSDocInfo.class);
        when(checkGlobalThis.getFunctionJsDocInfo(nMock)).thenReturn(jsDocMock);
        when(jsDocMock.isConstructor()).thenReturn(false);
        when(jsDocMock.isInterface()).thenReturn(false);
        when(jsDocMock.hasThisType()).thenReturn(false);
        when(jsDocMock.isOverride()).thenReturn(false);
        boolean result = checkGlobalThis.shouldTraverse(tMock, nMock, parentMock);
        assertEquals(true, result);
    }

    @Test
    public void visit_ReportsErrorForGlobalThisUsage_ShouldReportError() {
        NodeTraversal tMock = mock(NodeTraversal.class);
        Node nMock = mock(Node.class);
        Node parentMock = mock(Node.class);
        checkGlobalThis.visit(tMock, nMock, parentMock);
        verify(compilerMock, times(1)).report(any());
    }

    @Test
    public void visit_DoesNotReportErrorForNonGlobalThisUsage_ShouldNotReportError() {
        NodeTraversal tMock = mock(NodeTraversal.class);
        Node nMock = mock(Node.class);
        Node parentMock = mock(Node.class);
        when(nMock.getType()).thenReturn(Token.NAME);
        checkGlobalThis.visit(tMock, nMock, parentMock);
        verify(compilerMock, never()).report(any());
    }

    @Test
    public void visit_ClearsAssignLhsChild_WhenNodeIsAssignLhsChild_ShouldClearAssignLhsChild() {
        NodeTraversal tMock = mock(NodeTraversal.class);
        Node nMock = mock(Node.class);
        Node parentMock = mock(Node.class);
        checkGlobalThis.assignLhsChild = nMock;
        checkGlobalThis.visit(tMock, nMock, parentMock);
        assertEquals(null, checkGlobalThis.assignLhsChild);
    }

    @Test
    public void visit_DoesNotClearAssignLhsChild_WhenNodeIsNotAssignLhsChild_ShouldNotClearAssignLhsChild() {
        NodeTraversal tMock = mock(NodeTraversal.class);
        Node nMock = mock(Node.class);
        Node parentMock = mock(Node.class);
        Node assignLhsChildMock = mock(Node.class);
        checkGlobalThis.assignLhsChild = assignLhsChildMock;
        checkGlobalThis.visit(tMock, nMock, parentMock);
        assertEquals(assignLhsChildMock, checkGlobalThis.assignLhsChild);
    }

    @Test
    public void shouldReportThis_ReturnsTrueForAssignLhsChild_ShouldReturnTrue() {
        Node nMock = mock(Node.class);
        Node parentMock = mock(Node.class);
        checkGlobalThis.assignLhsChild = nMock;
        boolean result = checkGlobalThis.shouldReportThis(nMock, parentMock);
        assertEquals(true, result);
    }

    @Test
    public void shouldReportThis_ReturnsTrueForParentIsGet_ShouldReturnTrue() {
        Node nMock = mock(Node.class);
        Node parentMock = mock(Node.class);
        when(NodeUtil.isGet(parentMock)).thenReturn(true);
        boolean result = checkGlobalThis.shouldReportThis(nMock, parentMock);
        assertEquals(true, result);
    }

    @Test
    public void shouldReportThis_ReturnsFalseForParentIsNotGet_ShouldReturnFalse() {
        Node nMock = mock(Node.class);
        Node parentMock = mock(Node.class);
        when(NodeUtil.isGet(parentMock)).thenReturn(false);
        boolean result = checkGlobalThis.shouldReportThis(nMock, parentMock);
        assertEquals(false, result);
    }

    @Test
    public void getFunctionJsDocInfo_ReturnsJSDocInfoFromNode_ShouldReturnJSDocInfo() {
        Node nMock = mock(Node.class);
        JSDocInfo jsDocMock = mock(JSDocInfo.class);
        when(nMock.getJSDocInfo()).thenReturn(null);
        Node parentMock = mock(Node.class);
        when(parentMock.getType()).thenReturn(Token.NAME);
        when(parentMock.getJSDocInfo()).thenReturn(jsDocMock);
        Node grandparentMock = mock(Node.class);
        when(parentMock.getParent()).thenReturn(grandparentMock);
        when(grandparentMock.getType()).thenReturn(Token.VAR);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(nMock);
        assertEquals(jsDocMock, result);
    }

    @Test
    public void getFunctionJsDocInfo_ReturnsNullForNoJSDocInfo_ShouldReturnNull() {
        Node nMock = mock(Node.class);
        when(nMock.getJSDocInfo()).thenReturn(null);
        Node parentMock = mock(Node.class);
        when(parentMock.getType()).thenReturn(Token.NAME);
        when(parentMock.getJSDocInfo()).thenReturn(null);
        Node grandparentMock = mock(Node.class);
        when(parentMock.getParent()).thenReturn(grandparentMock);
        when(grandparentMock.getType()).thenReturn(Token.VAR);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(nMock);
        assertEquals(null, result);
    }
}