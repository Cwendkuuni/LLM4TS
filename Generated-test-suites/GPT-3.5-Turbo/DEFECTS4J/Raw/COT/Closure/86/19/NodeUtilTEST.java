
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        boolean result = NodeUtil.isForIn(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.EMPTY);

        boolean result = NodeUtil.isForIn(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.EMPTY);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        node.setParent(parent);

        boolean result = NodeUtil.isStatement(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.EMPTY);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(node);
        node.setParent(parent);

        boolean result = NodeUtil.isStatement(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(node);
        node.setParent(parent);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(node);
        node.setParent(parent);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        node.setParent(parent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(node);
        node.setParent(parent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        Assert.assertFalse(result);
    }
}
