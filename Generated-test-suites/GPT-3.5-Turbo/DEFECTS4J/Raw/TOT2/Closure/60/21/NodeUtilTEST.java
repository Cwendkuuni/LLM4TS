import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parentScript = new Node(Token.SCRIPT);
        Node parentBlock = new Node(Token.BLOCK);
        Node parentLabel = new Node(Token.LABEL);

        Node node1 = new Node(Token.FUNCTION, parentScript);
        Node node2 = new Node(Token.FUNCTION, parentBlock);
        Node node3 = new Node(Token.FUNCTION, parentLabel);

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION, new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION, new Node(Token.SCRIPT));
        Node parentScript = new Node(Token.SCRIPT);
        parentScript.addChildToFront(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

}