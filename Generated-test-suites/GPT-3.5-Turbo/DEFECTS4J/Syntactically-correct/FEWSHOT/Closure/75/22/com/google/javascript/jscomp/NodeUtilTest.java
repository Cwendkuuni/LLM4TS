import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.NAME);
        Node child3 = new Node(Token.BLOCK);

        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        assertFalse(NodeUtil.isForIn(node));

        Node node2 = new Node(Token.FOR);
        Node child4 = new Node(Token.FOR);
        Node child5 = new Node(Token.NAME);
        Node child6 = new Node(Token.BLOCK);

        node2.addChildToBack(child4);
        node2.addChildToBack(child5);
        node2.addChildToBack(child6);

        assertFalse(NodeUtil.isForIn(node2));

        Node node3 = new Node(Token.FOR);
        Node child7 = new Node(Token.NAME);
        Node child8 = new Node(Token.NAME);
        Node child9 = new Node(Token.BLOCK);

        node3.addChildToBack(child7);
        node3.addChildToBack(child8);
        node3.addChildToBack(child9);

        assertTrue(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.NAME);

        node.addChildToBack(child1);
        node.addChildToBack(child2);

        assertTrue(NodeUtil.isStatement(child1));

        Node node2 = new Node(Token.BLOCK);
        Node child3 = new Node(Token.NAME);
        Node child4 = new Node(Token.NAME);

        node2.addChildToBack(child3);
        node2.addChildToBack(child4);

        assertTrue(NodeUtil.isStatement(child3));

        Node node3 = new Node(Token.LABEL);
        Node child5 = new Node(Token.NAME);
        Node child6 = new Node(Token.NAME);

        node3.addChildToBack(child5);
        node3.addChildToBack(child6);

        assertTrue(NodeUtil.isStatement(child5));

        Node node4 = new Node(Token.ASSIGN);
        Node child7 = new Node(Token.NAME);
        Node child8 = new Node(Token.NAME);

        node4.addChildToBack(child7);
        node4.addChildToBack(child8);

        assertFalse(NodeUtil.isStatement(child7));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node child = new Node(Token.NAME);

        node.addChildToBack(child);

        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node node2 = new Node(Token.FUNCTION);
        Node child2 = new Node(Token.NAME);
        Node parent2 = new Node(Token.SCRIPT);

        node2.addChildToBack(child2);
        parent2.addChildToBack(node2);

        assertTrue(NodeUtil.isFunctionDeclaration(node2));

        Node node3 = new Node(Token.FUNCTION);
        Node child3 = new Node(Token.NAME);
        Node parent3 = new Node(Token.NOT);

        node3.addChildToBack(child3);
        parent3.addChildToBack(node3);

        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node child = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);

        node.addChildToBack(child);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        Node node2 = new Node(Token.FUNCTION);
        Node child2 = new Node(Token.NAME);
        Node parent2 = new Node(Token.FUNCTION);

        node2.addChildToBack(child2);
        parent2.addChildToBack(node2);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

        Node node3 = new Node(Token.FUNCTION);
        Node child3 = new Node(Token.NAME);
        Node parent3 = new Node(Token.NOT);

        node3.addChildToBack(child3);
        parent3.addChildToBack(node3);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}