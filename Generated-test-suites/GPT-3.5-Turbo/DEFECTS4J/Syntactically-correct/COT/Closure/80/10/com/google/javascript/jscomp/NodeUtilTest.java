import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NodeUtilTest {

    @Test
    void testIsForIn() {
        Node forInNode = createForInNode();
        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    void testIsForIn_NotForInNode() {
        Node otherNode = createOtherNode();
        assertFalse(NodeUtil.isForIn(otherNode));
    }

    @Test
    void testIsStatement() {
        Node statementNode = createStatementNode();
        assertTrue(NodeUtil.isStatement(statementNode));
    }

    @Test
    void testIsStatement_NotStatementNode() {
        Node nonStatementNode = createNonStatementNode();
        assertFalse(NodeUtil.isStatement(nonStatementNode));
    }

    @Test
    void testIsFunctionDeclaration() {
        Node functionDeclarationNode = createFunctionDeclarationNode();
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    }

    @Test
    void testIsFunctionDeclaration_NotFunctionDeclarationNode() {
        Node nonFunctionDeclarationNode = createNonFunctionDeclarationNode();
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
    }

    @Test
    void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclarationNode = createHoistedFunctionDeclarationNode();
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    }

    @Test
    void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclarationNode() {
        Node nonHoistedFunctionDeclarationNode = createNonHoistedFunctionDeclarationNode();
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
    }

    // Helper methods for creating test nodes

    private Node createForInNode() {
        return new Node(Token.FOR, new Node[3]);
    }

    private Node createOtherNode() {
        return new Node(Token.IF, new Node[2]);
    }

    private Node createStatementNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToFront(new Node(Token.EXPR_RESULT));
        scriptNode.addChildToFront(blockNode);
        return blockNode.getFirstChild();
    }

    private Node createNonStatementNode() {
        return new Node(Token.ADD, new Node[2]);
    }

    private Node createFunctionDeclarationNode() {
        return new Node(Token.FUNCTION);
    }

    private Node createNonFunctionDeclarationNode() {
        return new Node(Token.ARROW_FUNCTION);
    }

    private Node createHoistedFunctionDeclarationNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(new Node(Token.FUNCTION));
        return scriptNode.getFirstChild();
    }

    private Node createNonHoistedFunctionDeclarationNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToFront(functionNode);
        scriptNode.addChildToFront(parentFunctionNode);
        return functionNode;
    }
}