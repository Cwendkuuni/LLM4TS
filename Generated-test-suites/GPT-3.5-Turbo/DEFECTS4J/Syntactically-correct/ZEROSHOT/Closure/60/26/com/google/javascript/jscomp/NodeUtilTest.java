import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));

        boolean result = NodeUtil.isForIn(node);

        assertTrue(result);
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.IF);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));

        boolean result = NodeUtil.isForIn(node);

        assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        node.addChildToFront(new Node(Token.EMPTY));

        boolean result = NodeUtil.isStatement(node);

        assertTrue(result);
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.EMPTY));

        boolean result = NodeUtil.isStatement(node);

        assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.EMPTY));

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.IF);
        node.addChildToFront(new Node(Token.EMPTY));

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node parent = new Node(Token.IF);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertFalse(result);
    }
}