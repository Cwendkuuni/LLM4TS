import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_True() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));

        boolean result = NodeUtil.isForIn(n);

        assertTrue(result);
    }

    @Test
    public void testIsForIn_False() {
        Node n = new Node(Token.IF);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));

        boolean result = NodeUtil.isForIn(n);

        assertFalse(result);
    }

    @Test
    public void testIsStatement_True() {
        Node n = new Node(Token.EXP_RESULT);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        boolean result = NodeUtil.isStatement(n);

        assertTrue(result);
    }

    @Test
    public void testIsStatement_False() {
        Node n = new Node(Token.EXP_RESULT);
        Node parent = new Node(Token.IF);
        parent.addChildToBack(n);
        n.setParent(parent);

        boolean result = NodeUtil.isStatement(n);

        assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_True() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_False() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.IF);
        parent.addChildToBack(n);
        n.setParent(parent);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_True() {
        Node n = new Node(Token.FUNCTION);
        Node grandParent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.BLOCK);
        grandParent.addChildToBack(parent);
        parent.addChildToBack(n);
        n.setParent(parent);
        parent.setParent(grandParent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_False() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        assertFalse(result);
    }
}