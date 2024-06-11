import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_Typical() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.LP));
        node.addChildToBack(new Node(Token.RP));
        node.addChildToBack(new Node(Token.SCRIPT));
        
        assertTrue(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsForIn_Edge() {
        Node node = new Node(Token.FOR);
        
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement_Typical() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsStatement_Edge() {
        Node node = new Node(Token.NAME);
        
        try {
            NodeUtil.isStatement(node);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
    
    @Test
    public void testIsFunctionDeclaration_Typical() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsFunctionDeclaration_Edge() {
        Node node = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_Typical() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(node);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(parent1);
        node.setParent(parent1);
        parent1.setParent(parent2);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_Edge1() {
        Node node = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_Edge2() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
}