import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class CheckGlobalThisTest {

    @Mock
    private AbstractCompiler compiler;

    @Mock
    private NodeTraversal.Callback callback;

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForConstructorFunction() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForFunctionWithThisOrOverrideAnnotation() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.hasThisType()).thenReturn(true);
        when(jsDoc.isOverride()).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForFunctionWithInvalidParentType() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(parent.getType()).thenReturn(Token.OTHER);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_shouldReturnTrueForValidFunction() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(parent.getType()).thenReturn(Token.BLOCK);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void shouldTraverse_shouldReturnTrueForLeftSideOfAssignment() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node lhs = mock(Node.class);
        Node rhs = mock(Node.class);
        when(n.getType()).thenReturn(Token.NAME);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(lhs.getNext()).thenReturn(rhs);
        when(n == lhs).thenReturn(true);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForPrototypePropertyAssignment() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node lhs = mock(Node.class);
        when(n.getType()).thenReturn(Token.OTHER);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(lhs.getType()).thenReturn(Token.GETPROP);
        when(lhs.getLastChild().getString()).thenReturn("prototype");

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForPrototypeSubpropertyAssignment() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node lhs = mock(Node.class);
        when(n.getType()).thenReturn(Token.OTHER);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(lhs.getType()).thenReturn(Token.GETPROP);
        when(lhs.getQualifiedName()).thenReturn("object.prototype.property");

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void visit_shouldReportGlobalThisWhenRequired() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.getType()).thenReturn(Token.THIS);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);

        checkGlobalThis.visit(t, n, parent);

        verify(compiler, times(1)).report(any());
    }

    @Test
    public void visit_shouldNotReportGlobalThisWhenNotRequired() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.getType()).thenReturn(Token.THIS);
        when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);

        checkGlobalThis.visit(t, n, parent);

        verify(compiler, times(0)).report(any());
    }

    @Test
    public void visit_shouldResetAssignLhsChildWhenMatchingNodeIsVisited() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        checkGlobalThis.assignLhsChild = n;

        checkGlobalThis.visit(t, n, parent);

        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void getFunctionJsDocInfo_shouldReturnJSDocInfoForNode() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.getJSDocInfo()).thenReturn(null);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenCallRealMethod();
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getJSDocInfo()).thenReturn(mock(JSDocInfo.class));
        Node gramps = mock(Node.class);
        when(parent.getParent()).thenReturn(gramps);
        when(gramps.getType()).thenReturn(Token.VAR);
        JSDocInfo expectedJsDoc = mock(JSDocInfo.class);
        when(gramps.getJSDocInfo()).thenReturn(expectedJsDoc);

        JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(expectedJsDoc, jsDoc);
    }
}