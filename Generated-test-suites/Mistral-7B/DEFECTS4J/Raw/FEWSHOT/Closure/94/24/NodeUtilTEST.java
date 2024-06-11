package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newNode(Token.FOR, null, null, null, null);
    Node nodeForWithThreeChildren = Node.newNode(Token.FOR, null, Node.newNode(Token.IDENT, "i", null), Node.newNode(Token.IDENT, "array", null), Node.newNode(Token.ASSIGN, null, Node.newNode(Token.NAME, "console", null), Node.newNode(Token.MEMBER, null, Node.newNode(Token.IDENT, "log", null), null), null));

    assertTrue(NodeUtil.isForIn(nodeForWithThreeChildren));
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeNotFor = Node.newNode(Token.IF, null, null, null, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node nodeVariable = Node.newNode(Token.VAR, null, Node.newNode(Token.IDENT, "x", null), null, null);

    assertFalse(NodeUtil.isForIn(nodeNotFor));
    assertFalse(NodeUtil.isForIn(nodeFunction));
    assertFalse(NodeUtil.isForIn(nodeVariable));
  }

  @Test
  public void testIsStatement() {
    Node nodeIf = Node.newNode(Token.IF, null, null, null, Node.newNode(Token.IDENT, "x", null));
    Node nodeFunction = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node nodeVariable = Node.newNode(Token.VAR, null, Node.newNode(Token.IDENT, "x", null), null, null);
    Node nodeLabel = Node.newNode(Token.IDENT, "label", null);
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null);

    assertTrue(NodeUtil.isStatement(nodeIf));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeVariable));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newNode(Token.PLUS, null, Node.newNode(Token.NUMBER, 5, null), Node.newNode(Token.NUMBER, 3, null));

    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, null, Node.newNode(Token.IDENT, "foo", null), null, null);
    Node nodeFunctionExpression = Node.newNode(Token.ASSIGN, null, Node.newNode(Token.IDENT, "x", null), Node.newNode(Token.FUNCTION, null, null, null, null));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, Node.newNode(Token.FUNCTION, null, Node.newNode(Token.IDENT, "foo", null), null, null));
    Node nodeFunctionInsideFunction = Node.newNode(Token.FUNCTION, null, null, null, Node.newNode(Token.FUNCTION, null, Node.newNode(Token.IDENT, "bar", null), null, null));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
  }

  @Test
  public void testParentCheck() {
    Node nodeFunctionWithinScript = Node.newNode(Token.FUNCTION, null, null, null, Node.newNode(Token.SCRIPT, null, null, null, null));
    Node nodeScriptWithinFunction = Node.newNode(Token.SCRIPT, null, null, null, Node.newNode(Token.FUNCTION, null, null, null, null));

    checkState(nodeFunctionWithinScript.getParent() != null);
    checkState(nodeScriptWithinFunction.getParent() != null);

    assertEquals(Node.newNode(Token.FUNCTION, null, null, null, null), nodeFunctionWithinScript.getParent());
    assertEquals(Node.newNode(Token.FUNCTION, null, null, null, null), nodeScriptWithinFunction.getParent());
  }
}