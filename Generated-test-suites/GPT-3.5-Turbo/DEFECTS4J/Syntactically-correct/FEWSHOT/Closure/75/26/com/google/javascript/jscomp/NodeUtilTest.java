import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.CHILD1));
        node1.addChildToBack(new Node(Token.CHILD2));
        node1.addChildToBack(new Node(Token.CHILD3));
        assertTrue(NodeUtil.isForIn(node1));
        
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.CHILD1));
        node2.addChildToBack(new Node(Token.CHILD2));
        assertFalse(NodeUtil.isForIn(node2));
    }
    
    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        
        Node node4 = new Node(Token.CHILD);
        node1.addChildToBack(node4);
        assertFalse(NodeUtil.isStatement(node4));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        node1.setType(Token.FUNCTION);
        Node node2 = new Node(Token.CHILD);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        node1.setType(Token.FUNCTION);
        Node node2 = new Node(Token.CHILD);
        Node node3 = new Node(Token.SCRIPT);
        Node node4 = new Node(Token.FUNCTION);
        node3.addChildToBack(node4);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}