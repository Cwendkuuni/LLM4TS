package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test cases for NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = mock(Node.class);
    when(nodeForIn.getType()).thenReturn(Token.FOR);
    when(nodeForIn.getChildCount()).thenReturn(3);

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = mock(Node.class);
    when(nodeNotForIn.getType()).thenReturn(Token.IF);
    when(nodeNotForIn.getChildCount()).thenReturn(1);

    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = mock(Node.class);
    when(nodeScript.getType()).thenReturn(Token.SCRIPT);

    Node nodeBlock = mock(Node.class);
    when(nodeBlock.getType()).thenReturn(Token.BLOCK);

    Node nodeLabel = mock(Node.class);
    when(nodeLabel.getType()).thenReturn(Token.LABEL);

    Node nodeFunction = mock(Node.class);
    Node parentFunction = mock(Node.class);
    when(nodeFunction.getParent()).thenReturn(parentFunction);
    when(parentFunction.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunction.getType()).thenReturn(Token.IF);

    Node nodeStatement = mock(Node.class);
    when(nodeStatement.getType()).thenReturn(Token.CALL);
    when(nodeStatement.getParent()).thenReturn(nodeScript);

    checkArgument(NodeUtil.isStatement(nodeScript), "Node is a statement.");
    checkArgument(NodeUtil.isStatement(nodeBlock), "Node is a statement.");
    checkArgument(NodeUtil.isStatement(nodeLabel), "Node is a statement.");
    assertFalse(NodeUtil.isStatement(nodeFunction), "Node is not a statement.");
    assertFalse(NodeUtil.isStatement(nodeFunction), "Node is not a statement.");

    checkState(NodeUtil.isStatement(nodeStatement), "Node is a statement.");
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = mock(Node.class);
    Node parentBlock = mock(Node.class);
    when(nodeFunctionDeclaration.getParent()).thenReturn(parentBlock);
    when(parentBlock.getType()).thenReturn(Token.BLOCK);
    when(nodeFunctionDeclaration.getType()).thenReturn(Token.FUNCTION);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = mock(Node.class);
    Node parentExpression = mock(Node.class);
    when(nodeFunctionExpression.getParent()).thenReturn(parentExpression);
    when(parentExpression.getType()).thenReturn(Token.CALL);
    when(nodeFunctionExpression.getType()).thenReturn(Token.FUNCTION);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = mock(Node.class);
    Node nodeFunctionDeclaration = mock(Node.class);

    when(nodeFunctionDeclaration.getParent()).thenReturn(nodeScript);
    when(nodeFunctionDeclaration.getType()).thenReturn(Token.FUNCTION);
    when(nodeScript.getType()).thenReturn(Token.SCRIPT);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclaration2 = mock(Node.class);
    Node nodeFunction = mock(Node.class);
    when(nodeFunctionDeclaration2.getParent()).thenReturn(nodeFunction);
    when(nodeFunctionDeclaration2.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration2));
  }
}