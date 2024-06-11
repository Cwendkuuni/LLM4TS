import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {
  
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        Node node3 = new Node(Token.WHILE);
      
        assertEquals(false, NodeUtil.isForIn(node1));
        assertEquals(true, NodeUtil.isForIn(node2));
        assertEquals(false, NodeUtil.isForIn(node3));
    }
  
    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.RETURN);
      
        assertEquals(true, NodeUtil.isStatement(node1));
        assertEquals(true, NodeUtil.isStatement(node2));
        assertEquals(true, NodeUtil.isStatement(node3));
        assertEquals(false, NodeUtil.isStatement(node4));
    }
  
    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.ASSIGN);
        Node node3 = new Node(Token.IF);
      
        assertEquals(true, NodeUtil.isFunctionDeclaration(node1));
        assertEquals(false, NodeUtil.isFunctionDeclaration(node2));
        assertEquals(false, NodeUtil.isFunctionDeclaration(node3));
    }
  
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToFront(node1); // Node is at top level
        functionNode.addChildToFront(node1); // Node is within a function
      
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node1));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(scriptNode));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}