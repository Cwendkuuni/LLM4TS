import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;
    private CheckLevel level;
    private NodeTraversal t;
    private Node n;
    private Node parent;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        level = CheckLevel.WARNING;
        t = mock(NodeTraversal.class);
        n = mock(Node.class);
        parent = mock(Node.class);
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnFalse() {
        when(n.getType()).thenReturn(Token.FUNCTION);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(CheckGlobalThisTest.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(true);

        boolean result = CheckGlobalThisTest.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnFalse() {
        when(n.getType()).thenReturn(Token.FUNCTION);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(CheckGlobalThisTest.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.hasThisType()).thenReturn(true);

        boolean result = CheckGlobalThisTest.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithNoLogicalPlaceForThisAnnotation_ReturnFalse() {
        when(n.getType()).thenReturn(Token.FUNCTION);
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(CheckGlobalThisTest.getFunctionJsDocInfo(n)).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(false);
        when(jsDoc.hasThisType()).thenReturn(false);
        when(jsDoc.isOverride()).thenReturn(false);

        Node blockParent = mock(Node.class);
        when(parent.getType()).thenReturn(Token.BLOCK);
        when(parent.getParent()).thenReturn(blockParent);
        when(blockParent.getType()).thenReturn(Token.FUNCTION);

        boolean result = CheckGlobalThisTest.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_NonFunctionParent_ReturnFalse() {
        when(n.getType()).thenReturn(Token.FUNCTION);

        boolean result = CheckGlobalThisTest.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_LeftSideOfAssignment_ReturnTrue() {
        when(parent.getType()).thenReturn(Token.ASSIGN);
        Node lhs = mock(Node.class);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(n == lhs).thenReturn(true);

        boolean result = CheckGlobalThisTest.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignmentToPrototypeProperty_ReturnFalse() {
        when(parent.getType()).thenReturn(Token.ASSIGN);
        Node lhs = mock(Node.class);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(n == lhs).thenReturn(false);
        Node lhsFirstChild = mock(Node.class);
        when(lhs.getType()).thenReturn(Token.GETPROP);
        when(lhs.getLastChild().getString()).thenReturn("prototype");
        when(lhs.getFirstChild()).thenReturn(lhsFirstChild);

        boolean result = CheckGlobalThisTest.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignmentToNestedPrototypeProperty_ReturnFalse() {
        when(parent.getType()).thenReturn(Token.ASSIGN);
        Node lhs = mock(Node.class);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(n == lhs).thenReturn(false);
        Node lhsFirstChild = mock(Node.class);
        when(lhs.getType()).thenReturn(Token.GETPROP);
        when(lhs.getLastChild().getString()).thenReturn("prototype");
        Node llhs = mock(Node.class);
        when(lhs.getFirstChild()).thenReturn(llhs);
        when(llhs.getType()).thenReturn(Token.GETPROP);
        when(llhs.getLastChild().getString()).thenReturn("prototype");

        boolean result = CheckGlobalThisTest.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignment_NotAssignmentToPrototypeProperty_ReturnTrue() {
        when(parent.getType()).thenReturn(Token.ASSIGN);
        Node lhs = mock(Node.class);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(n == lhs).thenReturn(false);
        Node lhsFirstChild = mock(Node.class);
        when(lhs.getType()).thenReturn(Token.GETPROP);
        when(lhs.getLastChild().getString()).thenReturn("property");

        boolean result = CheckGlobalThisTest.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ThisOnLeftSideOfAssign_ReturnTrue() {
        Node assignLhsChild = mock(Node.class);
        CheckGlobalThisTest.assignLhsChild = assignLhsChild;
        Node notAssignLhsChild = mock(Node.class);

        boolean result1 = CheckGlobalThisTest.shouldReportThis(n, assignLhsChild);

        assertTrue(result1);

        boolean result2 = CheckGlobalThisTest.shouldReportThis(n, notAssignLhsChild);

        assertFalse(result2);
    }

    @Test
    public void testVisit_ThisAndShouldReportThis_ReturnReport() {
        when(n.getType()).thenReturn(Token.THIS);
        when(CheckGlobalThisTest.shouldReportThis(n, parent)).thenReturn(true);

        CheckGlobalThisTest.visit(t, n, parent);

        verify(compiler).report(t.makeError(n, level, GLOBAL_THIS));
    }

    @Test
    public void testVisit_NotThis_ReturnDoNothing() {
        CheckGlobalThisTest.visit(t, n, parent);

        verify(compiler, never()).report(any());
    }

    @Test
    public void testVisit_NullAssignLhsChild_ReturnDoNothing() {
        CheckGlobalThisTest.assignLhsChild = null;

        CheckGlobalThisTest.visit(t, n, parent);

        verify(compiler, never()).report(any());
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJsDoc_ReturnJsDoc() {
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getJSDocInfo()).thenReturn(jsDoc);

        JSDocInfo result = CheckGlobalThisTest.getFunctionJsDocInfo(n);

        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentNameWithJsDoc_ReturnJsDoc() {
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        Node parentName = mock(Node.class);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getFirstChild()).thenReturn(parentName);
        when(parent.getJSDocInfo()).thenReturn(jsDoc);

        JSDocInfo result = CheckGlobalThisTest.getFunctionJsDocInfo(n);

        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentAssignWithNameChildWithJsDoc_ReturnJsDoc() {
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        Node parentNameChild = mock(Node.class);
        Node grandparent = mock(Node.class);
        when(n.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        when(parent.getParent()).thenReturn(grandparent);
        when(parent.getFirstChild()).thenReturn(parentNameChild);
        when(parent.getJSDocInfo()).thenReturn(null);
        when(grandparent.getType()).thenReturn(Token.VAR);
        when(grandparent.getJSDocInfo()).thenReturn(jsDoc);

        JSDocInfo result = CheckGlobalThisTest.getFunctionJsDocInfo(n);

        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDoc_ReturnNull() {
        when(n.getJSDocInfo()).thenReturn(null);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getJSDocInfo()).thenReturn(null);

        JSDocInfo result = CheckGlobalThisTest.getFunctionJsDocInfo(n);

        assertNull(result);
    }
}