package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNodeForType(Token.FOR, 1, new Object[]{"init", "condition", "increment"});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeFor = createNodeForType(Token.FOR, 1, new Object[]{null, null, null});
    assertFalse(NodeUtil.isForIn(nodeFor));

    Node nodeIf = createNodeForType(Token.IF, 1, null);
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = createNodeForType(Token.SCRIPT, 0, null);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeIf = createNodeForType(Token.IF, 1, null);
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeExpression = createNodeForType(Token.IDENT, 1, null);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = createNodeForType(Token.FUNCTION, 1, new Object[]{"name", "params", "body"});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = createNodeForType(Token.FUNCTION, 1, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = createNodeForType(Token.FUNCTION, 1, new Object[]{"name", "params", "body"});
    Node parentScript = createNodeForType(Token.SCRIPT, 0, null);
    parentScript.addChild(nodeFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunction2 = createNodeForType(Token.FUNCTION, 1, new Object[]{"name", "params", "body"});
    Node parentFunction = createNodeForType(Token.FUNCTION, 0, null);
    parentFunction.addChild(nodeFunction2);
    parentScript.addChild(parentFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction2));

    Node nodeFunction3 = createNodeForType(Token.FUNCTION, 1, new Object[]{"name", "params", "body"});
    Node parentBlock = createNodeForType(Token.BLOCK, 0, null);
    parentBlock.addChild(nodeFunction3);
    Node parentFunction4 = createNodeForType(Token.FUNCTION, 0, null);
    parentFunction4.addChild(parentBlock);
    parentScript.addChild(parentFunction4);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction3));
  }

  private Node createNodeForType(int type, int numChildren, Object[] children) {
    Node node = new Node();
    node.setType(type);
    node.setLine(1);
    if (numChildren > 0) {
      for (Object child : children) {
        Node childNode = new Node();
        node.addChild(childNode);
        if (child != null) {
          childNode.setType(type);
        }
      }
    }
    checkNotNull(node);
    return node;
  }
}