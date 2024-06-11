package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TernaryValue;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

/**
 * Tests for the MinimizeExitPoints class.
 */
@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    // Given a simple node
    Node root = IR.block().srcref(NodeUtil.createNodeFromString(
        "function foo() { if (true) return; if (false) break; }"));

    // When passing the node to MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with multiple exits
    root = IR.block().srcref(NodeUtil.createNodeFromString(
        "function foo() { if (true) return; if (false) return; }"));

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should only have one exit
    Node lastChild = root.getLastChild();
    assertTrue(NodeUtil.isReturnStatement(lastChild));

    // Given a node with a labeled break
    root = IR.block().srcref(NodeUtil.createNodeFromString(
        "foo: function foo() { if (true) break foo; }"));

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a loop and a continue
    root = NodeUtil.createNodeFromString(
        "function foo() { for (var i = 0; i < 10; i++) {" +
        " if (i % 2 == 0) continue;" +
        " } }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a loop and a labeled break
    root = NodeUtil.createNodeFromString(
        "foo: function foo() {" +
        " for (var i = 0; i < 10; i++) {" +
        " if (i % 2 == 0) break foo;" +
        " } }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a try-catch-finally block and a return
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " try {" +
        " return; " +
        " } catch (e) {" +
        " } finally {" +
        " return; " +
        " }" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should only have one exit
    lastChild = root.getLastChild();
    assertTrue(NodeUtil.isReturnStatement(lastChild));

    // Given a node with a try-catch block and a continue
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " try {" +
        " for (var i = 0; i < 10; i++) {" +
        " if (i % 2 == 0) continue;" +
        " } " +
        " } catch (e) {}" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a try-finally block and a return
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " try {" +
        " return; " +
        " } finally {" +
        " return; " +
        " }" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should only have one exit
    lastChild = root.getLastChild();
    assertTrue(NodeUtil.isReturnStatement(lastChild));

    // Given a node with a try-catch-finally block and a labeled break
    root = NodeUtil.createNodeFromString(
        "foo: function foo() {" +
        " try {" +
        " for (var i = 0; i < 10; i++) {" +
        " if (i % 2 == 0) break foo;" +
        " } " +
        " } catch (e) {" +
        " } finally {" +
        " }" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a switch statement and a break
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " switch (true) {" +
        " case 0:" +
        " break;" +
        " }" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a function declaration and a return
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " return;" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should only have one exit
    lastChild = root.getLastChild();
    assertTrue(NodeUtil.isReturnStatement(lastChild));

    // Given a node with a function declaration and a throw
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " throw new Error();" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a function declaration and a continue
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " for (var i = 0; i < 10; i++) {" +
        " if (i % 2 == 0) continue;" +
        " }" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a do-while loop and a return
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " do {" +
        " if (false) return;" +
        " } while (true);" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a do-while loop and a continue
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " do {" +
        " if (i % 2 == 0) continue;" +
        " } while (true);" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a for-in loop and a return
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " for (var prop in obj) {" +
        " if (prop == 'propName') return;" +
        " }" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);

    // Given a node with a for-in loop and a continue
    root = NodeUtil.createNodeFromString(
        "function foo() {" +
        " for (var prop in obj) {" +
        " if (prop == 'propName') continue;" +
        " }" +
        " }");

    // When passing the node to MinimizeExitPoints
    minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then the returned node should not have any exits
    assertTrue(NodeUtil.isBlock(root) && root.getLastChild() == null);
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPointsThrowsOnInvalidNodeType() {
    Node root = IR.block().srcref(NodeUtil.createNodeFromString(
        "function foo() {" +
        " // invalid node type" +
        " }"));

    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then an AssertionError should be thrown
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPointsThrowsOnInvalidNodeParentType() {
    Node root = IR.block().srcref(NodeUtil.createNodeFromString(
        "function foo() {" +
        " // valid node, but invalid parent type" +
        " return; " +
        " }"));

    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then an AssertionError should be thrown
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPointsThrowsOnInvalidLabelName() {
    Node root = IR.block().srcref(NodeUtil.createNodeFromString(
        "foo:function foo() {" +
        " if (true) break bar; " +
        " }"));

    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then an AssertionError should be thrown
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPointsThrowsOnInvalidExitType() {
    Node root = IR.block().srcref(NodeUtil.createNodeFromString(
        "function foo() {" +
        " if (true) throw new Error();" +
        " }"));

    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(null, root, minimizer);

    // Then an AssertionError should be thrown
  }
}