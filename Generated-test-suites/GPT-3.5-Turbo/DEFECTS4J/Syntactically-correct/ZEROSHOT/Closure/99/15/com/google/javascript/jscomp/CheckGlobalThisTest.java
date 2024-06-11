import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.DiagnosticType;

public class CheckGlobalThisTest {
    
    @Mock
    AbstractCompiler compiler;
    
    @Mock
    NodeTraversal t;
    
    Node n;
    
    Node parent;
    
    CheckLevel level;
    
    CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }
    
    @Test
    public void testShouldTraverse_globalContext_returnsTrue() {
        n = new Node();
        parent = new Node();
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_constructorFunction_returnsFalse() {
        n = new Node();
        parent = new Node();
        parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo.Builder(true).build());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionWithThisAnnotation_returnsFalse() {
        n = new Node();
        parent = new Node();
        parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo.Builder().withThisType().build());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionWithOverrideAnnotation_returnsFalse() {
        n = new Node();
        parent = new Node();
        parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo.Builder().withOverride().build());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionInBlock_returnsFalse() {
        n = new Node();
        parent = new Node(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionInScript_returnsFalse() {
        n = new Node();
        parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionWithName_returnsFalse() {
        n = new Node();
        parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionWithAssign_returnsFalse() {
        n = new Node();
        parent = new Node(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }
    
    @Test
    public void testShouldReportThis_onLeftSideOfAssignment_returnsTrue() {
        n = new Node(Token.THIS);
        parent = new Node(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = n;
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertTrue(result);
    }
    
    @Test
    public void testShouldReportThis_withPropertyAccess_returnsTrue() {
        n = new Node(Token.THIS);
        parent = new Node();
        parent.putBooleanProp(NodeUtil.IS_GET, true);
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertTrue(result);
    }
    
    @Test
    public void testShouldReportThis_notOnLeftSideOfAssignment_returnsFalse() {
        n = new Node(Token.THIS);
        parent = new Node(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertFalse(result);
    }
    
    @Test
    public void testVisit_thisWithShouldReport_callsReport() {
        n = new Node(Token.THIS);
        parent = new Node();
        checkGlobalThis.shouldReportThis(n, parent);
        checkGlobalThis.visit(t, n, parent);
        verify(compiler).report(any());
    }
    
    @Test
    public void testVisit_notAssignLhsChild_assignLhsChildSetToNull() {
        n = new Node();
        parent = new Node();
        checkGlobalThis.visit(t, n, parent);
        assertNull(checkGlobalThis.assignLhsChild);
    }
}