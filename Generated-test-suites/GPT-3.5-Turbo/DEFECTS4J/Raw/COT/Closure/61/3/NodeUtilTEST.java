
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import static org.junit.Assert.*;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        n.addChildToFront(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);
        
        assertTrue(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsNotForIn() {
        Node n = new Node(Token.EMPTY);
        
        assertFalse(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        
        assertTrue(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsNotStatement() {
        Node n = new Node(Token.EMPTY);
        
        assertFalse(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsNotFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        n.setParent(parent1);
        parent1.setParent(parent2);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
    
    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}