package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{0, 0});

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{0, 0});
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Node nodeExpression = new Node(Token.IDENT, new int[]{}, new int[]{0, 0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInScript = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentFunction() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunctionInScript = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    parentFunction.setChild(0, nodeFunctionInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentScript() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunctionInScript = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    parentScript.setChild(0, nodeFunctionInScript);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }
}