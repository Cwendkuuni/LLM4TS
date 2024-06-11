import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_empty() {
        Node node = new Node(Token.FOR);

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_script() {
        Node node = new Node(Token.SCRIPT);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_block() {
        Node node = new Node(Token.BLOCK);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_label() {
        Node node = new Node(Token.LABEL);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_expression() {
        Node node = new Node(Token.EXPR_RESULT);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_expression() {
        Node node = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.NAME));
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notInGlobalScope() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.NAME));
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToBack(functionNode);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}