package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node nodeForIn = mock(Node.class);
    Node nodeFor = mock(Node.class);
    Node nodeIn = mock(Node.class);

    when(nodeForIn.getType()).thenReturn(Token.FOR);
    when(nodeForIn.getChildCount()).thenReturn(3);
    when(nodeForIn.getChild(0)).thenReturn(nodeFor);
    when(nodeFor.getType()).thenReturn(Token.FOR);
    when(nodeFor.getChildCount()).thenReturn(1);
    when(nodeFor.getChild(0)).thenReturn(nodeIn);
    when(nodeIn.getType()).thenReturn(Token.IN);

    assertTrue(NodeUtil.isForIn(nodeForIn));
  }

  @Test
  public void testIsForInNotForIn() {
    Node nodeNotForIn = mock(Node.class);

    when(nodeNotForIn.getType()).thenReturn(Token.IF);
    when(nodeNotForIn.getChildCount()).thenReturn(1);

    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = mock(Node.class);
    Node nodeBlock = mock(Node.class);
    Node nodeLabel = mock(Node.class);
    Node nodeFunction = mock(Node.class);

    when(nodeScript.getType()).thenReturn(Token.SCRIPT);
    when(nodeBlock.getType()).thenReturn(Token.BLOCK);
    when(nodeLabel.getType()).thenReturn(Token.LABEL);
    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);

    when(nodeScript.getParent()).thenReturn(null);
    when(nodeBlock.getParent()).thenReturn(nodeScript);
    when(nodeLabel.getParent()).thenReturn(nodeScript);
    when(nodeFunction.getParent()).thenReturn(nodeScript);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Preconditions.checkState(nodeFunction.getParent() != null);
    assertTrue(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsStatementNotStatement() {
    Node nodeVariable = mock(Node.class);

    when(nodeVariable.getType()).thenReturn(Token.VAR);
    when(nodeVariable.getParent()).thenReturn(null);

    assertFalse(NodeUtil.isStatement(nodeVariable));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = mock(Node.class);
    Node nodeStatement = mock(Node.class);

    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunction.getChildCount()).thenReturn(2);
    when(nodeFunction.getChild(0)).thenReturn(nodeStatement);

    when(nodeStatement.getType()).thenReturn(Token.SCRIPT);
    when(nodeStatement.getChildCount()).thenReturn(1);
    when(nodeStatement.getChild(0)).thenReturn(null);

    Preconditions.checkState(nodeFunction.getParent() != null);
    when(nodeFunction.getParent().getType()).thenReturn(Token.SCRIPT);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclarationNotFunctionDeclaration() {
    Node nodeVariable = mock(Node.class);
    Node nodeFunctionExpression = mock(Node.class);

    when(nodeVariable.getType()).thenReturn(Token.VAR);
    when(nodeVariable.getChildCount()).thenReturn(1);
    when(nodeVariable.getChild(0)).thenReturn(null);

    when(nodeFunctionExpression.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunctionExpression.getChildCount()).thenReturn(1);
    when(nodeFunctionExpression.getChild(0)).thenReturn(null);

    when(nodeVariable.getParent()).thenReturn(null);
    when(nodeFunctionExpression.getParent()).thenReturn(null);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeVariable));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = mock(Node.class);
    Node nodeFunctionDeclaration = mock(Node.class);
    Node nodeFunction = mock(Node.class);

    when(nodeScript.getType()).thenReturn(Token.SCRIPT);
    when(nodeScript.getChildCount()).thenReturn(1);
    when(nodeScript.getChild(0)).thenReturn(nodeFunctionDeclaration);

    when(nodeFunctionDeclaration.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunctionDeclaration.getChildCount()).thenReturn(2);
    when(nodeFunctionDeclaration.getChild(0)).thenReturn(nodeFunction);

    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunction.getChildCount()).thenReturn(1);
    when(nodeFunction.getChild(0)).thenReturn(null);

    when(nodeFunctionDeclaration.getParent()).thenReturn(nodeScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclarationNotHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = mock(Node.class);
    Node nodeFunction = mock(Node.class);

    when(nodeFunctionDeclaration.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunctionDeclaration.getChildCount()).thenReturn(2);
    when(nodeFunctionDeclaration.getChild(0)).thenReturn(nodeFunction);

    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunction.getChildCount()).thenReturn(1);
    when(nodeFunction.getChild(0)).thenReturn(null);

    when(nodeFunctionDeclaration.getParent()).thenReturn(null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}