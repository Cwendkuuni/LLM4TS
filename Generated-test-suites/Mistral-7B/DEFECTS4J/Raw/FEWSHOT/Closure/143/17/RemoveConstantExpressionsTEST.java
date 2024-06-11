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
  private RemoveConstantExpressions removeConstantExpressions;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testRemoveConstantExpressions() {
    Node root = new Node(Token.PROGRAM, null);

    Node add1 = new Node(Token.ADD, null);
    add1.addChildToEnd(new Node(Token.NUMBER, 1));
    add1.addChildToEnd(new Node(Token.IDENT, "foo"));
    add1.addChildToEnd(new Node(Token.ADD, null));
    add1.getLastChild().addChildToEnd(new Node(Token.NUMBER, 1));
    add1.getLastChild().addChildToEnd(new Node(Token.IDENT, "bar"));

    root.addChildToEnd(add1);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, cb);

    Result result = cb.getResult();
    Node resultNode = result.changed ? root : add1;

    List<Node> expected = Lists.newArrayList(
        new Node(Token.ADD, null),
        new Node(Token.IDENT, "foo"),
        new Node(Token.IDENT, "bar")
    );

    NodeUtil.isStructureEqual(expected, resultNode.getChildren());
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffect() {
    Node root = new Node(Token.PROGRAM, null);

    Node add1 = new Node(Token.ADD, null);
    add1.addChildToEnd(new Node(Token.NUMBER, 1));
    add1.addChildToEnd(new Node(Token.IDENT, "foo"));
    add1.addChildToEnd(new Node(Token.ASSIGN, Token.ASSIGN_ADD, null));
    add1.getLastChild().addChildToEnd(new Node(Token.IDENT, "x"));
    add1.addChildToEnd(new Node(Token.NUMBER, 1));
    add1.addChildToEnd(new Node(Token.IDENT, "bar"));

    root.addChildToEnd(add1);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, cb);

    Result result = cb.getResult();
    Node resultNode = result.changed ? root : add1;

    List<Node> expected = Lists.newArrayList(
        new Node(Token.ASSIGN, Token.ASSIGN_ADD, null),
        new Node(Token.IDENT, "x"),
        new Node(Token.IDENT, "foo"),
        new Node(Token.IDENT, "bar")
    );

    NodeUtil.isStructureEqual(expected, resultNode.getChildren());
  }

  @Test
  public void testRemoveConstantExpressionsWithOnlyConstantExpression() {
    Node root = new Node(Token.PROGRAM, null);

    Node add1 = new Node(Token.ADD, null);
    add1.addChildToEnd(new Node(Token.NUMBER, 1));
    add1.addChildToEnd(new Node(Token.NUMBER, 1));

    root.addChildToEnd(add1);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, cb);

    assertNull(cb.getResult().changed);
  }

  @Test
  public void testRemoveConstantExpressionsWithEmptyParent() {
    Node root = new Node(Token.PROGRAM, null);

    RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
    NodeTraversal.traverse(null, root, cb);

    assertNull(cb.getResult().changed);
  }
}