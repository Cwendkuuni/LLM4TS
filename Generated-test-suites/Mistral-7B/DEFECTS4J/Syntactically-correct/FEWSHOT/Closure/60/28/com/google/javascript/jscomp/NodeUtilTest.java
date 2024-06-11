package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction(null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeLabel = Node.newLabel("label");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeNotStatement = Node.newNumberLiteral(1);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(null, Node.newIdent("f"), null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(Node.newIdent("f"), null, null, null, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeTopLevelFunction = Node.newFunction(null, Node.newIdent("f"), null, null, null);
    Node nodeTopLevelScript = Node.newScript();
    nodeTopLevelScript.addChild(nodeTopLevelFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeTopLevelFunction));

    Node nodeNestedFunction = Node.newFunction(null, Node.newIdent("g"), null, null, null);
    Node nodeFunctionScript = Node.newScript();
    Node functionNode = nodeFunctionScript.addChild(nodeFunctionDeclaration);
    functionNode.setParent(nodeFunctionScript);
    functionNode.setNext(nodeNestedFunction);
    nodeFunctionScript.addChild(nodeNestedFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNestedFunction));

    Node nodeNestedFunction2 = Node.newFunction(null, Node.newIdent("h"), null, null, null);
    Node nodeTopLevelFunction2 = Node.newFunction(null, Node.newIdent("h"), null, null, null);
    Node nodeTopLevelFunctionScript2 = Node.newScript();
    nodeTopLevelFunctionScript2.addChild(nodeTopLevelFunction2);
    Node functionNode2 = nodeTopLevelFunctionScript2.addChild(nodeFunctionDeclaration);
    functionNode2.setParent(nodeTopLevelFunctionScript2);
    functionNode2.setNext(nodeNestedFunction2);
    nodeTopLevelFunctionScript2.addChild(nodeNestedFunction2);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeTopLevelFunction2));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeNestedFunction2));
  }

  private void assertFalse(boolean condition) {
    // JUnit does not have a built-in assertFalse method.
    assertTrue(!condition);
  }

  @Test
  public void testCheckState() {
    Node node = null;
    try {
      checkState(node != null);
    } catch (IllegalArgumentException e) {
      return;
    }
    throw new Error("Expected IllegalArgumentException");
  }
}