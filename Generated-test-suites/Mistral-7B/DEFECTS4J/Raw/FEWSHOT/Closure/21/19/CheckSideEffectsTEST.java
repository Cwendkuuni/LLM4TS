package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects sideEffects = new CheckSideEffects(compiler, level, false);

    String testCode = "function foo() { return 1; } " +
        "var x = foo(); ";

    Node root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(sideEffects.problemNodes.isEmpty());

    testCode = "function foo() { return 1; } " +
        "var x = foo(); " +
        "// this comment would be a statement if the code wasn't minified.";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(sideEffects.problemNodes.isEmpty());

    testCode = "function foo() { return 1; } " +
        "var x = foo(); " +
        "// this empty statement is useless";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(!sideEffects.problemNodes.isEmpty());
    assertEquals(1, sideEffects.problemNodes.size());

    Node problemNode = sideEffects.problemNodes.get(0);
    assertFalse(problemNode.isString());
    assertFalse(problemNode.isQualifiedName());
    assertFalse(problemNode.isExprResult());
    assertFalse(problemNode.isBlock());
    assertFalse(problemNode.isComma());
    assertTrue(NodeUtil.isStatement(problemNode));

    testCode = "function foo() { return 1; } " +
        "var x = foo(); " +
        "// this empty statement is useless" +
        "var y = 1 + 1;";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(!sideEffects.problemNodes.isEmpty());
    assertEquals(2, sideEffects.problemNodes.size());

    testCode = "function foo() { return 1 + ''; } " +
        "var x = foo(); ";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(!sideEffects.problemNodes.isEmpty());
    assertEquals(1, sideEffects.problemNodes.size());

    problemNode = sideEffects.problemNodes.get(0);
    assertTrue(problemNode.isCall());
    Node target = problemNode.getFirstChild();
    assertTrue(target.isName());
    assertTrue(target.getString().contains("foo"));
    Node expr = problemNode.getLastChild();
    assertFalse(expr.isString());
    assertFalse(expr.isQualifiedName());
    assertFalse(expr.isExprResult());
    assertFalse(expr.isBlock());
    assertFalse(expr.isComma());
    assertTrue(NodeUtil.isExpression(expr));

    testCode = "function foo() { return 1 + ''; } " +
        "var x = foo(); " +
        "var y = 1 + 1;";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(!sideEffects.problemNodes.isEmpty());
    assertEquals(2, sideEffects.problemNodes.size());

    testCode = "function foo() { return 1 + ''; } " +
        "var x = foo(); " +
        "var y = 1 + 1; " +
        "// this is a comment";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(sideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel. Warning;
    CheckSideEffects sideEffects = new CheckSideEffects(compiler, level, true);

    String testCode = "function foo() { return 1; } " +
        "var x = foo(); ";

    Node root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(sideEffects.problemNodes.isEmpty());

    testCode = "function foo() { return 1; } " +
        "var x = foo(); " +
        "// this comment would be a statement if the code wasn't minified.";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(sideEffects.problemNodes.isEmpty());

    testCode = "function foo() { return 1; } " +
        "var x = foo(); " +
        "// this empty statement is useless";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(!sideEffects.problemNodes.isEmpty());
    assertEquals(1, sideEffects.problemNodes.size());

    testCode = "function foo() { return 1; } " +
        "var x = foo(); " +
        "// this empty statement is useless" +
        "var y = 1 + 1;";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(!sideEffects.problemNodes.isEmpty());
    assertEquals(2, sideEffects.problemNodes.size());

    testCode = "function foo() { return 1 + ''; } " +
        "var x = foo(); ";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(!sideEffects.problemNodes.isEmpty());
    assertEquals(1, sideEffects.problemNodes.size());

    testCode = "function foo() { return 1 + ''; } " +
        "var x = foo(); " +
        "var y = 1 + 1;";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(!sideEffects.problemNodes.isEmpty());
    assertEquals(2, sideEffects.problemNodes.size());

    testCode = "function foo() { return 1 + ''; } " +
        "var x = foo(); " +
        "var y = 1 + 1; " +
        "// this is a comment";

    root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    assertTrue(sideEffects.problemNodes.isEmpty());

    // Test that side-effect free nodes are protected
    testCode = "function foo() { return 1 + ''; } " +
        "var x = foo(); ";

    root = ParserUtil.parse(testCode);
    sideEffects.protectSideEffects();

    Node problemNode = sideEffects.problemNodes.get(0);
    Node protectorFn = problemNode.getFirstChild();
    assertTrue(protectorFn.isName());
    assertTrue(protectorFn.getString().equals(sideEffects.PROTECTOR_FN));
    Node replacement = problemNode.getLastChild();
    assertTrue(replacement.isCall());
    Node target = replacement.getFirstChild();
    assertTrue(target.isName());
    assertTrue(target.getString().equals(sideEffects.PROTECTOR_FN));
    Node argument = replacement.getLastChild();
    assertTrue(argument.isStatementResult());
    List<Node> children = problemNode.getChildren();
    assertTrue(children.size() == 2);
    assertTrue(children.get(0) == protectorFn);
    assertTrue(children.get(1) == replacement);
  }

  @Test
  public void testStripProtection() {
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects sideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    String testCode = "function JSCOMPILER_PRESERVE(x) { return x; } " +
        "JSCOMPILER_PRESERVE(1 + ''); ";

    Node root = ParserUtil.parse(testCode);
    sideEffects.process(null, root);
    sideEffects.stripProtection();

    assertTrue(sideEffects.problemNodes.isEmpty());
    assertTrue(root.isString());
    assertEquals("1", root.getString());
  }
}