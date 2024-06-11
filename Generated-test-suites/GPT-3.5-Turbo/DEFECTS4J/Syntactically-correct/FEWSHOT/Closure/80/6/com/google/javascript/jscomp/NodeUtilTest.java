import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node());
        n.addChildToBack(new Node());
        n.addChildToBack(new Node());
        assertTrue(NodeUtil.isForIn(n));

        n = new Node(Token.SCRIPT);
        n.addChildToBack(new Node());
        n.addChildToBack(new Node());
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.REGEXP);
        n.addChildToFront(new Node(Token.REGEXP));
        assertTrue(NodeUtil.isStatement(n));

        n = new Node();
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(n));

        Node parent = new Node(Token.SCRIPT);
        n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        parent = new Node(Token.BLOCK);
        n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        parent = new Node(Token.LABEL);
        n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        parent = new Node(Token.FUNCTION);
        n = new Node(Token.FUNCTION);
        parent.addChildToBack(new Node());
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        parent.addChildToBack(new Node());
        parent.addChildToBack(grandParent);
        grandParent.addChildToBack(new Node());
        Node n = new Node(Token.FUNCTION);
        grandParent.addChildToBack(n);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        parent = new Node(Token.BLOCK);
        parent = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(new Node());
        parent.addChildToBack(new Node());
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}