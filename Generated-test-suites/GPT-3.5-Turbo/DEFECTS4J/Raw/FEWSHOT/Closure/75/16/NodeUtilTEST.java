import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(n));
        
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        
        n.addChildToFront(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);
        
        assertTrue(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        
        assertFalse(NodeUtil.isStatement(n));
        
        Node block = new Node(Token.BLOCK);
        block.addChildToFront(n);
        
        assertTrue(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isFunctionDeclaration(n));
        
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
        
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
        
        Node grandparent = new Node(Token.FUNCTION);
        grandparent.addChildToFront(parent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}