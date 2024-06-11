import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(n));
      
        Node n2 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isStatement(n2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        Node n2 = new Node(Token.FOR);
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        Node n2 = new Node(Token.SCRIPT, new Node(Token.FUNCTION));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));
  
        Node n3 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));
    }
}