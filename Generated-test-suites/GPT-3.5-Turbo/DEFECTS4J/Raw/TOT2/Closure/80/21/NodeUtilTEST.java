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
        Node parent = new Node(Token.SCRIPT, new Node(Token.NAME));
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME));
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        parent.setParent(grandParent);
        node.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}