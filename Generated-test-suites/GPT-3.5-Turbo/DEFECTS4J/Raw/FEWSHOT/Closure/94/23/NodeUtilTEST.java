import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.BLOCK), new Node(Token.BLOCK), new Node(Token.BLOCK));

        assertFalse(NodeUtil.isForIn(node));
        
        node.addChildToFront(new Node(Token.FOR, new Node(Token.BLOCK), new Node(Token.BLOCK), new Node(Token.BLOCK)));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, new Node(Token.BLOCK), new Node(Token.BLOCK));

        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.BLOCK, new Node(Token.BLOCK), new Node(Token.BLOCK));

        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.LABEL, new Node(Token.BLOCK), new Node(Token.BLOCK));

        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.TEST, new Node(Token.BLOCK), new Node(Token.BLOCK));

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.BLOCK), new Node(Token.BLOCK));

        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.BLOCK, new Node(Token.BLOCK), new Node(Token.BLOCK));

        assertFalse(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT, new Node(Token.FUNCTION, new Node(Token.BLOCK), new Node(Token.BLOCK)));

        Node functionNode = scriptNode.getFirstChild();

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        scriptNode = new Node(Token.SCRIPT, new Node(Token.BLOCK, new Node(Token.FUNCTION, new Node(Token.BLOCK), new Node(Token.BLOCK))));

        functionNode = scriptNode.getFirstChild().getFirstChild();

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        scriptNode = new Node(Token.SCRIPT, new Node(Token.FUNCTION));

        functionNode = scriptNode.getFirstChild();

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}