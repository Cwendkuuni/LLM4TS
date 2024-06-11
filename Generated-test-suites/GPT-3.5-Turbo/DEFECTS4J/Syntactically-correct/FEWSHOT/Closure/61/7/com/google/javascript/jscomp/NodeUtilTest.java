import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        assertTrue(NodeUtil.isForIn(createNode(Token.FOR, createChildNodes(3))));
        assertFalse(NodeUtil.isForIn(createNode(Token.FOR, createChildNodes(2))));
        assertFalse(NodeUtil.isForIn(createNode(Token.FUNCTION, createChildNodes(3))));
    }
    
    @Test
    public void testIsStatement() {
        assertTrue(NodeUtil.isStatement(createNode(Token.SCRIPT)));
        assertTrue(NodeUtil.isStatement(createNode(Token.BLOCK)));
        assertTrue(NodeUtil.isStatement(createNode(Token.LABEL)));
        assertFalse(NodeUtil.isStatement(createNode(Token.FUNCTION)));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        assertTrue(NodeUtil.isFunctionDeclaration(createNode(Token.FUNCTION)));
        assertFalse(NodeUtil.isFunctionDeclaration(createNode(Token.VAR)));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(createNode(Token.FUNCTION, createParentNode(Token.SCRIPT))));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(createNode(Token.FUNCTION, createParentNode(createParentNode(Token.FUNCTION)))));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(createNode(Token.FUNCTION, createParentNode(Token.LET))));
    }
    
    // Helper methods for creating test nodes
    private Node createNode(int type) {
        Node node = new Node(type);
        return node;
    }
    
    private Node createNode(int type, Node parent) {
        Node node = new Node(type);
        node.addChildToBack(parent);
        return node;
    }
    
    private Node createNode(int type, Node[] children) {
        Node node = new Node(type);
        for (Node child : children) {
            node.addChildToBack(child);
        }
        return node;
    }
    
    private Node createParentNode(int type) {
        Node parent = new Node(type);
        return parent;
    }
    
    private Node[] createChildNodes(int count) {
        Node[] children = new Node[count];
        for (int i = 0; i < count; i++) {
            children[i] = new Node(Token.EMPTY);
        }
        return children;
    }
}