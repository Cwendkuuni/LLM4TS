import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class CheckGlobalThisTest {

    @Mock
    private AbstractCompiler mockCompiler;
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        checkGlobalThis = new CheckGlobalThis(mockCompiler);
    }

    @Test
    public void shouldReportThis_WhenThisOnLeftSideOfAssignment() {
        Node node = new Node(Token.ASSIGN);
        node.addChildToFront(new Node(Token.THIS));

        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(true);

        checkGlobalThis.visit(new NodeTraversal(mockCompiler), node, null);

        verify(mockCompiler).report(any(DiagnosticType.class));
    }

    @Test
    public void shouldReportThis_WhenThisWithPropertyAccess() {
        Node node = new Node(Token.GETPROP);
        node.addChildToFront(new Node(Token.THIS));
        node.addChildToBack(new Node(Token.STRING));

        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(true);

        checkGlobalThis.visit(new NodeTraversal(mockCompiler), node, null);

        verify(mockCompiler).report(any(DiagnosticType.class));
    }

    @Test
    public void shouldNotReportThis_WhenThisNotOnLeftSideOfAssignmentOrWithPropertyAccess() {
        Node node = new Node(Token.THIS);

        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        checkGlobalThis.visit(new NodeTraversal(mockCompiler), node, null);

        verify(mockCompiler, never()).report(any(DiagnosticType.class));
    }

    @Test
    public void shouldTraverse_WhenFunctionNotConstructorOrAnnotatedWithThis() {
        Node node = new Node(Token.FUNCTION);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(null);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, null);

        assertTrue(result);
    }

    @Test
    public void shouldNotTraverse_WhenFunctionIsConstructor() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo mockJsDoc = mock(JSDocInfo.class);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(mockJsDoc);
        when(mockJsDoc.isConstructor()).thenReturn(true);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, null);

        assertFalse(result);
    }

    @Test
    public void shouldNotTraverse_WhenFunctionAnnotatedWithThis() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo mockJsDoc = mock(JSDocInfo.class);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(mockJsDoc);
        when(mockJsDoc.hasThisType()).thenReturn(true);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, null);

        assertFalse(result);
    }

    @Test
    public void shouldNotTraverse_WhenFunctionAnnotatedWithOverride() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo mockJsDoc = mock(JSDocInfo.class);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(mockJsDoc);
        when(mockJsDoc.isOverride()).thenReturn(true);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, null);

        assertFalse(result);
    }

    @Test
    public void shouldNotTraverse_WhenFunctionHasNoLogicalPlaceForThisAnnotation() {
        Node node = new Node(Token.FUNCTION);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(null);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, null);

        assertFalse(result);
    }

    @Test
    public void shouldNotTraverse_WhenFunctionInsideNonGlobalContext() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(null);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, parent);

        assertFalse(result);
    }

    @Test
    public void shouldTraverse_WhenFunctionInsideGlobalContext() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.getParent().setType(Token.SCRIPT);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(null);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, parent);

        assertTrue(result);
    }

    @Test
    public void shouldTraverse_WhenFunctionInsideAssignment() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(null);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, parent);

        assertTrue(result);
    }

    @Test
    public void shouldNotTraverse_WhenFunctionGettingLentToPrototype() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.GETPROP);

        JSDocInfo mockJsDoc = mock(JSDocInfo.class);
        Node grandParent = mock(Node.class);
        when(grandParent.getJSDocInfo()).thenReturn(mockJsDoc);
        when(mockJsDoc.getLendsName()).thenReturn("xyz.prototype");
        parent.getParent().setType(Token.GETPROP);
        parent.getParent().addChildToFront(grandParent);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(null);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, parent);

        assertFalse(result);
    }

    @Test
    public void shouldNotTraverse_WhenFunctionHasThisAnnotation() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = mock(JSDocInfo.class);
        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(jsDocInfo);
        when(jsDocInfo.hasThisType()).thenReturn(true);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, null);

        assertFalse(result);
    }

    @Test
    public void shouldNotTraverse_WhenFunctionHasOverrideAnnotation() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = mock(JSDocInfo.class);
        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(jsDocInfo);
        when(jsDocInfo.isOverride()).thenReturn(true);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, null);

        assertFalse(result);
    }

    @Test
    public void shouldNotTraverse_WhenFunctionIsVariableDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(node);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(null);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, parent);

        assertFalse(result);
    }

    @Test
    public void shouldNotTraverse_WhenFunctionIsVariableInitialization() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(node);

        when(checkGlobalThis.getFunctionJsDocInfo(any(Node.class))).thenReturn(null);
        when(checkGlobalThis.shouldReportThis(any(Node.class))).thenReturn(false);

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(mockCompiler), node, parent);

        assertFalse(result);
    }
}