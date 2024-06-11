import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node();
        Node child2 = new Node();
        Node child3 = new Node();
        node.addChildToFront(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        
        boolean result = NodeUtil.isForIn(node);
        
        assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node();
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);

        boolean result = NodeUtil.isStatement(node);

        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_noParent() {
        Node node = new Node();
        
        boolean result = NodeUtil.isStatement(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_notStatement() {
        Node node = new Node(Token.FUNCTION);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        node.setParent(parent);
        parent.setParent(grandParent);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        assertFalse(result);
    }
}