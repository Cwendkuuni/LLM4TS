package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.test.testType.AstChangeProxyTestType;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class RemoveConstantExpressionsTest {
  private Compiler compiler;
  private Node root;
  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setUp() {
    compiler = new Compiler();
    root = new Node(Token.PROGRAM, null);
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testRemoveConstantExpressions() {
    Node constantExpression = new Node(Token.EXPR_RESULT,
        new Node(Token.EXPR_CONST, "1"));
    Node parent = new Node(Token.EXPR_STATEMENT, null);
    parent.addChildToStatementList(constantExpression);
    root.addChildToTree(parent);

    removeConstantExpressions.process(null, root);

    assertTrue(removeConstantExpressions.getResult().changed);
    assertNull(parent.getFirstChild());
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffect() {
    Node sideEffectExpression = new Node(Token.EXPR_CALL, null);
    sideEffectExpression.addChildToStatementList(
        new Node(Token.IDENT, "console.log"));
    sideEffectExpression.addChildToStatementList(
        new Node(Token.STRING_LITERAL, "Hello World"));

    Node constantExpression = new Node(Token.EXPR_RESULT, sideEffectExpression);
    Node parent = new Node(Token.EXPR_STATEMENT, null);
    parent.addChildToStatementList(constantExpression);
    root.addChildToTree(parent);

    removeConstantExpressions.process(null, root);

    assertTrue(removeConstantExpressions.getResult().changed);
    assertFalse(parent.getFirstChild().isResolved());
    assertTrue(parent.getFirstChild().getFirstChild().isResolved());
  }

  @Test(expected = AssertionError.class)
  public void testRemoveConstantExpressionsWithNonExpressionParent() {
    Node parent = new Node(Token.VAR_DECLARATION, null);
    Node constantExpression = new Node(Token.EXPR_RESULT,
        new Node(Token.EXPR_CONST, "1"));
    parent.addChildToStatementList(constantExpression);
    root.addChildToTree(parent);

    removeConstantExpressions.process(null, root);

    // This test should fail with an AssertionError since
    // the parent of the constant expression is not an expression.
  }

  @Test
  public void testRemoveConstantExpressionsWithMultipleConstantExpressions() {
    Node constantExpression1 = new Node(Token.EXPR_RESULT,
        new Node(Token.EXPR_CONST, "1"));
    Node constantExpression2 = new Node(Token.EXPR_RESULT,
        new Node(Token.EXPR_CONST, "2"));
    Node parent = new Node(Token.EXPR_STATEMENT, null);
    parent.addChildToStatementList(constantExpression1);
    parent.addChildToStatementList(constantExpression2);
    root.addChildToTree(parent);

    removeConstantExpressions.process(null, root);

    assertTrue(removeConstantExpressions.getResult().changed);
    assertNull(parent.getFirstChild());
  }

  @Test
  public void testRemoveConstantExpressionsWithComplexExpression() {
    AstChangeProxyTestType changeProxyTestType = new AstChangeProxyTestType();
    changeProxyTestType.setChangeProxy(new AstChangeProxy());
    changeProxyTestType.setCompiler(compiler);

    Node complexExpression = new Node(Token.EXPR_RESULT,
        new Node(Token.EXPR_CALL, null,
            new Node(Token.IDENT, "Math"),
            new Node(Token.IDENT, "sqrt"),
            new Node(Token.EXPR_RESULT,
                new Node(Token.EXPR_BINOP, null,
                    new Node(Token.EXPR_CONST, "4"),
                    new Node(Token.EXPR_CONST, "2"),
                    Token.OP_PLUS))));

    Node parent = new Node(Token.EXPR_STATEMENT, null);
    parent.addChildToStatementList(complexExpression);
    root.addChildToTree(parent);

    removeConstantExpressions.process(null, root);

    assertTrue(removeConstantExpressions.getResult().changed);
    assertNull(parent.getFirstChild());
  }
}