import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    // Test isForIn() method
    @Test
    public void testIsForIn() {
        // Test when node represents a FOR-IN loop
        Node forInNode = new Node(Token.FOR);
        Node conditionNode = new Node(Token.STRING);
        Node bodyNode = new Node(Token.BLOCK);
        forInNode.addChildToBack(conditionNode);
        forInNode.addChildToBack(new Node(Token.TRUE));
        forInNode.addChildToBack(bodyNode);
        assertTrue(NodeUtil.isForIn(forInNode));

        // Test when node does not represent a FOR-IN loop
        Node notForInNode = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    // Test isStatement() method
    @Test
    public void testIsStatement() {
        // Test when node is used as a statement
        Node statementNode = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(statementNode));

        // Test when node is not used as a statement
        Node notStatementNode = new Node(Token.EXPR_RESULT);
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }

    // Test isFunctionDeclaration() method
    @Test
    public void testIsFunctionDeclaration() {
        // Test when node is a function declaration
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parentBlockNode = new Node(Token.BLOCK);
        Node parentScriptNode = new Node(Token.SCRIPT);
        parentScriptNode.addChildToBack(parentBlockNode);
        parentBlockNode.addChildToBack(functionDeclarationNode);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        // Test when node is not a function declaration
        Node notFunctionDeclarationNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    // Test isHoistedFunctionDeclaration() method
    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test when node is a hoisted function declaration
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        parentScriptNode.addChildToBack(hoistedFunctionDeclarationNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

        // Test when node is not a hoisted function declaration
        Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }

    // Additional edge cases and exception handling scenarios can be tested here

}