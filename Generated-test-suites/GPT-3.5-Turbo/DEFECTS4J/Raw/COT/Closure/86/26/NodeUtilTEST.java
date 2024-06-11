import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NUMBER));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.STRING));

        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NUMBER));
        node.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        node.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.LABEL);
        node.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.GETELEM);
        node.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.NAME);
        node.addChildToBack(new Node(Token.NAME));
        
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.NAME));
        scriptNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}