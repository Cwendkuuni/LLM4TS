import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(n));
        
        n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isForIn(n));
        
        n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        n.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(n));
        
        n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isStatement(n));
        
        n = new Node(Token.BLOCK);
        n.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(n));
        
        n = new Node(Token.LABEL);
        n.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(n));
        
        n = new Node(Token.NAME);
        n.getParent().addChildToFront(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
        
        n = new Node(Token.SCRIPT);
        n.addChildToBack(new Node(Token.FUNCTION));
        assertTrue(NodeUtil.isFunctionDeclaration(n.getFirstChild()));
        
        n = new Node(Token.FUNCTION);
        n.getParent().addChildToFront(new Node(Token.SCRIPT));
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.getParent().addChildToFront(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
        
        n = new Node(Token.SCRIPT);
        n.addChildToBack(new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n.getFirstChild()));
        
        n = new Node(Token.NAME);
        n.getParent().addChildToFront(new Node(Token.SCRIPT));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}