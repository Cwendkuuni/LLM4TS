import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.NAME, "test")), new Node(Token.BLOCK));
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        Node statementNode = new Node(Token.BLOCK, new Node(Token.EMPTY));
        scriptNode.addChildToFront(statementNode);
        assertTrue(NodeUtil.isStatement(statementNode));

        Node expressionNode = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.NAME, "test")), new Node(Token.BLOCK));
        assertFalse(NodeUtil.isStatement(expressionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.NAME, "test")), new Node(Token.BLOCK));
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

        Node notFunctionDeclarationNode = new Node(Token.ADD, new Node(Token.NAME, new Node(Token.NAME, "test")), new Node(Token.STRING, new Node(Token.STRING, "test")));
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionNode = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.NAME, "test")), new Node(Token.BLOCK));
        Node scriptNode = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        scriptNode.addChildToFront(hoistedFunctionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));

        Node notHoistedFunctionNode = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.NAME, "test")), new Node(Token.BLOCK));
        Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.NAME, "test")), new Node(Token.BLOCK));
        functionNode.addChildToFront(notHoistedFunctionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));
    }
}