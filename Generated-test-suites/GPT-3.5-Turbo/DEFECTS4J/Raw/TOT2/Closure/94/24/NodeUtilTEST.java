import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.BLOCK));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.BLOCK));
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.BLOCK));
        grandparent.addChildToFront(node);
        parent.addChildToFront(grandparent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}