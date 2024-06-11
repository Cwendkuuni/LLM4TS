import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;


public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        // Act
        boolean result = nodeUtil.isForIn(node);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.EMPTY);

        // Act
        boolean result = nodeUtil.isForIn(node);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement_WhenNodeIsStatement_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.SCRIPT);
        node.addChildToBack(new Node(Token.EMPTY));

        // Act
        boolean result = nodeUtil.isStatement(node);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_WhenNodeIsNotStatement_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.EMPTY);

        // Act
        boolean result = nodeUtil.isStatement(node);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        nodeUtil.setParent(node, parent);

        // Act
        boolean result = nodeUtil.isFunctionDeclaration(node);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.EMPTY);

        // Act
        boolean result = nodeUtil.isFunctionDeclaration(node);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        Node grandparent = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(grandparent);
        grandparent.addChildToBack(node);
        nodeUtil.setParent(node, grandparent);
        nodeUtil.setParent(grandparent, parent);

        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        nodeUtil.setParent(node, parent);

        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

        // Assert
        Assert.assertFalse(result);
    }
}