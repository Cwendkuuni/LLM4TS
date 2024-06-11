import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node testNode = new Node(Token.FOR, new Node(Token.INIT), new Node(Token.COND), new Node(Token.BLOCK));
        assertTrue(NodeUtil.isForIn(testNode));
    }

    @Test
    public void testIsStatement() {
        Node testNode = new Node(Token.EXPR_RESULT);
        assertTrue(NodeUtil.isStatement(testNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node testNode = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(testNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        Node testNode = new Node(Token.FUNCTION, parent2, parent1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(testNode));
    }
}