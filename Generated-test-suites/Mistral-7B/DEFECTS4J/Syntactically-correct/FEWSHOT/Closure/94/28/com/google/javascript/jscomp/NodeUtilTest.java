package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{}, new int[0]);
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{}, new int[0]);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement1 = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    Node nodeStatement2 = new Node(Token.BLOCK, new int[]{}, new int[]{}, new int[0]);
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new int[]{}, new int[0]);
    Node nodeExpression = new Node(Token.ASSIGN, new int[]{}, new int[]{}, new int[0]);

    assertTrue(NodeUtil.isStatement(nodeStatement1));
    assertTrue(NodeUtil.isStatement(nodeStatement2));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration1 = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[]{"name1"});
    Node nodeFunctionDeclaration2 = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]);
    Node nodeStatement = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration1));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration2));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[]{"name"});
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[]{"functionName"});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionInScript = new Node(nodeFunctionDeclarationInScript, new int[]{}, new int[]{}, new int[0]);
    Node nodeFunctionInFunction = new Node(nodeFunctionDeclarationInFunction, new int[]{}, new int[]{}, new int[0]);
    Node nodeFunctionInFunction2 = new Node(new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]), new int[]{}, new int[]{}, new int[0]);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction2));
  }

  @Test
  public void testParentNodeTypes() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]);
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{}, new int[0]);
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new int[]{}, new int[0]);

    checkState(nodeScript.getParent() == null);
    checkState(nodeFunction.getParent() != null);
    Node parentFunction = nodeFunction.getParent();
    checkState(parentFunction.getType() == Token.FUNCTION);
    checkState(nodeBlock.getParent() != null);
    Node parentBlock = nodeBlock.getParent();
    checkState(parentBlock.getType() == Token.BLOCK);
    checkState(nodeLabel.getParent() != null);
    Node parentLabel = nodeLabel.getParent();
    checkState(parentLabel.getType() == Token.LABEL);
  }
}