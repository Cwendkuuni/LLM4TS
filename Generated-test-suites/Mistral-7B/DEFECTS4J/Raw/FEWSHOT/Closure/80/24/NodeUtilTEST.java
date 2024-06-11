package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChild(0, new Node(Token.IDENT, "i"));
    nodeForIn.setChild(1, new Node(Token.KEYWORD, "in"));
    nodeForIn.setChild(2, new Node(Token.IDENT, "arr"));

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.FOR);
    nodeNotForIn.setChild(0, new Node(Token.IDENT, "i"));
    nodeNotForIn.setChild(1, new Node(Token.ASSIGN, null, new Node(Token.NUMBER, "1")));
    nodeNotForIn.setChild(2, new Node(Token.IDENT, "arr"));

    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT);

    Node nodeStatement = new Node(Token.IDENT, "x");
    nodeStatement.setParent(nodeScript);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.IDENT, "x");

    Node nodeParent = new Node(Token.ASSIGN, null, nodeExpression);

    nodeExpression.setParent(nodeParent);
    nodeParent.setParent(nodeScript);

    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = new Node(Token.FUNCTION);
    nodeFunction.setChild(0, new Node(Token.IDENT, "foo"));
    nodeFunction.setChild(1, nodeStatement);
    nodeFunction.setParent(nodeScript);

    assertTrue(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENT, "foo"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.BLOCK, null, new Node(Token.RETURN, null, new Node(Token.NUMBER, "42"))));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null);
    nodeFunctionExpression.setChild(0, new Node(Token.IDENT, "bar"));
    nodeFunctionExpression.setChild(1, new Node(Token.BLOCK, null, new Node(Token.RETURN, null, new Node(Token.NUMBER, "42"))));

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT);

    Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENT, "foo"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.BLOCK, null, new Node(Token.RETURN, null, new Node(Token.NUMBER, "42"))));
    nodeFunctionDeclaration.setParent(nodeScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION);
    nodeFunctionDeclarationInFunction.setChild(0, new Node(Token.IDENT, "bar"));
    Node nodeFunctionDeclarationInBlock = new Node(Token.BLOCK);
    nodeFunctionDeclarationInBlock.setChild(0, nodeFunctionDeclarationInFunction);
    Node nodeFunction = new Node(Token.FUNCTION);
    nodeFunction.setChild(0, new Node(Token.IDENT, "baz"));
    nodeFunction.setChild(1, nodeFunctionDeclarationInBlock);
    nodeFunction.setParent(nodeScript);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}