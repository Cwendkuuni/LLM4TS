import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.BLOCK));
        node1.addChildToBack(new Node(Token.BLOCK));
        node1.addChildToBack(new Node(Token.BLOCK));
        assertTrue(NodeUtil.isForIn(node1));

        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.BLOCK));
        node2.addChildToBack(new Node(Token.BLOCK));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node1));

        Node node2 = new Node(Token.IF);
        node2.setParent(new Node(Token.BLOCK));
        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        node1.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        Node node2 = new Node(Token.FUNCTION);
        node2.setParent(new Node(Token.IF));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(node1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(node2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}