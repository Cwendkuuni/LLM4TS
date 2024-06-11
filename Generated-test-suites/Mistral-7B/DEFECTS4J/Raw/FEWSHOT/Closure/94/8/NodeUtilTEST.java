package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, 1, new Node[] { Node.newNode(Token.IDENT, "i"), Node.newNode(Token.IDENT, "array"), Node.newNode(Token.KEYWORD, "in") });
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, 1, new Node[] { Node.newNode(Token.IDENT, "x") });
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, 0, new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunction = Node.newNode(Token.FUNCTION, 1, new Node[] { Node.newNode(Token.IDENT, "f") });
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeIf = Node.newNode(Token.IF, 1, new Node[] { Node.newNode(Token.IDENT, "x") });
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeExpression = Node.newNode(Token.IDENT, "x");
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, 1, new Node[] { Node.newNode(Token.IDENT, "f") });
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, 1, new Node[] { Node.newNode(Token.IDENT, "f"), Node.newNode(Token.BLOCK, 0, new Node[0]) });
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, 0, new Node[] { Node.newNode(Token.FUNCTION, 1, new Node[] { Node.newNode(Token.IDENT, "f") }) });
    Node nodeFunction = Node.newNode(Token.FUNCTION, 1, new Node[] { Node.newNode(Token.IDENT, "f") });
    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, 1, new Node[] { Node.newNode(Token.IDENT, "g"), Node.newNode(Token.FUNCTION, 1, new Node[] { Node.newNode(Token.IDENT, "f") }) });

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testParentOfFunction() {
    Node nodeScript = Node.newNode(Token.SCRIPT, 0, new Node[0]);
    Node nodeFunction = Node.newNode(Token.FUNCTION, 1, new Node[] { Node.newNode(Token.IDENT, "f") });
    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, 1, new Node[] { Node.newNode(Token.IDENT, "g"), Node.newNode(Token.FUNCTION, 1, new Node[] { Node.newNode(Token.IDENT, "f") }) });

    checkState(nodeFunction.getParent() == nodeScript, "Function node's parent should be the script node");
    checkState(nodeFunctionInFunction.getParent().getParent() == nodeScript, "Function node in function's parent should be the script node");
  }
}