package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new Node[]{new Node(Token.IDENT, new int[]{}, "for"), new Node(Token.ASSIGN, new int[]{}, new Node[]{new Node(Token.IDENT, new int[]{}, "i"), new Node(Token.NUMBER, new int[]{}, "0")}), new Node(Token.KEYWORD, new int[]{}, "in"), new Node(Token.IDENT, new int[]{}, "array")});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new Node[]{new Node(Token.KEYWORD, new int[]{}, "if"), new Node(Token.LPAREN, new int[]{}, null), new Node(Token.RPAREN, new int[]{}, null), new Node(Token.BLOCK, new int[]{}, new Node[0])});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new Node[]{new Node(Token.KEYWORD, new int[]{}, "if"), new Node(Token.LPAREN, new int[]{}, null), new Node(Token.RPAREN, new int[]{}, null), new Node(Token.BLOCK, new int[]{}, new Node[0])});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.PLUS, new int[]{}, new Node[]{new Node(Token.NUMBER, new int[]{}, "5"), new Node(Token.NUMBER, new int[]{}, "3")});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new Node[]{new Node(Token.IDENT, new int[]{}, "myFunction"), new Node(Token.BLOCK, new int[]{}, new Node[0])});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new Node[]{new Node(Token.IDENT, new int[]{}, null), new Node(Token.LPAREN, new int[]{}, null), new Node(Token.RPAREN, new int[]{}, null), new Node(Token.BLOCK, new int[]{}, new Node[0])});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeTopLevelFunction = new Node(Token.FUNCTION, new int[]{}, new Node[]{new Node(Token.IDENT, new int[]{}, "myFunction"), new Node(Token.BLOCK, new int[]{}, new Node[0])});
    Node parentOfTopLevelFunction = new Node(Token.SCRIPT, new int[]{}, new Node[0]);
    nodeTopLevelFunction.setParent(parentOfTopLevelFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeTopLevelFunction));

    Node nodeNestedFunction = new Node(Token.FUNCTION, new int[]{}, new Node[]{new Node(Token.IDENT, new int[]{}, "myFunction"), new Node(Token.BLOCK, new int[]{}, new Node[0])});
    Node parentOfNestedFunction = new Node(Token.BLOCK, new int[]{}, new Node[]{new Node(Token.VAR, new int[]{}, "x", new Node[]{new Node(Token.ASSIGN, new int[]{}, new Node[]{new Node(Token.IDENT, new int[]{}, "x"), new Node(Token.NUMBER, new int[]{}, "5")})})});
    nodeNestedFunction.setParent(parentOfNestedFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNestedFunction));
  }
}