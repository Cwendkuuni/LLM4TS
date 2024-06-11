import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node));
        
        Node node2 = new Node(Token.WHILE, new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT, new Node());
        Node parentNode = new Node(Token.SCRIPT, scriptNode);
        
        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(parentNode));
        
        Node blockNode = new Node(Token.BLOCK, new Node());
        assertTrue(NodeUtil.isStatement(blockNode));
        
        Node labelNode = new Node(Token.LABEL, new Node());
        assertTrue(NodeUtil.isStatement(labelNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION, new Node(), new Node());
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        
        Node variableNode = new Node(Token.VAR, new Node());
        assertFalse(NodeUtil.isFunctionDeclaration(variableNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT, new Node());
        Node functionNode = new Node(Token.FUNCTION, new Node(), new Node());
        scriptNode.addChildToFront(functionNode);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}