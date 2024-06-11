import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

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
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY, new Node(Token.EMPTY), new Node(Token.EMPTY)));
        
        // Act
        boolean result = nodeUtil.isForIn(node);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.IF);
        
        // Act
        boolean result = nodeUtil.isForIn(node);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsStatement_WhenNodeIsPartOfScript_ReturnsTrue() {
        // Arrange
        Node scriptNode = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        scriptNode.addChildToBack(node);
        
        // Act
        boolean result = nodeUtil.isStatement(node);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_WhenNodeIsPartOfBlock_ReturnsTrue() {
        // Arrange
        Node blockNode = new Node(Token.BLOCK);
        Node node = new Node(Token.EMPTY);
        blockNode.addChildToBack(node);
        
        // Act
        boolean result = nodeUtil.isStatement(node);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_WhenNodeIsPartOfLabel_ReturnsTrue() {
        // Arrange
        Node labelNode = new Node(Token.LABEL);
        Node node = new Node(Token.EMPTY);
        labelNode.addChildToBack(node);
        
        // Act
        boolean result = nodeUtil.isStatement(node);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_WhenNodeIsNotPartOfScriptBlockOrLabel_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.IF);
        
        // Act
        boolean result = nodeUtil.isStatement(node);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.NAME));
        nodeUtil.setParent(node); // Setting parent
        
        // Act
        boolean result = nodeUtil.isFunctionDeclaration(node);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.IF);
        nodeUtil.setParent(node); // Setting parent
        
        // Act
        boolean result = nodeUtil.isFunctionDeclaration(node);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
        // Arrange
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToFront(new Node(Token.NAME));
        scriptNode.addChildToBack(functionNode);
        nodeUtil.setParent(functionNode); // Setting parent
        
        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(functionNode);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
        // Arrange
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.NAME));
        nodeUtil.setParent(node); // Setting parent
        
        // Act
        boolean result = nodeUtil.isHoistedFunctionDeclaration(node);
        
        // Assert
        assertFalse(result);
    }

}