
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
    CheckGlobalThis checkGlobalThis;
    AbstractCompiler compiler;
    CheckLevel level;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_WithFunctionNotConstructorOrThisAnnotation_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(compiler);

        Node n = new Node(Token.FUNCTION);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_WithFunctionConstructor_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler);

        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithFunctionThisAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler);

        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WithAssignmentThis_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(compiler);

        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        Node rhs = new Node(Token.THIS);
        parent.addChildToBack(lhs);
        parent.addChildToBack(rhs);
        boolean result = checkGlobalThis.shouldTraverse(t, rhs, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_WithAssignmentPrototype_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal(compiler);

        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.THIS));
        lhs.addChildToBack(new Node(Token.STRING));
        lhs.getLastChild().setString("prototype");
        Node rhs = new Node(Token.NAME);
        parent.addChildToBack(lhs);
        parent.addChildToBack(rhs);
        boolean result = checkGlobalThis.shouldTraverse(t, rhs, parent);

        assertFalse(result);
    }

    @Test
    public void testVisit_WithThisInGlobalContextAndLevelWarning_ReportsError() {
        NodeTraversal t = new NodeTraversal(compiler);

        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        compiler.setReportedErrors(1);
        checkGlobalThis.visit(t, n, parent);

        assertEquals(1, compiler.getReportedErrors());
    }

    @Test
    public void testVisit_WithThisInNonGlobalContext_DoesNotReportError() {
        NodeTraversal t = new NodeTraversal(compiler);

        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.STRING);
        compiler.setReportedErrors(0);
        checkGlobalThis.visit(t, n, parent);

        assertEquals(0, compiler.getReportedErrors());
    }

    @Test
    public void testVisit_WithAssignLhsChildNotNull_SetsAssignLhsChildToNull() {
        NodeTraversal t = new NodeTraversal(compiler);

        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        lhs.addChildToBack(new Node(Token.THIS));
        Node rhs = new Node(Token.NAME);
        parent.addChildToBack(lhs);
        parent.addChildToBack(rhs);
        checkGlobalThis.assignLhsChild = lhs;
        compiler.setReportedErrors(0);
        checkGlobalThis.visit(t, n, parent);

        assertNull(checkGlobalThis.assignLhsChild);
    }
}
