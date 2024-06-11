import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.NAME);
        Node child3 = new Node(Token.NAME);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        
        assertTrue(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.NAME);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatementReturnsTrue() {
        Node node = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsStatementReturnsFalse() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        parent1.addChildToBack(node);
        parent2.addChildToBack(parent1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}