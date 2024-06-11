import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = buildForInNode();
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsNotForIn() {
        Node n = buildNonForInNode();
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = buildStatementNode();
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsNotStatement() {
        Node n = buildNonStatementNode();
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = buildFunctionDeclarationNode();
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node n = buildNonFunctionDeclarationNode();
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = buildHoistedFunctionDeclarationNode();
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node n = buildNonHoistedFunctionDeclarationNode();
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    private Node buildForInNode() {
        Node forNode = new Node(Token.FOR);
        forNode.addChildToFront(new Node(Token.EMPTY));
        forNode.addChildToFront(new Node(Token.EMPTY));
        forNode.addChildToFront(new Node(Token.EMPTY));
        return forNode;
    }

    private Node buildNonForInNode() {
        Node node = new Node(Token.EMPTY);
        return node;
    }

    private Node buildStatementNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToFront(new Node(Token.EMPTY));
        blockNode.addChildToFront(labelNode);
        scriptNode.addChildToFront(blockNode);
        return labelNode;
    }

    private Node buildNonStatementNode() {
        Node node = new Node(Token.EMPTY);
        return node;
    }

    private Node buildFunctionDeclarationNode() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(functionNode);
        return functionNode;
    }

    private Node buildNonFunctionDeclarationNode() {
        Node functionNode = new Node(Token.FUNCTION);
        return functionNode;
    }

    private Node buildHoistedFunctionDeclarationNode() {
        Node functionNode = buildFunctionDeclarationNode();
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(functionNode);
        Node functionParentNode = new Node(Token.FUNCTION);
        functionParentNode.addChildToFront(scriptNode);
        return functionNode;
    }

    private Node buildNonHoistedFunctionDeclarationNode() {
        Node functionNode = buildFunctionDeclarationNode();
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToFront(functionNode);
        return functionNode;
    }
}