import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.NAME);
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node nodeStatement = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(nodeStatement));

        Node nodeNotStatement = new Node(Token.NAME);
        assertFalse(NodeUtil.isStatement(nodeNotStatement));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
        nodeFunctionDeclaration = new Node(Token.SCRIPT, nodeFunctionDeclaration);
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

        Node nodeNotFunctionDeclaration = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.SCRIPT);
        parentNode.addChildToBack(nodeHoistedFunctionDeclaration);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

        Node nodeNotHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
    }
}