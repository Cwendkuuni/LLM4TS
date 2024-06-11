package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR);
    nodeFor.setChildCount(3);
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeNotFor = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(nodeNotFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatementInScript = new Node(Token.PRINT);
    Node parentScript = new Node(Token.SCRIPT);
    parentScript.setChild(0, nodeStatementInScript);
    assertTrue(NodeUtil.isStatement(nodeStatementInScript));

    Node nodeStatementInBlock = new Node(Token.IF, new Node(Token.PRINT));
    Node parentBlock = new Node(Token.BLOCK);
    parentBlock.setChild(0, nodeStatementInBlock);
    assertTrue(NodeUtil.isStatement(nodeStatementInBlock));

    Node nodeStatementInLabel = new Node(Token.LABEL, "label");
    Node parentLabel = new Node(Token.LABEL, "label");
    parentLabel.setChild(0, nodeStatementInLabel);
    assertTrue(NodeUtil.isStatement(nodeStatementInLabel));

    Node nodeExpression = new Node(Token.MINUS, new Node(Token.IDENT, "x"), new Node(Token.INT_LITERAL, 5));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo"));
    nodeFunctionDeclaration.setChildCount(1);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo"), new Node(Token.BLOCK, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeFunctionDeclaration1 = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo1"));
    nodeFunctionDeclaration1.setChildCount(1);
    nodeScript.setChild(0, nodeFunctionDeclaration1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration1));

    Node nodeFunctionDeclaration2 = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo2"));
    nodeFunctionDeclaration2.setChildCount(1);
    Node nodeFunction = new Node(Token.FUNCTION, new Node(Token.IDENT, "bar"), nodeFunctionDeclaration2);
    nodeScript.setChild(0, nodeFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration2));

    Node nodeFunctionDeclaration3 = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo3"));
    nodeFunctionDeclaration3.setChildCount(1);
    Node nodeFunctionDeclaration4 = new Node(Token.FUNCTION, new Node(Token.IDENT, "bar"), nodeFunctionDeclaration3);
    Node nodeFunction = new Node(Token.FUNCTION, new Node(Token.IDENT, "baz"), nodeFunctionDeclaration4);
    nodeScript.setChild(0, nodeFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration3));
  }
}