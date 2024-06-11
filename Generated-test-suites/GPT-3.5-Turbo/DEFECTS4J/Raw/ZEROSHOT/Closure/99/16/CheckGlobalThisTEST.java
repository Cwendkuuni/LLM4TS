import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.DiagnosticType;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;
    private CheckLevel level;
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void shouldTraverse_itIsNotFunction_returnsTrue() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.NOT);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
        verifyZeroInteractions(t);
        verifyZeroInteractions(parent);
    }

    @Test
    public void shouldTraverse_functionIsConstructorOrHasThisType_returnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
        verifyZeroInteractions(t);
        verifyZeroInteractions(parent);
    }

    @Test
    public void shouldTraverse_functionIsValid_returnsTrue() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(false);
        when(jsDoc.hasThisType()).thenReturn(false);
        when(jsDoc.isOverride()).thenReturn(false);

        when(parent.getType()).thenReturn(Token.NAME);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
        verifyZeroInteractions(t);
        verifyZeroInteractions(parent);
    }

    @Test
    public void visit_thisIsEncounteredAndShouldReportThis_returnsError() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.THIS);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);

        checkGlobalThis.visit(t, n, parent);

        verify(t).makeError(n, level, CheckGlobalThis.GLOBAL_THIS);
        verifyNoMoreInteractions(t);
        verifyZeroInteractions(parent);
    }

    @Test
    public void visit_thisIsNotEncountered_doesNothing() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.NAME);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);

        checkGlobalThis.visit(t, n, parent);

        verifyZeroInteractions(t);
        verifyZeroInteractions(parent);
    }

    @Test
    public void visit_assignLhsChildIsNotNull_doesNothing() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        checkGlobalThis.assignLhsChild = n;

        checkGlobalThis.visit(t, n, parent);

        verifyZeroInteractions(t);
        verifyZeroInteractions(parent);
    }

    @Test
    public void visit_assignLhsChildIsNull_doesNothing() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        checkGlobalThis.assignLhsChild = null;

        checkGlobalThis.visit(t, n, parent);

        verifyZeroInteractions(t);
        verifyZeroInteractions(parent);
    }

    @Test
    public void visit_assignLhsChildIsNotNullAndnIsAssignLhsChild_resetsAssignLhsChild() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        checkGlobalThis.assignLhsChild = n;

        checkGlobalThis.visit(t, n, parent);

        assertNull(checkGlobalThis.assignLhsChild);
        verifyZeroInteractions(t);
        verifyZeroInteractions(parent);
    }

    @Test
    public void getFunctionJsDocInfo_jsDocInfoIsNull_returnsParentJSDocInfo() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.getJSDocInfo()).thenReturn(null);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getJSDocInfo()).thenReturn(jsDoc);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDoc, result);
        verifyZeroInteractions(n);
        verifyZeroInteractions(parent);
    }

    @Test
    public void getFunctionJsDocInfo_jsDocInfoIsNotNull_returnsJsDocInfo() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);

        when(n.getJSDocInfo()).thenReturn(jsDoc);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDoc, result);
        verifyZeroInteractions(n);
        verifyZeroInteractions(parent);
    }

}