import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

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
    public void testShouldTraverse_ConstructorWithThisAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordConstructor();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_InterfaceWithThisAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordInterface();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordOverride();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionNotInValidScope_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.IF);
        n.setParent(parent);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionInValidScope_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        n.setParent(parent);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_AssignmentToLeftSide_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(n);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_GetPropertyNotOnPrototype_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToFront(n);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(lhs);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_GetPropertyOnPrototype_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node lhs = new Node(Token.GETPROP);
        Node prop = new Node(Token.STRING);
        prop.setString("prototype");
        lhs.addChildToBack(prop);
        lhs.addChildToBack(n);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(lhs);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testVisit_ThisAndShouldReportThis_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit_ThisAndShouldNotReportThis_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.STRING);
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertFalse(result);
    }

    @Test
    public void testVisit_AssignLhsChildNotNull_ResetsAssignLhsChild() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        checkGlobalThis.assignLhsChild = n;
        checkGlobalThis.visit(t, n, null);
        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testVisit_AssignLhsChildNull_DoesNotResetAssignLhsChild() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, null);
        assertNotNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testVisit_ThisAndShouldReportThis_CallsCompilerReport() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        // Assert logic for compiler.report
    }

}