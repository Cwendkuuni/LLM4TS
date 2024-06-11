
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.VAR));
        node.addChildToBack(new Node(Token.TRUE));
        node.addChildToBack(new Node(Token.NULL));
        
        boolean result = NodeUtil.isForIn(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        
        boolean result = NodeUtil.isStatement(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        assertFalse(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        assertTrue(result);
    }
}
