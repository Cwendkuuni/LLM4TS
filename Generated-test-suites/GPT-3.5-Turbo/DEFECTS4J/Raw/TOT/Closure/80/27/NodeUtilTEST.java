import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @Before
    public void setup() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn_WithForInLoop_ReturnsTrue() {
        // Arrange
        Node forInNode = new Node(Token.FOR);
        Node child1 = new Node();
        Node child2 = new Node();
        Node child3 = new Node();

        forInNode.addChildToBack(child1);
        forInNode.addChildToBack(child2);
        forInNode.addChildToBack(child3);

        // Act
        boolean result = nodeUtil.isForIn(forInNode);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsForIn_WithNonForInLoop_ReturnsFalse() {
        // Arrange
        Node nonForInNode = new Node();

        // Act
        boolean result = nodeUtil.isForIn(nonForInNode);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsStatement_WithStatementNode_ReturnsTrue() {
        // Arrange
        Node statementNode = new Node();
        Node parent = new Node(Token.SCRIPT);

        statementNode.setParent(parent);

        // Act
        boolean result = nodeUtil.isStatement(statementNode);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsStatement_WithNonStatementNode_ReturnsFalse() {
        // Arrange
        Node nonStatementNode = new Node();
        Node parent = new Node(Token.FUNCTION);

        nonStatementNode.setParent(parent);

        // Act
        boolean result = nodeUtil.isStatement(nonStatementNode);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_WithFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);

        functionDeclarationNode.setParent(parentScriptNode);

        // Act
        boolean result = nodeUtil.isFunctionDeclaration(functionDeclarationNode);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_WithNonFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node nonFunctionDeclarationNode = new Node(Token.VAR);
        Node parentBlockNode = new Node(Token.BLOCK);

        nonFunctionDeclarationNode.setParent(parentBlockNode);

        // Act
        boolean result = nodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithHoistedFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);

        hoistedFunctionDeclarationNode.setParent(parentScriptNode);

        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithNonHoistedFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION);

        nonHoistedFunctionDeclarationNode.setParent(parentFunctionNode);

        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode);

        // Assert
        assertFalse(result);
    }
}