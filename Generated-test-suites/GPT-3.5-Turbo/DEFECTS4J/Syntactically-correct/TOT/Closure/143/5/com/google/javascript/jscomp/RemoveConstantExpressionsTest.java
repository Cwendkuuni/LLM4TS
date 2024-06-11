import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RemoveConstantExpressionsTest {

  @Test
  public void testProcess_WhenFirstChildHasSideEffect_ShouldReplaceExpressionWithSideEffects() {
    // Create test input
    Node root = new Node(Token.SCRIPT);
    Node exprResult = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD, new Node(Token.NUMBER, 1), new Node(Token.CALL, new Node(Token.NAME, "foo")));
    exprResult.addChildToBack(exprBody);
    root.addChildToBack(exprResult);

    // Create expected output
    Node expectedRoot = new Node(Token.SCRIPT);
    Node expectedExprResult = new Node(Token.EXPR_RESULT);
    expectedExprResult.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "foo")));
    expectedRoot.addChildToBack(expectedExprResult);

    // Create test instance
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions();

    // Process the nodes
    removeConstantExpressions.process(null, root);

    // Assert the result
    assertEquals(expectedRoot.toStringTree(), root.toStringTree());
  }

  @Test
  public void testProcess_WhenFirstChildHasNoSideEffect_ShouldNotReplaceExpression() {
    // Create test input
    Node root = new Node(Token.SCRIPT);
    Node exprResult = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.PROP, new Node(Token.NAME, "obj"), new Node(Token.STRING, "field"));
    exprResult.addChildToBack(exprBody);
    root.addChildToBack(exprResult);

    // Create expected output
    Node expectedRoot = new Node(Token.SCRIPT);
    expectedRoot.addChildToBack(exprResult); // Expression remains unchanged

    // Create test instance
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions();

    // Process the nodes
    removeConstantExpressions.process(null, root);

    // Assert the result
    assertEquals(expectedRoot.toStringTree(), root.toStringTree());
  }

  @Test
  public void testProcess_WhenMultipleSideEffectsInsideExpression_ShouldReplaceExpressionWithSideEffects() {
    // Create test input
    Node root = new Node(Token.SCRIPT);
    Node exprResult = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD,
        new Node(Token.NUMBER, 1),
        new Node(Token.CALL, new Node(Token.NAME, "foo")),
        new Node(Token.CALL, new Node(Token.NAME, "bar")));
    exprResult.addChildToBack(exprBody);
    root.addChildToBack(exprResult);

    // Create expected output
    Node expectedRoot = new Node(Token.SCRIPT);
    Node expectedExprResult = new Node(Token.EXPR_RESULT);
    expectedExprResult.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "foo")));
    expectedExprResult.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "bar")));
    expectedRoot.addChildToBack(expectedExprResult);

    // Create test instance
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions();

    // Process the nodes
    removeConstantExpressions.process(null, root);

    // Assert the result
    assertEquals(expectedRoot.toStringTree(), root.toStringTree());
  }

  @Test
  public void testProcess_WhenMultipleNestedSideEffectsInsideExpression_ShouldReplaceExpressionWithSideEffects() {
    // Create test input
    Node root = new Node(Token.SCRIPT);
    Node exprResult = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD,
        new Node(Token.NUMBER, 1),
        new Node(Token.CALL, new Node(Token.NAME, "foo"),
            new Node(Token.CALL, new Node(Token.NAME, "bar"))));
    exprResult.addChildToBack(exprBody);
    root.addChildToBack(exprResult);

    // Create expected output
    Node expectedRoot = new Node(Token.SCRIPT);
    Node expectedExprResult = new Node(Token.EXPR_RESULT);
    expectedExprResult.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "foo")));
    expectedExprResult.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "bar")));
    expectedRoot.addChildToBack(expectedExprResult);

    // Create test instance
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions();

    // Process the nodes
    removeConstantExpressions.process(null, root);

    // Assert the result
    assertEquals(expectedRoot.toStringTree(), root.toStringTree());
  }

  @Test
  public void testProcess_WhenInputNodeIsNotExprResult_ShouldNotReplaceExpression() {
    // Create test input
    Node root = new Node(Token.SCRIPT);
    Node assignNode = new Node(Token.ASSIGN);
    root.addChildToBack(assignNode); // Not an EXPR_RESULT node

    // Create expected output
    Node expectedRoot = new Node(Token.SCRIPT);
    expectedRoot.addChildToBack(assignNode); // Expression remains unchanged

    // Create test instance
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions();

    // Process the nodes
    removeConstantExpressions.process(null, root);

    // Assert the result
    assertEquals(expectedRoot.toStringTree(), root.toStringTree());
  }

  @Test
  public void testProcess_WhenSubexpressionsHaveSideEffects_ShouldReplaceSubexpressionsWithSideEffects() {
    // Create test input
    Node root = new Node(Token.SCRIPT);
    Node exprResult = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.ADD,
        new Node(Token.CALL, new Node(Token.NAME, "foo")),
        new Node(Token.CALL, new Node(Token.NAME, "bar")),
        new Node(Token.NUMBER, 1),
        new Node(Token.CALL, new Node(Token.NAME, "baz")));
    exprResult.addChildToBack(exprBody);
    root.addChildToBack(exprResult);

    // Create expected output
    Node expectedRoot = new Node(Token.SCRIPT);
    Node expectedExprResult = new Node(Token.EXPR_RESULT);
    expectedExprResult.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "foo")));
    expectedExprResult.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "bar")));
    expectedExprResult.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "baz")));
    expectedRoot.addChildToBack(expectedExprResult);

    // Create test instance
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions();

    // Process the nodes
    removeConstantExpressions.process(null, root);

    // Assert the result
    assertEquals(expectedRoot.toStringTree(), root.toStringTree());
  }

  @Test
  public void testProcess_WhenInputHasNoSideEffect_ShouldNotReplaceExpression() {
    // Create test input
    Node root = new Node(Token.SCRIPT);
    Node exprResult = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.NUMBER, 5);
    exprResult.addChildToBack(exprBody);
    root.addChildToBack(exprResult);

    // Create expected output
    Node expectedRoot = new Node(Token.SCRIPT);
    expectedRoot.addChildToBack(exprResult); // Expression remains unchanged

    // Create test instance
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions();

    // Process the nodes
    removeConstantExpressions.process(null, root);

    // Assert the result
    assertEquals(expectedRoot.toStringTree(), root.toStringTree());
  }

  @Test
  public void testProcess_WhenSubexpressionsAreNestedInObjects_ShouldNotReplaceExpression() {
    // Create test input
    Node root = new Node(Token.SCRIPT);
    Node exprResult = new Node(Token.EXPR_RESULT);
    Node exprBody = new Node(Token.COMMA,
        new Node(Token.OBJECTLIT,
            new Node(Token.STRING, "field1"),
            new Node(Token.CALL, new Node(Token.NAME, "foo"))),
        new Node(Token.NUMBER, 1));
    exprResult.addChildToBack(exprBody);
    root.addChildToBack(exprResult);

    // Create expected output
    Node expectedRoot = new Node(Token.SCRIPT);
    expectedRoot.addChildToBack(exprResult); // Expression remains unchanged

    // Create test instance
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions();

    // Process the nodes
    removeConstantExpressions.process(null, root);

    // Assert the result
    assertEquals(expectedRoot.toStringTree(), root.toStringTree());
  }
}