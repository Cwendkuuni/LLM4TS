import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // typical use case
        Assert.assertTrue(NodeUtil.isForIn(createForInNode()));

        // edge case
        Assert.assertFalse(NodeUtil.isForIn(createNonForInNode()));

        // error scenario
        Assert.assertFalse(NodeUtil.isForIn(null));
    }

    @Test
    public void testIsStatement() {
        // typical use case
        Assert.assertTrue(NodeUtil.isStatement(createStatementNode()));

        // edge case
        Assert.assertFalse(NodeUtil.isStatement(createNonStatementNode()));

        // error scenario
        Assert.assertFalse(NodeUtil.isStatement(null));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // typical use case
        Assert.assertTrue(NodeUtil.isFunctionDeclaration(createFunctionDeclarationNode()));

        // edge case
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(createNonFunctionDeclarationNode()));

        // error scenario
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(null));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // typical use case
        Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(createHoistedFunctionDeclarationNode()));

        // edge case
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(createNonHoistedFunctionDeclarationNode()));

        // error scenario
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
    }

    private Node createForInNode() {
        Node forNode = new Node(Token.FOR);
        forNode.addChildToBack(new Node(Token.SCRIPT));
        forNode.addChildToBack(new Node(Token.SCRIPT));
        forNode.addChildToBack(new Node(Token.SCRIPT));
        return forNode;
    }

    private Node createNonForInNode() {
        return new Node(Token.IF);
    }

    private Node createStatementNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToBack(new Node(Token.SCRIPT));
        scriptNode.addChildToBack(blockNode);
        return scriptNode;
    }

    private Node createNonStatementNode() {
        return new Node(Token.IF);
    }

    private Node createFunctionDeclarationNode() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.SCRIPT));
        return functionNode;
    }

    private Node createNonFunctionDeclarationNode() {
        return new Node(Token.VAR);
    }

    private Node createHoistedFunctionDeclarationNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(functionNode);
        return functionNode;
    }

    private Node createNonHoistedFunctionDeclarationNode() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.SCRIPT));
        return functionNode;
    }
}