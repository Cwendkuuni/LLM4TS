import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class NodeUtilTest {
  
  private NodeUtil nodeUtil;
  
  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }
  
  @Test
  public void testIsForIn_WhenNodeIsForInLoop_ReturnsTrue() {
    // Arrange
    Node forInLoop = new Node(Token.FOR);
    forInLoop.addChildToBack(new Node(Token.EMPTY));
    forInLoop.addChildToBack(new Node(Token.EMPTY));
    forInLoop.addChildToBack(new Node(Token.EMPTY));
    
    // Act
    boolean result = NodeUtil.isForIn(forInLoop);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_WhenNodeIsNotForInLoop_ReturnsFalse() {
    // Arrange
    Node notForInLoop = new Node(Token.EMPTY);
    
    // Act
    boolean result = NodeUtil.isForIn(notForInLoop);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement_WhenNodeIsUsedAsStatement_ReturnsTrue() {
    // Arrange
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);
    
    // Act
    boolean scriptResult = NodeUtil.isStatement(scriptNode);
    boolean blockResult = NodeUtil.isStatement(blockNode);
    boolean labelResult = NodeUtil.isStatement(labelNode);
    
    // Assert
    assertTrue(scriptResult);
    assertTrue(blockResult);
    assertTrue(labelResult);
  }
  
  @Test
  public void testIsStatement_WhenNodeIsNotUsedAsStatement_ReturnsFalse() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    
    // Act
    boolean result = NodeUtil.isStatement(functionNode);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
    // Arrange
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    functionDeclarationNode.setParent(parent);
    
    // Act
    boolean result = NodeUtil.isFunctionDeclaration(functionDeclarationNode);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
    // Arrange
    Node notFunctionDeclarationNode = new Node(Token.EMPTY);
    
    // Act
    boolean result = NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
    // Arrange
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    hoistedFunctionDeclarationNode.setParent(parent);
    parent.addChildToFront(grandparent);
    
    // Act
    boolean result = NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
    // Arrange
    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ROOT);
    notHoistedFunctionDeclarationNode.setParent(parent);
    
    // Act
    boolean result = NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode);
    
    // Assert
    assertFalse(result);
  }
}