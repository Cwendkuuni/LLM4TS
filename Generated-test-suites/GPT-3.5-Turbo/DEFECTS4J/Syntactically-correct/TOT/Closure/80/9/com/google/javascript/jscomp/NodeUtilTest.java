import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
        // Arrange
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));

        // Act
        boolean result = NodeUtil.isForIn(n);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
        // Arrange
        Node n = new Node(Token.FUNCTION);

        // Act
        boolean result = NodeUtil.isForIn(n);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement_WhenNodeIsStatement_ReturnsTrue() {
        // Arrange
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.NAME);
        n.setParent(parent);

        // Act
        boolean result = NodeUtil.isStatement(n);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_WhenNodeIsNotStatement_ReturnsFalse() {
        // Arrange
        Node parent = new Node(Token.EXPR_RESULT);
        Node n = new Node(Token.NAME);
        n.setParent(parent);

        // Act
        boolean result = NodeUtil.isStatement(n);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node n = new Node(Token.FUNCTION);
        n.setParent(new Node(Token.SCRIPT));

        // Act
        boolean result = NodeUtil.isFunctionDeclaration(n);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node n = new Node(Token.STRING);

        // Act
        boolean result = NodeUtil.isFunctionDeclaration(n);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        grandParent.addChildToFront(new Node(Token.STRING));
        parent.addChildToBack(grandParent);
        Node n = new Node(Token.FUNCTION);
        n.setParent(grandParent);

        // Act
        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.IF);
        grandParent.addChildToFront(new Node(Token.STRING));
        parent.addChildToBack(grandParent);
        Node n = new Node(Token.FUNCTION);
        n.setParent(parent);

        // Act
        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        // Assert
        Assert.assertFalse(result);
    }

}