import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node child1 = new Node(Token.FOR);
        Node child2 = new Node(Token.FOR);
        Node child3 = new Node(Token.FOR);
        node1.addChildToBack(child1);
        node1.addChildToBack(child2);
        node1.addChildToBack(child3);
        assertTrue(NodeUtil.isForIn(node1));

        Node node2 = new Node(Token.FOR);
        Node child4 = new Node(Token.FOR);
        Node child5 = new Node(Token.FOR);
        node2.addChildToBack(child4);
        node2.addChildToBack(child5);
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node child1 = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node1));

        Node node2 = new Node(Token.LABEL);
        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        Node node2 = new Node(Token.FOR);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        node1.addChildToBack(parent1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.FUNCTION);
        Node grandparent2 = new Node(Token.FUNCTION);
        node2.addChildToBack(parent2);
        parent2.addChildToBack(grandparent2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

        Node node3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.BLOCK);
        node3.addChildToBack(parent3);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }

}