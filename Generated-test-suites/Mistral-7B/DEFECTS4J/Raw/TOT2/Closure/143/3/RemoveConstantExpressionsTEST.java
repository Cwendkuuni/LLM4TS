import static org.junit.Assert.assertEquals;

public class RemoveConstantExpressionsTest {
  @Test
  public void testProcess() {
    // Create RemoveConstantExpressions instance
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(new Compiler());

    // Create test nodes and root node
    Node externs = new Node(Token.PROGRAM, null);
    Node root = new Node(Token.EXPR_STATEMENT, null);

    // Call the process method
    removeConstantExpressions.process(externs, root);

    // Check the result
    assertEquals("Expected no changes", false, removeConstantExpressions.getRemoveConstantRValuesCallback().getResult().changed);
  }
}