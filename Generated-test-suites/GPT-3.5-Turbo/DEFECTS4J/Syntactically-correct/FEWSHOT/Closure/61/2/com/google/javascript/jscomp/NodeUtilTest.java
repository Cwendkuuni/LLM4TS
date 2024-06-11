import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n));
        
        n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n));
        
        n = new Node(Token.FUNCTION);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        Node parent = new Node(Token.FUNCTION);
        n.setParent(parent);
        assertTrue(NodeUtil.isStatement(n));
        
        n = new Node(Token.BLOCK);
        parent = new Node(Token.SCRIPT);
        n.setParent(parent);
        assertTrue(NodeUtil.isStatement(n));
        
        n = new Node(Token.EMPTY);
        parent = new Node(Token.LABEL);
        n.setParent(parent);
        assertTrue(NodeUtil.isStatement(n));
        
        n = new Node(Token.EMPTY);
        parent = new Node(Token.EXPR_RESULT);
        n.setParent(parent);
        assertFalse(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(n));
        
        n = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FUNCTION);
        
        Node n = new Node(Token.FUNCTION);
        n.setParent(scriptParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
        
        n = new Node(Token.FUNCTION);
        n.setParent(functionParent);
        functionParent.addChildToBack(n);
        Node grandParent = new Node(Token.SCRIPT);
        grandParent.addChildToBack(functionParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
        
        n = new Node(Token.FUNCTION);
        n.setParent(functionParent);
        functionParent.addChildToFront(new Node(Token.EMPTY));
        functionParent.addChildToBack(n);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
        
        n = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}