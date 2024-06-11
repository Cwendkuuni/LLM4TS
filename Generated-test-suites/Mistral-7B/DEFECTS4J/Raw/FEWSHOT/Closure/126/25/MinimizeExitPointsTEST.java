package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TernaryValue;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal.AbstractPostOrderCallback;

/**
 * Unit tests for the MinimizeExitPoints class.
 */
@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    Node sourceNode = createSourceCode("testMinimizeExitPoints");

    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    NodeTraversal traversal = new NodeTraversal(minimizer, null);
    NodeUtil.traverse(traversal, sourceNode);

    assertMinimizedExitPoints(sourceNode);
  }

  /**
   * Asserts that the given source code has been minimized as expected.
   * @param sourceNode The root node of the source code.
   */
  private void assertMinimizedExitPoints(Node sourceNode) {
    // The root node must be a function node.
    assertTrue(NodeUtil.isFunctionDeclaration(sourceNode));

    // The function node's body should only contain a single block node.
    Node functionBody = sourceNode.getFirstChild();
    assertTrue(functionBody.isBlock());

    // The block node should contain no exit points.
    Node lastNode = functionBody.getLastChild();
    assertNull(findExitPoint(lastNode, Token.RETURN));
    assertNull(findExitPoint(lastNode, Token.BREAK));
    assertNull(findExitPoint(lastNode, Token.CONTINUE));
  }

  /**
   * Finds the first occurrence of an exit point (return, break, or continue)
   * in the given node.
   * @param node The node to search.
   * @param exitType The type of exit point to look for.
   * @return The node containing the exit point, or null if not found.
   */
  private Node findExitPoint(Node node, int exitType) {
    if (node.getType() == exitType) {
      return node;
    }

    if (node.isBlock()) {
      for (Node childNode : node.children()) {
        Node exitNode = findExitPoint(childNode, exitType);
        if (exitNode != null) {
          return exitNode;
        }
      }
    }

    return null;
  }

  /**
   * Creates a Node object representing the given JavaScript source code.
   * @param name The name of the source code.
   * @return The root node of the source code.
   */
  private Node createSourceCode(String name) {
    String javaScriptSource = String.format(
        "function %s() {%n" +
        "  if (true) return;%n" +
        "  if (false) break;%n" +
        "  if (true) continue;%n" +
        "  // Some unrelated statements%n" +
        "}", name);

    return IR.parseStatementList(javaScriptSource);
  }
}