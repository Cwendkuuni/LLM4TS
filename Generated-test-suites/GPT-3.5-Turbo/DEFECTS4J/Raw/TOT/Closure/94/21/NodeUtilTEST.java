import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setup() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.IN));
    node.addChildToBack(new Node(Token.TRUE));

    // Act
    boolean result = nodeUtil.isForIn(node);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.IF);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.TRUE));
    
    // Act
    boolean result = nodeUtil.isForIn(node);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsStatement_WhenNodeIsStatement_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.VAR);

    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);

    // Act
    boolean result = nodeUtil.isStatement(node);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsStatement_WhenNodeIsNotStatement_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.NAME);
    
    // Act
    boolean result = nodeUtil.isStatement(node);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.FUNCTION);

    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);

    // Act
    boolean result = nodeUtil.isFunctionDeclaration(node);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.NAME);
    
    // Act
    boolean result = nodeUtil.isFunctionDeclaration(node);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.FUNCTION);

    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    parent1.addChildToFront(node);
    parent2.addChildToFront(parent1);
    node.setParent(parent2);
    parent1.setParent(parent2);

    // Act
    boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.FUNCTION);

    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.NAME);
    parent1.addChildToFront(node);
    parent2.addChildToFront(parent1);
    node.setParent(parent2);
    parent1.setParent(parent2);

    // Act
    boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

    // Assert
    assertFalse(result);
  }

}