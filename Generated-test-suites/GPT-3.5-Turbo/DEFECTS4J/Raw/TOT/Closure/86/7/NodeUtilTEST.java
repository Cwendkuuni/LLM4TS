import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_edge() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_typical() {
        Node node = new Node(Token.EMPTY);
        node.setParent(new Node(Token.SCRIPT));

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_edge() {
        Node node = new Node(Token.EMPTY);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        node.setParent(new Node(Token.SCRIPT));
        node.setType(Token.STRING);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_edge() {
        Node node = new Node(Token.STRING);
        node.setType(Token.FUNCTION);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        node.setParent(new Node(Token.SCRIPT));
        node.setType(Token.STRING);

        Node parent = new Node(Token.FUNCTION);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_edge() {
        Node node = new Node(Token.STRING);
        node.setType(Token.FUNCTION);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}