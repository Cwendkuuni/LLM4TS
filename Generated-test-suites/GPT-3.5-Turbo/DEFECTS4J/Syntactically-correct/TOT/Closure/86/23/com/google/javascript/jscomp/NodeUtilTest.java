import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        Node childOne = new Node(Token.NAME);
        Node childTwo = new Node(Token.NUMBER);
        Node childThree = new Node(Token.STRING);

        // typical use case
        forInNode.addChildToBack(childOne);
        forInNode.addChildToBack(childTwo);
        forInNode.addChildToBack(childThree);
        assertTrue(NodeUtil.isForIn(forInNode));

        // edge case: empty node
        assertFalse(NodeUtil.isForIn(new Node(Token.FOR)));

        // error scenario: invalid child count
        Node invalidChildCountNode = new Node(Token.FOR);
        invalidChildCountNode.addChildToBack(childOne);
        invalidChildCountNode.addChildToBack(childTwo);
        assertFalse(NodeUtil.isForIn(invalidChildCountNode));

        // error scenario: invalid token type
        Node invalidTokenTypeNode = new Node(Token.WHILE);
        invalidTokenTypeNode.addChildToBack(childOne);
        invalidTokenTypeNode.addChildToBack(childTwo);
        invalidTokenTypeNode.addChildToBack(childThree);
        assertFalse(NodeUtil.isForIn(invalidTokenTypeNode));
    }

    @Test
    public void testIsStatement() {
        Node statementNode = new Node(Token.SCRIPT);
        Node childOne = new Node(Token.NAME);
        statementNode.addChildToBack(childOne);

        // typical use case
        assertTrue(NodeUtil.isStatement(childOne));

        // edge case: empty node
        assertFalse(NodeUtil.isStatement(new Node(Token.NAME)));

        // error scenario: null parent
        Node parentIsNullNode = new Node(Token.NAME);
        parentIsNullNode.setParent(null);
        assertFalse(NodeUtil.isStatement(parentIsNullNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parentScript = new Node(Token.SCRIPT);
        Node parentFunction = new Node(Token.FUNCTION);
        parentScript.addChildToBack(functionDeclarationNode);

        // typical use case
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        // edge case: empty node
        assertFalse(NodeUtil.isFunctionDeclaration(new Node(Token.FUNCTION)));

        // error scenario: not a statement
        assertFalse(NodeUtil.isFunctionDeclaration(parentScript));

        // error scenario: function expression
        Node functionExpressionNode = new Node(Token.FUNCTION);
        functionExpressionNode.getParent().addChildToBack(functionExpressionNode);
        assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        Node parentScript = new Node(Token.SCRIPT);
        Node parentFunction = new Node(Token.FUNCTION);
        parentScript.addChildToBack(hoistedFunctionDeclarationNode);
        parentFunction.addChildToFront(hoistedFunctionDeclarationNode);

        // typical use case
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

        // edge case: empty node
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION)));

        // error scenario: not a statement
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentScript));

        // error scenario: function expression
        Node functionExpressionNode = new Node(Token.FUNCTION);
        functionExpressionNode.getParent().addChildToBack(functionExpressionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
    }

}