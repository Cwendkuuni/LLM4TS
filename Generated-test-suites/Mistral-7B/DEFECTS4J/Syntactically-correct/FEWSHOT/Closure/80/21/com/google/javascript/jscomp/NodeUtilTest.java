package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, null, 0, 0);
    Node nodeForIn = new Node(Token.FOR, new Node[] {new Node(Token.IDENTIFIER, "i", 0, 0)}, 0, 0);
    Node nodeIf = new Node(Token.IF, null, 0, 0);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeBlock = new Node(Token.BLOCK, null, 0, 0);
    Node nodeFunction = new Node(Token.FUNCTION, null, 0, 0);
    Node nodeExpression = new Node(Token.ASSIGN, null, 0, 0);
    Node nodeIf = new Node(Token.IF, null, 0, 0);
    Node nodeLabel = new Node(Token.LABEL, null, 0, 0);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
    assertFalse(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENTIFIER, "foo", 0, 0)}, null, 0);
    Node nodeExpression = new Node(Token.ASSIGN, new Node[] {new Node(Token.IDENTIFIER, "x", 0, 0)}, null, 0);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeFunction = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENTIFIER, "foo", 0, 0)}, null, 0);
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, null, new Node[] {nodeFunction}, 0);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testNullParent() {
    Node nodeFunction = new Node(Token.FUNCTION, null, 0, 0);

    checkState(NodeUtil.isStatement(nodeFunction), "Expected isStatement to be true for a function declaration with a null parent.");
  }
}