package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;

@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  private Compiler compiler;
  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setUp() {
    compiler = new Compiler();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeExitPoints_Label() {
    Node root = new IR("function() {}").toNode();
    Node labelNode = Node.newLabel("label1");
    root.addChildToBack(labelNode);
    Node breakNode = Node.newExpression(Token.BREAK, labelNode);
    labelNode.addChildToBack(NodeUtil.newBlock(breakNode));

    minimizeExitPoints.process(NodeUtil.createExterns(), root);
    Node modifiedRoot = compiler.getOutput();

    assertEquals(0, modifiedRoot.getNumChildren());
  }

  @Test
  public void testMinimizeExitPoints_Loop() {
    Node root = new IR("function() {}").toNode();
    Node forNode = new IR("for (;;) {}").toNode();
    root.addChildToBack(forNode);
    Node continueNode = Node.newExpression(Token.CONTINUE);
    Node continueBlock = NodeUtil.newBlock(continueNode);
    forNode.addChildToBack(continueBlock);

    minimizeExitPoints.process(NodeUtil.createExterns(), root);
    Node modifiedRoot = compiler.getOutput();

    assertEquals(0, modifiedRoot.getNumChildren());
  }

  @Test
  public void testMinimizeExitPoints_DoWhileLoop() {
    Node root = new IR("function() {}").toNode();
    Node doWhileNode = Node.newExpression(Token.DO, null, null);
    Node conditionNode = Node.newExpression(Token.FALSE);
    Node loopCodeBlock = NodeUtil.newBlock(NodeUtil.newExpression(Token.WHILE, conditionNode, null));
    Node continueNode = Node.newExpression(Token.CONTINUE);
    Node continueBlock = NodeUtil.newBlock(continueNode);
    Node breakNode = Node.newExpression(Token.BREAK);
    Node breakBlock = NodeUtil.newBlock(breakNode);
    Node loopBlock = NodeUtil.newBlock(NodeUtil.newExpression(Token.WHILE, conditionNode, null), loopCodeBlock, continueBlock, breakBlock);
    doWhileNode.addChildToBack(loopBlock);
    root.addChildToBack(doWhileNode);

    minimizeExitPoints.process(NodeUtil.createExterns(), root);
    Node modifiedRoot = compiler.getOutput();

    assertEquals(0, modifiedRoot.getNumChildren());
  }

  @Test
  public void testMinimizeExitPoints_Function() {
    Node root = new IR("function() {}").toNode();
    Node functionNode = Node.newExpression(Token.FUNCTION, null, null, NodeUtil.createStatementList(NodeUtil.newReturnStatement(NodeUtil.newExpression(Token.INT_LITERAL, 1))));
    root.addChildToBack(functionNode);

    minimizeExitPoints.process(NodeUtil.createExterns(), root);
    Node modifiedRoot = compiler.getOutput();

    Node functionNodeInOutput = modifiedRoot.getLastChild();
    assertEquals(NodeUtil.createStatementList(NodeUtil.newExpression(Token.RETURN, NodeUtil.newExpression(Token.INT_LITERAL, 1))), functionNodeInOutput.getFirstChild());
  }

  @Test(expected = NullPointerException.class)
  public void testMinimizeExitPoints_NullParameter() {
    minimizeExitPoints.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMinimizeExitPoints_NullRoot() {
    minimizeExitPoints.process(NodeUtil.createExterns(), null);
  }

  @Test
  public void testMinimizeExitPoints_NestedIfBlocks() {
    Node root = new IR("function() { if (true) { if (true) {} } }").toNode();
    minimizeExitPoints.process(NodeUtil.createExterns(), root);
    Node modifiedRoot = compiler.getOutput();

    Node ifStatement = modifiedRoot.getLastChild();
    assertEquals(NodeUtil.createBlock(NodeUtil.createStatementList(NodeUtil.newExpression(Token.IF, NodeUtil.newExpression(Token.TRUE), NodeUtil.newBlock(NodeUtil.newStatementList())))), ifStatement);
  }
}