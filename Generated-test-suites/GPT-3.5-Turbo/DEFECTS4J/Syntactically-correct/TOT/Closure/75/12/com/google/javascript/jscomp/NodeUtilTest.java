import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.IN));
        node.addChildToBack(new Node(Token.EXPR_RESULT));

        boolean result = NodeUtil.isForIn(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsForIn_emptyNode() {
        Node node = new Node();

        boolean result = NodeUtil.isForIn(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement_scriptParent() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node node = new Node(Token.EXPR_RESULT);
        scriptNode.addChildToBack(node);

        boolean result = NodeUtil.isStatement(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_blockParent() {
        Node blockNode = new Node(Token.BLOCK);
        Node node = new Node(Token.EXPR_RESULT);
        blockNode.addChildToBack(node);

        boolean result = NodeUtil.isStatement(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_labelParent() {
        Node labelNode = new Node(Token.LABEL);
        Node node = new Node(Token.EXPR_RESULT);
        labelNode.addChildToBack(node);

        boolean result = NodeUtil.isStatement(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_parentNotFound() {
        Node node = new Node(Token.EXPR_RESULT);

        boolean result = NodeUtil.isStatement(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_typical() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        Node grandparent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(node);
        node.addChildToBack(grandparent);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_statementFalse() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_wrongParent() {
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        Node rootScope = new Node(Token.SCRIPT);
        Node parentFunction = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION);
        parentFunction.addChildToBack(node);
        rootScope.addChildToBack(parentFunction);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notDeclaration() {
        Node scriptRoot = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        Node grandparent = new Node(Token.FUNCTION);
        scriptRoot.addChildToBack(node);
        node.addChildToBack(grandparent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_wrongParent() {
        Node blockNode = new Node(Token.BLOCK);
        Node functionNode = new Node(Token.FUNCTION);
        blockNode.addChildToBack(functionNode);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

        Assert.assertFalse(result);
    }

}