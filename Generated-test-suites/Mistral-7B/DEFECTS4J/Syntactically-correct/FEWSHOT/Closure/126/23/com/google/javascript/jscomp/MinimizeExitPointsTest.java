package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    Node root = Node.parse("function foo() {"
        + "if (x) return;"
        + "else if (y) return;"
        + "bar();"
        + "}");

    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(minimizer, root, null);

    assertTrue(NodeUtil.isFunctionDeclaration(root.getFirstChild()));
    Node ifBlock = root.getFirstChild().getNext();
    assertTrue(ifBlock.isBlock());
    Node elseBlock = ifBlock.getNext();
    assertTrue(elseBlock != null);
    assertTrue(elseBlock.isBlock());

    // Check that 'bar()' has been moved into the else block.
    Node barNode = null;
    for (Node n : elseBlock.children()) {
      if (n.isExpressionStatement()) {
        if (n.getExpression().isCall()) {
          barNode = n;
          break;
        }
      }
    }
    assertTrue(barNode != null);
    assertEquals(1, elseBlock.children().size());

    // Check that there are no 'return' statements left.
    assertFalse(NodeUtil.hasReturn(root));
  }

  @Test
  public void testMinimizeExitPointsWithBreak() {
    Node root = Node.parse("function foo() {"
        + "label1: {"
        + "  if (x) break label1;"
        + "  else if (y) break label1;"
        + "  bar();"
        + "}"
        + "}");

    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(minimizer, root, null);

    assertTrue(NodeUtil.isFunctionDeclaration(root.getFirstChild()));
    Node ifBlock = root.getFirstChild().getNext();
    assertTrue(ifBlock.isBlock());
    Node elseBlock = ifBlock.getNext();
    assertTrue(elseBlock != null);
    assertTrue(elseBlock.isBlock());

    // Check that 'bar()' has been moved into the else block.
    Node barNode = null;
    for (Node n : elseBlock.children()) {
      if (n.isExpressionStatement()) {
        if (n.getExpression().isCall()) {
          barNode = n;
          break;
        }
      }
    }
    assertTrue(barNode != null);
    assertEquals(1, elseBlock.children().size());

    // Check that there are no 'break' statements left.
    assertNull(NodeUtil.getLabelByName("label1", root));
  }

  @Test
  public void testMinimizeExitPointsWithNoExplicitExits() {
    Node root = Node.parse("function foo() {"
        + "if (x) {"
        + "  if (y) {"
        + "    if (z) {"
        + "      return;"
        + "    }"
        + "  }"
        + "}");

    MinimizeExitPoints minimizer = new MinimizeExitPoints(null);
    NodeTraversal.traverse(minimizer, root, null);

    assertTrue(NodeUtil.isFunctionDeclaration(root.getFirstChild()));
    Node ifBlock = root.getFirstChild().getNext();
    assertTrue(ifBlock.isBlock());
    Node innerIfBlock = ifBlock.getFirstChild().getNext();
    assertTrue(innerIfBlock.isBlock());
    Node innerReturnNode = innerIfBlock.getLastChild();
    assertTrue(innerReturnNode.isReturn());

    // Check that there is no implicit exit (return) at the end of the function.
    assertFalse(NodeUtil.hasReturn(root.getLastChild()));
  }
}