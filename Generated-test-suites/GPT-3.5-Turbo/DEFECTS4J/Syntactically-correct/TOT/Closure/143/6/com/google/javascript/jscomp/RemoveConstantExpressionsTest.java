import org.junit.Test;
import com.google.javascript.jscomp.*;
import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new Compiler();
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);

    // Test case 1: Empty root
    pass.process(externs, root);
    assertNull(root.getFirstChild());

    // Test case 2: Single expression statement
    Node expr = new Node(Token.EXPR_RESULT, new Node(Token.NUMBER, "3"));
    root.addChildToBack(expr);
    pass.process(externs, root);
    assertEquals(null, root.getFirstChild());

    // Test case 3: Expression statement with side effect
    Node sideEffect = new Node(Token.EXPR_RESULT, new Node(Token.CALL, new Node(Token.NAME, "foo")));
    root.addChildToBack(sideEffect);
    pass.process(externs, root);
    assertEquals(1, root.getChildCount());
    assertEquals(sideEffect, root.getFirstChild());

  }

  @Test
  public void testRemoveConstantRValuesCallback() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    TestNodeTraversal traversal = new TestNodeTraversal();

    // Test case 1: Non-EXPR_RESULT node
    Node node1 = new Node(Token.FUNCTION);
    callback.visit(traversal, node1, null);
    assertTrue(traversal.isVisited());

    // Test case 2: EXPR_RESULT node with no side effect
    Node node2 = new Node(Token.EXPR_RESULT, new Node(Token.NUMBER, "5"));
    Node parent = new Node(Token.SCRIPT);
    callback.trySimplify(parent, node2);
    assertNodeEqual(node2, parent.getFirstChild());

    // Test case 3: EXPR_RESULT node with side effect
    Node node3 = new Node(Token.EXPR_RESULT, new Node(Token.CALL, new Node(Token.NAME, "foo")));
    callback.trySimplify(parent, node3);
    assertEquals(1, parent.getChildCount());
    assertEquals(node3, parent.getFirstChild());
  }

  @Test
  public void testGetSideEffectNodes() {
    RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    List<Node> subexpressions = new ArrayList<>();

    // Test case 1: Empty node
    Node node1 = new Node(Token.SCRIPT);
    List<Node> replacements1 = callback.getSideEffectNodes(node1);
    assertEquals(0, replacements1.size());

    // Test case 2: Node with subexpressions
    Node node2 = new Node(Token.EXPR_RESULT);
    Node subexpression1 = new Node(Token.NUMBER, "1");
    Node subexpression2 = new Node(Token.NAME, "x");
    node2.addChildToBack(subexpression1);
    node2.addChildToBack(subexpression2);
    List<Node> replacements2 = callback.getSideEffectNodes(node2);
    assertEquals(2, replacements2.size());
    assertTrue(replacements2.contains(subexpression1));
    assertTrue(replacements2.contains(subexpression2));
  }

  private void assertNodeEqual(Node expected, Node actual) {
    assertEquals(expected.getToken(), actual.getToken());
    assertEquals(expected.getString(), actual.getString());
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getChildren().size(), actual.getChildren().size());
    for (int i = 0; i < expected.getChildCount(); i++) {
      assertNodeEqual(expected.getChildAtIndex(i), actual.getChildAtIndex(i));
    }
  }

  private class TestNodeTraversal extends NodeTraversal {
    private boolean visited = false;

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      visited = true;
    }

    public boolean isVisited() {
      return visited;
    }
  }
}