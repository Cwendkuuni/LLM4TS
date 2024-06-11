package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the MinimizeExitPoints class.
 */
public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    nodeFactory = compiler.getContext().getFactory();
  }

  @Test
  public void testMinimizeExitPointsBasicFunction() {
    Node root = NodeUtil.createFunctionDeclaration(
        "testFunction", NodeUtil.createLabel("testLabel"), "() {}", nodeFactory);
    Node exitNode = NodeUtil.createReturnStatement(nodeFactory);
    Node exitParent = NodeUtil.createExpressionStatement(exitNode, nodeFactory);
    root.addChildToBack(exitParent);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    assertTrue(root.getLastChild() instanceof IR);
    assertFalse(exitParent.getParent() instanceof IR);
  }

  @Test
  public void testMinimizeExitPointsFunctionWithElse() {
    Node root = NodeUtil.createFunctionDeclaration(
        "testFunction", NodeUtil.createLabel("testLabel"), "() {}", nodeFactory);
    Node ifNode = NodeUtil.createIfStatement(
        NodeUtil.createBooleanLiteral(true, nodeFactory),
        NodeUtil.createLabel("ifLabel"), NodeUtil.createBlock(nodeFactory),
        NodeUtil.createLabel("elseLabel"), NodeUtil.createBlock(nodeFactory),
        nodeFactory);
    Node exitNode = NodeUtil.createReturnStatement(nodeFactory);
    Node exitParent = NodeUtil.createExpressionStatement(exitNode, nodeFactory);
    ifNode.getElseBranch().addChildToBack(exitParent);
    root.addChildToBack(ifNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    assertTrue(root.getLastChild() instanceof IR);
    assertTrue(ifNode.getElseBranch().getLastChild() instanceof IR);
    assertFalse(exitParent.getParent() instanceof IR);
  }

  @Test
  public void testMinimizeExitPointsLabel() {
    Node root = NodeUtil.createLabel("testLabel");
    Node exitNode = NodeUtil.createLabel("exitLabel");
    Node exitParent = NodeUtil.createExpressionStatement(
        NodeUtil.createStatement(NodeUtil.createJump(exitNode), nodeFactory),
        nodeFactory);
    root.addChildToBack(exitParent);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    assertTrue(root.getLastChild() instanceof IR);
    assertTrue(exitParent.getParent() instanceof IR);
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPointsUnexpectedExit() {
    Node root = NodeUtil.createFunctionDeclaration(
        "testFunction", NodeUtil.createLabel("testLabel"), "() {}", nodeFactory);
    Node exitNode = NodeUtil.createExpressionStatement(
        NodeUtil.createStatement(NodeUtil.createReturnStatement(nodeFactory), nodeFactory),
        nodeFactory);
    root.addChildToBack(exitNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    // This assertion should fail as the exit point has been removed.
    assertTrue(exitNode.getParent() instanceof IR);
  }

  @Test
  public void testMinimizeExitPointsNestedLabel() {
    Node root = NodeUtil.createLabel("outerLabel");
    Node innerLabel = NodeUtil.createLabel("innerLabel");
    Node exitNode = NodeUtil.createExpressionStatement(
        NodeUtil.createStatement(NodeUtil.createJump(innerLabel), nodeFactory),
        nodeFactory);
    Node exitParent = NodeUtil.createExpressionStatement(
        NodeUtil.createStatement(NodeUtil.createJump(innerLabel), nodeFactory),
        nodeFactory);
    root.addChildToBack(exitParent);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    assertTrue(root.getLastChild() instanceof IR);
    assertTrue(exitParent.getParent() instanceof IR);
    assertTrue(exitParent.getLastChild().getValue().isLabel());
    assertTrue(exitParent.getLastChild().getValue().getName().equals("innerLabel"));
  }

  @Test
  public void testMinimizeExitPointsSwitchStatement() {
    Node root = NodeUtil.createSwitchStatement(
        NodeUtil.createExpressionStatement(NodeUtil.createBinaryOperator(
            NodeUtil.createIdentifier("x"), Token.EQ, NodeUtil.createNumber(0),
            nodeFactory), nodeFactory), nodeFactory);
    Node caseNode = NodeUtil.createSwitchCaseStatement(
        NodeUtil.createLabel("caseLabel"), NodeUtil.createBlock(nodeFactory),
        nodeFactory);
    Node exitNode = NodeUtil.createExpressionStatement(
        NodeUtil.createStatement(NodeUtil.createReturnStatement(nodeFactory), nodeFactory),
        nodeFactory);
    caseNode.addChildToBack(exitNode);
    root.getCaseValue().addChildToBack(caseNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    assertTrue(root.getLastChild() instanceof IR);
    assertFalse(exitNode.getParent() instanceof IR);
  }
}