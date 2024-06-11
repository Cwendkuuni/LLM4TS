
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY, new Node(Token.EMPTY), null));
        node.addChildToBack(new Node(Token.EMPTY, new Node(Token.EMPTY), null));
        node.addChildToBack(new Node(Token.EMPTY, new Node(Token.EMPTY), null));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.EMPTY);

        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);

        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);

        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}
