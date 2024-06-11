import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.TRUE));
        assertTrue(NodeUtil.isForIn(node));

        Node node2 = new Node(Token.WHILE, new Node(Token.VAR), new Node(Token.TRUE));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.LABEL, new Node(Token.VAR));
        assertTrue(NodeUtil.isStatement(node));

        Node node2 = new Node(Token.FUNCTION, new Node(Token.VAR));
        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.VAR));
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node node2 = new Node(Token.LABEL, new Node(Token.VAR));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT, new Node(Token.FUNCTION, new Node(Token.VAR)));
        Node node = new Node(Token.FUNCTION, new Node(Token.VAR), parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        Node parent2 = new Node(Token.SCRIPT, new Node(Token.VAR));
        Node node2 = new Node(Token.FUNCTION, new Node(Token.VAR), parent2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}