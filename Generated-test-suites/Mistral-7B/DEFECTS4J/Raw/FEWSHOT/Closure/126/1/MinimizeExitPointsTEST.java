package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.Compiler;
import com.google.javascript.jscomp.compiler.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    Compiler compiler = new Compiler();
    Node root = IR.program().srcref(1);

    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(null, root);

    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node n, Node parent) {
        if (n.isLabel()) {
          Node codeBlock = n.getLastChild();
          int exitType = Token.BREAK;
          String labelName = n.getString();
          minimizeExits(traversal, codeBlock, exitType, labelName);
        }
      }
    });

    // Test for label with break statement.
    Node labelNode = root.getFirstChild().getNext();
    Node labelCodeBlock = labelNode.getLastChild();
    assertFalse(labelCodeBlock.hasChild(Token.BREAK));
    Node breakNode = labelCodeBlock.getFirstChild();
    assertTrue(breakNode.getType() == Token.BREAK);
    assertNull(breakNode.getParent());

    // Test for label with continue statement.
    labelNode = root.getFirstChild().getNext().getNext();
    labelCodeBlock = labelNode.getLastChild();
    assertFalse(labelCodeBlock.hasChild(Token.CONTINUE));
    breakNode = labelCodeBlock.getFirstChild();
    assertTrue(breakNode.getType() == Token.CONTINUE);
    assertNull(breakNode.getParent());

    // Test for label with return statement.
    labelNode = root.getFirstChild().getNext().getNext().getNext();
    labelCodeBlock = labelNode.getLastChild();
    assertFalse(labelCodeBlock.hasChild(Token.RETURN));
    Node returnNode = labelCodeBlock.getFirstChild();
    assertTrue(returnNode.getType() == Token.RETURN);
    assertNull(returnNode.getParent());

    // Test for if statement with break statement.
    labelNode = root.getLastChild().getNext();
    labelCodeBlock = labelNode.getLastChild();
    minimizeExits(null, labelCodeBlock, exitType, labelName);
    assertFalse(labelCodeBlock.hasChild(Token.BREAK));
    breakNode = labelCodeBlock.getFirstChild();
    assertTrue(breakNode.getType() == Token.BREAK);
    assertNull(breakNode.getParent());

    // Test for if statement with continue statement.
    labelNode = labelNode.getNext();
    labelCodeBlock = labelNode.getLastChild();
    minimizeExits(null, labelCodeBlock, exitType, labelName);
    assertFalse(labelCodeBlock.hasChild(Token.CONTINUE));
    breakNode = labelCodeBlock.getFirstChild();
    assertTrue(breakNode.getType() == Token.CONTINUE);
    assertNull(breakNode.getParent());

    // Test for if statement with return statement.
    labelNode = labelNode.getNext();
    labelCodeBlock = labelNode.getLastChild();
    minimizeExits(null, labelCodeBlock, exitType, labelName);
    assertFalse(labelCodeBlock.hasChild(Token.RETURN));
    returnNode = labelCodeBlock.getFirstChild();
    assertTrue(returnNode.getType() == Token.RETURN);
    assertNull(returnNode.getParent());

    // Test for for statement with break statement.
    labelNode = root.getFirstChild().getNext().getNext().getNext().getNext();
    labelCodeBlock = labelNode.getLastChild();
    minimizeExits(null, labelCodeBlock, exitType, labelName);
    assertFalse(labelCodeBlock.hasChild(Token.BREAK));
    breakNode = labelCodeBlock.getFirstChild();
    assertTrue(breakNode.getType() == Token.BREAK);
    assertNull(breakNode.getParent());

    // Test for for statement with continue statement.
    labelNode = labelNode.getNext();
    labelCodeBlock = labelNode.getLastChild();
    minimizeExits(null, labelCodeBlock, exitType, labelName);
    assertFalse(labelCodeBlock.hasChild(Token.CONTINUE));
    breakNode = labelCodeBlock.getFirstChild();
    assertTrue(breakNode.getType() == Token.CONTINUE);
    assertNull(breakNode.getParent());

    // Test for while statement with break statement.
    labelNode = root.getFirstChild().getNext().getNext().getNext().getNext().getNext();
    labelCodeBlock = labelNode.getLastChild();
    minimizeExits(null, labelCodeBlock, exitType, labelName);
    assertFalse(labelCodeBlock.hasChild(Token.BREAK));
    breakNode = labelCodeBlock.getFirstChild();
    assertTrue(breakNode.getType() == Token.BREAK);
    assertNull(breakNode.getParent());

    // Test for while statement with continue statement.
    labelNode = labelNode.getNext();
    labelCodeBlock = labelNode.getLastChild();
    minimizeExits(null, labelCodeBlock, exitType, labelName);
    assertFalse(labelCodeBlock.hasChild(Token.CONTINUE));
    breakNode = labelCodeBlock.getFirstChild();
    assertTrue(breakNode.getType() == Token.CONTINUE);
    assertNull(breakNode.getParent());

    // Test for do statement with break statement.
    labelNode = root.getFirstChild().getNext().getNext().getNext().getNext().getNext().getNext();
    labelCodeBlock = labelNode.getLastChild();
    minimizeExits(null, labelCodeBlock, exitType, labelName);
    assertFalse(labelCodeBlock.hasChild(Token.BREAK));
    breakNode = labelCodeBlock.getFirstChild();
    assertTrue(breakNode.getType() == Token.BREAK);
    assertNull(breakNode.getParent());

    // Test for do statement with continue statement.
    labelNode = labelNode.getNext();
    labelCodeBlock = labelNode.getLastChild();
    minimizeExits(null, labelCodeBlock, exitType, labelName);
    assertFalse(labelCodeBlock.hasChild(Token.CONTINUE));
    breakNode = labelCodeBlock.getFirstChild();
    assertTrue(breakNode.getType() == Token.CONTINUE);
    assertNull(breakNode.getParent());
  }

  /**
   * Helper function to test minimizeExits method with given labelCodeBlock.
   * @param traversal JUnit Traversal instance.
   * @param labelCodeBlock The code block of the given label.
   * @param exitType The type of exit to look for.
   * @param labelName The name associated with the exit, if any.
   */
  private void minimizeExits(NodeTraversal traversal, Node labelCodeBlock,
      int exitType, String labelName) {
    if (labelCodeBlock == null) {
      return;
    }

    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);
    minimizeExitPoints.tryMinimizeExits(labelCodeBlock, exitType, labelName);
  }

  /**
   * Helper function to test NodeUtil.isFunctionDeclaration.
   * @param node The node to check.
   * @return true if node is a function declaration.
   */
  private static boolean isFunctionDeclaration(Node node) {
    return NodeUtil.isFunctionDeclaration(node);
  }
}