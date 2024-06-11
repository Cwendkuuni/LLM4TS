package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.Arrays;
import java.util.List;

public class RemoveConstantExpressionsTest {

  private AbstractCompiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;
  private Result result;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    result = new Result();
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testNoSideEffectSubexpressions() {
    AbstractCompiler compilerSpy = mock(AbstractCompiler.class);
    when(compilerSpy.getParents()).thenReturn(Arrays.asList(mock(Node.class)));

    Node node1 = new Node(Token.EXPR_RESULT, null);
    Node exprBody1 = new Node(Token.ADD, Arrays.asList(
        new Node(Token.NUMBER, 1),
        node1,
        new Node(Token.NUMBER, 2)));
    node1.setFirstChild(exprBody1);

    Node node2 = new Node(Token.EXPR_RESULT, null);
    Node exprBody2 = new Node(Token.ADD, Arrays.asList(
        new Node(Token.NUMBER, 3),
        node2,
        new Node(Token.NUMBER, 4)));
    node2.setFirstChild(exprBody2);

    Node root = new Node(Token.EXPR_STATEMENT, Arrays.asList(node1, node2));

    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, callback);
    callback.getResult().notifyCompiler(compilerSpy);

    List<Node> children = Arrays.asList(root.getFirstChild(), root.getLastChild());
    for (Node node : children) {
      assertThat(NodeUtil.nodeTypeMayHaveSideEffects(node)).isFalse();
    }
  }

  @Test
  public void testSideEffectSubexpressions() {
    AbstractCompiler compilerSpy = mock(AbstractCompiler.class);
    when(compilerSpy.getParents()).thenReturn(Arrays.asList(mock(Node.class)));

    Node node1 = new Node(Token.EXPR_RESULT, null);
    Node exprBody1 = new Node(Token.ASSIGN, Arrays.asList(node1, new Node(Token.NUMBER, 1)));
    node1.setFirstChild(exprBody1);

    Node node2 = new Node(Token.EXPR_RESULT, null);
    Node exprBody2 = new Node(Token.ASSIGN, Arrays.asList(node2, new Node(Token.NUMBER, 2)));
    node2.setFirstChild(exprBody2);

    Node root = new Node(Token.EXPR_STATEMENT, Arrays.asList(node1, node2));

    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, callback);
    callback.getResult().notifyCompiler(compilerSpy);

    List<Node> children = Arrays.asList(root.getFirstChild(), root.getLastChild());
    for (Node node : children) {
      assertThat(NodeUtil.nodeTypeMayHaveSideEffects(node)).isTrue();
    }
  }

  @Test
  public void testSingleSideEffectSubexpression() {
    AbstractCompiler compilerSpy = mock(AbstractCompiler.class);
    when(compilerSpy.getParents()).thenReturn(Arrays.asList(mock(Node.class)));

    Node node = new Node(Token.EXPR_RESULT, null);
    Node exprBody = new Node(Token.ASSIGN, Arrays.asList(node, new Node(Token.NUMBER, 1)));
    node.setFirstChild(exprBody);

    Node root = new Node(Token.EXPR_STATEMENT, Arrays.asList(node));

    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, callback);
    callback.getResult().notifyCompiler(compilerSpy);

    List<Node> children = Arrays.asList(root.getFirstChild());
    for (Node node : children) {
      assertThat(NodeUtil.nodeTypeMayHaveSideEffects(node)).isTrue();
    }
  }

  @Test
  public void testNoSideEffectSubexpressionsWithParent() {
    AbstractCompiler compilerSpy = mock(AbstractCompiler.class);
    when(compilerSpy.getParents()).thenReturn(Arrays.asList(new Node(Token.ASSIGN, null)));

    Node node1 = new Node(Token.EXPR_RESULT, null);
    Node exprBody1 = new Node(Token.ADD, Arrays.asList(
        new Node(Token.NUMBER, 1),
        node1,
        new Node(Token.NUMBER, 2)));
    node1.setFirstChild(exprBody1);

    Node node2 = new Node(Token.EXPR_RESULT, null);
    Node exprBody2 = new Node(Token.ADD, Arrays.asList(
        new Node(Token.NUMBER, 3),
        node2,
        new Node(Token.NUMBER, 4)));
    node2.setFirstChild(exprBody2);

    Node parent = new Node(Token.ASSIGN, null);

    Node root = new Node(Token.EXPR_STATEMENT, Arrays.asList(parent, node1, node2));

    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, callback);
    callback.getResult().notifyCompiler(compilerSpy);

    List<Node> children = Arrays.asList(root.getFirstChild(), root.getFirstChild().getLastChild(), root.getLastChild());
    for (Node node : children) {
      assertThat(NodeUtil.nodeTypeMayHaveSideEffects(node)).isFalse();
    }
  }

  @Test
  public void testSideEffectSubexpressionWithParent() {
    AbstractCompiler compilerSpy = mock(AbstractCompiler.class);
    when(compilerSpy.getParents()).thenReturn(Arrays.asList(new Node(Token.ASSIGN, null)));

    Node node1 = new Node(Token.EXPR_RESULT, null);
    Node exprBody1 = new Node(Token.ASSIGN, Arrays.asList(node1, new Node(Token.NUMBER, 1)));
    node1.setFirstChild(exprBody1);

    Node node2 = new Node(Token.EXPR_RESULT, null);
    Node exprBody2 = new Node(Token.ASSIGN, Arrays.asList(node2, new Node(Token.NUMBER, 2)));
    node2.setFirstChild(exprBody2);

    Node parent = new Node(Token.ASSIGN, null);

    Node root = new Node(Token.EXPR_STATEMENT, Arrays.asList(parent, node1, node2));

    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, callback);
    callback.getResult().notifyCompiler(compilerSpy);

    List<Node> children = Arrays.asList(root.getFirstChild(), root.getFirstChild().getLastChild(), root.getLastChild());
    for (Node node : children) {
      if (node.equals(parent)) {
        continue;
      }
      assertThat(NodeUtil.nodeTypeMayHaveSideEffects(node)).isTrue();
    }
  }

  @Test
  public void testEmptyListOfSubexpressions() {
    AbstractCompiler compilerSpy = mock(AbstractCompiler.class);
    when(compilerSpy.getParents()).thenReturn(Arrays.asList(mock(Node.class)));

    Node node = new Node(Token.EXPR_RESULT, null);
    Node root = new Node(Token.EXPR_STATEMENT, Arrays.asList(node));

    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, callback);
    callback.getResult().notifyCompiler(compilerSpy);

    assertNull(callback.getResult().getChanged());
  }
}