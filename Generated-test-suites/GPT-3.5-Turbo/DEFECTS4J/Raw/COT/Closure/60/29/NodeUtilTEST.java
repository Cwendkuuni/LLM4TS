import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        
        boolean result = NodeUtil.isForIn(n);
        
        assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        Node parent = new Node(Token.EMPTY);
        parent.addChildToFront(n);
        
        boolean result = NodeUtil.isStatement(n);
        
        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        
        boolean result = NodeUtil.isFunctionDeclaration(n);
        
        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.SCRIPT);
        grandparent.addChildToFront(parent);
        parent.addChildToFront(n);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
        
        assertFalse(result);
    }

}