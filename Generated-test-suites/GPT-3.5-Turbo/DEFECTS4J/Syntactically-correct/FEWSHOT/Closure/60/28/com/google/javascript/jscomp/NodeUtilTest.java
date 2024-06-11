import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToFront(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(node));
        
        node.removeChild(node.getFirstChild());
        
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.NAME);
        node.addChildToFront(new Node(Token.NAME));
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node func = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        script.addChildToFront(func);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(func));
        
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToFront(func);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(func));
        
        Node grandparent = new Node(Token.BLOCK);
        grandparent.addChildToFront(parent);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(func));
    }

}