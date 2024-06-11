package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.AbstractCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizer;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    minimizer = new MinimizeExitPoints(compiler);
  }

  /**
   * Test the basic functionality of visit method for different node types.
   */
  @Test
  public void testBasicFunctionality() {
    Node root = Node.createScriptletContext();
    Node labelNode = Node.newLabel("label");
    Node breakNode = Node.newBreakStatement("label");
    Node continueNode = Node.newContinueStatement();
    Node ifNode = Node.newIfStatement();
    Node forNode = Node.newForStatement();
    Node doNode = Node.newDoStatement();
    Node functionNode = Node.newFunctionStatement("func", Node.newList(), Node.newList(), Node.newList());

    root.addChild(labelNode);
    root.addChild(ifNode);
    root.addChild(forNode);
    root.addChild(doNode);
    root.addChild(functionNode);

    NodeTraversal.traverse(compiler, root, minimizer);

    assertEquals(0, labelNode.getChildCount());
    assertEquals(0, ifNode.getChildCount());
    assertEquals(0, forNode.getChildCount());
    assertEquals(0, doNode.getChildCount());
    assertEquals(0, functionNode.getChildCount());
  }

  /**
   * Test handling of return statement in a function.
   */
  @Test
  public void testReturnStatement() {
    Node root = Node.createScriptletContext();
    Node functionNode = Node.newFunctionStatement("func", Node.newList(), Node.newList(), Node.newList());
    Node returnNode = Node.newReturnStatement();

    functionNode.addChild(returnNode);
    root.addChild(functionNode);

    NodeTraversal.traverse(compiler, root, minimizer);

    assertTrue(functionNode.getLastChild() instanceof IR);
  }

  /**
   * Test handling of break statement within a label.
   */
  @Test
  public void testBreakStatementWithLabel() {
    Node root = Node.createScriptletContext();
    Node labelNode = Node.newLabel("label");
    Node breakNode = Node.newBreakStatement("label");

    labelNode.addChild(breakNode);
    root.addChild(labelNode);

    NodeTraversal.traverse(compiler, root, minimizer);

    assertEquals(0, labelNode.getChildCount());
  }

  /**
   * Test handling of continue statement within a loop.
   */
  @Test
  public void testContinueStatementWithinLoop() {
    Node root = Node.createScriptletContext();
    Node forNode = Node.newForStatement();
    Node continueNode = Node.newContinueStatement();

    forNode.addChild(continueNode);
    root.addChild(forNode);

    NodeTraversal.traverse(compiler, root, minimizer);

    assertEquals(0, forNode.getLastChild().getChildCount());
  }

  /**
   * Test handling of break statement within a loop.
   */
  @Test
  public void testBreakStatementWithinLoop() {
    Node root = Node.createScriptletContext();
    Node forNode = Node.newForStatement();
    Node labelNode = Node.newLabel("label");
    Node breakNode = Node.newBreakStatement("label");

    forNode.addChild(labelNode);
    labelNode.addChild(breakNode);
    root.addChild(forNode);

    NodeTraversal.traverse(compiler, root, minimizer);

    assertEquals(0, forNode.getChildCount());
  }

  /**
   * Test handling of break statement without a label.
   */
  @Test
  public void testBreakStatementWithoutLabel() {
    Node root = Node.createScriptletContext();
    Node forNode = Node.newForStatement();
    Node breakNode = Node.newBreakStatement();

    forNode.addChild(breakNode);
    root.addChild(forNode);

    try {
      NodeTraversal.traverse(compiler, root, minimizer);
      fail("Expected an exception but none was thrown.");
    } catch (AssertionError e) {
      // Expected exception
    }
  }

  /**
   * Test handling of nested if statements.
   */
  @Test
  public void testNestedIfStatements() {
    Node root = Node.createScriptletContext();
    Node ifNode1 = Node.newIfStatement();
    Node ifNode2 = Node.newIfStatement();
    Node elseBlock1 = Node.newBlock();
    Node elseBlock2 = Node.newBlock();
    Node trueBlock = Node.newBlock();
    Node falseBlock = Node.newBlock();

    ifNode1.addChild(ifNode2);
    ifNode1.addChild(elseBlock1);
    ifNode2.addChild(elseBlock2);
    ifNode1.addChild(trueBlock);
    ifNode1.addChild(falseBlock);

    root.addChild(ifNode1);

    NodeTraversal.traverse(compiler, root, minimizer);

    assertFalse(ifNode1.getLastChild().getChildCount() > 0);
    assertFalse(ifNode2.getLastChild().getChildCount() > 0);
    assertFalse(elseBlock1.getChildCount() > 0);
    assertFalse(elseBlock2.getChildCount() > 0);
    assertTrue(trueBlock.getChildCount() > 0);
    assertTrue(falseBlock.getChildCount() > 0);
  }

  /**
   * Test handling of try/catch/finally statements.
   */
  @Test
  public void testTryCatchFinallyStatements() {
    Node root = Node.createScriptletContext();
    Node tryNode = Node.newTryStatement();
    Node catchNode = Node.newCatchStatement();
    Node finallyNode = Node.newFinallyStatement();
    Node tryCodeBlock = Node.newBlock();
    Node catchCodeBlock = Node.newBlock();
    Node finallyCodeBlock = Node.newBlock();

    tryNode.addChild(tryCodeBlock);
    tryCodeBlock.addChild(new IR(Token.ASSIGN, Node.newIdentifier("x"), Node.newNumberLiteral(1)));
    tryNode.addChild(catchNode);
    catchNode.addChild(catchCodeBlock);
    tryNode.addChild(finallyNode);
    finallyNode.addChild(finallyCodeBlock);

    root.addChild(tryNode);

    NodeTraversal.traverse(compiler, root, minimizer);

    assertTrue(tryCodeBlock.getChildCount() == 0);
    assertTrue(catchCodeBlock.getChildCount() > 0);
    assertTrue(finallyCodeBlock.getChildCount() > 0);
  }
}