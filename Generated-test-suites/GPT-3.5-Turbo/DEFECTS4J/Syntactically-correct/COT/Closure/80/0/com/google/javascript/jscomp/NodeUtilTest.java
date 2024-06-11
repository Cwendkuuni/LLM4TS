import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node n = createNode(Token.FOR, createNode(), createNode());
        assertTrue(NodeUtil.isForIn(n));
        
        Node n2 = createNode(Token.WHILE, createNode(), createNode());
        assertFalse(NodeUtil.isForIn(n2));
    }
    
    @Test
    public void testIsStatement() {
        Node n = createNode(Token.LABEL, createNode());
        assertTrue(NodeUtil.isStatement(n));
        
        Node n2 = createNode(Token.ADD, createNode(), createNode());
        assertFalse(NodeUtil.isStatement(n2));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node n = createNode(Token.FUNCTION, createNode());
        assertTrue(NodeUtil.isFunctionDeclaration(n));
        
        Node n2 = createNode(Token.LET, createNode());
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = createNode(Token.FUNCTION, createNode());
        n.setParent(createNode());
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
        
        Node n2 = createNode(Token.FUNCTION, createNode());
        Node parent = createNode();
        parent.setParent(createNode());
        n2.setParent(parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
    }
    
    // Helper method to create a Node with the given type and children
    private Node createNode(int type, Node... children) {
        Node node = new Node(type);
        for (Node child : children) {
            node.addChildToBack(child);
        }
        return node;
    }
}