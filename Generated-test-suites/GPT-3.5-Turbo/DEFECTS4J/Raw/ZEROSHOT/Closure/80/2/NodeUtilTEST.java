import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsForIn_NotForIn() {
        Node node = new Node(Token.EMPTY);
        
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsStatement_NotStatement() {
        Node node = new Node(Token.VAR);
        
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsFunctionDeclaration_NotFunctionDeclaration() {
        Node node = new Node(Token.EMPTY);
        
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        parent.addChildToFront(node);
        node.setParent(parent);
        grandParent.addChildToFront(parent);
        parent.setParent(grandParent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        node.setParent(parent);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}