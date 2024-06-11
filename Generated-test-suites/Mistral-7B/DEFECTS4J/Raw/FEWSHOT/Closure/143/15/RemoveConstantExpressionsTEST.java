package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeTraversal.AbstractCallback;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Test cases for RemoveConstantExpressions class.
 */
public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler = mock(AbstractCompiler.class);
  private Result result = new Result();

  @Test
  public void testRemoveConstantExpressions() {
    Node node1 = new Node(Token.EXPR_RESULT, new Node(Token.PLUS,
        new Node(Token.EXPR_RESULT, new Node(Token.IDENT, "foo")),
        new Node(Token.EXPR_RESULT, new Node(Token.IDENT, "bar"))));
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(node1));

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.EXPR_LIST, Lists.newArrayList(
        new Node(Token.IDENT, "foo"),
        new Node(Token.IDENT, "bar")
    ));
    Node actual = result.code;

    assertThat(actual).isEqualTo(expected);
    assertThat(result.changed).isTrue();
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffect() {
    Node node1 = new Node(Token.EXPR_RESULT, new Node(Token.PLUS,
        new Node(Token.IDENT, "console"),
        new Node(Token.EXPR_RESULT, new Node(Token.IDENT, "log")),
        new Node(Token.STRING_LITERAL, "Hello"))
    );
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(node1));

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.EXPR_LIST, Lists.newArrayList(
        new Node(Token.IDENT, "console"),
        new Node(Token.IDENT, "log"),
        new Node(Token.STRING_LITERAL, "Hello")
    ));
    Node actual = result.code;

    assertThat(actual).isEqualTo(expected);
    assertThat(result.changed).isTrue();
  }

  @Test
  public void testRemoveConstantExpressionsNoSideEffect() {
    Node node1 = new Node(Token.EXPR_RESULT, new Node(Token.PLUS,
        new Node(Token.NUMBER_LITERAL, 1),
        new Node(Token.IDENT, "foo")
    ));
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(node1));

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.PROGRAM, Lists.newArrayList(node1));
    Node actual = result.code;

    assertThat(actual).isEqualTo(expected);
    assertThat(result.changed).isFalse();
  }

  @Test
  public void testRemoveConstantExpressionsEmpty() {
    Node root = new Node(Token.PROGRAM, Lists.newArrayList());

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    Node expected = new Node(Token.PROGRAM, Lists.newArrayList());
    Node actual = result.code;

    assertThat(actual).isEqualTo(expected);
    assertThat(result.changed).isFalse();
  }

  @Test
  public void testRemoveConstantExpressionsNull() {
    Node root = null;

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, root);

    assertNull(result.code);
    assertNull(result.changed);
  }

  private static class GatherSideEffectSubexpressionsCallback
      extends AbstractCallback {

    private final GatherSideEffectSubexpressionsCallback next;
    private final List<Node> subexpressions;

    GatherSideEffectSubexpressionsCallback(Node parent, List<Node> subexpressions) {
      this.next = null;
      this.subexpressions = subexpressions;
    }

    GatherSideEffectSubexpressionsCallback(GatherSideEffectSubexpressionsCallback next, List<Node> subexpressions) {
      this.next = next;
      this.subexpressions = subexpressions;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
        subexpressions.add(node);
      }
      if (next != null) {
        next.visit(traversal, node, parent);
      }
    }
  }
}