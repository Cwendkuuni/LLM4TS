import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
        
        Node invalidNode = new Node(Token.WHILE, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(invalidNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        
        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        
        Node invalidNode = new Node(Token.IF);
        assertFalse(NodeUtil.isStatement(invalidNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node invalidNode = new Node(Token.VAR);
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isFunctionDeclaration(invalidNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(scriptNode);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        Node invalidNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(invalidNode));
    }
}