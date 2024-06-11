import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NodeUtilTest {

    @Test
    void testIsForIn_WithForInLoop_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));

        // Act
        boolean result = NodeUtil.isForIn(node);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void testIsForIn_WithNonForInLoop_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.FOR);

        // Act
        boolean result = NodeUtil.isForIn(node);

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void testIsStatement_WithStatementParent_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        // Act
        boolean result = NodeUtil.isStatement(node);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void testIsStatement_WithNonStatementParent_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.IF);
        parent.addChildToBack(node);
        node.setParent(parent);

        // Act
        boolean result = NodeUtil.isStatement(node);

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void testIsFunctionDeclaration_WithFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        // Act
        boolean result = NodeUtil.isFunctionDeclaration(node);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void testIsFunctionDeclaration_WithNonFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.NAME);

        // Act
        boolean result = NodeUtil.isFunctionDeclaration(node);

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void testIsHoistedFunctionDeclaration_WithHoistedFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.FUNCTION);
        grandparent.addChildToFront(node);
        node.setParent(parent);
        parent.setParent(grandparent);

        // Act
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void testIsHoistedFunctionDeclaration_WithNonHoistedFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.FUNCTION);

        // Act
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        // Assert
        Assertions.assertFalse(result);
    }
}