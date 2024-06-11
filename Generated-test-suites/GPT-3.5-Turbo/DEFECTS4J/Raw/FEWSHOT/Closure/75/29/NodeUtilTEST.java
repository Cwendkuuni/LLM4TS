import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.FOR);
        Node child2 = new Node(Token.FOR);
        Node child3 = new Node(Token.FOR);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        
        assertEquals(true, NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        
        assertEquals(true, NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        
        assertEquals(true, NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        node.setParent(parent);
        parent.setParent(grandParent);
        
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
    }
}