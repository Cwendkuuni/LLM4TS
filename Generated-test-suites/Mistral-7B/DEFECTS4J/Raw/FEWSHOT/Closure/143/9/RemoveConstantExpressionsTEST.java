package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class RemoveConstantExpressionsTest {
  private Node root;
  private AbstractCompiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testNoConstantExpression() {
    root = new Node(Token.BLOCK, Lists.newArrayList(
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
            new Node(Token.NUM, 5)),
        new Node(Token.ASSIGN, new Node(Token.NAME, "y"),
            new Node(Token.NUM, 10))));

    removeConstantExpressions.process(null, root);

    assertEquals(root.toString(), "var x = 5;\nvar y = 10;");
  }

  @Test
  public void testConstantExpressionWithSideEffect() {
    root = new Node(Token.BLOCK, Lists.newArrayList(
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
            new Node(Token.EXPR_RESULT,
                new Node(Token.NAME, "foo"))),
        new Node(Token.ASSIGN, new Node(Token.NAME, "y"),
            new Node(Token.NUM, 10))));

    root = new Node(Token.BLOCK, Lists.newArrayList(
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
            new Node(Token.EXPR_RESULT,
                new Node(Token.EXPR_STMT, new Node(Token.NAME, "foo")))),
        new Node(Token.ASSIGN, new Node(Token.NAME, "y"),
            new Node(Token.NUM, 10))));

    removeConstantExpressions.process(null, root);

    assertEquals(root.toString(), "var x = foo;\nvar y = 10;");
  }

  @Test
  public void testConstantExpressionWithoutSideEffect() {
    root = new Node(Token.BLOCK, Lists.newArrayList(
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
            new Node(Token.EXPR_RESULT,
                new Node(Token.NUM, 5))),
        new Node(Token.ASSIGN, new Node(Token.NAME, "y"),
            new Node(Token.EXPR_RESULT,
                new Node(Token.NUM, 10)))));

    removeConstantExpressions.process(null, root);

    assertEquals(root.toString(), "var x = 5;\nvar y = 10;");

    root = new Node(Token.BLOCK, Lists.newArrayList(
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
            new Node(Token.NUM, 5)),
        new Node(Token.ASSIGN, new Node(Token.NAME, "y"),
            new Node(Token.EXPR_RESULT,
                new Node(Token.ADD,
                    new Node(Token.NUM, 1),
                    new Node(Token.NUM, 5))))));

    removeConstantExpressions.process(null, root);

    assertEquals(root.toString(), "var x = 5;\nvar y = 6;");
  }

  @Test
  public void testComplexConstantExpressionWithSideEffect() {
    root = new Node(Token.BLOCK, Lists.newArrayList(
        new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
            new Node(Token.EXPR_RESULT,
                new Node(Token.EXPR_STMT,
                    new Node(Token.ASSIGN, new Node(Token.NAME, "y"),
                        new Node(Token.NUM, 10)))),
            new Node(Token.EXPR_STMT, new Node(Token.NAME, "foo"))),
        new Node(Token.ASSIGN, new Node(Token.NAME, "z"),
            new Node(Token.EXPR_RESULT,
                new Node(Token.ADD,
                    new Node(Token.NUM, 1),
                    new Node(Token.EXPR_RESULT,
                        new Node(Token.MUL,
                            new Node(Token.NAME, "x"),
                            new Node(Token.NUM, 2))),
                    new Node(Token.EXPR_RESULT,
                        new Node(Token.NAME, "y")))))));

    removeConstantExpressions.process(null, root);

    assertEquals(root.toString(), "var x = foo();\nvar y = 10;\nvar z = x * 2 + y + 1;");
  }
}