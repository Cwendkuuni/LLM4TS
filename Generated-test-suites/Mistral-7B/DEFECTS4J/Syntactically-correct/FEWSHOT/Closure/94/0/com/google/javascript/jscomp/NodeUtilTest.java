package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, null, null, null, null, 0);
    Node nodeForIn = new Node(Token.FOR, new Node("in"), new Node("array"), null, null, 0);
    Node nodeNotFor = new Node(Token.IF, null, null, null, null, 0);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeNotFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null, null, null, null, 0);
    Node nodeBlock = new Node(Token.BLOCK, null, null, null, null, 0);
    Node nodeLabel = new Node(Token.LABEL, null, null, null, null, 0);
    Node nodeFunction = new Node(Token.FUNCTION, null, null, null, null, 0);
    Node nodeExpression = new Node(Token.ASSIGN, null, new Node("x"), null, null, 0);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node("myFunction"), null, null, null, 0);
    Node nodeFunctionExpression = new Node(Token.ASSIGN, null, new Node(Token.FUNCTION, null, null, nodeFunctionDeclaration, null, 0), null, null, 0);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeTopLevelFunction = new Node(Token.FUNCTION, new Node("myFunction"), null, null, null, 0);
    Node nodeFunctionInsideFunction = new Node(Token.FUNCTION, new Node("myFunction"), null, null, nodeTopLevelFunction, 0);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeTopLevelFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
  }

  @Test
  public void testNullParent() {
    Node node = new Node(Token.SCRIPT, null, null, null, null, 0);
    checkState(NodeUtil.isStatement(node), "isStatement should return true for script nodes.");
  }
}