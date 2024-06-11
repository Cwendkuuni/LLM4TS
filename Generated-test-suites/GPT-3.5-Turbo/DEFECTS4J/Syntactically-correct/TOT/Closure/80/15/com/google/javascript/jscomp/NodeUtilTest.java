import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.STRING);
        Node child3 = new Node(Token.TRUE);
        
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        
        boolean result = NodeUtil.isForIn(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsForIn_edge() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.STRING);
        Node child3 = new Node(Token.TRUE);
        
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        
        boolean result = NodeUtil.isForIn(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsForIn_error() {
        Node node = new Node(Token.NAME);
        
        boolean result = NodeUtil.isForIn(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsStatement_typical() {
        Node node = new Node(Token.BLOCK);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        
        boolean result = NodeUtil.isStatement(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_edge() {
        Node node = new Node(Token.LABEL);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        
        boolean result = NodeUtil.isStatement(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_error() {
        Node node = new Node(Token.NAME);
        
        boolean result = NodeUtil.isStatement(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_edge() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_error() {
        Node node = new Node(Token.NAME);
        
        boolean result = NodeUtil.isFunctionDeclaration(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        script.addChildToBack(parent);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_edge() {
        Node node = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        script.addChildToBack(node);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_error() {
        Node node = new Node(Token.FUNCTION);
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        
        assertFalse(result);
    }
}