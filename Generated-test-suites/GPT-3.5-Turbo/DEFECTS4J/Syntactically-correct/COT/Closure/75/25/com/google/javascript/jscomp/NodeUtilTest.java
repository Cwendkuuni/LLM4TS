
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        boolean result = NodeUtil.isForIn(node);

        assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.NAME);
        node.setParent(new Node(Token.SCRIPT));

        boolean result = NodeUtil.isStatement(node);

        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        node.setParent(new Node(Token.SCRIPT));

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        node.setParent(new Node(Token.SCRIPT));

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertTrue(result);
    }
}
