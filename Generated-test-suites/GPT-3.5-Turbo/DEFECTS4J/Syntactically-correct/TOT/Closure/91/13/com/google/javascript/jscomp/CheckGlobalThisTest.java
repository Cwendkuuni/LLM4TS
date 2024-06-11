import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

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
    public void testShouldTraverse_GivenFunctionWithConstructorAnnotation_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDocInfo = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDocInfo);
        when(jsDocInfo.isConstructor()).thenReturn(true);

        boolean actualResult = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(actualResult);
    }

    @Test
    public void testShouldTraverse_GivenFunctionWithThisTypeAnnotation_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDocInfo = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDocInfo);
        when(jsDocInfo.hasThisType()).thenReturn(true);

        boolean actualResult = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(actualResult);
    }

    @Test
    public void testShouldTraverse_GivenFunctionWithOverrideAnnotation_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        JSDocInfo jsDocInfo = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(checkGlobalThis.getFunctionJsDocInfo(n)).thenReturn(jsDocInfo);
        when(jsDocInfo.isOverride()).thenReturn(true);

        boolean actualResult = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(actualResult);
    }

    @Test
    public void testShouldTraverse_GivenFunctionWithNoLogicalPlaceForThisAnnotation_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        Node grandParent = mock(Node.class);
        when(parent.getType()).thenReturn(Token.VAR);
        when(parent.getParent()).thenReturn(grandParent);

        boolean actualResult = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(actualResult);
    }

    @Test
    public void testShouldTraverse_GivenAssignmentToPrototypeProperty_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        Node lhs = mock(Node.class);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(lhs.getType()).thenReturn(Token.GETPROP);
        Node lastChild = mock(Node.class);
        when(lhs.getLastChild()).thenReturn(lastChild);
        when(lastChild.getString()).thenReturn("prototype");

        boolean actualResult = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(actualResult);
    }

    @Test
    public void testShouldTraverse_GivenAssignmentNestedInPrototype_ReturnsFalse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(parent.getType()).thenReturn(Token.ASSIGN);
        Node lhs = mock(Node.class);
        when(parent.getFirstChild()).thenReturn(lhs);
        when(lhs.getType()).thenReturn(Token.GETPROP);
        Node firstChild = mock(Node.class);
        when(lhs.getFirstChild()).thenReturn(firstChild);
        when(firstChild.getType()).thenReturn(Token.GETPROP);
        Node lastChild = mock(Node.class);
        when(firstChild.getLastChild()).thenReturn(lastChild);
        when(lastChild.getString()).thenReturn("prototype");

        boolean actualResult = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(actualResult);
    }

    @Test
    public void testVisit_GivenThisOnLeftSideOfAssign_CallsCompilerReport() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        checkGlobalThis.shouldReportThis(n, parent); //set assignLhsChild to null
        when(n.getType()).thenReturn(Token.THIS);

        checkGlobalThis.visit(t, n, parent);

        verify(compiler, times(1)).report(any());
    }

    @Test
    public void testVisit_GivenThisWithPropertyAccess_CallsCompilerReport() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        checkGlobalThis.shouldReportThis(n, parent); //set assignLhsChild to null
        when(n.getType()).thenReturn(Token.THIS);
        when(NodeUtil.isGet(parent)).thenReturn(true);

        checkGlobalThis.visit(t, n, parent);

        verify(compiler, times(1)).report(any());
    }

    @Test
    public void testShouldReportThis_GivenAssignLhsChildIsNotNull_ReturnsTrue() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        checkGlobalThis.visit(mock(NodeTraversal.class), n, parent); //set assignLhsChild to null

        boolean actualResult = checkGlobalThis.shouldReportThis(n, parent);

        assertTrue(actualResult);
    }

    @Test
    public void testShouldReportThis_GivenParentIsNull_ReturnsFalse() {
        Node n = mock(Node.class);
        Node parent = null;
        checkGlobalThis.visit(mock(NodeTraversal.class), n, parent); //set assignLhsChild to null

        boolean actualResult = checkGlobalThis.shouldReportThis(n, parent);

        assertFalse(actualResult);
    }

    @Test
    public void testShouldReportThis_GivenParentIsNotGet_ReturnsFalse() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        checkGlobalThis.visit(mock(NodeTraversal.class), n, parent); //set assignLhsChild to null
        when(NodeUtil.isGet(parent)).thenReturn(false);

        boolean actualResult = checkGlobalThis.shouldReportThis(n, parent);

        assertFalse(actualResult);
    }

    @Test
    public void testGetFunctionJsDocInfo_GivenFunctionNodeWithJsDocInfo_ReturnsJsDocInfo() {
        Node n = mock(Node.class);
        when(n.getJSDocInfo()).thenReturn(mock(JSDocInfo.class));

        JSDocInfo actualResult = checkGlobalThis.getFunctionJsDocInfo(n);

        assertNotNull(actualResult);
    }

    @Test
    public void testGetFunctionJsDocInfo_GivenFunctionNodeWithJsDocInfoIsNull_ReturnsJsDocInfoFromParentName() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.getJSDocInfo()).thenReturn(null);
        when(n.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getJSDocInfo()).thenReturn(mock(JSDocInfo.class));

        JSDocInfo actualResult = checkGlobalThis.getFunctionJsDocInfo(n);

        assertNotNull(actualResult);
    }

    @Test
    public void testGetFunctionJsDocInfo_GivenFunctionAssignNodeWithJsDocInfoIsNull_ReturnsJsDocInfoFromParent() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Node grandParent = mock(Node.class);
        when(n.getJSDocInfo()).thenReturn(null);
        when(n.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.NAME);
        when(parent.getParent()).thenReturn(grandParent);
        when(grandParent.getType()).thenReturn(Token.VAR);
        when(grandParent.getJSDocInfo()).thenReturn(mock(JSDocInfo.class));

        JSDocInfo actualResult = checkGlobalThis.getFunctionJsDocInfo(n);

        assertNotNull(actualResult);
    }

    @Test
    public void testGetFunctionJsDocInfo_GivenFunctionNodeWithJsDocInfoIsNull_ReturnsNullIfNoParentInfo() {
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        when(n.getJSDocInfo()).thenReturn(null);
        when(n.getParent()).thenReturn(parent);
        when(parent.getType()).thenReturn(Token.NAME);

        JSDocInfo actualResult = checkGlobalThis.getFunctionJsDocInfo(n);

        assertNull(actualResult);
    }

}