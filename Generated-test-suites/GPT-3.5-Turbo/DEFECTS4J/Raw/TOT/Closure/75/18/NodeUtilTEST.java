import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Typical use case
        Node node1 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Assert.assertTrue(NodeUtil.isForIn(node1));

        // Edge case - no children
        Node node2 = new Node(Token.FOR);
        Assert.assertFalse(NodeUtil.isForIn(node2));

        // Edge case - only 2 children
        Node node3 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        Assert.assertFalse(NodeUtil.isForIn(node3));

        // Edge case - more than 3 children
        Node node4 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Assert.assertFalse(NodeUtil.isForIn(node4));

        // Error scenario - null node
        Assert.assertFalse(NodeUtil.isForIn(null));
    }

    @Test
    public void testIsStatement() {
        // Typical use case
        Node node1 = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        Assert.assertTrue(NodeUtil.isStatement(node1));

        // Edge case - parent is null
        Node node2 = new Node(Token.EMPTY);
        Assert.assertThrows(NullPointerException.class, () -> {
            NodeUtil.isStatement(node2);
        });

        // Edge case - parent is not a statement
        Node node3 = new Node(Token.ADD, new Node(Token.EMPTY), new Node(Token.EMPTY));
        node3.addChildToFront(new Node(Token.NEW));
        Assert.assertFalse(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION);
        node1.addChildToFront(new Node(Token.NAME));
        Assert.assertTrue(NodeUtil.isFunctionDeclaration(node1));

        // Edge case - not a statement
        Node node2 = new Node(Token.FUNCTION, new Node(Token.NAME));
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(node2));

        // Edge case - parent is null
        Node node3 = new Node(Token.FUNCTION);
        node3.addChildToFront(new Node(Token.NAME));
        node3.setParent(null);
        Assert.assertThrows(NullPointerException.class, () -> {
            NodeUtil.isFunctionDeclaration(node3);
        });
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical use case
        Node node1 = new Node(Token.FUNCTION);
        node1.addChildToFront(new Node(Token.NAME));
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(node1);
        Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        // Edge case - not a statement
        Node node2 = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToFront(node2);
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

        // Edge case - parent's parent is not a function
        Node node3 = new Node(Token.FUNCTION);
        node3.addChildToFront(new Node(Token.NAME));
        node3.setParent(new Node(Token.SCRIPT));
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}