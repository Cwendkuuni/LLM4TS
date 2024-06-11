import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node expressionNode = new Node(Token.EXPR_RESULT);

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertTrue(NodeUtil.isStatement(expressionNode));

        Node parent = new Node(Token.NAME);
        expressionNode.setParent(parent);

        assertFalse(NodeUtil.isStatement(expressionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setIsAddedBlock(true);

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

        Node parent = new Node(Token.SCRIPT);
        functionNode.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

        parent = new Node(Token.LABEL);
        functionNode.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

        parent = new Node(Token.NAME);
        functionNode.setParent(parent);

        assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setIsAddedBlock(true);

        Node parent = new Node(Token.SCRIPT);
        functionNode.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        parent = new Node(Token.FUNCTION);
        Node grandParent = new Node(Token.SCRIPT);
        functionNode.setParent(parent);
        parent.setParent(grandParent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        parent = new Node(Token.NAME);
        functionNode.setParent(parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}