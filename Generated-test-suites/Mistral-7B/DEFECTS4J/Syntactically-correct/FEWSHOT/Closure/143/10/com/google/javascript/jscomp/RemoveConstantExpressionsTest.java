package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeUtil;
import org.junit.Before;
import org.junit.Test;

public class RemoveConstantExpressionsTest {
  private Result result;
  private Node root;

  @Before
  public void setUp() {
    result = new Result();
    root = new Node(Token.PROGRAM, null);
  }

  @Test
  public void testRemoveConstantExpressions() {
    Node expr1 = new Node(Token.EXPR_RESULT, null);
    expr1.setFirstChild(new Node(Token.PLUS, null));
    expr1.getFirstChild().setFirstChild(new Node(Token.NUMBER, 1));
    expr1.getFirstChild().getNext().setFirstChild(new Node(Token.IDENT, "foo"));
    expr1.getFirstChild().getNext().getNext().setFirstChild(new Node(Token.IDENT, "bar"));
    expr1.setParent(root);

    Node expr2 = new Node(Token.EXPR_RESULT, null);
    expr2.setFirstChild(new Node(Token.MINUS, null));
    expr2.getFirstChild().setFirstChild(new Node(Token.NUMBER, -1));
    expr2.getFirstChild().getNext().setFirstChild(new Node(Token.IDENT, "foo"));
    expr2.setParent(root);

    Node expr3 = new Node(Token.EXPR_RESULT, null);
    expr3.setFirstChild(new Node(Token.MINUS, null));
    expr3.getFirstChild().setFirstChild(new Node(Token.IDENT, "foo"));
    expr3.getFirstChild().getNext().setFirstChild(new Node(Token.NUMBER, 1));
    expr3.setParent(root);

    Node expectedResult1 = new Node(Token.IDENT, "foo");
    expectedResult1.setParent(root);
    Node expectedResult2 = new Node(Token.IDENT, "bar");
    expectedResult2.setParent(root);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(null);
    pass.process(null, root);

    Node actualResult1 = findNodeByValue(root, "foo");
    Node actualResult2 = findNodeByValue(root, "bar");

    assertThat(result.changed).isTrue();
    assertThat(actualResult1).isEqualTo(expectedResult1);
    assertThat(actualResult2).isEqualTo(expectedResult2);
    assertThat(expr1.getFirstChild()).isNull();
    assertThat(expr2.getFirstChild()).isNull();
    assertNull(expr3.getFirstChild());
  }

  private Node findNodeByValue(Node node, String value) {
    if (node.getType() != Token.IDENT || !node.getValue().equals(value)) {
      Node next = node.getNext();
      return next == null ? null : findNodeByValue(next, value);
    }
    return node;
  }
}

class NodeUtilTest {
  @Test
  public void testNodeTypeMayHaveSideEffects() {
    Node node1 = new Node(Token.ASSIGN, null);
    Node node2 = new Node(Token.PLUS, null);
    Node node3 = new Node(Token.MINUS, null);
    Node node4 = new Node(Token.IDENT, "foo");

    assertThat(NodeUtil.nodeTypeMayHaveSideEffects(node1)).isTrue();
    assertThat(NodeUtil.nodeTypeMayHaveSideEffects(node2)).isTrue();
    assertThat(NodeUtil.nodeTypeMayHaveSideEffects(node3)).isTrue();
    assertThat(NodeUtil.nodeTypeMayHaveSideEffects(node4)).isFalse();
  }
}