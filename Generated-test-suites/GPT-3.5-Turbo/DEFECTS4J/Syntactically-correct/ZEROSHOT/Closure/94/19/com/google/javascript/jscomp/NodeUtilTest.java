import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
        
        node = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(node));
        
        node = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.LABEL);
        node = new Node(Token.EMPTY);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.EMPTY);
        node = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent1);
        parent1.addChildToBack(parent2);
        parent2.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent1 = new Node(Token.LABEL);
        parent2 = new Node(Token.FUNCTION);
        node = new Node(Token.FUNCTION);
        node.setParent(parent1);
        parent1.addChildToBack(parent2);
        parent2.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent1 = new Node(Token.SCRIPT);
        parent2 = new Node(Token.SCRIPT);
        node = new Node(Token.FUNCTION);
        node.setParent(parent1);
        parent1.addChildToBack(parent2);
        parent2.addChildToBack(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
        
        node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
        
        node = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}