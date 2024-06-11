package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, 1, new Object[] {new Node(Token.IDENT, "i"), new Node(Token.IDENT, "arr"), new Node(Token.ASSIGN, null, new Object[] {new Node(Token.IDENT, "i"), new Node(Token.GETPROP, null, new Node(Token.IDENT, "length"))}), new Node(Token.KEYWORD, "in"), new Node(Token.IDENT, "arr")});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.ASSIGN, null, new Object[] {new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 42)});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, 1, new Object[] {});
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, 1, new Object[] {new Node(Token.IDENT, "foo"), new Node(Token.BLOCK, 1, new Object[] {})});
    assertFalse(NodeUtil.isStatement(nodeFunctionExpression));

    Node nodeIfStatement = new Node(Token.IF, 2, new Object[] {new Node(Token.LPAREN, 0), new Node(Token.IDENT, "x"), new Node(Token.RPAREN, 0), new Node(Token.BLOCK, 1, new Object[] {})});
    assertTrue(NodeUtil.isStatement(nodeIfStatement));

    Node nodeVariableDeclaration = new Node(Token.VAR, 1, new Object[] {new Node(Token.IDENT, "x")});
    assertTrue(NodeUtil.isStatement(nodeVariableDeclaration));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, 1, new Object[] {new Node(Token.IDENT, "foo"), new Node(Token.BLOCK, 1, new Object[] {})});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, 1, new Object[] {new Node(Token.IDENT, "foo"), new Node(Token.BLOCK, 1, new Object[] {})});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeVariable = new Node(Token.VAR, 1, new Object[] {new Node(Token.IDENT, "foo")});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeVariable));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, 1, new Object[] {new Node(Token.FUNCTION, 1, new Object[] {new Node(Token.IDENT, "foo"), new Node(Token.BLOCK, 1, new Object[] {})})});
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));

    Node nodeFunctionInsideBlock = new Node(Token.FUNCTION, 1, new Object[] {new Node(Token.IDENT, "foo"), new Node(Token.BLOCK, 1, new Object[] {})});
    Node nodeBlock = new Node(Token.BLOCK, 1, new Object[] {nodeFunctionInsideBlock});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeBlock.getFirstChild()));

    Node nodeFunctionInsideFunction = new Node(Token.FUNCTION, 1, new Object[] {new Node(Token.IDENT, "foo"), new Node(Token.BLOCK, 1, new Object[] {})});
    Node nodeFunction = new Node(Token.FUNCTION, 1, new Object[] {new Node(Token.IDENT, "bar"), new Node(Token.BLOCK, 1, new Object[] {})});
    Node nodeScriptWithFunction = new Node(Token.SCRIPT, 1, new Object[] {nodeFunction});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeScriptWithFunction.getFirstChild()));
  }

  private boolean assertFalse(boolean condition) {
    if (condition) {
      throw new AssertionError("Expected false, but got true");
    }
    return true;
  }
}