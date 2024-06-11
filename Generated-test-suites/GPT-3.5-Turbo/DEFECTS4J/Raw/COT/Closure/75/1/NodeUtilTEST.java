
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.IF, new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.IF);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.IF);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.getParent().setType(Token.IF);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}

