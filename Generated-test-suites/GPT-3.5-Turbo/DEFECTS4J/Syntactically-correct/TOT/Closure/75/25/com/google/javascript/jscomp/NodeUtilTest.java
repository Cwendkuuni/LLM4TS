import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    
    // Act
    boolean result = NodeUtil.isForIn(node);
    
    // Assert
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.IF);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    
    // Act
    boolean result = NodeUtil.isForIn(node);
    
    // Assert
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsStatement_WhenNodeIsStatement_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.SCRIPT);
    Node parent = new Node();
    parent.addChildToBack(node);
    node.setParent(parent);
    
    // Act
    boolean result = NodeUtil.isStatement(node);
    
    // Assert
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsStatement_WhenNodeIsNotStatement_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.IF);
    Node parent = new Node();
    parent.addChildToBack(node);
    node.setParent(parent);
    
    // Act
    boolean result = NodeUtil.isStatement(node);
    
    // Assert
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandParent = new Node(Token.SCRIPT);
    parent.setParent(grandParent);
    node.setParent(parent);
    
    // Act
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    // Assert
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.IF);
    Node parent = new Node(Token.BLOCK);
    Node grandParent = new Node(Token.SCRIPT);
    parent.setParent(grandParent);
    node.setParent(parent);
    
    // Act
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    // Assert
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.BLOCK);
    grandParent.addChildToFront(node);
    parent.addChildToFront(grandParent);
    node.setParent(grandParent);
    grandParent.setParent(parent);
    
    // Act
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    // Assert
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.IF);
    Node parent = new Node(Token.BLOCK);
    Node grandParent = new Node(Token.SCRIPT);
    parent.setParent(grandParent);
    node.setParent(parent);
    
    // Act
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    // Assert
    Assert.assertFalse(result);
  }
}