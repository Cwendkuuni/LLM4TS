import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    // Test cases for isForIn() method
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isForIn(node));
    }

    // Test cases for isStatement() method
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.EXPR_RESULT);
        assertFalse(NodeUtil.isStatement(node));
    }

    // Test cases for isFunctionDeclaration() method
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(node);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    // Test cases for isHoistedFunctionDeclaration() method
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

}