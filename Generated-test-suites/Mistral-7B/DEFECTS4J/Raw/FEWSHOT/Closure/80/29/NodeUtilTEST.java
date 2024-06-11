package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new Node[] {new Node(Token.IDENT, "i"), new Node(Token.KEYWORD, "in"), new Node(Token.IDENT, "A")});
    Node nodeNotForIn = new Node(Token.IF, new Node[] {new Node(Token.IDENT, "condition")});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new Node[] {});
    Node nodeBlock = new Node(Token.BLOCK, new Node[] {new Node(Token.IDENT, "x")});
    Node nodeLabel = new Node(Token.LABEL, "label");
    Node nodeFunction = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENT, "func"), new Node(Token.BLOCK, new Node[] {})});
    Node nodeExpression = new Node(Token.IDENT, "expression");

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertTrue(NodeUtil.isStatement(nodeFunction));

    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENT, "func"), new Node(Token.BLOCK, new Node[] {})});
    Node nodeFunctionExpression = new Node(Token.IDENT, "func");

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new Node[] {new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENT, "func"), new Node(Token.BLOCK, new Node[] {})})});
    Node nodeFunctionBlock = new Node(Token.BLOCK, new Node[] {new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENT, "func"), new Node(Token.BLOCK, new Node[] {})})});
    Node nodeFunctionFunction = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENT, "func"), new Node(Token.BLOCK, new Node[] {})});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionBlock.getFirstChild()));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionFunction));
  }

  @Test(expected = AssertionError.class)
  public void testCheckParentNull() {
    Node nodeFunction = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENT, "func"), new Node(Token.BLOCK, new Node[] {})});
    Node nullParent = null;

    checkState(nodeFunction.getParent() != null);
    checkState(nullParent == null);
  }
}