import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    private Node node;

    @Before
    public void setUp() {
        node = new Node(Token.SCRIPT);
    }

    @Test
    public void testIsForIn_True() {
        node.setType(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_False() {
        node.setType(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_True() {
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_False() {
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_True() {
        node.setType(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_False() {
        node.setType(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_True() {
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(node);

        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(node);
        parent1.addChildToBack(parent2);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_False() {
        Node parent1 = new Node(Token.BLOCK);
        parent1.addChildToBack(node);

        Node parent2 = new Node(Token.SCRIPT);
        parent2.addChildToBack(node);
        parent1.addChildToBack(parent2);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}