package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new Node[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn1 = new Node(Token.FOR, new Node[2]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn1));

    Node nodeNotForIn2 = new Node(Token.IF, new Node[1]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn2));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeBlock = new Node(Token.BLOCK, new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeLabel = new Node(Token.LABEL, new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeFunction = new Node(Token.FUNCTION, new Node[0]);
    Node nodeFunctionAsExpression = new Node(Token.FUNCTION, new Node[1]);
    Node nodeFunctionAsStatement = new Node(Token.FUNCTION, new Node[2]);
    Node parentOfFunctionAsStatement = new Node(Token.SCRIPT, new Node[1]);
    parentOfFunctionAsStatement.setChild(0, nodeFunctionAsStatement);

    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeFunctionAsExpression));
    assertTrue(NodeUtil.isStatement(nodeFunctionAsStatement));

    Node nodeIf = new Node(Token.IF, new Node[1]);
    Node parentOfNodeIf = new Node(Token.SCRIPT, new Node[1]);
    parentOfNodeIf.setChild(0, nodeIf);

    assertFalse(NodeUtil.isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node[2]);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENT, new String[]{"funcName"}));
    Node nodeStatementFunction = new Node(Token.FUNCTION, new Node[2]);
    Node parentOfStatementFunction = new Node(Token.SCRIPT, new Node[1]);
    parentOfStatementFunction.setChild(0, nodeStatementFunction);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeStatementFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node[2]);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENT, new String[]{"funcName"}));

    Node nodeScript = new Node(Token.SCRIPT, new Node[0]);
    Node nodeFunctionDeclarationInScript = new Node(Token.SCRIPT, new Node[]{nodeFunctionDeclaration});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionInFunction = new Node(Token.FUNCTION, new Node[1]);
    nodeFunctionInFunction.setChild(0, nodeFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }
}