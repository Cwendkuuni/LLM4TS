import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;

import java.util.List;

public class RemoveConstantExpressionsTest {

  private AbstractCompiler compiler;
  private Node root;
  private Result result;

  @org.junit.Before
  public void setup() {
    // Create a mock compiler for testing
    this.compiler = mock(AbstractCompiler.class);
    this.root = newNode("root");
  }

  @org.junit.Test
  public void testProcess_NoConstantExpressions() {
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);
    assertEquals(0, result.getChanges().size());
  }

  @org.junit.Test
  public void testProcess_ConstantExpressionWithSideEffect() {
    Node constantExpression = newNode("1 + console.log('Hello')");
    Node statement = newNode("statement", Token.STATEMENT);
    statement.addChildToEnd(constantExpression);
    root.addChildToEnd(statement);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node consoleLogNode = findNodeByType(root, Token.CALL_EXPR);
    assertFalse("Console log node should not be removed",
        result.getChanges().contains(consoleLogNode));
  }

  @org.junit.Test
  public void testProcess_ConstantExpressionWithoutSideEffect() {
    Node constantExpression = newNode("1 + 2");
    Node statement = newNode("statement", Token.STATEMENT);
    statement.addChildToEnd(constantExpression);
    root.addChildToEnd(statement);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node replacedConstantExpression = findNodeByType(root, Token.EXPR_RESULT);
    List<Node> sideEffectNodes = List.of(newNode("console.log('Side effect')"));
    List<Node> replacements = List.of(newNode("EXPR_RESULT", sideEffectNodes.get(0)));
    assertTrue("Constant expression should be replaced",
        result.getChanges().equals(replacements));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testProcess_NullRoot() {
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, null);
  }

  @org.junit.Test
  public void testGetResult_NoChanges() {
    Result expectedResult = new Result();
    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    assertEquals(expectedResult, cb.getResult());
  }

  @org.junit.Test
  public void testGetResult_WithChanges() {
    Node constantExpression = newNode("1 + 'constant'");
    Node statement = newNode("statement", Token.STATEMENT);
    statement.addChildToEnd(constantExpression);
    root.addChildToEnd(statement);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    Node sideEffectNode = newNode("console.log('Side effect')");
    Node statementWithSideEffect = newNode("statementWithSideEffect", Token.STATEMENT);
    statementWithSideEffect.addChildToEnd(sideEffectNode);
    root.addChildToEnd(statementWithSideEffect);

    NodeTraversal.traverse(null, root, cb);

    Result expectedResult = new Result();
    expectedResult.changed = true;
    List<Node> changes = List.of(constantExpression, sideEffectNode);
    expectedResult.changes = changes;

    assertEquals(expectedResult, cb.getResult());
  }

  private Node newNode(String nodeType) {
    return new Node(new Token(Token.KEYWORD, nodeType));
  }

  private Node newNode(String nodeType, int... childrenTokens) {
    Node node = new Node(new Token(Token.KEYWORD, nodeType));
    for (int token : childrenTokens) {
      node.addChildToEnd(new Node(new Token(token)));
    }
    return node;
  }

  private Node findNodeByType(Node node, int nodeType) {
    for (int i = 0; i < node.getNumChildren(); i++) {
      Node child = node.getChild(i);
      if (child.getType() == nodeType) {
        return child;
      }
      Node foundNode = findNodeByType(child, nodeType);
      if (foundNode != null) {
        return foundNode;
      }
    }
    return null;
  }
}