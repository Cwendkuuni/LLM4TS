import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.FOR);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(new Node());
        assertTrue(NodeUtil.isStatement(parent));

        parent = new Node(Token.BLOCK);
        parent.addChildToBack(new Node());
        assertTrue(NodeUtil.isStatement(parent));

        parent = new Node(Token.LABEL);
        parent.addChildToBack(new Node());
        assertTrue(NodeUtil.isStatement(parent));

        parent = new Node(Token.DOT);
        parent.addChildToBack(new Node());
        assertFalse(NodeUtil.isStatement(parent));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.ADD);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.FUNCTION);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}