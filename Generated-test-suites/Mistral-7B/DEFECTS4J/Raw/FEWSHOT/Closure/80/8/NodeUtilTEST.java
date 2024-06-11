package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[0]);
    nodeForIn.setChild(0, new Node(Token.IDENT, "foo"));
    nodeForIn.setChild(1, new Node(Token.KEYWORD, "in"));
    nodeForIn.setChild(2, new Node(Token.IDENT, "array"));

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.FOR, new int[0]);
    nodeNotForIn.setChild(0, new Node(Token.IDENT, "foo"));
    nodeNotForIn.setChild(1, new Node(Token.ASSIGN, null));
    nodeNotForIn.setChild(2, new Node(Token.IDENT, "bar"));

    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[0]);
    nodeStatement.setChild(0, new Node(Token.IDENT, "condition"));
    Node parentScript = new Node(Token.SCRIPT, new int[0]);
    parentScript.setChild(0, nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.IDENT, "foo");
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[0]);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENT, "foo"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.BLOCK, new int[0]));
    Node parentScript = new Node(Token.SCRIPT, new int[0]);
    parentScript.setChild(0, nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[0]);
    nodeFunctionExpression.setChild(0, new Node(Token.IDENT, "foo"));
    nodeFunctionExpression.setChild(1, new Node(Token.ASSIGN, null));
    nodeFunctionExpression.setChild(2, new Node(Token.BLOCK, new int[0]));
    Node parentExpression = new Node(Token.ASSIGN, new int[0]);
    parentExpression.setChild(0, new Node(Token.IDENT, "bar"));
    parentExpression.setChild(1, nodeFunctionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[0]);
    nodeHoistedFunctionDeclaration.setChild(0, new Node(Token.IDENT, "foo"));
    nodeHoistedFunctionDeclaration.setChild(1, new Node(Token.BLOCK, new int[0]));
    Node parentScript = new Node(Token.SCRIPT, new int[0]);
    parentScript.setChild(0, nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[0]);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENT, "foo"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.BLOCK, new int[0]));
    Node parentFunction = new Node(Token.FUNCTION, new int[0]);
    parentFunction.setChild(0, new Node(Token.IDENT, "bar"));
    parentFunction.setChild(1, nodeFunctionDeclaration);
    Node parentScript2 = new Node(Token.SCRIPT, new int[0]);
    parentScript2.setChild(0, parentFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}