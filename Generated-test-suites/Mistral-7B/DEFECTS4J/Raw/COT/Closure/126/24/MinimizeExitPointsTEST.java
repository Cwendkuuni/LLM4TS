package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TernaryValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {
  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setup() {
    minimizeExitPoints = new MinimizeExitPoints(null);
  }

  @Test
  public void testTryMinimizeExits_Label_Break() {
    Node labelNode = Node.newLabel("label");
    Node breakNode = Node.newBreak("label");
    Node blockNode = Node.newBlock();

    blockNode.addChildToBack(labelNode);
    blockNode.addChildToBack(breakNode);

    minimizeExitPoints.tryMinimizeExits(blockNode, Token.BREAK, null);

    assertFalse("The break statement should be removed",
        blockNode.hasChild(breakNode));
    assertNull("The break statement's parent should be null",
        breakNode.getParent());
  }

  @Test
  public void testTryMinimizeExits_Label_Break_WithName() {
    String labelName = "label";
    Node labelNode = Node.newLabel(labelName);
    Node breakNode = Node.newBreak(labelName);
    Node blockNode = Node.newBlock();

    blockNode.addChildToBack(labelNode);
    blockNode.addChildToBack(breakNode);

    minimizeExitPoints.tryMinimizeExits(blockNode, Token.BREAK, labelName);

    assertFalse("The break statement should be removed",
        blockNode.hasChild(breakNode));
    assertNull("The break statement's parent should be null",
        breakNode.getParent());
  }

  @Test
  public void testTryMinimizeExits_If_Return() {
    Node ifNode = Node.newIfStatement();
    Node returnNode = Node.newReturnStatement();
    Node blockNode = Node.newBlock();

    blockNode.addChildToBack(ifNode);
    ifNode.addChildToBack(returnNode);

    minimizeExitPoints.tryMinimizeExits(blockNode, Token.RETURN, null);

    assertEquals("The return statement should be removed", 0, blockNode.numChildren());
  }

  @Test
  public void testTryMinimizeExits_If_Continue() {
    Node ifNode = Node.newIfStatement();
    Node continueNode = Node.newContinueStatement();
    Node blockNode = Node.newBlock();

    blockNode.addChildToBack(ifNode);
    ifNode.addChildToBack(continueNode);

    minimizeExitPoints.tryMinimizeExits(blockNode, Token.CONTINUE, null);

    assertFalse("The continue statement should be removed",
        blockNode.hasChild(continueNode));
    assertNull("The continue statement's parent should be null",
        continueNode.getParent());
  }

  @Test
  public void testTryMinimizeExits_If_Break() {
    String labelName = "label";
    Node ifNode = Node.newIfStatement();
    Node breakNode = Node.newBreak(labelName);
    Node blockNode = Node.newBlock();

    blockNode.addChildToBack(ifNode);
    ifNode.addChildToBack(breakNode);

    minimizeExitPoints.tryMinimizeExits(blockNode, Token.BREAK, labelName);

    assertFalse("The break statement should be removed",
        blockNode.hasChild(breakNode));
    assertNull("The break statement's parent should be null",
        breakNode.getParent());
  }

  @Test
  public void testTryMinimizeExits_If_Nested_Return() {
    Node outerIfNode = Node.newIfStatement();
    Node outerReturnNode = Node.newReturnStatement();
    Node innerIfNode = Node.newIfStatement();
    Node innerReturnNode = Node.newReturnStatement();
    Node blockNode = Node.newBlock();

    blockNode.addChildToBack(outerIfNode);
    outerIfNode.addChildToBack(innerIfNode);
    innerIfNode.addChildToBack(outerReturnNode);
    innerIfNode.addChildToBack(innerReturnNode);

    minimizeExitPoints.tryMinimizeExits(blockNode, Token.RETURN, null);

    assertFalse("The outer return statement should be removed",
        outerIfNode.hasChild(outerReturnNode));
    assertNull("The outer return statement's parent should be null",
        outerReturnNode.getParent());
  }

  @Test
  public void testTryMinimizeExits_If_Nested_Break() {
    String labelName = "label";
    Node outerIfNode = Node.newIfStatement();
    Node outerBreakNode = Node.newBreak(labelName);
    Node innerIfNode = Node.newIfStatement();
    Node innerBreakNode = Node.newBreak(labelName);
    Node blockNode = Node.newBlock();

    blockNode.addChildToBack(outerIfNode);
    outerIfNode.addChildToBack(innerIfNode);
    innerIfNode.addChildToBack(outerBreakNode);
    innerIfNode.addChildToBack(innerBreakNode);

    minimizeExitPoints.tryMinimizeExits(blockNode, Token.BREAK, labelName);

    assertFalse("The outer break statement should be removed",
        outerIfNode.hasChild(outerBreakNode));
    assertNull("The outer break statement's parent should be null",
        outerBreakNode.getParent());
  }

  @Test
  public void testTryMinimizeExits_TryCatchFinally() {
    Node tryNode = Node.newTryStatement();
    Node catchNode = Node.newCatch(Node.newIdentifier("e"), null);
    Node finallyNode = Node.newStatementList();
    Node blockNode = Node.newBlock();

    blockNode.addChildToBack(tryNode);
    tryNode.addChildToBack(NodeUtil.newExpressionStatement(Node.newEmptyStatement()));
    tryNode.addChildToBack(catchNode);
    tryNode.addChildToBack(finallyNode);

    minimizeExitPoints.tryMinimizeExits(blockNode, Token.RETURN, null);

    assertEquals("The try node should not be changed", 3, tryNode.numChildren());

    minimizeExitPoints.tryMinimizeExits(blockNode, Token.RETURN, null);

    assertEquals("The try, catch, and finally nodes should not be changed", 3, tryNode.numChildren());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeExits_NullBlock() {
    minimizeExitPoints.tryMinimizeExits(null, Token.RETURN, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeExits_NullCompiler() {
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);
    minimizeExitPoints.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeExits_NullParent() {
    Node root = Node.newBlock();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);
    minimizeExitPoints.process(null, root);

    minimizeExitPoints.visit(null, null, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeExits_NullChild() {
    Node node = Node.newBlock();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);
    minimizeExitPoints.visit(null, null, node);

    minimizeExitPoints.visit(null, null, node.getLastChild());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTryMinimizeExits_InvalidNodeType() {
    Node node = Node.newIdentifier("test");
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);
    minimizeExitPoints.visit(null, null, node);
  }
}