import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @After
    public void tearDown() {
        nodeUtil = null;
    }

    @Test
    public void testIsForIn_WithForLoop_ReturnsTrue() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        boolean result = nodeUtil.isForIn(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsForIn_WithOtherLoop_ReturnsFalse() {
        Node node = new Node(Token.WHILE, new Node(), new Node());
        boolean result = nodeUtil.isForIn(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement_WithStatementParent_ReturnsTrue() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);
        boolean result = nodeUtil.isStatement(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_WithNonStatementParent_ReturnsFalse() {
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);
        boolean result = nodeUtil.isStatement(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_WithFunctionDeclaration_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        boolean result = nodeUtil.isFunctionDeclaration(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_WithFunctionExpression_ReturnsFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(node);
        boolean result = nodeUtil.isFunctionDeclaration(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithFunctionDeclarationInScript_ReturnsTrue() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(functionNode);
        boolean result = nodeUtil.isHoistedFunctionDeclaration(functionNode);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithFunctionDeclarationInFunction_ReturnsTrue() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToFront(functionNode);
        boolean result = nodeUtil.isHoistedFunctionDeclaration(functionNode);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithFunctionDeclarationInNonScriptNonFunction_ReturnsFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentStatementNode = new Node(Token.EXPR_RESULT);
        parentStatementNode.addChildToFront(functionNode);
        boolean result = nodeUtil.isHoistedFunctionDeclaration(functionNode);
        Assert.assertFalse(result);
    }
}