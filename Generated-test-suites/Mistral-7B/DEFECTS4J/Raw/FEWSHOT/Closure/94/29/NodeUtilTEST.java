package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[] {5, 6, 7}, new Node[] {});
    Node ifNode = Node.newNode(Token.IF, new int[] {1, 2, 3}, new Node[] {});

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {}, new Node[] {});
    Node functionNode = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {});
    Node ifNode = Node.newNode(Token.IF, new int[] {1, 2, 3}, new Node[] {});
    Node labelNode = Node.newNode(Token.IDENT, new int[] {1}, new String[] {"label"});

    assertTrue(NodeUtil.isStatement(scriptNode.getFirstChild()));
    assertTrue(NodeUtil.isStatement(functionNode));
    assertTrue(NodeUtil.isStatement(labelNode));

    Node expressionNode = Node.newNode(Token.ASSIGN, new int[] {1, 2}, new Node[] {Node.newTextNode("1")});
    assertFalse(NodeUtil.isStatement(expressionNode));

    Node forNode = Node.newNode(Token.FOR, new int[] {5, 6, 7}, new Node[] {});
    assertFalse(NodeUtil.isStatement(forNode)); // For node is not a statement itself
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {Node.newTextNode("functionName")});
    Node functionExpressionNode = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {});
    Node ifStatementNode = Node.newNode(Token.IF, new int[] {1, 2, 3}, new Node[] {});

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(ifStatementNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {Node.newTextNode("functionName")});
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {}, new Node[] {functionDeclaration});
    Node functionInsideFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {});
    Node functionInsideFunctionInsideFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {functionInsideFunction});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(ifStatementNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInsideFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionInsideFunctionInsideFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentNull() {
    Node functionNode = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}, new Node[] {});

    // checkState will throw an exception in this case
    checkState(functionNode.getParent() != null);
    NodeUtil.isHoistedFunctionDeclaration(functionNode);
  }
}