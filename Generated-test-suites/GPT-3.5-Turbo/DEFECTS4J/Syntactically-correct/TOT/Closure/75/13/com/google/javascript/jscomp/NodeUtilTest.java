import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        // Typical use case: the node represents a FOR-IN loop
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.VAR));
        forInNode.addChildToBack(new Node(Token.VAR));
        forInNode.addChildToBack(new Node(Token.VAR));

        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsForIn_edge() {
        // Edge case: the node does not represent a FOR-IN loop
        Node notForInNode = new Node(Token.IF);
        notForInNode.addChildToBack(new Node(Token.TRUE));
        notForInNode.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement_typical() {
        // Typical use case: the node is used as a statement
        Node statementNode = new Node(Token.SCRIPT);
        statementNode.addChildToBack(new Node(Token.VAR));
        statementNode.addChildToBack(new Node(Token.FOR));
        statementNode.addChildToBack(new Node(Token.IF));

        assertTrue(NodeUtil.isStatement(statementNode));
    }

    @Test
    public void testIsStatement_edge() {
        // Edge case: the node is not used as a statement
        Node notStatementNode = new Node(Token.FUNCTION);
        notStatementNode.addChildToFront(new Node(Token.SCRIPT));
        notStatementNode.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isStatement(notStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration_typical() {
        // Typical use case: the node is a function declaration
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToBack(new Node(Token.NAME));
        functionDeclarationNode.addChildToBack(new Node(Token.PARAM_LIST));
        functionDeclarationNode.addChildToBack(new Node(Token.BLOCK));

        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    }

    @Test
    public void testIsFunctionDeclaration_edge() {
        // Edge case: the node is not a function declaration
        Node notFunctionDeclarationNode = new Node(Token.FUNCTION);
        notFunctionDeclarationNode.addChildToFront(new Node(Token.SCRIPT));
        notFunctionDeclarationNode.addChildToBack(new Node(Token.BLOCK));

        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        // Typical use case: the node is a hoisted function declaration
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));
        hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.PARAM_LIST));
        hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.BLOCK));
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(hoistedFunctionDeclarationNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_edge() {
        // Edge case: the node is not a hoisted function declaration
        Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        notHoistedFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));
        notHoistedFunctionDeclarationNode.addChildToBack(new Node(Token.PARAM_LIST));
        notHoistedFunctionDeclarationNode.addChildToBack(new Node(Token.BLOCK));
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(notHoistedFunctionDeclarationNode);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionNode);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }

}