import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        // Typical Use Case
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        Node node3 = new Node(Token.SCRIPT);
        node1.addChildToBack(new Node(Token.SCRIPT));
        node1.addChildToBack(new Node(Token.SCRIPT));
        node1.addChildToBack(new Node(Token.SCRIPT));
        
        assertTrue(NodeUtil.isForIn(node1));
        
        // Edge Case
        assertFalse(NodeUtil.isForIn(node2));
        
        // Error Scenario
        assertFalse(NodeUtil.isForIn(node3));
    }
    
    @Test
    public void testIsStatement() {
        // Typical Use Case
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.FUNCTION);
        
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        
        // Edge Case
        assertFalse(NodeUtil.isStatement(node4));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        // Typical Use Case
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.SCRIPT);
        node1.addChildToFront(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        
        // Edge Case
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        
        // Error Scenario
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical Use Case
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        node1.addChildToFront(new Node(Token.NAME));
        node2.addChildToFront(new Node(Token.NAME));
        node1.setParent(parent1);
        node2.setParent(parent2);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        
        // Edge Case
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        
        // Error Scenario
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parent1));
    }
}