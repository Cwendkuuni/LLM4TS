import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Mockito.when(node.getType()).thenReturn(Token.FOR);
        Mockito.when(node.getChildCount()).thenReturn(3);

        // Act
        boolean result = nodeUtil.isForIn(node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Mockito.when(node.getType()).thenReturn(Token.FOR);
        Mockito.when(node.getChildCount()).thenReturn(2);

        // Act
        boolean result = nodeUtil.isForIn(node);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsStatement_WhenParentIsScript_ReturnsTrue() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(node.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.SCRIPT);

        // Act
        boolean result = nodeUtil.isStatement(node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsStatement_WhenParentIsBlock_ReturnsTrue() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(node.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.BLOCK);

        // Act
        boolean result = nodeUtil.isStatement(node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsStatement_WhenParentIsLabel_ReturnsTrue() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(node.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.LABEL);

        // Act
        boolean result = nodeUtil.isStatement(node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsStatement_WhenParentIsNotScriptOrBlockOrLabel_ReturnsFalse() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(node.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.ADD);

        // Act
        boolean result = nodeUtil.isStatement(node);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclarationAndStatement_ReturnsTrue() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(nodeUtil.isStatement(node)).thenReturn(true);

        // Act
        boolean result = nodeUtil.isFunctionDeclaration(node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclarationButNotStatement_ReturnsFalse() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(nodeUtil.isStatement(node)).thenReturn(false);

        // Act
        boolean result = nodeUtil.isFunctionDeclaration(node);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsFunctionDeclarationAndParentIsScript_ReturnsTrue() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(node.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.SCRIPT);

        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsFunctionDeclarationAndParentIsFunction_ReturnsTrue() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Node grandparent = Mockito.mock(Node.class);
        Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(node.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.ADD);
        Mockito.when(parent.getParent()).thenReturn(grandparent);
        Mockito.when(grandparent.getType()).thenReturn(Token.FUNCTION);

        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Mockito.when(node.getType()).thenReturn(Token.ADD);

        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsFunctionDeclarationButParentIsNotScriptOrFunction_ReturnsFalse() {
        // Arrange
        Node node = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(node.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.BLOCK);

        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

        // Assert
        assertFalse(result);
    }
}