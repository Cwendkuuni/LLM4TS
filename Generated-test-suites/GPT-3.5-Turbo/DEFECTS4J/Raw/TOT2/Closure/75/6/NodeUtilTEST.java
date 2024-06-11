import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.TRUE), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.TRUE));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        scriptNode.addChildToBack(blockNode);
        Node labelNode = new Node(Token.LABEL);
        blockNode.addChildToBack(labelNode);
        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.EXPR_RESULT);
        node.setParent(parent);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.EXPR_RESULT);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node funcNode = new Node(Token.FUNCTION);
        scriptNode.addChildToFront(funcNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(funcNode));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node blockNode = new Node(Token.BLOCK);
        Node funcNode = new Node(Token.FUNCTION);
        blockNode.addChildToBack(funcNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(funcNode));
    }
}