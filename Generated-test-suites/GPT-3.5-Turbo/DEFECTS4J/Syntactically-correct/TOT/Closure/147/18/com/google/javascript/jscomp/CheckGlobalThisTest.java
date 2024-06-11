import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
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
        level = CheckLevel.ERROR;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_WhenFunctionIsConstructor_ShouldReturnFalse() {
        NodeTraversal t = createMockNodeTraversal();
        Node functionNode = createMockNode(Token.FUNCTION);
        JSDocInfo jsDoc = createMockJSDocInfo(true, false, false, false);
        functionNode.setJSDocInfo(jsDoc);
        Node traversalNode = createMockNode(Token.NAME);
        traversalNode.addChildToFront(functionNode);
        Node parent = createMockNode(Token.FUNCTION);
        boolean result = checkGlobalThis.shouldTraverse(t, traversalNode, parent);
        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WhenFunctionHasThisTypeAnnotation_ShouldReturnFalse() {
        NodeTraversal t = createMockNodeTraversal();
        Node functionNode = createMockNode(Token.FUNCTION);
        JSDocInfo jsDoc = createMockJSDocInfo(false, false, true, false);
        functionNode.setJSDocInfo(jsDoc);
        Node traversalNode = createMockNode(Token.BLOCK);
        traversalNode.addChildToFront(functionNode);
        Node parent = createMockNode(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(t, traversalNode, parent);
        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WhenFunctionIsAssignment_ShouldReturnFalse() {
        NodeTraversal t = createMockNodeTraversal();
        Node functionNode = createMockNode(Token.FUNCTION);
        JSDocInfo jsDoc = createMockJSDocInfo(false, false, false, true);
        functionNode.setJSDocInfo(jsDoc);
        Node traversalNode = createMockNode(Token.SCRIPT);
        traversalNode.addChildToFront(functionNode);
        Node parent = createMockNode(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(t, traversalNode, parent);
        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_WhenParentIsAssignmentAndTraversalNodeIsAssignLhsChild_ShouldReturnTrue() {
        NodeTraversal t = createMockNodeTraversal();
        Node assignNode = createMockNode(Token.ASSIGN);
        Node traversalNode = createMockNode(Token.NAME);
        assignNode.addChildToFront(traversalNode);
        Node rhs = createMockNode(Token.NAME);
        assignNode.addChildToBack(rhs);
        checkGlobalThis.visit(t, traversalNode, assignNode);
        Node parent = createMockNode(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(t, traversalNode, parent);
        Assert.assertTrue(result);
    }

    @Test
    public void testShouldTraverse_WhenParentIsAssignmentAndLhsIsGetAndNotPrototype_ShouldReturnTrue() {
        NodeTraversal t = createMockNodeTraversal();
        Node assignNode = createMockNode(Token.ASSIGN);
        Node lhs = createMockNode(Token.GETPROP);
        lhs.addChildToFront(createMockNode(Token.NAME));
        lhs.addChildToBack(createMockNode(Token.STRING));
        assignNode.addChildToFront(lhs);
        Node rhs = createMockNode(Token.NAME);
        assignNode.addChildToBack(rhs);
        checkGlobalThis.visit(t, rhs, assignNode);
        Node parent = createMockNode(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(t, rhs, parent);
        Assert.assertTrue(result);
    }

    @Test
    public void testVisit_WhenTypeIsThisAndShouldReportThis_ShouldReportError() {
        NodeTraversal t = createMockNodeTraversal();
        Node n = createMockNode(Token.THIS);
        Node parent = createMockNode(Token.GETPROP);
        checkGlobalThis.visit(t, n, parent);
        // TODO: Assert error is reported
    }

    @Test
    public void testVisit_WhenTypeIsNotThisAndAssignLhsChildIsNotNull_ShouldSetAssignLhsChildNull() {
        NodeTraversal t = createMockNodeTraversal();
        Node n = createMockNode(Token.NAME);
        Node parent = createMockNode(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = createMockNode(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        Assert.assertNull(checkGlobalThis.assignLhsChild);
    }

    private NodeTraversal createMockNodeTraversal() {
        return new NodeTraversal(compiler, checkGlobalThis);
    }

    private Node createMockNode(Token token) {
        return new Node(token);
    }

    private JSDocInfo createMockJSDocInfo(boolean isConstructor, boolean isInterface, boolean hasThisType, boolean isOverride) {
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(isConstructor);
        jsDoc.setInterface(isInterface);
        jsDoc.recordThisType();
        jsDoc.setOverride(isOverride);
        return jsDoc;
    }

}