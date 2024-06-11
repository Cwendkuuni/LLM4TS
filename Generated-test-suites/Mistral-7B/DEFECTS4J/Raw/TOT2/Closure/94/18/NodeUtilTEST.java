package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));

    Node nodeEmptyFor = new Node(Token.FOR, new int[] {});
    assertFalse(NodeUtil.isForIn(nodeEmptyFor));

    Node nodeForLoop = new Node(Token.FOR, new int[] {Token.IDENTIFIER, Token.LITERAL, Token.IDENTIFIER});
    assertTrue(NodeUtil.isForIn(nodeForLoop));

    Node nodeLabeledFor = new Node(Token.FOR, new int[] {Token.LABEL, Token.FOR, Token.IDENTIFIER});
    assertFalse(NodeUtil.isForIn(nodeLabeledFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[] {1, 2});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.PLUS, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeEmptyStatement = new Node(Token.EMPTY, new int[0]);
    assertTrue(NodeUtil.isStatement(nodeEmptyStatement));

    Node nodeLabeledStatement = new Node(Token.LABEL, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeLabeledStatement));

    Node nodeDeclaration = new Node(Token.VAR, new int[] {Token.IDENTIFIER, Token.ASSIGN, Token.IDENTIFIER});
    assertFalse(NodeUtil.isStatement(nodeDeclaration));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2});
    nodeFunctionDeclaration.setProperty("id", "testFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationNoId = new Node(Token.FUNCTION, new int[] {1, 2});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationNoId));

    Node nodeFunctionDeclarationNullParent = new Node(Token.FUNCTION, new int[] {1, 2});
    nodeFunctionDeclarationNullParent.setParent(null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationNullParent));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2});
    nodeHoistedFunctionDeclaration.setParent(new Node(Token.SCRIPT, new int[0]));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[] {1, 2});
    Node parentFunction = new Node(Token.FUNCTION, new int[0]);
    nodeFunctionDeclarationInFunction.setParent(parentFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[] {1, 2});
    Node parentScript = new Node(Token.SCRIPT, new int[0]);
    nodeFunctionDeclarationInScript.setParent(parentScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
  }
}