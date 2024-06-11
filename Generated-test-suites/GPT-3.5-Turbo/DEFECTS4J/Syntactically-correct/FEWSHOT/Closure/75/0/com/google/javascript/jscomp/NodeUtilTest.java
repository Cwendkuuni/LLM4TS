import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        Node node3 = new Node(Token.FOR);

        node3.addChildToBack(new Node(Token.FOR));

        assertFalse(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.EXPR_RESULT);

        assertFalse(NodeUtil.isStatement(node1));
        assertFalse(NodeUtil.isStatement(node2));
        assertFalse(NodeUtil.isStatement(node3));
        assertTrue(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.EMPTY);
        Node node3 = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        assertTrue(NodeUtil.isFunctionDeclaration(node3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.FUNCTION);
        Node node4 = new Node(Token.FUNCTION);

        node4.setParent(new Node(Token.SCRIPT));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node4));
    }
}