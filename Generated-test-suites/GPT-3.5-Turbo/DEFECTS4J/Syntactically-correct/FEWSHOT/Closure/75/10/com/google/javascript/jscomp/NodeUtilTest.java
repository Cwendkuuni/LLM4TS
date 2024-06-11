import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NUMBER));
        node.addChildToBack(new Node(Token.NAME));
        
        assertEquals(true, NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node child = new Node(Token.NAME);
        node.addChildToBack(child);
        
        assertEquals(true, NodeUtil.isStatement(child));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        
        assertEquals(true, NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
    }
}