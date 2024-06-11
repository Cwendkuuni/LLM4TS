package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffectsDefaultLevel() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.warning(CheckSideEffects.class), false);

    CompilerInput input = NodeFactory.createCompilerInput("test", "test", null);
    Node root = NodeFactory.createBlock("{}");
    checkSideEffects.process(input, root);

    Node result = root.getFirstChild();
    assertEquals(0, result.getChildCount());

    input.setAstRoot(root);
    Node node1 = NodeFactory.createName("x");
    Node node2 = NodeFactory.createName("foo");
    Node node3 = NodeFactory.createName("eval");
    Node assignment = NodeFactory.createAssign(Token.ASSIGN, node1, NodeFactory.createCall(node2));
    Node expressionStatement1 = NodeFactory.createExpressionStatement(assignment);
    Node statement1 = NodeFactory.createStatement(expressionStatement1);
    Node statement2 = NodeFactory.createStatement(NodeFactory.createCall(node3));
    Node block = NodeFactory.createBlock(Lists.newArrayList(statement1, statement2));
    input.setAstRoot(block);

    checkSideEffects.process(input, root);

    Node problemNodes = checkSideEffects.getProblemNodes();
    assertNotNull(problemNodes);
    assertEquals(1, problemNodes.size());
    Node problemNode = problemNodes.get(0);
    assertEquals(statement1.getFirstChild(), problemNode);
  }

  @Test
  public void testCheckSideEffectsErrorLevel() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.error(CheckSideEffects.class), false);

    CompilerInput input = NodeFactory.createCompilerInput("test", "test", null);
    Node root = NodeFactory.createBlock("{}");
    checkSideEffects.process(input, root);

    Node result = root.getFirstChild();
    assertEquals(0, result.getChildCount());

    input.setAstRoot(root);
    Node node1 = NodeFactory.createName("x");
    Node node2 = NodeFactory.createName("foo");
    Node node3 = NodeFactory.createName("eval");
    Node assignment = NodeFactory.createAssign(Token.ASSIGN, node1, NodeFactory.createCall(node2));
    Node expressionStatement1 = NodeFactory.createExpressionStatement(assignment);
    Node statement1 = NodeFactory.createStatement(expressionStatement1);
    Node statement2 = NodeFactory.createStatement(NodeFactory.createCall(node3));
    Node block = NodeFactory.createBlock(Lists.newArrayList(statement1, statement2));
    input.setAstRoot(block);

    checkSideEffects.process(input, root);

    Node problemNodes = checkSideEffects.getProblemNodes();
    assertNotNull(problemNodes);
    assertEquals(1, problemNodes.size());
    Node problemNode = problemNodes.get(0);
    assertEquals(statement1.getFirstChild(), problemNode);

    // Check error messages
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", problemNode.getErrorMessages(checkSideEffects.getCompiler()).get(0).getMessage());
  }

  @Test
  public void testCheckSideEffectsProtectSideEffectFreeCode() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.warning(CheckSideEffects.class), true);

    CompilerInput input = NodeFactory.createCompilerInput("test", "test", null);
    Node root = NodeFactory.createBlock("{}");
    checkSideEffects.process(input, root);

    Node result = root.getFirstChild();
    assertEquals(0, result.getChildCount());

    input.setAstRoot(root);
    Node node1 = NodeFactory.createName("x");
    Node node2 = NodeFactory.createName("foo");
    Node node3 = NodeFactory.createName("eval");
    Node assignment = NodeFactory.createAssign(Token.ASSIGN, node1, NodeFactory.createCall(node2));
    Node expressionStatement1 = NodeFactory.createExpressionStatement(assignment);
    Node statement1 = NodeFactory.createStatement(expressionStatement1);
    Node statement2 = NodeFactory.createStatement(NodeFactory.createCall(node3));
    Node block = NodeFactory.createBlock(Lists.newArrayList(statement1, statement2));
    input.setAstRoot(block);

    checkSideEffects.process(input, root);

    Node problemNodes = checkSideEffects.getProblemNodes();
    assertNotNull(problemNodes);
    assertEquals(1, problemNodes.size());
    Node problemNode = problemNodes.get(0);
    assertEquals(statement1.getFirstChild(), problemNode);

    input.setAstRoot(root);
    checkSideEffects.protectSideEffects();

    Node protectedNode = problemNode.getFirstChild();
    Node protectorFn = NodeFactory.createName("JSCOMPILER_PRESERVE");
    Node callToProtectorFn = NodeFactory.createCall(protectorFn, protectedNode);
    Node callStatement = NodeFactory.createExpressionStatement(callToProtectorFn);
    Node newBlock = NodeFactory.createBlock(Lists.newArrayList(callStatement));
    input.setAstRoot(newBlock);

    checkSideEffects.process(input, root);

    // The protectedNode should not be considered as a problem node anymore
    problemNodes = checkSideEffects.getProblemNodes();
    assertEquals(0, problemNodes.size());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsProtectSideEffectFreeCodeErrorLevel() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.error(CheckSideEffects.class), true);

    CompilerInput input = NodeFactory.createCompilerInput("test", "test", null);
    Node root = NodeFactory.createBlock("{}");
    checkSideEffects.process(input, root);

    Node result = root.getFirstChild();
    assertEquals(0, result.getChildCount());

    input.setAstRoot(root);
    Node node1 = NodeFactory.createName("x");
    Node node2 = NodeFactory.createName("foo");
    Node node3 = NodeFactory.createName("eval");
    Node assignment = NodeFactory.createAssign(Token.ASSIGN, node1, NodeFactory.createCall(node2));
    Node expressionStatement1 = NodeFactory.createExpressionStatement(assignment);
    Node statement1 = NodeFactory.createStatement(expressionStatement1);
    Node statement2 = NodeFactory.createStatement(NodeFactory.createCall(node3));
    Node block = NodeFactory.createBlock(Lists.newArrayList(statement1, statement2));
    input.setAstRoot(block);

    checkSideEffects.process(input, root);

    Node problemNodes = checkSideEffects.getProblemNodes();
    assertNotNull(problemNodes);
    assertEquals(1, problemNodes.size());
    Node problemNode = problemNodes.get(0);
    assertEquals(statement1.getFirstChild(), problemNode);

    // Check error messages
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", problemNode.getErrorMessages(checkSideEffects.getCompiler()).get(0).getMessage());

    input.setAstRoot(root);
    checkSideEffects.protectSideEffects();
  }

  @Test
  public void testCheckSideEffectsStripProtection() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.warning(CheckSideEffects.class), false);
    StripProtection stripProtection = new StripProtection(compiler);

    CompilerInput input = NodeFactory.createCompilerInput("test", "test", null);
    Node root = NodeFactory.createBlock("{}");
    checkSideEffects.process(input, root);
    stripProtection.process(input, root);

    Node result = root.getFirstChild();
    assertEquals(0, result.getChildCount());

    input.setAstRoot(root);
    Node protectorFn = NodeFactory.createName("JSCOMPILER_PRESERVE");
    Node protectorCall = NodeFactory.createCall(protectorFn, NodeFactory.createName("x"));
    Node expressionStatement = NodeFactory.createExpressionStatement(protectorCall);
    Node statement = NodeFactory.createStatement(expressionStatement);
    Node block = NodeFactory.createBlock(Lists.newArrayList(statement));
    input.setAstRoot(block);

    checkSideEffects.process(input, root);

    Node problemNodes = checkSideEffects.getProblemNodes();
    assertNotNull(problemNodes);
    assertEquals(1, problemNodes.size());
    Node problemNode = problemNodes.get(0);
    assertEquals(statement.getFirstChild(), problemNode);

    input.setAstRoot(root);
    checkSideEffects.protectSideEffects();

    problemNodes = checkSideEffects.getProblemNodes();
    assertNotNull(problemNodes);
    assertEquals(0, problemNodes.size());

    input.setAstRoot(root);
    stripProtection.process(input, root);

    problemNodes = checkSideEffects.getProblemNodes();
    assertNotNull(problemNodes);
    assertEquals(1, problemNodes.size());
    problemNode = problemNodes.get(0);
    assertEquals(statement.getFirstChild(), problemNode);
  }
}