import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NodeUtilTest {

    @Test
    void testIsForIn() {
        // Typical use case
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        Assertions.assertTrue(NodeUtil.isForIn(forInNode));

        // Edge case
        Node notForInNode = new Node(Token.FOR);
        notForInNode.addChildToFront(new Node(Token.EMPTY));
        Assertions.assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    void testIsStatement() {
        // Typical use case
        Node statementNode = new Node(Token.SCRIPT);
        statementNode.addChildToBack(new Node(Token.EMPTY));
        Assertions.assertTrue(NodeUtil.isStatement(statementNode));

        // Edge case
        Node notStatementNode = new Node(Token.INSTANCEOF);
        Assertions.assertFalse(NodeUtil.isStatement(notStatementNode));
    }

    @Test
    void testIsFunctionDeclaration() {
        // Typical use case
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToBack(new Node(Token.EMPTY));
        Assertions.assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        // Edge case
        Node notFunctionDeclarationNode = new Node(Token.STRING);
        Assertions.assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    @Test
    void testIsHoistedFunctionDeclaration() {
        // Typical use case
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.EMPTY));
        hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.EMPTY));
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(new Node(Token.EMPTY));
        parent.addChildToBack(hoistedFunctionDeclarationNode);
        Assertions.assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

        // Edge case
        Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        notHoistedFunctionDeclarationNode.addChildToBack(new Node(Token.EMPTY));
        Node notHoistedParent = new Node(Token.BLOCK);
        notHoistedParent.addChildToBack(notHoistedFunctionDeclarationNode);
        Assertions.assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }
}