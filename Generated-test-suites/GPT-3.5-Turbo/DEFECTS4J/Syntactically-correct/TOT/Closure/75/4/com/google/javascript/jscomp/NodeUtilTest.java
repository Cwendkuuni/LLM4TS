import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Typical use case
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.VAR));
        node1.addChildToBack(new Node(Token.NAME));
        node1.addChildToBack(new Node(Token.IN));

        Assert.assertTrue(NodeUtil.isForIn(node1));

        // Edge case
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.VAR));
        node2.addChildToBack(new Node(Token.NAME));

        Assert.assertFalse(NodeUtil.isForIn(node2));

        // Error scenario
        Node node3 = null;

        try {
            NodeUtil.isForIn(node3);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsStatement() {
        // Typical use case
        Node node1 = new Node(Token.SCRIPT);
        node1.addChildToBack(new Node(Token.IF));

        Assert.assertTrue(NodeUtil.isStatement(node1));

        // Edge case
        Node node2 = new Node(Token.EXPR_RESULT);
        node2.addChildToBack(new Node(Token.NAME));

        Assert.assertFalse(NodeUtil.isStatement(node2));

        // Error scenario
        Node node3 = new Node(Token.NAME);

        Assert.assertFalse(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION);
        node1.addChildToBack(new Node(Token.NAME));

        Assert.assertTrue(NodeUtil.isFunctionDeclaration(node1));

        // Edge case
        Node node2 = new Node(Token.EXPR_RESULT);
        node2.addChildToBack(new Node(Token.FUNCTION));

        Assert.assertFalse(NodeUtil.isFunctionDeclaration(node2));

        // Error scenario
        Node node3 = null;

        try {
            NodeUtil.isFunctionDeclaration(node3);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION);
        node1.addChildToBack(new Node(Token.NAME));
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(node1);

        Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        // Edge case
        Node node2 = new Node(Token.FUNCTION);
        node2.addChildToBack(new Node(Token.NAME));
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(node2);

        Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

        // Error scenario
        Node node3 = new Node(Token.FUNCTION);
        node3.addChildToBack(new Node(Token.NAME));

        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}