import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
    assertTrue(result);
  }

  @Test
  public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.NOT);

    // Act
    boolean result = nodeUtil.isForIn(node);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsStatement_WhenNodeIsPartOfScript_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.NAME);
    node.getParent().setType(Token.SCRIPT);

    // Act
    boolean result = nodeUtil.isStatement(node);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsStatement_WhenNodeIsNotPartOfScript_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.NAME);
    node.getParent().setType(Token.BINOP);

    // Act
    boolean result = nodeUtil.isStatement(node);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.FUNCTION);
    node.setType(Token.FUNCTION);
    node.getParent().setType(Token.SCRIPT);

    // Act
    boolean result = nodeUtil.isFunctionDeclaration(node);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.NAME);
    node.setType(Token.NAME);
    node.getParent().setType(Token.SCRIPT);

    // Act
    boolean result = nodeUtil.isFunctionDeclaration(node);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
    // Arrange
    Node node = new Node(Token.FUNCTION);
    node.setType(Token.FUNCTION);
    node.getParent().setType(Token.SCRIPT);

    // Act
    boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
    // Arrange
    Node node = new Node(Token.FUNCTION);
    node.setType(Token.FUNCTION);
    node.getParent().getParent().setType(Token.NAME);

    // Act
    boolean result = nodeUtil.isHoistedFunctionDeclaration(node);

    // Assert
    assertFalse(result);
  }

}