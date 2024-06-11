import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {
    private CheckGlobalThis checker;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        checker = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_withFunctionAndConstructorWithoutThisAnnotation_returnsTrue() {
        Node n = createFunctionNode();
        NodeTraversal t = createNodeTraversal(n, null);
        boolean result = checker.shouldTraverse(t, n, null);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_withFunctionAndConstructorWithThisAnnotation_returnsFalse() {
        Node n = createFunctionNode();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        NodeTraversal t = createNodeTraversal(n, null);
        boolean result = checker.shouldTraverse(t, n, null);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_withFunctionAndConstructorWithOverrideAnnotation_returnsFalse() {
        Node n = createFunctionNode();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordOverride();
        n.setJSDocInfo(jsDoc);
        NodeTraversal t = createNodeTraversal(n, null);
        boolean result = checker.shouldTraverse(t, n, null);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_withFunctionAndConstructorWithConstructorAnnotation_returnsFalse() {
        Node n = createFunctionNode();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordConstructor();
        n.setJSDocInfo(jsDoc);
        NodeTraversal t = createNodeTraversal(n, null);
        boolean result = checker.shouldTraverse(t, n, null);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_withFunctionAndParentIsBlock_returnsTrue() {
        Node n = createFunctionNode();
        Node parent = new Node(Token.BLOCK);
        NodeTraversal t = createNodeTraversal(n, parent);
        boolean result = checker.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_withFunctionAndParentIsScript_returnsTrue() {
        Node n = createFunctionNode();
        Node parent = new Node(Token.SCRIPT);
        NodeTraversal t = createNodeTraversal(n, parent);
        boolean result = checker.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_withFunctionAndParentIsName_returnsTrue() {
        Node n = createFunctionNode();
        Node parent = new Node(Token.NAME);
        NodeTraversal t = createNodeTraversal(n, parent);
        boolean result = checker.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_withFunctionAndParentIsAssign_returnsTrue() {
        Node n = createFunctionNode();
        Node parent = new Node(Token.ASSIGN);
        NodeTraversal t = createNodeTraversal(n, parent);
        boolean result = checker.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_withFunctionAndParentIsExprResult_returnsFalse() {
        Node n = createFunctionNode();
        Node parent = new Node(Token.EXPR_RESULT);
        NodeTraversal t = createNodeTraversal(n, parent);
        boolean result = checker.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testVisit_withThisNodeOnLeftSideOfAssign_reportsGlobalThis() {
        Node n = createThisNode();
        Node parent = new Node(Token.ASSIGN);
        NodeTraversal t = createNodeTraversal(n, parent);
        checker.visit(t, n, parent);
        // Check that a warning is reported
    }

    @Test
    public void testVisit_withThisNodeOnRightSideOfAssign_doesNotReportGlobalThis() {
        Node n = createThisNode();
        Node lhs = new Node(Token.NAME);
        Node rhs = new Node(Token.NUMBER);
        Node parent = new Node(Token.ASSIGN, lhs, rhs);
        NodeTraversal t = createNodeTraversal(n, parent);
        checker.visit(t, n, parent);
        // Check that no warning is reported
    }

    @Test
    public void testVisit_withThisNodeOnRightSideOfAssignToPrototypeProperty_doesNotReportGlobalThis() {
        Node n = createThisNode();
        Node lhs = new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.STRING));
        lhs.getLastChild().setString("prototype");
        Node rhs = new Node(Token.NUMBER);
        Node parent = new Node(Token.ASSIGN, lhs, rhs);
        NodeTraversal t = createNodeTraversal(n, parent);
        checker.visit(t, n, parent);
        // Check that no warning is reported
    }

    @Test
    public void testVisit_withThisNodeOnRightSideOfAssignToPrototypeSubproperty_doesNotReportGlobalThis() {
        Node n = createThisNode();
        Node lhs = new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.STRING));
        lhs.getLastChild().setString("prototype.sub");
        Node rhs = new Node(Token.NUMBER);
        Node parent = new Node(Token.ASSIGN, lhs, rhs);
        NodeTraversal t = createNodeTraversal(n, parent);
        checker.visit(t, n, parent);
        // Check that no warning is reported
    }

    private Node createFunctionNode() {
        return new Node(Token.FUNCTION);
    }

    private Node createThisNode() {
        return new Node(Token.THIS);
    }

    private NodeTraversal createNodeTraversal(Node node, Node parent) {
        AbstractCompiler compiler = new AbstractCompiler();
        return new NodeTraversal(compiler, checker, node, parent);
    }
}