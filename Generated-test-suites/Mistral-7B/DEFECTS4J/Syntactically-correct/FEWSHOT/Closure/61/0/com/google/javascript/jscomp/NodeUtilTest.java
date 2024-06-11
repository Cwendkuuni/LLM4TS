package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, 0, 0);
    Node nodeForIn = new Node(Token.FOR, 0, 0);
    nodeForIn.setChild(0, new Node(Token.IDENT, 0, 0, "i"));
    nodeForIn.setChild(1, new Node(Token.KEYWORD, 0, 0, "in"));
    nodeForIn.setChild(2, new Node(Token.IDENT, 0, 0, "arr"));

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, 0, 0);
    Node nodeFunction = new Node(Token.FUNCTION, 0, 0);
    Node nodeStatement1 = new Node(Token.ASSIGN, 0, 0);
    Node nodeStatement2 = new Node(Token.ASSIGN, 0, 0);
    Node nodeExpression = new Node(Token.IDENT, 0, 0, "x");

    nodeScript.setChild(0, nodeStatement1);
    nodeScript.setChild(1, nodeStatement2);

    assertTrue(NodeUtil.isStatement(nodeStatement1));
    assertTrue(NodeUtil.isStatement(nodeStatement2));
    assertFalse(NodeUtil.isStatement(nodeExpression));
    assertFalse(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, 0, 0);
    Node nodeFunctionName = new Node(Token.IDENT, 0, 0, "myFunction");

    nodeFunctionDeclaration.setChild(0, nodeFunctionName);
    nodeFunctionExpression.setChild(0, nodeFunctionName);

    nodeFunctionExpression.setChild(1, new Node(Token.BLOCK, 0, 0));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, 0, 0);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, 0);

    Node nodeFunctionName = new Node(Token.IDENT, 0, 0, "myFunction");
    Node nodeFunctionBody = new Node(Token.BLOCK, 0, 0);

    nodeFunctionDeclaration.setChild(0, nodeFunctionName);
    nodeFunctionDeclaration.setChild(1, nodeFunctionBody);

    nodeScript.setChild(0, nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunction = new Node(Token.FUNCTION, 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, 0, 0);
    Node nodeFunctionName2 = new Node(Token.IDENT, 0, 0, "myFunction");

    nodeFunction.setChild(0, nodeFunctionName2);
    nodeFunctionExpression.setChild(0, nodeFunctionName2);
    nodeFunctionExpression.setChild(1, new Node(Token.BLOCK, 0, 0));

    nodeScript.setChild(1, nodeFunctionExpression);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsFunctionExpression() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, 0, 0);
    Node nodeFunctionName = new Node(Token.IDENT, 0, 0, "myFunction");
    Node nodeFunctionBody = new Node(Token.BLOCK, 0, 0);

    nodeFunctionDeclaration.setChild(0, nodeFunctionName);
    nodeFunctionDeclaration.setChild(1, nodeFunctionBody);

    nodeFunctionExpression.setChild(0, nodeFunctionName);
    nodeFunctionExpression.setChild(1, new Node(Token.ASSIGN, 0, 0, null, nodeFunctionBody));

    assertFalse(NodeUtil.isFunctionExpression(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isFunctionExpression(nodeFunctionExpression));
  }

  @Test
  public void testCheckState() {
    Node nodeNull = null;

    try {
      NodeUtil.checkState(nodeNull != null);
    } catch (IllegalArgumentException e) {
      return;
    }

    fail("Expected an IllegalArgumentException");
  }
}