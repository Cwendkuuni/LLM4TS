import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.EMPTY);

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsNotStatement() {
        Node parent = new Node(Token.EMPTY);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node parent = new Node(Token.EMPTY);
        Node node = new Node(Token.FUNCTION);
        node.setParent(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.SCRIPT);

        parentNode.addChildToBack(script);
        script.addChildToBack(function);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.EMPTY);

        parentNode.addChildToBack(script);
        script.addChildToBack(function);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
    }
}