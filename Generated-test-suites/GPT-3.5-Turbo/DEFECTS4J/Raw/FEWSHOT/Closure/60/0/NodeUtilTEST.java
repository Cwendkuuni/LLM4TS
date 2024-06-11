import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.NAME));
        n.addChildToFront(new Node(Token.NAME));
        n.addChildToFront(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(n));

        n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.NAME));
        n.addChildToFront(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.BLOCK);
        n.addChildToFront(new Node(Token.NAME));

        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.SCRIPT);
        n.addChildToFront(new Node(Token.NAME));

        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.LABEL);
        n.addChildToFront(new Node(Token.NAME));

        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.NAME));

        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);

        assertTrue(NodeUtil.isFunctionDeclaration(n));

        parent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(n));

        parent = new Node(Token.SCRIPT);
        parent.addChildToFront(new Node(Token.FUNCTION));
        parent.addChildToFront(n);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

        parent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

        parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        grandParent.addChildToFront(parent);
        parent.addChildToFront(n);
        n.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}