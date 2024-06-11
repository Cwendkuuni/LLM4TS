
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    // Test isForIn method
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.IN));
        assertTrue(NodeUtil.isForIn(node1));
        
        Node node2 = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.IN), new Node(Token.TRUE));
        assertFalse(NodeUtil.isForIn(node2));
        
        Node node3 = new Node(Token.FUNCTION, new Node(Token.VAR), new Node(Token.IN));
        assertFalse(NodeUtil.isForIn(node3));
    }

    // Test isStatement method
    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node1));
        
        Node node2 = new Node(Token.EXPR_RESULT);
        assertFalse(NodeUtil.isStatement(node2));
        
        Node node3 = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node3));
    }

    // Test isFunctionDeclaration method
    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(node1));
        
        Node node2 = new Node(Token.EXPR_RESULT, new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        
        Node node3 = new Node(Token.FUNCTION, new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(node3));
    }

    // Test isHoistedFunctionDeclaration method
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        
        Node node2 = new Node(Token.EXPR_RESULT, new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        
        Node node3 = new Node(Token.FUNCTION, new Node(Token.NAME));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
        
        Node node4 = new Node(Token.SCRIPT, new Node(Token.FUNCTION, new Node(Token.NAME)));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node4));
        
        Node node5 = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.FUNCTION, new Node(Token.NAME))));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node5));
    }
}
