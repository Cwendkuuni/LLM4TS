import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler compiler;
    private CheckLevel level;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void shouldTraverse_shouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertTrue(result);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseWhenFunctionIsConstructor() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        compiler.report = new Report();
        checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertFalse(compiler.report.errorReported);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseWhenFunctionHasThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        compiler.report = new Report();
        checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertFalse(compiler.report.errorReported);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseWhenFunctionIsInNonGlobalContext() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        compiler.report = new Report();
        checkGlobalThis.shouldTraverse(t, n, parent);
        Assert.assertFalse(compiler.report.errorReported);
    }

    @Test
    public void visit_shouldReportWarningWhenThisIsUsedOnLHSOfAssignment() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        CheckGlobalThis.Report report = new CheckGlobalThis.Report();
        compiler.setReport(report);
        checkGlobalThis.visit(t, n, parent);
        Assert.assertTrue(report.errorReported);
    }

    @Test
    public void visit_shouldReportWarningWhenThisIsUsedOnRHSOfPropertyAccess() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        parent.setType(Token.GETPROP);
        CheckGlobalThis.Report report = new CheckGlobalThis.Report();
        compiler.setReport(report);
        checkGlobalThis.visit(t, n, parent);
        Assert.assertTrue(report.errorReported);
    }

    private class NodeTraversal {
        boolean makeErrorCalled = false;

        CheckGlobalThis.Report makeError(Node n, CheckLevel l, CheckGlobalThis.DiagnosticType t) {
            makeErrorCalled = true;
            return new CheckGlobalThis.Report();
        }
    }

    private class Report {
        boolean errorReported = false;

        void report(CheckGlobalThis.Report r) {
            errorReported = true;
        }
    }
}