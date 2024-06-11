
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typicalCase() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node());
        node.addChildToFront(new Node());
        node.addChildToFront(new Node());
        
        assertTrue(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsForIn_emptyNode() {
        Node node = new Node(Token.FOR);
        
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement_scriptParent() {
        Node node = new Node();
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsStatement_blockParent() {
        Node node = new Node();
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsStatement_labelParent() {
        Node node = new Node();
        Node parent = new Node(Token.LABEL);
        parent.addChildToFront(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsStatement_nonStatementParent() {
        Node node = new Node();
        Node parent = new Node(Token.TYPEOF);
        parent.addChildToFront(node);
        node.setParent(parent);
        
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration_functionNode() {
        Node node = new Node(Token.FUNCTION);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsFunctionDeclaration_nonFunctionNode() {
        Node node = new Node(Token.IF);
        
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_scriptParent() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_functionParent() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToFront(new Node());
        parent.addChildToFront(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_nonHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(node);
        node.setParent(parent);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}
