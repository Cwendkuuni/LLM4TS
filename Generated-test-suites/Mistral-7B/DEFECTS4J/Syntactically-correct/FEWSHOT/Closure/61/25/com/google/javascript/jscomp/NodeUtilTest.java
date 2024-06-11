package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setProperty("children", new Object[] { new Node(Token.IDENTIFIER, "i"), new Node(Token.KEYWORD, "in"), new Node(Token.IDENTIFIER, "arr") });
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeStatement = new Node(Token.ASSIGN, null, new Object[] { new Node(Token.IDENTIFIER, "x"), new Node(Token.NUMBER, 1) });
    nodeStatement.setParent(nodeScript);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.ASSIGN, null, new Object[] { new Node(Token.IDENTIFIER, "x"), new Node(Token.NUMBER, 1) });
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, null, new Object[] { new Node(Token.IDENTIFIER, "foo") });
    nodeFunction.setProperty("children", new Object[] { new Node(Token.IDENTIFIER, "foo"), new Node(Token.BLOCK, null, null) });
    nodeFunction.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = new Node(Token.ASSIGN, null, new Object[] { new Node(Token.IDENTIFIER, "x"), nodeFunction });
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, null, new Object[] { new Node(Token.IDENTIFIER, "foo") });
    nodeFunction.setProperty("children", new Object[] { new Node(Token.IDENTIFIER, "foo"), new Node(Token.BLOCK, null, null) });
    nodeFunction.setParent(new Node(Token.SCRIPT));

    Node nodeFunctionInFunction = new Node(Token.FUNCTION, null, new Object[] { new Node(Token.IDENTIFIER, "bar") });
    nodeFunctionInFunction.setProperty("children", new Object[] { new Node(Token.IDENTIFIER, "bar"), new Node(Token.BLOCK, null, null) });
    nodeFunction.setProperty("parent", nodeFunctionInFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionOutsideFunction = new Node(Token.FUNCTION, null, new Object[] { new Node(Token.IDENTIFIER, "foo") });
    nodeFunctionOutsideFunction.setProperty("children", new Object[] { new Node(Token.IDENTIFIER, "foo"), new Node(Token.BLOCK, null, null) });
    nodeFunctionOutsideFunction.setParent(new Node(Token.SCRIPT));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionOutsideFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node nodeFunction = new Node(Token.FUNCTION, null, new Object[] { new Node(Token.IDENTIFIER, "foo") });
    nodeFunction.setProperty("children", new Object[] { new Node(Token.IDENTIFIER, "foo"), new Node(Token.BLOCK, null, null) });
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    assertNull(NodeUtil.isHoistedFunctionDeclaration(null));
  }
}