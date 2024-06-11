import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @BeforeClass
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn_WhenNodeIsForIn_ShouldReturnTrue() {
        // Arrange
        Node n = new Node(Token.FOR, 3);

        // Act
        boolean result = nodeUtil.isForIn(n);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsForIn_WhenNodeIsNotForIn_ShouldReturnFalse() {
        // Arrange
        Node n = new Node(Token.WHILE, 2);

        // Act
        boolean result = nodeUtil.isForIn(n);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsStatement_WhenNodeIsStatement_ShouldReturnTrue() {
        // Arrange
        Node n = new Node(Token.SCRIPT, 1);

        // Act
        boolean result = nodeUtil.isStatement(n);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsStatement_WhenNodeIsNotStatement_ShouldReturnFalse() {
        // Arrange
        Node n = new Node(Token.IDENTIFIER, 0);

        // Act
        boolean result = nodeUtil.isStatement(n);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ShouldReturnTrue() {
        // Arrange
        Node n = new Node(Token.FUNCTION, 1);
        n.setParent(new Node(Token.SCRIPT, 2));

        // Act
        boolean result = nodeUtil.isFunctionDeclaration(n);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ShouldReturnFalse() {
        // Arrange
        Node n = new Node(Token.IDENTIFIER, 0);

        // Act
        boolean result = nodeUtil.isFunctionDeclaration(n);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ShouldReturnTrue() {
        // Arrange
        Node n = new Node(Token.FUNCTION, 1);
        n.setParent(new Node(Token.SCRIPT, 2));

        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(n);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ShouldReturnFalse() {
        // Arrange
        Node n = new Node(Token.FUNCTION, 1);
        n.setParent(new Node(Token.IF, 1));

        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(n);

        // Assert
        assertFalse(result);
    }
}