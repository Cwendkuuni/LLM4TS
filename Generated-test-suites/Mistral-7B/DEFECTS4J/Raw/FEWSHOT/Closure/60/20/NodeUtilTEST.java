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
    Node nodeForIn = Node.newForInStatement(0, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newExpression(null, null, Node.IDENT("x")));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newBlock(0, Node.newList(), null);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newExpression(null, null, Node.IDENT("x")));
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction(0, "foo", null, Node.newList(), Node.newList(), Node.newList());
    assertFalse(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(0, "foo", null, Node.newList(), Node.newList(), Node.newList());
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpressionStatement(nodeFunction);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(0, Node.newList(), Node.newList());
    nodeScript.setFirstChild(Node.newFunction(0, "foo", null, Node.newList(), Node.newList(), Node.newList()));

    Node nodeFunctionInFunction = Node.newFunction(0, "foo", null, Node.newList(), Node.newList(), Node.newList());
    Node nodeFunctionInScript = Node.newExpressionStatement(nodeFunctionInFunction);
    Node nodeScriptWithFunction = Node.newBlock(0, Node.newList(), nodeScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckStateWithNullParent() {
    Node node = Node.newExpressionStatement(Node.newExpression(null, null, Node.IDENT("x")));
    Node parent = null;
    checkState(parent != null, "parent cannot be null");
  }
}