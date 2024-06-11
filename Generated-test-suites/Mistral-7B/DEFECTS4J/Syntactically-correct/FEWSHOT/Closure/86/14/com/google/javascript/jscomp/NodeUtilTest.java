package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR);
    Node nodeForWithThreeChildren = new Node(Token.FOR);
    nodeForWithThreeChildren.setChild(0, new Node(Token.IDENTIFIER, "i"));
    nodeForWithThreeChildren.setChild(1, new Node(Token.KEYWORD, "in"));
    nodeForWithThreeChildren.setChild(2, new Node(Token.IDENTIFIER, "array"));

    assertTrue(NodeUtil.isForIn(nodeForWithThreeChildren));
    assertFalse(NodeUtil.isForIn(nodeFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeFunction = new Node(Token.FUNCTION);
    Node nodeBlock = new Node(Token.BLOCK);
    Node nodeLabel = new Node(Token.LABEL);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeExpression = new Node(Token.IDENTIFIER, "foo");
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null);
    nodeFunctionExpression.setChild(0, nodeExpression);

    assertFalse(NodeUtil.isStatement(nodeExpression));
    assertFalse(NodeUtil.isStatement(nodeFunctionExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, null, null);
    Node nodeFunctionWithName = new Node(Token.FUNCTION);
    Node nodeFunctionWithNameAndChild = new Node(Token.FUNCTION);
    nodeFunctionWithName.setChild(0, new Node(Token.IDENTIFIER, "funcName"));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionWithName));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionWithNameAndChild));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, null);
    Node nodeFunction = new Node(Token.FUNCTION, null, null);
    Node nodeFunctionWithinFunction = new Node(Token.FUNCTION, null, null);
    nodeFunctionWithinFunction.setChild(0, nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionWithinFunction));

    Node nodeScriptWithinFunction = new Node(Token.SCRIPT);
    nodeScriptWithinFunction.setParent(nodeFunctionWithinFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}