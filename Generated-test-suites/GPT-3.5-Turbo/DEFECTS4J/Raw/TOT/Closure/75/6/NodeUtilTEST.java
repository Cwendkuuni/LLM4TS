import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalCase() {
    // Arrange
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToFront(new Node(Token.EMPTY).srcref("a"));
    forInNode.addChildToFront(new Node(Token.EMPTY).srcref("b"));
    forInNode.addChildToFront(new Node(Token.EMPTY).srcref("c"));

    // Act
    boolean result = NodeUtil.isForIn(forInNode);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsForIn_edgeCase() {
    // Arrange
    Node forInNode = new Node(Token.FOR);

    // Act
    boolean result = NodeUtil.isForIn(forInNode);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsForIn_errorScenario() {
    // Arrange
    Node forInNode = new Node(Token.SCRIPT);

    // Act
    boolean result = NodeUtil.isForIn(forInNode);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsStatement_typicalCase() {
    // Arrange
    Node statementNode = new Node(Token.SCRIPT);
    statementNode.addChildToFront(new Node(Token.BLOCK));
    statementNode.addChildToFront(new Node(Token.LABEL));

    // Act
    boolean result = NodeUtil.isStatement(statementNode);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsStatement_edgeCase() {
    // Arrange
    Node statementNode = new Node(Token.EXPR_RESULT);

    // Act
    boolean result = NodeUtil.isStatement(statementNode);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsStatement_errorScenario() {
    // Arrange
    Node statementNode = new Node(Token.ADD);

    // Act
    boolean result = NodeUtil.isStatement(statementNode);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_typicalCase() {
    // Arrange
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(functionDeclarationNode);

    // Act
    boolean result = NodeUtil.isFunctionDeclaration(functionDeclarationNode);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_edgeCase() {
    // Arrange
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(functionDeclarationNode);

    // Act
    boolean result = NodeUtil.isFunctionDeclaration(functionDeclarationNode);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_errorScenario() {
    // Arrange
    Node functionDeclarationNode = new Node(Token.STRING_KEY);

    // Act
    boolean result = NodeUtil.isFunctionDeclaration(functionDeclarationNode);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typicalCase() {
    // Arrange
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(functionDeclarationNode);

    // Act
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_edgeCase() {
    // Arrange
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandparent = new Node(Token.FUNCTION);
    grandparent.addChildToBack(functionDeclarationNode);
    parent.addChildToBack(grandparent);

    // Act
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_errorScenario() {
    // Arrange
    Node functionDeclarationNode = new Node(Token.STRING_KEY);

    // Act
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode);

    // Assert
    assertFalse(result);
  }
}