package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
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
 * Unit tests for the MinimizeExitPoints class.
 */
public class MinimizeExitPointsTest {

  private NodeFactory nodeFactory;
  private MinimizeExitPoints minimizer;
  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
    nodeUtil = new NodeUtil();
    minimizer = new MinimizeExitPoints(null);
  }

  /**
   * Test that a simple return statement is removed when possible.
   */
  @Test
  public void testSimpleReturn() {
    Node root = NodeUtil.parseJavaScript(
        "function test() { return 42; }", nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = IR.block().add(
        NodeUtil.parseJavaScript("{}", nodeFactory));

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a simple break statement is removed when possible.
   */
  @Test
  public void testSimpleBreak() {
    Node root = NodeUtil.parseJavaScript(
        "function test(label) { break label; }", nodeFactory);

    Node label = NodeUtil.parseJavaScript("label:", nodeFactory);
    Node labelBlock = NodeUtil.parseJavaScript("{ foo(); }", nodeFactory);

    root.addChildToBack(label);
    root.addChildToBack(labelBlock);

    NodeTraversal.traverse(minimizer, root, label);

    Node expected = NodeUtil.parseJavaScript(
        "function test(label) { foo(); }", nodeFactory);

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a simple continue statement is removed when possible.
   */
  @Test
  public void testSimpleContinue() {
    Node root = NodeUtil.parseJavaScript(
        "function test() { for (var i = 0; i < 10; i++) { continue; } }",
        nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = IR.block().add(
        NodeUtil.parseJavaScript("{}", nodeFactory));

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a return statement with an expression is not removed.
   */
  @Test
  public void testReturnExpression() {
    Node root = NodeUtil.parseJavaScript(
        "function test() { return 42 + 2; }", nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = root;

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a break statement with a label is not removed when there is no
   * corresponding if statement.
   */
  @Test
  public void testInvalidBreak() {
    Node root = NodeUtil.parseJavaScript(
        "function test(label) { break label; }", nodeFactory);

    Node label = NodeUtil.parseJavaScript("label:", nodeFactory);

    root.addChildToBack(label);

    NodeTraversal.traverse(minimizer, root, label);

    Node expected = root;

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a break statement is removed when it is the only exit point from
   * a block.
   */
  @Test
  public void testBreakOnlyExit() {
    Node root = NodeUtil.parseJavaScript(
        "function test(label) { for (var i = 0; i < 10; i++) { if (i == 5) break label; } }"
        + "label: {}", nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = IR.block().add(
        NodeUtil.parseJavaScript(
            "function test(label) { for (var i = 0; i < 10; i++) { } }", nodeFactory));

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a continue statement is removed when it is the only exit point from
   * a loop block.
   */
  @Test
  public void testContinueOnlyExit() {
    Node root = NodeUtil.parseJavaScript(
        "function test() { for (var i = 0; i < 10; i++) { if (i % 2 == 0) continue; } }",
        nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = IR.block().add(
        NodeUtil.parseJavaScript(
            "function test() { for (var i = 0; i < 10; i += 2) { } }", nodeFactory));

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a break statement within a loop is removed when there is an if statement
   * that will always evaluate to false.
   */
  @Test
  public void testBreakInsideFalseIf() {
    Node root = NodeUtil.parseJavaScript(
        "function test() { for (var i = 0; i < 10; i++) { if (false) break; } }",
        nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = IR.block().add(
        NodeUtil.parseJavaScript(
            "function test() { for (var i = 0; i < 10; i++) { }}", nodeFactory));

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a break statement within a loop is not removed when there is no if
   * statement or the if statement can evaluate to true.
   */
  @Test
  public void testBreakInsideTrueIf() {
    Node root = NodeUtil.parseJavaScript(
        "function test() { var x = 5; for (var i = 0; i < 10; i++) { if (x > 4) break; } }",
        nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = root;

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a break statement with a label is removed when there is a corresponding
   * if statement and it always evaluates to false.
   */
  @Test
  public void testLabeledBreakInsideFalseIf() {
    Node root = NodeUtil.parseJavaScript(
        "function test() {"
        + "label: for (var i = 0; i < 10; i++) {"
        + " if (false) break label;"
        + "}"
        + "}", nodeFactory);

    Node label = NodeUtil.parseJavaScript("label:", nodeFactory);

    NodeTraversal.traverse(minimizer, root, label);

    Node expected = NodeUtil.parseJavaScript(
        "function test() {"
        + "for (var i = 0; i < 10; i++) {"
        + "}"
        + "}", nodeFactory);

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a break statement with a label is not removed when there is a corresponding
   * if statement and it can evaluate to true.
   */
  @Test
  public void testLabeledBreakInsideTrueIf() {
    Node root = NodeUtil.parseJavaScript(
        "function test() {"
        + "label: for (var i = 0; i < 10; i++) {"
        + " if (i % 2 == 0) break label;"
        + "}"
        + "}", nodeFactory);

    Node label = NodeUtil.parseJavaScript("label:", nodeFactory);

    NodeTraversal.traverse(minimizer, root, label);

    Node expected = root;

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a return statement within a function is not removed when there is an
   * if statement that can evaluate to true.
   */
  @Test
  public void testReturnInsideTrueIf() {
    Node root = NodeUtil.parseJavaScript(
        "function test() {"
        + "if (true) return;"
        + "else return 42;"
        + "}", nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = root;

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a return statement within a function is removed when there is an
   * if statement that always evaluates to false.
   */
  @Test
  public void testReturnInsideFalseIf() {
    Node root = NodeUtil.parseJavaScript(
        "function test() {"
        + "if (false) return;"
        + "else return 42;"
        + "}", nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = NodeUtil.parseJavaScript(
        "function test() { return 42; }", nodeFactory);

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a continue statement within a loop is not removed when there is an
   * if statement that can evaluate to true.
   */
  @Test
  public void testContinueInsideTrueIf() {
    Node root = NodeUtil.parseJavaScript(
        "function test() {"
        + "for (var i = 0; i < 10; i++) {"
        + " if (i % 2 != 0) continue;"
        + "}"
        + "}", nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = root;

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a continue statement within a loop is not removed when there is no if
   * statement.
   */
  @Test
  public void testContinueWithNoIf() {
    Node root = NodeUtil.parseJavaScript(
        "function test() {"
        + "for (var i = 0; i < 10; i++) {"
        + " continue;"
        + "}"
        + "}", nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = root;

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a continuing statement within a loop is removed when there is an if
   * statement that always evaluates to false.
   */
  @Test
  public void testContinuingInsideFalseIf() {
    Node root = NodeUtil.parseJavaScript(
        "function test() {"
        + "for (var i = 0; i < 10; i++) {"
        + " if (false) continue;"
        + "}"
        + "}", nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = NodeUtil.parseJavaScript(
        "function test() {"
        + "for (var i = 0; i < 10; i++) {"
        + "}"
        + "}", nodeFactory);

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }

  /**
   * Test that a continuing statement within a loop is not removed when there is an
   * if statement that evaluates to true for some iterations.
   */
  @Test
  public void testContinuingWithTrueIf() {
    Node root = NodeUtil.parseJavaScript(
        "function test() {"
        + "for (var i = 0; i < 10; i++) {"
        + " if (i % 2 == 0) continue;"
        + "}"
        + "}", nodeFactory);

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = root;

    assertTrue(NodeUtil.areDeeplyEqual(root, expected));
  }
}