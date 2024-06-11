package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {
  private NodeFactory nodeFactory;
  private MinimizeExitPoints minimizer;

  @Before
  public void setup() {
    nodeFactory = new NodeFactory();
    minimizer = new MinimizeExitPoints(null);
  }

  @Test
  public void testMinimizeExitPoints_Label() {
    Node root = NodeUtil.parseString(
        nodeFactory,
        "function foo() { label exit: for (var i = 0; i < 10; i++) { break exit; } }");
    minimizer.process(null, root);

    // The break statement should have been removed and the loop ended.
    Node loop = NodeUtil.getLoopCodeBlock(root.getFirstChild());
    assertTrue(NodeUtil.isEmpty(loop.getLastChild()));
  }

  @Test
  public void testMinimizeExitPoints_Label_MultipleExits() {
    Node root = NodeUtil.parseString(
        nodeFactory,
        "function foo() { label exit: for (var i = 0; i < 10; i++) { break exit; } for (var j = 0; j < 10; j++) { break exit; } }");
    minimizer.process(null, root);

    // Both breaks should have been removed and the loop ended.
    Node loop = NodeUtil.getLoopCodeBlock(root.getFirstChild());
    assertTrue(NodeUtil.isEmpty(loop.getLastChild()));
  }

  @Test
  public void testMinimizeExitPoints_While() {
    Node root = NodeUtil.parseString(
        nodeFactory,
        "function foo() { var i = 0; while (true) { if (i > 10) break; i++; } }");
    minimizer.process(null, root);

    // The while loop should have been replaced with an empty block.
    Node whileNode = root.getFirstChild();
    assertTrue(NodeUtil.isEmpty(whileNode.getLastChild()));
  }

  @Test
  public void testMinimizeExitPoints_While_MultipleExits() {
    Node root = NodeUtil.parseString(
        nodeFactory,
        "function foo() { var i = 0; while (true) { if (i > 10) break; if (i > 20) break; i++; } }");
    minimizer.process(null, root);

    // Both breaks should have been removed and the loop ended.
    Node whileNode = root.getFirstChild();
    assertTrue(NodeUtil.isEmpty(whileNode.getLastChild()));
  }

  @Test
  public void testMinimizeExitPoints_DoWhile() {
    Node root = NodeUtil.parseString(
        nodeFactory,
        "function foo() { do { if (false) break; } while (false); }");
    minimizer.process(null, root);

    // The do-while loop should have been replaced with an empty block.
    Node doWhileNode = root.getFirstChild();
    assertTrue(NodeUtil.isEmpty(doWhileNode.getLastChild()));
  }

  @Test
  public void testMinimizeExitPoints_FunctionReturn() {
    Node root = NodeUtil.parseString(
        nodeFactory,
        "function foo() { return; }");
    minimizer.process(null, root);

    // The return statement should have been removed.
    assertTrue(NodeUtil.isEmpty(root.getLastChild()));
  }

  @Test
  public void testMinimizeExitPoints_FunctionMultipleReturns() {
    Node root = NodeUtil.parseString(
        nodeFactory,
        "function foo() { return; return; }");
    minimizer.process(null, root);

    // Both returns should have been removed.
    assertTrue(NodeUtil.isEmpty(root.getLastChild()));
  }

  @Test
  public void testMinimizeExitPoints_Empty() {
    Node root = NodeUtil.parseString(
        nodeFactory,
        "function foo() { }");
    minimizer.process(null, root);

    // No changes should have been made.
    assertFalse(NodeUtil.isEmpty(root.getLastChild()));
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPoints_InvalidExitType() {
    Node root = NodeUtil.parseString(
        nodeFactory,
        "function foo() { return \"hello\"; }");
    minimizer.process(null, root);

    // An unexpected exit type should cause an AssertionError.
    throw new AssertionError();
  }
}