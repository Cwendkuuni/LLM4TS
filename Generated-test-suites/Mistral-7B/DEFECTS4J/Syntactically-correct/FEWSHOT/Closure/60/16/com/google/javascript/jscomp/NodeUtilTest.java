package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0}, null);
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0}, null);

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}, null);
    Node blockNode = new Node(Token.BLOCK, new int[]{}, new int[]{0, 0, 0}, null);
    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, null);
    Node ifNode = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0}, null);
    Node labelNode = new Node(Token.LABEL, new int[]{}, new int[]{0, 0, 0}, null);

    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}, null);
    parentScript.addChildToEnd(scriptNode);

    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, null);
    parentFunction.addChildToEnd(functionNode);

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(functionNode));
    assertTrue(NodeUtil.isStatement(labelNode));

    checkState(scriptNode.getParent() != null);
    checkState(blockNode.getParent() != null);
    checkState(functionNode.getParent() != null);
    checkState(labelNode.getParent() != null);

    switch (scriptNode.getParent().getType()) {
      case Token.SCRIPT:
      case Token.BLOCK:
      case Token.LABEL:
        break;
      default:
        throw new AssertionError("Unexpected parent type: " + scriptNode.getParent().getType());
    }

    switch (blockNode.getParent().getType()) {
      case Token.SCRIPT:
      case Token.BLOCK:
      case Token.LABEL:
        break;
      default:
        throw new AssertionError("Unexpected parent type: " + blockNode.getParent().getType());
    }

    switch (functionNode.getParent().getType()) {
      case Token.SCRIPT:
      case Token.BLOCK:
        break;
      default:
        assertFalse(NodeUtil.isStatement(functionNode));
        break;
    }

    switch (labelNode.getParent().getType()) {
      case Token.SCRIPT:
      case Token.BLOCK:
        break;
      default:
        assertFalse(NodeUtil.isStatement(labelNode));
        break;
    }

    assertFalse(NodeUtil.isStatement(ifNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, null);
    Node functionExpressionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, null);
    functionExpressionNode.setProperty("name", "foo");

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}, null);
    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, null);
    functionNode.setProperty("name", "foo");
    scriptNode.addChildToEnd(functionNode);

    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, null);
    parentFunction.addChildToEnd(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}