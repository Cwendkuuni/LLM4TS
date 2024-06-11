package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
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
    when(nodeNotForIn.getChildCount()).thenReturn(2);

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
    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunction.getParent()).thenReturn(mock(Node.class));
    when(nodeFunction.getParent().getType()).thenReturn(Token.SCRIPT);

    Node nodeStatement = mock(Node.class);
    Node parentStatement = mock(Node.class);
    when(nodeStatement.getParent()).thenReturn(parentStatement);
    when(parentStatement.getType()).thenReturn(Token.SCRIPT);
    when(nodeStatement.getType()).thenReturn(Token.IF);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    assertFalse(NodeUtil.isStatement(nodeFunction)); // function expressions are not statements
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = mock(Node.class);
    Node parent = mock(Node.class);
    when(nodeFunctionDeclaration.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    when(nodeFunctionDeclaration.getParent()).thenReturn(parent);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = mock(Node.class);
    when(nodeFunctionExpression.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunctionExpression.getParent()).thenReturn(mock(Node.class));
    when(nodeFunctionExpression.getParent().getType()).thenReturn(Token.ASSIGN);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = mock(Node.class);
    Node nodeFunctionDeclaration = mock(Node.class);

    when(nodeFunctionDeclaration.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunctionDeclaration.getParent()).thenReturn(nodeScript);
    when(nodeScript.getType()).thenReturn(Token.SCRIPT);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = mock(Node.class);
    Node nodeFunction = mock(Node.class);
    Node parentFunction = mock(Node.class);

    when(nodeFunctionDeclarationInFunction.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunctionDeclarationInFunction.getParent()).thenReturn(nodeFunction);
    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunction.getParent()).thenReturn(parentFunction);
    when(parentFunction.getType()).thenReturn(Token.SCRIPT);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}