import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(node));

        node.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.BLOCK);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.LABEL);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.FUNCTION);
        node.setParent(parent);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.FUNCTION);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(node));

        parent = new Node(Token.SCRIPT);
        node.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.FUNCTION);
        Node grandParent = new Node(Token.SCRIPT);
        grandParent.addChildToBack(parent);
        node.setParent(parent);
        parent.setParent(grandParent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.BLOCK);
        node.setParent(parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}