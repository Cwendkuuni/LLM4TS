package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {0, 0, 0});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {0, 0, 0});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.PRINT, new int[] {0, 0, 0});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.ADD, new int[] {0, 0, 0, 0, 0});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0, 0, 1}, null, new int[] {0});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {0, 0, 1}, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {0}, null, null);
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, new int[] {0, 0, 1}, null, new int[] {0});
    Node nodeFunctionDeclarationInFunction = Node.newNode(
        Token.FUNCTION,
        new int[] {0, 0, 1},
        null,
        null,
        Node.newNode(Token.FUNCTION, new int[] {0, 0, 1}, null, null)
    );

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationNotInScriptOrFunction = Node.newNode(
        Token.FUNCTION,
        new int[] {0, 0, 1},
        null,
        null,
        Node.newNode(Token.IF, new int[] {0, 0, 0})
    );
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationNotInScriptOrFunction));
  }

  @Test
  public void testIsFunctionExpression() {
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {0, 0, 1}, null, null);
    assertTrue(NodeUtil.isFunctionExpression(nodeFunctionExpression));

    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0, 0, 1}, null, new int[] {0});
    assertFalse(NodeUtil.isFunctionExpression(nodeFunctionDeclaration));
  }

  @Test
  public void testIsStatementParentScriptOrBlock() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {0}, null, null);
    Node nodeBlock = Node.newNode(Token.BLOCK, new int[] {0}, null, null);
    Node nodeStatement = Node.newNode(Token.PRINT, new int[] {0, 0, 0});

    Node statementAsChildOfScript = Node.newNode(Token.ASSIGN, new int[] {0, 0, 1}, null, new Node[] {nodeStatement});
    Node statementAsChildOfBlock = Node.newNode(Token.ASSIGN, new int[] {0, 0, 1}, null, new Node[] {nodeStatement});

    checkState(nodeScript != null);
    checkState(nodeBlock != null);
    checkState(nodeStatement != null);

    Node parentOfStatementInScript = Node.newNode(Token.SCRIPT, new int[] {0}, null, new Node[] {statementAsChildOfScript});
    Node parentOfStatementInBlock = Node.newNode(Token.BLOCK, new int[] {0}, null, new Node[] {statementAsChildOfBlock});

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(statementAsChildOfScript));
    assertTrue(NodeUtil.isStatement(statementAsChildOfBlock));
    assertTrue(NodeUtil.isStatement(parentOfStatementInScript));
    assertTrue(NodeUtil.isStatement(parentOfStatementInBlock));
  }

  @Test
  public void testIsStatementParentIf() {
    Node nodeIf = Node.newNode(Token.IF, new int[] {0, 0, 0});
    Node nodeStatement = Node.newNode(Token.PRINT, new int[] {0, 0, 0});
    Node statementAsChildOfIf = Node.newNode(Token.ASSIGN, new int[] {0, 0, 1}, null, new Node[] {nodeStatement});

    checkState(nodeIf != null);
    checkState(nodeStatement != null);

    assertFalse(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(statementAsChildOfIf));
    assertFalse(NodeUtil.isStatement(nodeIf));
  }
}