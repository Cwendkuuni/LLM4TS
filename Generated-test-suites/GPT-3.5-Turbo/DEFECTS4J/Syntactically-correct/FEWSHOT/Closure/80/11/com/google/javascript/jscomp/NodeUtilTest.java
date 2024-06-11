import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR, new Node(), new Node(), new Node());
        Node node2 = new Node(Token.FOR, new Node(), new Node());
        
        assertTrue(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT, new Node());
        Node node2 = new Node(Token.BLOCK, new Node());
        Node node3 = new Node(Token.LABEL, new Node());
        Node node4 = new Node(Token.STRING, new Node());
        
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node());
        Node node2 = new Node(Token.VAR, new Node());
        Node node3 = new Node(Token.FUNCTION, new Node());
        Node node4 = new Node(Token.STRING, new Node());
        
        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        assertTrue(NodeUtil.isFunctionDeclaration(node3));
        assertFalse(NodeUtil.isFunctionDeclaration(node4));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node());
        Node parent1 = new Node(Token.SCRIPT, node1);
        Node node2 = new Node(Token.FUNCTION, new Node());
        Node parent2 = new Node(Token.FUNCTION, node2);
        Node node3 = new Node(Token.FUNCTION, new Node());
        Node parent3 = new Node(Token.SCRIPT);
        Node grandparent3 = new Node(Token.FUNCTION, parent3);
        Node node4 = new Node(Token.STRING, new Node());
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
    }
}