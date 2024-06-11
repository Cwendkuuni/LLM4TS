package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MinimizeExitPoints.
 */
public class MinimizeExitPointsTest {

  private NodeFactory nodeFactory;
  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizer;

  @Before
  public void setUp() {
    compiler = new CompilerPassManager().getPassManager().getFactory()
        .newAbstractCompiler();
    nodeFactory = compiler.getFactory();
    minimizer = new MinimizeExitPoints(compiler);
  }

  /**
   * Test for method process(externs, root).
   */
  @Test
  public void testProcess() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    minimizer.process(null, root);
  }

  /**
   * Test for method visit(NodeTraversal, Node, Node).
   */
  @Test
  public void testVisit() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node n = NodeUtil.createLabelNode("label1", nodeFactory);
    Node breakStatement = NodeUtil.createBreakStatement(nodeFactory);
    n.addChildToBack(breakStatement);
    root.addChildToBack(n);

    minimizer.process(null, root);

    Node parent = n.getParent();
    assertTrue(parent.getLastChild() == breakStatement);
    assertNull(parent.getLastChild().getNext());
  }

  /**
   * Test tryMinimizeExits with multiple break statements in a label block.
   */
  @Test
  public void testTryMinimizeExitsWithMultipleBreaksInLabelBlock() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node labelNode = NodeUtil.createLabelNode("label1", nodeFactory);
    Node breakStatement1 = NodeUtil.createBreakStatement(nodeFactory);
    Node ifNode1 = NodeUtil.createIfStatement(nodeFactory);
    Node ifBody1 = NodeUtil.createBlockNode(nodeFactory);
    breakStatement1.addChildToBack(ifBody1);
    Node breakStatement2 = NodeUtil.createBreakStatement(nodeFactory);
    Node ifBody2 = NodeUtil.createBlockNode(nodeFactory);
    breakStatement2.addChildToBack(ifBody2);
    ifNode1.addChildToBack(breakStatement1);
    ifBody1.addChildToBack(ifNode1);
    labelNode.addChildToBack(ifNode1);
    root.addChildToBack(labelNode);

    minimizer.process(null, root);

    Node parent = labelNode.getParent();
    assertNull(parent.getLastChild());

    Node ifNode = parent.getFirstChild();
    assertTrue(ifNode.getNext() != null);

    Node ifBody = ifNode.getNext();
    assertTrue(ifBody.getChildCount() == 2);
  }

  /**
   * Test tryMinimizeExits with multiple continue statements in a loop block.
   */
  @Test
  public void testTryMinimizeExitsWithMultipleContinuesInLoopBlock() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node forNode = NodeUtil.createForStatement(nodeFactory);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(forNode);
    Node continueStatement1 = NodeUtil.createContinueStatement(nodeFactory);
    Node continueStatement2 = NodeUtil.createContinueStatement(nodeFactory);
    Node ifNode1 = NodeUtil.createIfStatement(nodeFactory);
    Node ifBody1 = NodeUtil.createBlockNode(nodeFactory);
    continueStatement1.addChildToBack(ifBody1);
    Node ifNode2 = NodeUtil.createIfStatement(nodeFactory);
    Node ifBody2 = NodeUtil.createBlockNode(nodeFactory);
    continueStatement2.addChildToBack(ifBody2);
    ifNode1.addChildToBack(continueStatement1);
    ifBody1.addChildToBack(ifNode1);
    loopCodeBlock.addChildToBack(continueStatement1);
    ifNode2.addChildToBack(continueStatement2);
    ifBody2.addChildToBack(ifNode2);
    loopCodeBlock.addChildToBack(ifNode2);
    forNode.addChildToBack(loopCodeBlock);
    root.addChildToBack(forNode);

    minimizer.process(null, root);

    Node parent = forNode.getParent();
    Node ifNode = parent.getFirstChild();
    assertTrue(ifNode.getNext() != null);

    Node ifBody = ifNode.getNext();
    assertTrue(ifBody.getChildCount() == 2);
  }

  /**
   * Test tryMinimizeExits with a function return statement.
   */
  @Test
  public void testTryMinimizeExitsWithFunctionReturnStatement() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node functionNode = NodeUtil.createFunctionDeclaration(
        "myFunction", null, null, null, null, null, nodeFactory);
    Node returnStatement = NodeUtil.createReturnStatement(nodeFactory);
    Node functionBody = functionNode.getLastChild();
    functionBody.addChildToBack(returnStatement);
    root.addChildToBack(functionNode);

    minimizer.process(null, root);

    Node parent = functionNode.getParent();
    assertTrue(parent.getLastChild() == returnStatement);
    assertNull(parent.getLastChild().getNext());
  }

  /**
   * Test tryMinimizeExits with a try/catch block.
   */
  @Test
  public void testTryMinimizeExitsWithTryCatchBlock() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node tryNode = NodeUtil.createTryStatement(nodeFactory);
    Node tryBlock = NodeUtil.getTryBlock(tryNode);
    Node catchNode = NodeUtil.createCatchStatement(nodeFactory);
    Node catchCodeBlock = NodeUtil.getCatchBlock(catchNode);
    Node exitStatement = NodeUtil.createReturnStatement(nodeFactory);
    Node functionNode = NodeUtil.createFunctionDeclaration(
        "myFunction", null, null, null, null, null, nodeFactory);
    Node functionBody = functionNode.getLastChild();
    functionBody.addChildToBack(exitStatement);
    tryBlock.addChildToBack(functionNode);
    catchCodeBlock.addChildToBack(exitStatement);
    tryNode.setFirstChild(tryBlock);
    root.addChildToBack(tryNode);

    minimizer.process(null, root);

    Node parent = tryNode.getParent();
    Node tryBlockParent = tryBlock.getParent();
    assertTrue(tryBlockParent.getLastChild() == functionNode);
    assertNull(tryBlockParent.getLastChild().getNext());

    Node catchCodeBlockParent = catchCodeBlock.getParent();
    assertTrue(catchCodeBlockParent.getLastChild() == functionNode);
    assertNull(catchCodeBlockParent.getLastChild().getNext());
  }

  /**
   * Test tryMinimizeExits with an empty if statement.
   */
  @Test
  public void testTryMinimizeExitsWithEmptyIfStatement() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node ifNode = NodeUtil.createIfStatement(nodeFactory);
    Node ifBlock = ifNode.getFirstChild();
    root.addChildToBack(ifNode);

    minimizer.process(null, root);

    Node parent = ifNode.getParent();
    assertNull(parent.getLastChild());
  }

  /**
   * Test tryMinimizeExits with a for loop containing an empty if statement.
   */
  @Test
  public void testTryMinimizeExitsWithForLoopContainingEmptyIfStatement() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node forNode = NodeUtil.createForStatement(nodeFactory);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(forNode);
    Node ifNode = NodeUtil.createIfStatement(nodeFactory);
    Node ifBlock = ifNode.getFirstChild();
    loopCodeBlock.addChildToBack(ifNode);
    root.addChildToBack(forNode);

    minimizer.process(null, root);

    Node parent = forNode.getParent();
    assertNull(parent.getLastChild());
  }

  /**
   * Test tryMinimizeExits with a for loop containing a single statement that is
   * not an exit.
   */
  @Test
  public void testTryMinimizeExitsWithForLoopContainingSingleStatement() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node forNode = NodeUtil.createForStatement(nodeFactory);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(forNode);
    Node statementNode = NodeUtil.createExpressionStatement(nodeFactory);
    loopCodeBlock.addChildToBack(statementNode);
    root.addChildToBack(forNode);

    minimizer.process(null, root);

    Node parent = forNode.getParent();
    assertNull(parent.getLastChild());
  }

  /**
   * Test tryMinimizeExits with a return statement within an if statement.
   */
  @Test
  public void testTryMinimizeExitsWithReturnStatementWithinIfStatement() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node ifNode = NodeUtil.createIfStatement(nodeFactory);
    Node ifBlock = ifNode.getFirstChild();
    Node returnStatement = NodeUtil.createReturnStatement(nodeFactory);
    ifBlock.addChildToBack(returnStatement);
    Node ifElseNode = NodeUtil.createElseIfStatement(nodeFactory);
    Node ifElseBlock = ifElseNode.getFirstChild();
    ifElseBlock.addChildToBack(NodeUtil.createExpressionStatement(nodeFactory));
    ifNode.addChildToBack(ifElseNode);
    root.addChildToBack(ifNode);

    minimizer.process(null, root);

    Node parent = ifNode.getParent();
    Node ifElseNodeParent = ifElseNode.getParent();
    assertNull(parent.getLastChild());
    assertTrue(ifElseNodeParent.getLastChild() == ifNode);
  }

  /**
   * Test tryMinimizeExits with a return statement within a for loop.
   */
  @Test
  public void testTryMinimizeExitsWithReturnStatementWithinForLoop() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node forNode = NodeUtil.createForStatement(nodeFactory);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(forNode);
    Node returnStatement = NodeUtil.createReturnStatement(nodeFactory);
    loopCodeBlock.addChildToBack(returnStatement);
    root.addChildToBack(forNode);

    minimizer.process(null, root);

    Node parent = forNode.getParent();
    assertTrue(parent.getLastChild() == returnStatement);
    assertNull(parent.getLastChild().getNext());
  }

  /**
   * Test tryMinimizeExits with a continue statement within a for loop.
   */
  @Test
  public void testTryMinimizeExitsWithContinueStatementWithinForLoop() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node forNode = NodeUtil.createForStatement(nodeFactory);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(forNode);
    Node continueStatement = NodeUtil.createContinueStatement(nodeFactory);
    loopCodeBlock.addChildToBack(continueStatement);
    root.addChildToBack(forNode);

    minimizer.process(null, root);

    Node parent = forNode.getParent();
    assertNull(parent.getLastChild());
  }

  /**
   * Test tryMinimizeExits with a break statement within a label.
   */
  @Test
  public void testTryMinimizeExitsWithBreakStatementWithinLabel() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node labelNode = NodeUtil.createLabelStatement(nodeFactory, "label1");
    Node breakStatement = NodeUtil.createBreakStatement(nodeFactory);
    Node ifNode = NodeUtil.createIfStatement(nodeFactory);
    Node ifBody = ifNode.getFirstChild();
    Node forNode = NodeUtil.createForStatement(nodeFactory);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(forNode);
    loopCodeBlock.addChildToBack(ifNode);
    Node labelBlock = NodeUtil.createBlockNode(nodeFactory);
    labelBlock.addChildToBack(labelNode);
    labelBlock.addChildToBack(forNode);
    labelNode.addChildToBack(labelBlock);
    root.addChildToBack(labelNode);

    minimizer.process(null, root);

    Node parent = labelNode.getParent();
    assertNull(parent.getLastChild());
  }

  /**
   * Test tryMinimizeExits with a break statement without a label.
   */
  @Test(expected = AssertionError.class)
  public void testTryMinimizeExitsWithBreakStatementWithoutLabel() {
    Node root = NodeUtil.createRootNode(nodeFactory);
    Node labelNode = NodeUtil.createLabelStatement(nodeFactory, "label1");
    Node breakStatement = NodeUtil.createBreakStatement(nodeFactory);
    Node ifNode = NodeUtil.createIfStatement(nodeFactory);
    Node ifBody = ifNode.getFirstChild();
    Node forNode = NodeUtil.createForStatement(nodeFactory);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(forNode);
    loopCodeBlock.addChildToBack(ifNode);
    Node labelBlock = NodeUtil.createBlockNode(nodeFactory);
    labelBlock.addChildToBack(labelNode);
    labelBlock.addChildToBack(forNode);
    root.addChildToBack(labelBlock);

    minimizer.process(null, root);

    Preconditions.checkState(false); // Should not reach here
  }
}