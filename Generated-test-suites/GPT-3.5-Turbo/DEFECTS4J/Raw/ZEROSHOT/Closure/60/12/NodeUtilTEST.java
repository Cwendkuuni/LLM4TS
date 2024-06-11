import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        boolean result = NodeUtil.isForIn(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsForInReturnsTrue() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        boolean result = NodeUtil.isForIn(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        
        boolean result = NodeUtil.isStatement(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsStatementReturnsFalse() {
        Node node = new Node(Token.UNKNOWN);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        
        boolean result = NodeUtil.isStatement(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsFunctionDeclarationReturnsFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclarationReturnsFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.UNKNOWN);
        parent.addChildToBack(node);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        assertFalse(result);
    }
}