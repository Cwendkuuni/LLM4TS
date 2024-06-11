import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn_True() {
        Node n = createForInNode();
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_False() {
        Node n = createNode(Token.FUNCTION, 2);
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement_True() {
        Node n = createNode(Token.SCRIPT, 1);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_False() {
        Node n = createNode(Token.VAR, 1);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration_True() {
        Node n = createNode(Token.FUNCTION, 1);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_False() {
        Node n = createNode(Token.VAR, 1);
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_True() {
        Node root = createNode(Token.SCRIPT, 1);
        Node function = createNode(Token.FUNCTION, 1);
        root.addChildToFront(function);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_False() {
        Node function = createNode(Token.FUNCTION, 2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
    }

    // Helper method to create Node with specified type and child count
    private Node createNode(int type, int childCount) {
        Node node = new Node(type);
        for (int i = 0; i < childCount; i++) {
            node.addChildToBack(new Node(Token.EMPTY));
        }
        return node;
    }

    // Helper method to create FOR-IN Node
    private Node createForInNode() {
        Node forNode = new Node(Token.FOR, 3);
        forNode.addChildToBack(new Node(Token.EMPTY));
        forNode.addChildToBack(new Node(Token.EMPTY));
        forNode.addChildToBack(new Node(Token.EMPTY));
        return forNode;
    }
}