import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));

        Node incorrectNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(incorrectNode));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        node.setParent(parent);

        assertTrue(NodeUtil.isStatement(node));

        Node incorrectParent = new Node(Token.IF);
        node.setParent(incorrectParent);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node incorrectNode = new Node(Token.VAR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isFunctionDeclaration(incorrectNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        node.setParent(scriptParent);
        functionParent.addChild(node);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        Node incorrectNode = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        incorrectNode.setParent(scriptParent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(incorrectNode));
    }
}