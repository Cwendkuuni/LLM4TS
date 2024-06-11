import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

  private RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback;

  @Before
  public void setUp() {
    removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
  }

  @Test
  public void trySimplify_shouldNotReplaceNode_whenNodeTypeIsNotExprResult() {
    // Arrange
    Node parent = new Node();
    Node node = new Node(Token.GETPROP);

    // Act
    removeConstantRValuesCallback.trySimplify(parent, node);

    // Assert
    assertEquals(0, parent.getChildren().size());
  }

  @Test
  public void trySimplify_shouldReplaceNodeWithSideEffectNodes_whenExprBodyHasNoSideEffects() {
    // Arrange
    Node parent = new Node();
    Node node = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.NAME);
    node.addChildToFront(exprBody);
    parent.addChildToFront(node);

    // Act
    removeConstantRValuesCallback.trySimplify(parent, node);

    // Assert
    assertEquals(1, parent.getChildren().size());
    assertEquals(Token.EXPR_RESULT, parent.getFirstChild().getType());
    assertEquals("name", parent.getFirstChild().getFirstChild().getString());
  }

  @Test
  public void getSideEffectNodes_shouldReturnEmptyList_whenNodeHasNoSideEffectSubexpressions() {
    // Arrange
    Node node = new Node(Token.NAME);

    // Act
    List<Node> result = removeConstantRValuesCallback.getSideEffectNodes(node);

    // Assert
    assertEquals(0, result.size());
  }

  @Test
  public void getSideEffectNodes_shouldReturnListWithSideEffectNodes_whenNodeHasSideEffectSubexpressions() {
    // Arrange
    Node node = new Node(Token.COMMA);
    Node subexpression1 = new Node(Token.NAME);
    Node subexpression2 = new Node(Token.NAME);
    node.addChildToFront(subexpression1);
    node.addChildToBack(subexpression2);

    // Act
    List<Node> result = removeConstantRValuesCallback.getSideEffectNodes(node);

    // Assert
    assertEquals(2, result.size());
    assertEquals(Token.EXPR_RESULT, result.get(0).getType());
    assertEquals("name", result.get(0).getFirstChild().getString());
    assertEquals(Token.EXPR_RESULT, result.get(1).getType());
    assertEquals("name", result.get(1).getFirstChild().getString());
  }
}