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
    public void testIsForIn_NotForInLoop() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        boolean result = NodeUtil.isForIn(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);

        boolean result = NodeUtil.isStatement(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_NotStatement() {
        Node node = new Node(Token.EMPTY);

        boolean result = NodeUtil.isStatement(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));

        boolean result = NodeUtil.isFunctionDeclaration(node);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_NotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.NAME));
        scriptNode.addChildToBack(functionNode);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.NAME));

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

        Assert.assertFalse(result);
    }
}