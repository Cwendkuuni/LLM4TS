import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_WhenForIn_ReturnsTrue() {
        // Arrange
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));

        // Act
        boolean result = NodeUtil.isForIn(n);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsForIn_WhenNotForIn_ReturnsFalse() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));

        // Act
        boolean result = NodeUtil.isForIn(n);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsStatement_WhenStatement_ReturnsTrue() {
        // Arrange
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        // Act
        boolean result = NodeUtil.isStatement(n);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsStatement_WhenNotStatement_ReturnsFalse() {
        // Arrange
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.MEMBER_FUNCTION_DEF);
        parent.addChildToBack(n);
        n.setParent(parent);

        // Act
        boolean result = NodeUtil.isStatement(n);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        // Act
        boolean result = NodeUtil.isFunctionDeclaration(n);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNotFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.MEMBER_FUNCTION_DEF);
        parent.addChildToBack(n);
        n.setParent(parent);

        // Act
        boolean result = NodeUtil.isFunctionDeclaration(n);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenHoistedFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        Node grandParent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.FUNCTION);
        grandParent.addChildToBack(parent);
        parent.addChildToBack(n);
        n.setParent(parent);
        parent.setParent(grandParent);

        // Act
        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNotHoistedFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        // Act
        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        // Assert
        assertFalse(result);
    }
}