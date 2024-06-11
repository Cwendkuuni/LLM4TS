import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node nodeStatement = new Node(Token.VAR);
        nodeStatement.setParent(parent);
        assertTrue(NodeUtil.isStatement(nodeStatement));

        Node nodeNotStatement = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isStatement(nodeNotStatement));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

        Node nodeNotFunctionDeclaration = new Node(Token.LET);
        assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node root = new Node(Token.SCRIPT);
        Node functionDeclaration = new Node(Token.FUNCTION);
        functionDeclaration.setParent(root);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));

        Node notHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
    }
}