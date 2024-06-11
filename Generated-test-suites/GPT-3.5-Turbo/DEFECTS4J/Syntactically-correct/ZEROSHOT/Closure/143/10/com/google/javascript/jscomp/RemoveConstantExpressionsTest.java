import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

  @Test
  public void testProcess_SimpleExpression() {
    // Arrange
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions();

    Node externs = null;
    Node root = new Node(Token.ROOT);
    
    Node exprResult = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD,
                             new Node(Token.NUMBER, 1),
                             new Node(Token.NUMBER, 2));
    exprResult.addChildToBack(exprBody);
    root.addChildToBack(exprResult);

    // Act
    removeConstantExpressions.process(externs, root);

    // Assert
    Node simplifiedExprResult = new Node(Token.EXPR_RESULT);
    Node simplifiedExprBody = new Node(Token.NUMBER, 3);
    simplifiedExprResult.addChildToBack(simplifiedExprBody);
    assertEquals(simplifiedExprResult, root.getFirstChild());
  }

  @Test
  public void testRemoveConstantRValuesCallback_Visit_ExprResult_NoSideEffect() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback
        = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

    NodeTraversal traversal = null;
    Node parent = new Node(Token.ROOT);
    Node node = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD,
                             new Node(Token.BLOCK),
                             new Node(Token.NUMBER, 2));
    node.addChildToBack(exprBody);

    // Act
    callback.visit(traversal, node, parent);

    // Assert
    Node expectedNode = new Node(Token.EXPR_RESULT);
    expectedNode.addChildToBack(new Node(Token.BLOCK));
    assertEquals(expectedNode, parent.getFirstChild());
  }

  @Test
  public void testRemoveConstantRValuesCallback_Visit_ExprResult_HasSideEffect() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback
        = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

    NodeTraversal traversal = null;
    Node parent = new Node(Token.ROOT);
    Node node = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD,
                             new Node(Token.NAME, "a"),
                             new Node(Token.NUMBER, 2));
    node.addChildToBack(exprBody);

    // Act
    callback.visit(traversal, node, parent);

    // Assert
    Node expectedNode = new Node(Token.EXPR_RESULT);
    expectedNode.addChildToBack(exprBody);
    assertEquals(expectedNode, parent.getFirstChild());
  }

  @Test
  public void testRemoveConstantRValuesCallback_TrySimplify_NodeTypeNotExprResult() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback
        = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

    Node parent = new Node(Token.ROOT);
    Node node = new Node(Token.ADD,
                         new Node(Token.BLOCK),
                         new Node(Token.NUMBER, 2));

    // Act
    callback.trySimplify(parent, node);

    // Assert
    assertEquals(0, parent.getChildCount());
  }

  @Test
  public void testRemoveConstantRValuesCallback_TrySimplify_ExprBodyNoSideEffect() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback
        = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

    Node parent = new Node(Token.ROOT);
    Node node = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD,
                             new Node(Token.BLOCK),
                             new Node(Token.NUMBER, 2));
    node.addChildToBack(exprBody);

    // Act
    callback.trySimplify(parent, node);

    // Assert
    Node expectedNode = new Node(Token.EXPR_RESULT);
    expectedNode.addChildToBack(new Node(Token.BLOCK));
    assertEquals(expectedNode, parent.getFirstChild());
  }

  @Test
  public void testRemoveConstantRValuesCallback_TrySimplify_ExprBodyHasSideEffect() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback
        = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

    Node parent = new Node(Token.ROOT);
    Node node = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD,
                             new Node(Token.NAME, "a"),
                             new Node(Token.NUMBER, 2));
    node.addChildToBack(exprBody);

    // Act
    callback.trySimplify(parent, node);

    // Assert
    Node expectedNode = new Node(Token.EXPR_RESULT);
    expectedNode.addChildToBack(exprBody);
    assertEquals(expectedNode, parent.getFirstChild());
  }

  @Test
  public void testRemoveConstantRValuesCallback_GetSideEffectNodes_NoSubexpressions() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback
        = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

    Node node = new Node(Token.ADD,
                         new Node(Token.STRING, "foo"),
                         new Node(Token.STRING, "bar"));

    // Act
    List<Node> sideEffectNodes = callback.getSideEffectNodes(node);

    // Assert
    assertEquals(0, sideEffectNodes.size());
  }

  @Test
  public void testRemoveConstantRValuesCallback_GetSideEffectNodes_WithSubexpressions() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback
        = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

    Node node = new Node(Token.ADD,
                         new Node(Token.NAME, "a"),
                         new Node(Token.ADD,
                                  new Node(Token.NAME, "b"),
                                  new Node(Token.NAME, "c")));

    // Act
    List<Node> sideEffectNodes = callback.getSideEffectNodes(node);

    // Assert
    assertEquals(3, sideEffectNodes.size());
    assertTrue(sideEffectNodes.contains(new Node(Token.EXPR_RESULT,
                                                  new Node(Token.NAME, "a"))));
    assertTrue(sideEffectNodes.contains(new Node(Token.EXPR_RESULT,
                                                  new Node(Token.NAME, "b"))));
    assertTrue(sideEffectNodes.contains(new Node(Token.EXPR_RESULT,
                                                  new Node(Token.NAME, "c"))));
  }

  @Test
  public void testRemoveConstantRValuesCallback_GetResult_Changed() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback
        = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    RemoveConstantExpressions.Result expectedResult
        = new RemoveConstantExpressions.Result();

    // Act
    RemoveConstantExpressions.Result result = callback.getResult();

    // Assert
    assertEquals(expectedResult, result);
  }

  @Test
  public void testRemoveConstantRValuesCallback_GetResult_NotChanged() {
    // Arrange
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback
        = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    RemoveConstantExpressions.Result expectedResult
        = new RemoveConstantExpressions.Result();

    // Act
    RemoveConstantExpressions.Result result = callback.getResult();

    // Assert
    assertEquals(expectedResult, result);
  }
}