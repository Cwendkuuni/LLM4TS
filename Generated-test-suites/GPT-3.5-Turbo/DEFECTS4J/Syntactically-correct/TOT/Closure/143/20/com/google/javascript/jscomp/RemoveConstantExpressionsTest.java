import org.junit.Test;
import org.junit.Assert;

public class RemoveConstantExpressionsTest {

  @Test
  public void testProcess_WithNoExpressionStatements() {
    // Arrange
    RemoveConstantExpressions removeExpr = new RemoveConstantExpressions(null);
    Node externs = new Node(Token.EXTERNs);
    Node root = new Node(Token.ROOT);

    // Act
    removeExpr.process(externs, root);

    // Assert
    // Verify that no changes were made
    Assert.assertFalse(removeExpr.getResult().hasChanged());
  }

  @Test
  public void testProcess_WithSimpleExpressionStatements() {
    // Arrange
    RemoveConstantExpressions removeExpr = new RemoveConstantExpressions(null);
    Node externs = new Node(Token.EXTERNs);
    Node root = new Node(Token.ROOT);
    Node exprNode = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "Hello, World!"));
    root.addChildToBack(exprNode);

    // Act
    removeExpr.process(externs, root);

    // Assert
    // Verify that no changes were made since the expression has side effects
    Assert.assertFalse(removeExpr.getResult().hasChanged());
  }

  @Test
  public void testProcess_WithExpressionStatements() {
    // Arrange
    RemoveConstantExpressions removeExpr = new RemoveConstantExpressions(null);
    Node externs = new Node(Token.EXTERNs);
    Node root = new Node(Token.ROOT);

    // Create expression statements with no side effects
    Node expr1 = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "Hello"));
    Node expr2 = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "World"));
    Node expr3 = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "!"));

    // Create an expression statement with side effects
    Node expr4 = new Node(Token.EXPR_RESULT,
        new Node(Token.ADD,
            new Node(Token.STRING, "Hello, "),
            new Node(Token.STRING, "World!")
        )
    );

    root.addChildToBack(expr1);
    root.addChildToBack(expr2);
    root.addChildToBack(expr3);
    root.addChildToBack(expr4);

    // Act
    removeExpr.process(externs, root);

    // Assert
    // Verify that the expression statement with no side effects are removed
    Assert.assertEquals(1, removeExpr.getResult().getRemovedNodes().size());
    Assert.assertEquals(expr1, removeExpr.getResult().getRemovedNodes().get(0));

    // Verify that the expression statement with side effects remains unchanged
    Assert.assertFalse(removeExpr.getResult().hasChanged());
  }

  @Test
  public void testProcess_WithNestedExpressionStatements() {
    // Arrange
    RemoveConstantExpressions removeExpr = new RemoveConstantExpressions(null);
    Node externs = new Node(Token.EXTERNs);
    Node root = new Node(Token.ROOT);

    // Create nested expression statements with no side effects
    Node expr1 = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "Hello"));
    Node expr2 = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "World"));
    Node expr3 = new Node(Token.EXPR_RESULT, new Node(Token.STRING, "!"));

    // Create a nested expression statement with side effects
    Node expr4 = new Node(Token.EXPR_RESULT,
        new Node(Token.ADD,
            new Node(Token.STRING, "Hello, "),
            new Node(Token.STRING, "World!")
        )
    );

    root.addChildToBack(expr1);
    root.addChildToBack(expr2);
    root.addChildToBack(expr3);
    root.addChildToBack(expr4);

    // Nest the expression statements within another expression statement
    Node nestedExpr = new Node(Token.EXPR_RESULT, root);
    root = new Node(Token.ROOT);
    root.addChildToBack(nestedExpr);

    // Act
    removeExpr.process(externs, root);

    // Assert
    // Verify that the nested expression statement with no side effects are removed
    Assert.assertEquals(1, removeExpr.getResult().getRemovedNodes().size());
    Assert.assertEquals(nestedExpr, removeExpr.getResult().getRemovedNodes().get(0));

    // Verify that the expression statements with side effects remains unchanged
    Assert.assertFalse(removeExpr.getResult().hasChanged());
  }

  // Add more test cases as needed...

}