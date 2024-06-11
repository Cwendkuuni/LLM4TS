import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {
    private static final DiagnosticType GLOBAL_THIS = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");

    @Test
    public void testShouldTraverse_GlobalContext_ReturnsTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checker.shouldTraverse(null, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        boolean result = checker.shouldTraverse(null, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionThis_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setThisType(new JSTypeExpression(new Node(Token.STRING)));
        n.setJSDocInfo(jsDoc);
        boolean result = checker.shouldTraverse(null, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_NonFunction_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.NAME);
        boolean result = checker.shouldTraverse(null, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldReportThis_AssignLhsChild_ReturnsTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME, "a");
        parent.addChildToFront(lhs);
        parent.addChildToBack(n);
        lhs.addChildToBack(parent);
        checker.assignLhsChild = lhs;
        boolean result = checker.shouldReportThis(n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldReportThis_PropertyAccess_ReturnsTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        Node lhs = new Node(Token.NAME, "a");
        parent.addChildToFront(lhs);
        parent.addChildToBack(n);
        boolean result = checker.shouldReportThis(n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldReportThis_DefaultCase_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        boolean result = checker.shouldReportThis(n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testVisit_ThisNode_ShouldReportDiagnostic() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = NodeTraversal.createEmptyCallback();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        t.report = (error) -> {
            assertEquals(GLOBAL_THIS, error.getDiagnosticType());
        };
        checker.visit(t, n, parent);
    }

    @Test
    public void testVisit_NotThisNode_ShouldNotReportDiagnostic() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = NodeTraversal.createEmptyCallback();
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.NAME);
        t.report = (error) -> {
            fail("Should not report diagnostic.");
        };
        checker.visit(t, n, parent);
    }

    @Test
    public void testVisit_AssignLhsChild_ShouldResetAssignLhsChildToNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = NodeTraversal.createEmptyCallback();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME, "a");
        parent.addChildToFront(lhs);
        parent.addChildToBack(n);
        lhs.addChildToBack(parent);
        checker.assignLhsChild = lhs;
        checker.visit(t, n, parent);
        assertEquals(null, checker.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionHasJsDocInfo_ReturnsJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo expected = new JSDocInfo();
        n.setJSDocInfo(expected);
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        JSDocInfo result = checker.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionParentIsName_ReturnsParentJsDocInfo() {
        Node n = new Node(Token.NAME);
        JSDocInfo expected = new JSDocInfo();
        Node parent = new Node(Token.VAR);
        parent.setJSDocInfo(expected);
        parent.addChildFront(n);
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        JSDocInfo result = checker.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionParentIsAssign_ReturnsParentJsDocInfo() {
        Node n = new Node(Token.NAME);
        JSDocInfo expected = new JSDocInfo();
        Node parent = new Node(Token.ASSIGN);
        parent.setJSDocInfo(expected);
        parent.addChildFront(n);
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        JSDocInfo result = checker.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionParentIsAssignGrandparentIsVar_ReturnsGrandparentJsDocInfo() {
        Node n = new Node(Token.NAME);
        JSDocInfo expected = new JSDocInfo();
        Node parent = new Node(Token.NAME);
        Node grandparent = new Node(Token.VAR);
        grandparent.setJSDocInfo(expected);
        grandparent.addChildToFront(parent);
        parent.addChildToFront(n);
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        JSDocInfo result = checker.getFunctionJsDocInfo(n);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDocInfo_ReturnsNull() {
        Node n = new Node(Token.FUNCTION);
        CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        JSDocInfo result = checker.getFunctionJsDocInfo(n);
        assertEquals(null, result);
    }
}