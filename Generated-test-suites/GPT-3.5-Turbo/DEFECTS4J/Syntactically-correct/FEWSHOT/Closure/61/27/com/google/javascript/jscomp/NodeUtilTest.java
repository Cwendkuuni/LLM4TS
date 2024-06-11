import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node));
        
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node2));
    }
    
    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(scriptNode));
        
        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(blockNode));

        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(labelNode));
        
        Node node = new Node(Token.ADD);
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        
        Node node = new Node(Token.ADD);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        Node functionNode2 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
    }
}