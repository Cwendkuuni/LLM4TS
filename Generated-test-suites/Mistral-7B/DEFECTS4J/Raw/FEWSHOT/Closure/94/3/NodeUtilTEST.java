package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3}, new Node[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2}, new Node[2]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.ASSIGN, new int[] {1}, new Node[1]);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IDENT, new int[] {1}, new String[] {"x"});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2}, new Node[2]);
    nodeFunctionDeclaration.setProperty("id", "myFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2}, new Node[2]);
    nodeFunctionExpression.setProperty("id", "myFunction");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1}, new Node[1]);
    nodeHoistedFunctionDeclaration.setProperty("id", "myFunction");
    Node parentScript = new Node(Token.SCRIPT, new int[] {}, new Node[0]);
    nodeHoistedFunctionDeclaration.setParent(parentScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1}, new Node[1]);
    nodeFunctionDeclaration.setProperty("id", "myFunction");
    Node parentFunction = new Node(Token.FUNCTION, new int[] {1}, new Node[1]);
    nodeFunctionDeclaration.setParent(parentFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1}, new Node[1]);
    nodeFunctionDeclaration.setProperty("id", "myFunction");
    assertNull(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}