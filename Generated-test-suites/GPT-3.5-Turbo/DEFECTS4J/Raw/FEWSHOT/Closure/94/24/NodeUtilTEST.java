import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(node));
        
        node.removeChild(node.getChildAtIndex(2));
        
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.EMPTY));
        
        parent.addChildToBack(node);
        
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.LABEL);
        parent.addChildToBack(node);
        
        assertTrue(NodeUtil.isStatement(node));
        
        parent.removeChild(parent.getFirstChild());
        parent.addChildToFront(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.VAR);
        node.addChildToBack(new Node(Token.FUNCTION));
        node.getFirstChild().addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(new Node(Token.EMPTY));
        parent.addChildToBack(new Node(Token.EMPTY));
        parent.addChildToBack(node);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent = new Node(Token.FUNCTION);
        parent.addChildToBack(new Node(Token.EMPTY));
        parent.addChildToBack(new Node(Token.EMPTY));
        parent.addChildToBack(new Node(Token.FUNCTION));
        parent.getLastChild().addChildToBack(node);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(new Node(Token.EMPTY));
        parent.addChildToBack(node);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}