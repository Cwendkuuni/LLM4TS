package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeUtil;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressions() {
    RemoveConstantExpressions pass = new RemoveConstantExpressions(null);

    Node root = new Node(Token.PROGRAM, null);

    Node expr1 = new Node(Token.EXPR_STATEMENT, null);
    Node add1 = new Node(Token.ADD, null);
    Node num1 = new Node(Token.NUMBER, 1);
    Node func1 = new Node(Token.CALL, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "foo")));
    add1.addChildToEnd(num1);
    add1.addChildToEnd(func1);
    expr1.addChildToEnd(add1);
    root.addChildToEnd(expr1);

    Node expr2 = new Node(Token.EXPR_STATEMENT, null);
    Node add2 = new Node(Token.ADD, null);
    Node num2 = new Node(Token.NUMBER, 2);
    Node func2 = new Node(Token.CALL, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "bar")));
    add2.addChildToEnd(num2);
    add2.addChildToEnd(func2);
    expr2.addChildToEnd(add2);
    root.addChildToEnd(expr2);

    Node expr3 = new Node(Token.EXPR_STATEMENT, null);
    Node add3 = new Node(Token.ADD, null);
    Node num3 = new Node(Token.NUMBER, 1);
    Node func3 = new Node(Token.CALL, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "foo"), new Node(Token.IDENTIFIER, "bar")));
    add3.addChildToEnd(num3);
    add3.addChildToEnd(func3);
    expr3.addChildToEnd(add3);
    root.addChildToEnd(expr3);

    Result result = new Result();
    PassManager passManager = new PassManager();
    passManager.addPass(RemoveConstantExpressions.class);
    passManager.runPasses(root, new CompilerOptions(), result);

    assertFalse("No changes should have occurred", result.changed);
    assertNotNull("Result should not be null", result.getLog());

    Node simplifiedRoot = result.getAst();

    Node simplifiedExpr1 = simplifiedRoot.getFirstChild();
    Node simplifiedFunc1 = simplifiedExpr1.getLastChild();
    assertNotNull("foo should exist", simplifiedFunc1);

    Node simplifiedExpr2 = simplifiedRoot.getLastChild();
    Node simplifiedFunc2 = simplifiedExpr2.getLastChild();
    assertNotNull("bar should exist", simplifiedFunc2);

    Node simplifiedExpr3 = simplifiedRoot.getLastChildBeforeLast();
    Node simplifiedFunc3 = simplifiedExpr3.getLastChild();
    assertNull("foo and bar should not be called together", simplifiedFunc3);
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffect() {
    RemoveConstantExpressions pass = new RemoveConstantExpressions(null);

    Node root = new Node(Token.PROGRAM, null);

    Node expr1 = new Node(Token.EXPR_STATEMENT, null);
    Node assign1 = new Node(Token.ASSIGN, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "x")));
    Node add1 = new Node(Token.ADD, null);
    Node num1 = new Node(Token.NUMBER, 1);
    Node func1 = new Node(Token.CALL, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "foo")));
    add1.addChildToEnd(num1);
    add1.addChildToEnd(func1);
    assign1.setRightChild(add1);
    expr1.addChildToEnd(assign1);
    root.addChildToEnd(expr1);

    Node expr2 = new Node(Token.EXPR_STATEMENT, null);
    Node assign2 = new Node(Token.ASSIGN, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "x")));
    Node add2 = new Node(Token.ADD, null);
    Node num2 = new Node(Token.NUMBER, 2);
    Node func2 = new Node(Token.CALL, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "bar")));
    add2.addChildToEnd(num2);
    add2.addChildToEnd(func2);
    assign2.setRightChild(add2);
    expr2.addChildToEnd(assign2);
    root.addChildToEnd(expr2);

    Node expr3 = new Node(Token.EXPR_STATEMENT, null);
    Node assign3 = new Node(Token.ASSIGN, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "x")));
    Node add3 = new Node(Token.ADD, null);
    Node num3 = new Node(Token.NUMBER, 1);
    Node func3 = new Node(Token.CALL, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "foo"), new Node(Token.IDENTIFIER, "bar")));
    add3.addChildToEnd(num3);
    add3.addChildToEnd(func3);
    assign3.setRightChild(add3);
    expr3.addChildToEnd(assign3);
    root.addChildToEnd(expr3);

    Result result = new Result();
    PassManager passManager = new PassManager();
    passManager.addPass(RemoveConstantExpressions.class);
    passManager.runPasses(root, new CompilerOptions(), result);

    assertFalse("No changes should have occurred", result.changed);
    assertNotNull("Result should not be null", result.getLog());

    Node simplifiedRoot = result.getAst();

    Node simplifiedExpr1 = simplifiedRoot.getFirstChild();
    Node simplifiedFunc1 = simplifiedExpr1.getLastChild();
    assertNotNull("foo should exist", simplifiedFunc1);

    Node simplifiedExpr2 = simplifiedRoot.getLastChildBeforeLast();
    Node simplifiedFunc2 = simplifiedExpr2.getLastChild();
    assertNotNull("bar should exist", simplifiedFunc2);

    Node simplifiedExpr3 = simplifiedRoot.getLastChild();
    Node simplifiedAssign3 = simplifiedExpr3.getFirstChild();
    Node simplifiedFunc3 = simplifiedAssign3.getLastChild();
    Node simplifiedCall3 = simplifiedFunc3.getFirstChild();
    Node simplifiedCallArg1 = simplifiedCall3.getFirstChild();
    Node simplifiedCallArg2 = simplifiedCall3.getLastChild();
    assertEquals("foo", simplifiedCallArg1.getType().name());
    assertEquals("bar", simplifiedCallArg2.getType().name());
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffectAndNoSideEffect() {
    RemoveConstantExpressions pass = new RemoveConstantExpressions(null);

    Node root = new Node(Token.PROGRAM, null);

    Node expr1 = new Node(Token.EXPR_STATEMENT, null);
    Node assign1 = new Node(Token.ASSIGN, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "x")));
    Node add1 = new Node(Token.ADD, null);
    Node num1 = new Node(Token.NUMBER, 1);
    Node func1 = new Node(Token.CALL, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "foo")));
    add1.addChildToEnd(num1);
    add1.addChildToEnd(func1);
    assign1.setRightChild(add1);
    expr1.addChildToEnd(assign1);
    root.addChildToEnd(expr1);

    Node expr2 = new Node(Token.EXPR_STATEMENT, null);
    Node assign2 = new Node(Token.ASSIGN, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "x")));
    Node add2 = new Node(Token.ADD, null);
    Node num2 = new Node(Token.NUMBER, 2);
    Node func2 = new Node(Token.CALL, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "bar")));
    add2.addChildToEnd(num2);
    add2.addChildToEnd(func2);
    assign2.setRightChild(add2);
    expr2.addChildToEnd(assign2);
    root.addChildToEnd(expr2);

    Node expr3 = new Node(Token.EXPR_STATEMENT, null);
    Node assign3 = new Node(Token.ASSIGN, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "x")));
    Node add3 = new Node(Token.ADD, null);
    Node num3 = new Node(Token.NUMBER, 1);
    Node func3 = new Node(Token.CALL, null, null, Lists.newArrayList(new Node(Token.IDENTIFIER, "foo"), new Node(Token.IDENTIFIER, "bar")));
    add3.addChildToEnd(num3);
    add3.addChildToEnd(func3);
    assign3.setRightChild(add3);
    expr3.addChildToEnd(assign3);
    root.addChildToEnd(expr3);

    Result result = new Result();
    PassManager passManager = new PassManager();
    passManager.addPass(RemoveConstantExpressions.class);
    passManager.runPasses(root, new CompilerOptions(), result);

    assertFalse("No changes should have occurred", result.changed);
    assertNotNull("Result should not be null", result.getLog());

    Node simplifiedRoot = result.getAst();

    Node simplifiedExpr1 = simplifiedRoot.getFirstChild();
    Node simplifiedFunc1 = simplifiedExpr1.getLastChild();
    assertNotNull("foo should exist", simplifiedFunc1);

    Node simplifiedExpr2 = simplifiedRoot.getLastChildBeforeLast();
    Node simplifiedFunc2 = simplifiedExpr2.getLastChild();
    assertNotNull("bar should exist", simplifiedFunc2);

    Node simplifiedExpr3 = simplifiedRoot.getLastChild();
    Node simplifiedAssign3 = simplifiedExpr3.getFirstChild();
    assertNull("foo and bar should not be called together", simplifiedAssign3.getLastChild());
  }
}