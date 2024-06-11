import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CheckGlobalThisTest {

    @Mock
    AbstractCompiler compiler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(true);
        when(jsDoc.hasThisType()).thenReturn(false);
        when(parent.getType()).thenReturn(Token.FUNCTION);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        verify(n).getType();
        verify(n).getJSDocInfo();
        verify(parent).getType();
        verify(jsDoc).isConstructor();
        verify(jsDoc, never()).hasThisType();

        assertEquals("The result should be false", false, result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisTypeAnnotation() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(false);
        when(jsDoc.hasThisType()).thenReturn(true);
        when(parent.getType()).thenReturn(Token.FUNCTION);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        verify(n).getType();
        verify(n).getJSDocInfo();
        verify(parent).getType();
        verify(jsDoc).isConstructor();
        verify(jsDoc).hasThisType();

        assertEquals("The result should be false", false, result);
    }

    @Test
    public void testShouldTraverse_FunctionNotConstructorNotThisTypeAnnotation() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(false);
        when(jsDoc.hasThisType()).thenReturn(false);
        when(parent.getType()).thenReturn(Token.FUNCTION);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        verify(n).getType();
        verify(n).getJSDocInfo();
        verify(parent).getType();
        verify(jsDoc).isConstructor();
        verify(jsDoc).hasThisType();

        assertEquals("The result should be true", true, result);
    }

    @Test
    public void testShouldTraverse_AssignLhsChildIsNull() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        JSDocInfo jsDoc = mock(JSDocInfo.class);
        Node lhs = mock(Node.class);
        Node rhs = mock(Node.class);

        when(n.getType()).thenReturn(Token.ADD);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(false);
        when(jsDoc.hasThisType()).thenReturn(false);
        when(parent.getType()).thenReturn(Token.FUNCTION);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(lhs.getNext()).thenReturn(rhs);
        when(lhs.getType()).thenReturn(Token.NAME);
        when(rhs.getType()).thenReturn(Token.GETPROP);
        when(rhs.getLastChild()).thenReturn(mock(Node.class));
        when(rhs.getQualifiedName()).thenReturn(null);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        verify(n).getType();
        verify(n).getJSDocInfo();
        verify(parent).getType();
        verify(parent).getFirstChild();
        verify(lhs).getNext();
        verify(lhs).getType();
        verify(rhs).getType();
        verify(rhs).getLastChild();
        verify(rhs).getQualifiedName();
        verify(jsDoc).isConstructor();
        verify(jsDoc).hasThisType();

        assertEquals("The result should be true", true, result);
    }

    @Test
    public void testShouldTraverse_AssignLhsChildNotNull() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        JSDocInfo jsDoc = mock(JSDocInfo.class);
        Node lhs = mock(Node.class);
        Node rhs = mock(Node.class);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        checkGlobalThis.assignLhsChild = lhs;

        when(n.getType()).thenReturn(Token.ADD);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(false);
        when(jsDoc.hasThisType()).thenReturn(false);
        when(parent.getType()).thenReturn(Token.FUNCTION);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(lhs.getNext()).thenReturn(rhs);
        when(lhs.getType()).thenReturn(Token.NAME);
        when(rhs.getType()).thenReturn(Token.GETPROP);
        when(rhs.getLastChild()).thenReturn(mock(Node.class));
        when(rhs.getQualifiedName()).thenReturn(null);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        verify(n).getType();
        verify(n).getJSDocInfo();
        verify(parent).getType();
        verify(parent).getFirstChild();
        verify(lhs).getNext();
        verify(lhs).getType();
        verify(rhs).getType();
        verify(rhs).getLastChild();
        verify(rhs).getQualifiedName();
        verify(jsDoc).isConstructor();
        verify(jsDoc).hasThisType();

        assertEquals("The result should be false", false, result);
        assertEquals("assignLhsChild should be null", null, checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testVisit_ThisShouldReport() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        when(n.getType()).thenReturn(Token.THIS);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);

        checkGlobalThis.visit(t, n, parent);

        verify(compiler).report(any());
        verify(t).makeError(n, checkGlobalThis.level, CheckGlobalThis.GLOBAL_THIS);
    }

    @Test
    public void testVisit_ThisShouldNotReport() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        when(n.getType()).thenReturn(Token.THIS);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);

        checkGlobalThis.visit(t, n, parent);

        verify(compiler, never()).report(any());
        verify(t, never()).makeError(any(), any(), any());
    }
}