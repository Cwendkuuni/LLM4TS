package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;

public class CheckSideEffectsTest {

  @Test
  public void testEmpty() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new TestCompiler(options);
    CompilerInput input = new CompilerInput("source", "// empty source", null);
    checkSideEffects.process(input, null);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testSimpleAssignments() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new TestCompiler(options);
    CompilerInput input = new CompilerInput("source", "var x = 1;\n" +
        "var y = 2;\n", null);
    checkSideEffects.process(input, null);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testAssignmentsWithSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new TestCompiler(options);
    CompilerInput input = new CompilerInput("source", "var x = 1;\n" +
        "x++;\n", null);
    checkSideEffects.process(input, null);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    List<Node> problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    Node expectedProblemNode = problemNodes.get(0);
    Node actualProblemNode = problemNodes.get(0);
    assertTrue(NodeUtil.isExpressionResult(expectedProblemNode));
    assertTrue(NodeUtil.isExpressionResult(actualProblemNode));
    assertTrue(expectedProblemNode.getFirstChild().isName());
    assertTrue(actualProblemNode.getFirstChild().isName());
    assertTrue(expectedProblemNode.getFirstChild().getString().equals("x"));
    assertTrue(actualProblemNode.getFirstChild().getString().equals("x"));
  }

  @Test
  public void testSimpleOperationsWithNoSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new TestCompiler(options);
    CompilerInput input = new CompilerInput("source", "var x = 1;\n" +
        "var y = 1 + x;\n", null);
    checkSideEffects.process(input, null);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testSimpleOperationsWithSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new TestCompiler(options);
    CompilerInput input = new CompilerInput("source", "var x = 1;\n" +
        "x = 1 + x;\n", null);
    checkSideEffects.process(input, null);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    List<Node> problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    Node expectedProblemNode = problemNodes.get(0);
    Node actualProblemNode = problemNodes.get(0);
    assertTrue(NodeUtil.isStatement(expectedProblemNode));
    assertTrue(NodeUtil.isStatement(actualProblemNode));
    assertTrue(NodeUtil.isAssignment(expectedProblemNode));
    assertTrue(NodeUtil.isAssignment(actualProblemNode));
    assertTrue(expectedProblemNode.getFirstChild().isName());
    assertTrue(actualProblemNode.getFirstChild().isName());
    assertTrue(expectedProblemNode.getFirstChild().getString().equals("x"));
    assertTrue(actualProblemNode.getFirstChild().getString().equals("x"));
    assertTrue(expectedProblemNode.getLastChild().isName());
    assertTrue(actualProblemNode.getLastChild().isName());
    assertTrue(expectedProblemNode.getLastChild().getString().equals("x"));
    assertTrue(actualProblemNode.getLastChild().getString().equals("x"));
  }

  @Test(expected = AssertionError.class)
  public void testErrorMessage() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.ERROR, false);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new TestCompiler(options);
    CompilerInput input = new CompilerInput("source", "var x = 1;\n" +
        "x = 2;\n", null);
    checkSideEffects.process(input, null);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    checkSideEffects.problemNodes.get(0);
  }

  @Test
  public void testProtectSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new TestCompiler(options);
    CompilerInput input = new CompilerInput("source", "var x = 1;\n" +
        "x++;\n", null);
    checkSideEffects.process(input, null);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    List<Node> problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    Node expectedProtectorFnCall = problemNodes.get(0).getLastChild();
    Node actualProtectorFnCall = problemNodes.get(0).getLastChild();
    String expectedProtectorFnName = "JSCOMPILER_PRESERVE";
    String actualProtectorFnName = IR.name(expectedProtectorFnName).getString();
    assertTrue(expectedProtectorFnCall.isCall());
    assertTrue(actualProtectorFnCall.isCall());
    assertTrue(expectedProtectorFnCall.getFirstChild().isName());
    assertTrue(actualProtectorFnCall.getFirstChild().isName());
    assertTrue(expectedProtectorFnName.equals(actualProtectorFnName));
  }

  @Test
  public void testStripProtection() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new TestCompiler(options);
    CompilerInput input = new CompilerInput("source", "function JSCOMPILER_PRESERVE() {}\n" +
        "JSCOMPILER_PRESERVE();\n", null);
    checkSideEffects.process(input, null);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    StripProtection stripProtection = new StripProtection(compiler);
    stripProtection.process(input, null);
    NodeTraversal.traverse(compiler, root, stripProtection);
    assertTrue(root.getChildCount() == 0);
  }
}