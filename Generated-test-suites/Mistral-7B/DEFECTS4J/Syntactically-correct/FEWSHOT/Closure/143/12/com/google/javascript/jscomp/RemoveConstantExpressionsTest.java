package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test class for RemoveConstantExpressions.
 */
public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testRemoveConstantExpressions() {
    Node root = createNode("1 + foo() + bar()");
    Result result = new Result();
    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    when(compiler.getResult()).thenReturn(result);

    NodeTraversal.traverse(null, root, cb);

    List<Node> sideEffectNodes = Lists.newArrayList(createNode("foo()"), createNode("bar()"));
    List<Node> replacements = cb.getResult().getReplacements();

    // Check that the number of nodes in the tree is reduced.
    int nodesBefore = getNumChildren(root);
    int nodesAfter = getNumChildren(replacements.get(0));
    assertThat(nodesBefore).isGreaterThan(nodesAfter);

    // Check that the nodes have been replaced as expected.
    for (int i = 0; i < replacements.size(); i++) {
      Node replacement = replacements.get(i);
      Node expected = sideEffectNodes.get(i);
      assertThat(replacement).isInstanceOf(Node.class);
      assertThat(getNumChildren(replacement)).isEqualTo(getNumChildren(expected));
      compareSubtree(replacement, expected);
    }
  }

  @Test
  public void testRemoveConstantExpressionsWithEmptySubtree() {
    Node root = createNode("1 + foo() + (1 + 1)");
    Result result = new Result();
    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    when(compiler.getResult()).thenReturn(result);

    NodeTraversal.traverse(null, root, cb);

    List<Node> sideEffectNodes = Lists.newArrayList(createNode("foo()"));
    List<Node> replacements = cb.getResult().getReplacements();

    // Check that the number of nodes in the tree is reduced.
    int nodesBefore = getNumChildren(root);
    int nodesAfter = getNumChildren(replacements.get(0));
    assertThat(nodesBefore).isEqualTo(nodesAfter + 1);

    // Check that the nodes have been replaced as expected.
    for (int i = 0; i < replacements.size(); i++) {
      Node replacement = replacements.get(i);
      Node expected = sideEffectNodes.get(i);
      assertThat(replacement).isInstanceOf(Node.class);
      assertThat(getNumChildren(replacement)).isEqualTo(getNumChildren(expected));
      compareSubtree(replacement, expected);
    }

    // Check that the empty subtree has been removed.
    Node expectedEmpty = createNode();
    Node actualEmpty = replacements.get(replacements.size() - 1);
    assertThat(actualEmpty).isInstanceOf(Node.class);
    assertThat(getNumChildren(actualEmpty)).isZero();
    compareSubtree(actualEmpty, expectedEmpty);
  }

  @Test
  public void testDoNotRemoveConstantExpressionsWithSideEffects() {
    Node root = createNode("1 + foo() + bar() + baz()");
    Result result = new Result();
    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    when(compiler.getResult()).thenReturn(result);

    NodeTraversal.traverse(null, root, cb);

    List<Node> sideEffectNodes = Lists.newArrayList(createNode("foo()"), createNode("bar()"), createNode("baz()"));
    List<Node> replacements = cb.getResult().getReplacements();

    // Check that the number of nodes in the tree is not reduced.
    int nodesBefore = getNumChildren(root);
    int nodesAfter = getNumChildren(replacements.get(0));
    assertThat(nodesBefore).isEqualTo(nodesAfter);

    // Check that the nodes have not been replaced.
    for (int i = 0; i < replacements.size(); i++) {
      Node replacement = replacements.get(i);
      Node expected = sideEffectNodes.get(i);
      assertThat(replacement).isNull();
    }
  }

  @Test
  public void testRemoveNullExpression() {
    Node root = createNode("1 + null + bar()");
    Result result = new Result();
    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    when(compiler.getResult()).thenReturn(result);

    NodeTraversal.traverse(null, root, cb);

    List<Node> sideEffectNodes = Lists.newArrayList(createNode("bar()"));
    List<Node> replacements = cb.getResult().getReplacements();

    // Check that the null expression has been removed.
    Node actualNull = replacements.get(0);
    Node expectedNull = createNode();
    assertThat(actualNull).isNull();
    assertThat(replacements.size()).isEqualTo(1);
    compareSubtree(replacements.get(0), expectedNull);

    // Check that the remaining node has been replaced as expected.
    Node replacement = replacements.get(1);
    Node expected = sideEffectNodes.get(0);
    assertThat(replacement).isInstanceOf(Node.class);
    assertThat(getNumChildren(replacement)).isEqualTo(getNumChildren(expected));
    compareSubtree(replacement, expected);
  }

  private void compareSubtree(Node a, Node b) {
    if (a == null || b == null) {
      return;
    }

    if (a.getType() != b.getType()) {
      throw new AssertionError(
          String.format(
              "Expected type %s, but got type %s for nodes %s and %s",
              NodeUtil.tokenToString(a.getType()),
              NodeUtil.tokenToString(b.getType()),
              a,
              b));
    }

    for (int i = 0; i < a.getNumChildren(); i++) {
      compareSubtree(a.getChild(i), b.getChild(i));
    }
  }

  private int getNumChildren(Node node) {
    if (node == null) {
      return 0;
    }
    return node.getNumChildren();
  }

  private Node createNode(String expression) {
    return new Node(Token.EXPR_RESULT, new Node(Token.EXPR, new Node(Token.NUMBER, Double.parseDouble(expression))));
  }

  private Node createNode() {
    return new Node(Token.NULL);
  }
}

class NodeUtil {
  public static boolean nodeTypeMayHaveSideEffects(Node node) {
    switch (node.getType()) {
      case Token.ASSIGN:
      case Token.ASSIGN_SHORTHAND:
      case Token.ASSIGN_OP:
      case Token.CALL:
      case Token.COND:
      case Token.DECLARE:
      case Token.EXPR_RESULT:
      case Token.FOR:
      case Token.IF:
      case Token.RETURN:
      case Token.SWITCH:
      case Token.THROW:
      case Token.TRY:
      case Token.UNARY_MINUS:
      case Token.UNARY_PLUS:
      case Token.WHILE:
        return true;

      default:
        return false;
    }
  }
}

class GatherSideEffectSubexpressionsCallback extends AbstractPostOrderCallback {
  private static final class CopySideEffectSubexpressions implements Answer<Void> {
    private final List<Node> result;
    private final List<Node> subexpressions;

    CopySideEffectSubexpressions(List<Node> subexpressions, List<Node> result) {
      this.subexpressions = subexpressions;
      this.result = result;
    }

    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable {
      Node node = (Node) invocation.getArgumentAt(0, Node.class);
      subexpressions.add(node);
      result.add(new Node(node.getFirstChild()));
      return null;
    }
  }

  private List<Node> subexpressions;

  private GatherSideEffectSubexpressionsCallback(List<Node> subexpressions, CopySideEffectSubexpressions callback) {
    this.subexpressions = subexpressions;
    this.callback = callback;
  }

  private CopySideEffectSubexpressions callback;

  @Override
  public void visit(NodeTraversal traversal, Node node, Node parent) {
    if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
      callback.answer(new InvocationOnMock() {});
    }
  }
}