import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn_true() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsForIn_false() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsStatement_true() {
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        
        assertTrue(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsStatement_false() {
        Node n = new Node(Token.EMPTY);
        
        assertFalse(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsFunctionDeclaration_true() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsFunctionDeclaration_false() {
        Node n = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_true() {
        Node n = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(n);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(parent1);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_false() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(n);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}