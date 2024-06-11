import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Typical use case
        Assert.assertTrue(NodeUtil.isForIn(createNode(Token.FOR, createNode(), createNode(), createNode())));
        
        // Edge case: No child nodes
        Assert.assertFalse(NodeUtil.isForIn(createNode(Token.FOR)));
        
        // Edge case: Only one child node
        Assert.assertFalse(NodeUtil.isForIn(createNode(Token.FOR, createNode())));
        
        // Edge case: More than 3 child nodes
        Assert.assertFalse(NodeUtil.isForIn(createNode(Token.FOR, createNode(), createNode(), createNode(), createNode())));
    }

    @Test
    public void testIsStatement() {
        // Typical use case
        Assert.assertTrue(NodeUtil.isStatement(createNode(Token.BLOCK)));
        
        // Edge case: No parent node
        Assert.assertFalse(NodeUtil.isStatement(createNode()));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Typical use case
        Assert.assertTrue(NodeUtil.isFunctionDeclaration(createNode(Token.FUNCTION)));
        
        // Edge case: Not a statement
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(createNode(Token.FUNCTION, createNode())));
        
        // Edge case: Parent is not SCRIPT, BLOCK, or LABEL
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(createNode(Token.FUNCTION, createNode(), createNode(), createNode())));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical use case
        Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(createNode(Token.FUNCTION)));
        
        // Edge case: Not a function declaration
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(createNode(Token.SCRIPT)));
        
        // Edge case: Parent is not SCRIPT or FUNCTION
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(createNode(Token.FUNCTION, createNode())));
    }

    private Node createNode() {
        return new Node();
    }

    private Node createNode(int type) {
        Node node = new Node();
        node.setType(type);
        return node;
    }

    private Node createNode(Node... children) {
        Node node = new Node();
        for (Node child : children) {
            node.addChildToBack(child);
        }
        return node;
    }
}