
package com.google.javascript.jscomp.jsonml.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{0, 1, 2});
    assertTrue(NodeUtil.isForIn(nodeForIn));
  }

  @Test
  public void testIsNotForIn() {
    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{0, 1, 1});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.PRINT, new int[]{0, 1});
    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    Node nullNode = null;
    try {
      NodeUtil.isStatement(nullNode);
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0, 1});
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node nullNode = null;
    try {
      NodeUtil.isFunctionDeclaration(nullNode);
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNonStatementNode() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0, 1});
    Node expression = Node.newNode(Token.PLUS, new int[]{0, 1, 1});
    expression.addChild(functionDeclaration);
    try {
      NodeUtil.isFunctionDeclaration(functionDeclaration);
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0, 1});
    Node script = Node.newNode(Token.SCRIPT, new int[]{0}, new int[] {});
    script.addChild(hoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0, 1});
    Node block = Node.newNode(Token.BLOCK, new int[]{0}, new int[] {});
    block.addChild(functionDeclaration);
    Node script = Node.newNode(Token.SCRIPT, new int[]{0}, new int[] {});
    script.addChild(block);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node nullNode = null;
    try {
      NodeUtil.isHoistedFunctionDeclaration(nullNode);
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNonFunctionDeclarationNode() {
    Node functionDeclaration = Node.newNode(Token.FOR, new int[]{0, 1}, new int[]{0, 1});
    Node script = Node.newNode(Token.SCRIPT, new int[]{0}, new int[] {});
    script.addChild(functionDeclaration);
    try {
      NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }
}