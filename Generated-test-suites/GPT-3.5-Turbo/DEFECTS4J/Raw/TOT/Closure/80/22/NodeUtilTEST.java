import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NodeUtilTest {

    @Test
    void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        // Act
        boolean result = NodeUtil.isForIn(node);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        // Act
        boolean result = NodeUtil.isForIn(node);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsStatement_WhenNodeIsPartOfAST_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        // Act
        boolean result = NodeUtil.isStatement(node);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsStatement_WhenNodeIsNotPartOfAST_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.NAME);

        // Act
        boolean result = NodeUtil.isStatement(node);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FUNCTION);

        // Act
        boolean result = NodeUtil.isFunctionDeclaration(node);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.NAME);

        // Act
        boolean result = NodeUtil.isFunctionDeclaration(node);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);

        // Act
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.FUNCTION);

        // Act
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        // Assert
        assertFalse(result);
    }
}