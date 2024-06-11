package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
  private static Node createNode(int type) {
    return new Node() {
      @Override
      public int getType() {
        return type;
      }

      @Override
      public int getLineNumber() {
        return 0;
      }

      @Override
      public Node getParent() {
        return null;
      }

      @Override
      public int getChildCount() {
        return 0;
      }
    };
  }

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(Token.FOR);
    nodeForIn.setChildCount(3);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(Token.FOR);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(Token.IF);
    Node parentScript = createNode(Token.SCRIPT);

    nodeStatement.setParent(parentScript);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = createNode(Token.IF);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION);
    Node parentBlock = createNode(Token.BLOCK);

    nodeFunctionDeclaration.setParent(parentBlock);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNode(Token.FUNCTION);
    Node parentExpression = createNode(Token.IF);

    nodeFunctionExpression.setParent(parentExpression);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION);
    Node parentScript = createNode(Token.SCRIPT);

    nodeFunctionDeclaration.setParent(parentScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = createNode(Token.FUNCTION);
    Node parentFunction = createNode(Token.FUNCTION);

    nodeFunctionDeclaration.setParent(parentFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullNode() {
    Node node = null;
    NodeUtil.isFunctionDeclaration(node);
  }

  @Test(expected = IllegalStateException.class)
  public void testIsStatementNullParentNode() {
    Node nodeStatement = createNode(Token.IF);
    Node parentNode = null;

    nodeStatement.setParent(parentNode);
    NodeUtil.isStatement(nodeStatement);
  }

  @Test
  public void testIsFunctionDeclarationEmptyParentNode() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION);
    Node parentNodeEmpty = createNode(Token.BLOCK);

    nodeFunctionDeclaration.setParent(parentNodeEmpty);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclarationEmptyParentNode() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION);
    Node parentNodeEmpty = createNode(Token.SCRIPT);

    nodeFunctionDeclaration.setParent(parentNodeEmpty);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationFunctionInExpression() {
    Node nodeFunctionExpression = createNode(Token.FUNCTION);
    Node parentExpression = createNode(Token.IF);

    nodeFunctionExpression.setParent(parentExpression);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsFunctionDeclarationFunctionInBlock() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION);
    Node parentBlock = createNode(Token.BLOCK);

    nodeFunctionDeclaration.setParent(parentBlock);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationFunctionInScript() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION);
    Node parentScript = createNode(Token.SCRIPT);

    nodeFunctionDeclaration.setParent(parentScript);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclarationFunctionInScript() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION);
    Node parentScript = createNode(Token.SCRIPT);

    nodeFunctionDeclaration.setParent(parentScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclarationFunctionInFunction() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION);
    Node parentFunction = createNode(Token.FUNCTION);

    nodeFunctionDeclaration.setParent(parentFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationFunctionInBlockWithStatement() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION);
    Node parentBlockWithStatement = createNode(Token.BLOCK);

    Node statement = createNode(Token.IF);
    parentBlockWithStatement.setChild(statement);
    nodeFunctionDeclaration.setParent(parentBlockWithStatement);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationFunctionInBlockWithExpression() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION);
    Node parentBlockWithExpression = createNode(Token.BLOCK);

    Node expression = createNode(Token.IF);
    parentBlockWithExpression.setChild(expression);
    nodeFunctionDeclaration.setParent(parentBlockWithExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }
}