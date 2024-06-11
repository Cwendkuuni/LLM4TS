import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test case 1
        Node n1 = new Node(Token.FOR);
        n1.addChildToBack(new Node());
        n1.addChildToBack(new Node());
        n1.addChildToBack(new Node());
        assertFalse(NodeUtil.isForIn(n1));

        // Test case 2
        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node());
        n2.addChildToBack(new Node());
        assertTrue(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        // Test case 1
        Node n1 = new Node();
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(n1);
        n1.setParent(parent1);
        assertTrue(NodeUtil.isStatement(n1));

        // Test case 2
        Node n2 = new Node();
        Node parent2 = new Node(Token.BLOCK);
        parent2.addChildToBack(n2);
        n2.setParent(parent2);
        assertTrue(NodeUtil.isStatement(n2));

        // Test case 3
        Node n3 = new Node();
        Node parent3 = new Node(Token.LABEL);
        parent3.addChildToBack(n3);
        n3.setParent(parent3);
        assertTrue(NodeUtil.isStatement(n3));

        // Test case 4
        Node n4 = new Node();
        assertFalse(NodeUtil.isStatement(n4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test case 1
        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(n1);
        n1.setParent(parent1);
        assertTrue(NodeUtil.isFunctionDeclaration(n1));

        // Test case 2
        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.BLOCK);
        parent2.addChildToBack(n2);
        n2.setParent(parent2);
        assertTrue(NodeUtil.isFunctionDeclaration(n2));

        // Test case 3
        Node n3 = new Node(Token.IF);
        assertFalse(NodeUtil.isFunctionDeclaration(n3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test case 1
        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(n1);
        n1.setParent(parent1);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n1));

        // Test case 2
        Node n2 = new Node(Token.FUNCTION);
        Node grandParent2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.SCRIPT);
        grandParent2.addChildToBack(parent2);
        parent2.addChildToBack(n2);
        n2.setParent(parent2);
        parent2.setParent(grandParent2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));

        // Test case 3
        Node n3 = new Node(Token.IF);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));
    }
}