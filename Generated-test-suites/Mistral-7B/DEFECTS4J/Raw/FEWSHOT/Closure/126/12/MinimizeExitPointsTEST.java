package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TernaryValue;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

public class MinimizeExitPointsTest {
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
  }

  @Test
  public void testMinimizeExitPoints() {
    Node root = Node.newNode(Token.PROGRAM, 0);

    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizeExitPoints);

    testMinimizeExits(root, Token.RETURN, null);
    testMinimizeExits(root, Token.BREAK, null);
    testMinimizeExits(root, Token.CONTINUE, null);

    testMinimizeExitsWithLabel(root, Token.RETURN, "label1");
    testMinimizeExitsWithLabel(root, Token.BREAK, "label1");
    testMinimizeExitsWithLabel(root, Token.CONTINUE, "label1");

    testMinimizeIfBlockExits(root);
  }

  private void testMinimizeExits(Node root, int exitType, String labelName) {
    Node exitNode = findExitNode(root, exitType, labelName);
    if (exitNode != null) {
      assertTrue(exitNode.isSynthetic());
    }
  }

  private void testMinimizeExitsWithLabel(Node root, int exitType, String labelName) {
    Node exitNode = findExitNode(root, exitType, labelName);
    if (exitNode != null) {
      assertTrue(exitNode.isLabel());
      assertTrue(exitNode.getString().equals(labelName));
    } else {
      assertNull(findLabelNode(root, labelName));
    }
  }

  private void testMinimizeIfBlockExits(Node root) {
    Node ifNode = createIfNode(root, 3);
    Node trueBlock = ifNode.getFirstChild().getNext();
    Node falseBlock = trueBlock.getNext();

    Node exitNode = findExitNode(trueBlock, Token.RETURN, null);
    assertFalse(exitNode != null);

    Node exitLabelNode = findLabelNode(trueBlock, "label1");
    assertNull(exitLabelNode);

    Node newDestBlock = IR.block();
    ifNode.addChildToBack(newDestBlock);
    trueBlock.replaceChildren(newDestBlock);

    exitNode = findExitNode(newDestBlock, Token.RETURN, null);
    assertNull(exitNode);

    Node labelNode = createLabelNode(newDestBlock, "label1");
    newDestBlock.addChildToBack(labelNode);

    exitNode = findExitNode(newDestBlock, Token.BREAK, "label1");
    assertNull(exitNode);

    Node nextNode = newDestBlock.getLastChild();
    Node nextNextNode = nextNode.getNext();
    Node statementNode = Node.newNode(Token.ASSIGN, 0, null, null, null);
    nextNode.addChildToBack(statementNode);

    exitNode = findExitNode(nextNode, Token.BREAK, "label1");
    assertNotNull(exitNode);
    assertTrue(exitNode.isSynthetic());
    assertTrue(exitNode.getParent() == nextNode);

    exitLabelNode = findLabelNode(nextNode, "label1");
    assertNotNull(exitLabelNode);
    assertTrue(exitLabelNode.isSynthetic());
    assertTrue(exitLabelNode.getParent() == nextNode);
  }

  private Node findExitNode(Node node, int exitType, String labelName) {
    if (node.getType() == exitType) {
      if (node.hasChildren()) {
        if (labelName != null && labelName.equals(node.getFirstChild().getString())) {
          return node;
        }
      }
      return null;
    }
    for (Node child : node.children()) {
      Node exitNode = findExitNode(child, exitType, labelName);
      if (exitNode != null) {
        return exitNode;
      }
    }
    return null;
  }

  private Node findLabelNode(Node node, String labelName) {
    if (node.isLabel() && node.getString().equals(labelName)) {
      return node;
    }
    for (Node child : node.children()) {
      Node labelNode = findLabelNode(child, labelName);
      if (labelNode != null) {
        return labelNode;
      }
    }
    return null;
  }

  private Node createIfNode(Node parent, int numChildren) {
    Node ifNode = Node.newNode(Token.IF, 0, null, null, null);
    parent.addChildToBack(ifNode);
    Node ifTrue = Node.newNode(Token.BLOCK, 0);
    ifNode.addChildToBack(ifTrue);
    for (int i = 0; i < numChildren; i++) {
      Node statementNode = Node.newNode(Token.ASSIGN, 0, null, null, null);
      ifTrue.addChildToBack(statementNode);
    }
    Node ifFalse = Node.newNode(Token.BLOCK, 0);
    ifNode.addChildToBack(ifFalse);
    return ifNode;
  }

  private Node createLabelNode(Node parent, String labelName) {
    Node labelNode = Node.newNode(Token.LABEL, 0, null, null, labelName);
    parent.addChildToBack(labelNode);
    return labelNode;
  }
}