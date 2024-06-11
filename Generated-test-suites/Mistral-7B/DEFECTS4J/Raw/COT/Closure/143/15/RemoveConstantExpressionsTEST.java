import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompiler;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.test.BaseCompilerTestCase;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;

public class RemoveConstantExpressionsTest extends BaseCompilerTestCase {

  public RemoveConstantExpressionsTest() {
    super(new Compiler(), new CompilerOptions());
  }

  @org.junit.Test
  public void testRemoveConstantExpressions() {
    String input = "function foo() { return 1; } function bar() { return 2; } " +
        "function baz() { return 3; } " +
        "1 + foo() + bar()";

    List<Node> expected = Lists.newArrayList(
        createCallExpressionNode(NodeFactory.createIdentifier("foo")),
        createCallExpressionNode(NodeFactory.createIdentifier("bar"))
    );

    List<Node> actual = getRewrittenNodes(input);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testNoSideEffectExpressions() {
    String input = "function foo() { return 1; } function bar() { return 2; } " +
        "1 + foo() + bar() + 2";

    List<Node> actual = getRewrittenNodes(input);
    List<Node> expected = Lists.newArrayList(
        createCallExpressionNode(NodeFactory.createIdentifier("foo")),
        createCallExpressionNode(NodeFactory.createIdentifier("bar")),
        createNumberLiteral(2)
    );
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testNoSideEffectExpressionsInsideParenthesis() {
    String input = "function foo() { return 1; } function bar() { return 2; } " +
        "((1 + foo()) + bar())";

    List<Node> expected = Lists.newArrayList(
        createCallExpressionNode(NodeFactory.createIdentifier("foo")),
        createCallExpressionNode(NodeFactory.createIdentifier("bar")),
        createCallExpressionNode(
            NodeFactory.createCall(
                NodeFactory.createIdentifier("+"),
                NodeFactory.createNodeList(
                    createCallExpressionNode(NodeFactory.createIdentifier("foo")),
                    createCallExpressionNode(NodeFactory.createIdentifier("bar"))
                )
            )
        )
    );

    List<Node> actual = getRewrittenNodes(input);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testSideEffectsWithinParenthesis() {
    String input = "function foo() { alert('foo'); return 1; } function bar() { alert('bar'); return 2; } " +
        "((1 + foo()) + bar())";

    List<Node> expected = Lists.newArrayList(
        createCallExpressionNode(NodeFactory.createIdentifier("foo")),
        createCallExpressionNode(NodeFactory.createIdentifier("bar")),
        createCallExpressionNode(
            NodeFactory.createCall(
                NodeFactory.createIdentifier("+"),
                NodeFactory.createNodeList(
                    createCallExpressionNode(NodeFactory.createIdentifier("foo")),
                    createCallExpressionNode(NodeFactory.createIdentifier("bar"))
                )
            )
        ),
        createCallExpressionNode(NodeFactory.createIdentifier("alert"))
    );

    List<Node> actual = getRewrittenNodes(input);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testSideEffectsWithMultipleExpressions() {
    String input = "function foo() { alert('foo'); return 1; } function bar() { alert('bar'); return 2; } " +
        "1 + foo() * bar()";

    List<Node> expected = Lists.newArrayList(
        createCallExpressionNode(NodeFactory.createIdentifier("foo")),
        createCallExpressionNode(NodeFactory.createIdentifier("bar")),
        createCallExpressionNode(NodeFactory.createIdentifier("*")),
        createCallExpressionNode(NodeFactory.createIdentifier("foo")),
        createCallExpressionNode(NodeFactory.createIdentifier("bar")),
        createCallExpressionNode(NodeFactory.createIdentifier("+")),
        createCallExpressionNode(NodeFactory.createIdentifier("foo")),
        createCallExpressionNode(NodeFactory.createIdentifier("bar")),
        createCallExpressionNode(NodeFactory.createIdentifier("*"))
    );

    List<Node> actual = getRewrittenNodes(input);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEmptyInput() {
    String input = "";

    List<Node> actual = getRewrittenNodes(input);
    assertTrue(actual.isEmpty());
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testNullInput() {
    String input = null;

    getRewrittenNodes(input);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testNullParentNode() {
    String input = "1 + null";

    Node root = new Node(TOKEN_INVALID, null);
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);
    assertNotNull(root.getFirstChild());

    Node parent = null;
    Node node = new Node(EXPR_RESULT, null);
    pass.process(null, new Node(EXPR_STMT, null, node));
    assertNull(node.getParent());

    try {
      pass.trySimplify(parent, node);
      fail();
    } catch (NullPointerException e) {
      // Expected
    }
  }

  private List<Node> getRewrittenNodes(String input) {
    Node root = parse(input);
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);
    return NodeUtil.getDescendantsOfType(root, EXPR_RESULT);
  }

  private static Node createCallExpressionNode(Node expression) {
    return new Node(CALL_EXPR, null, expression);
  }

  private static Node createNumberLiteral(int value) {
    return new Node(NUMBER_LITERAL, null, null, value, null);
  }
}