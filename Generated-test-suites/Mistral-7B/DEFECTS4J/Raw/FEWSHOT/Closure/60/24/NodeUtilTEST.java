package com.google.javascript.jscomp.jsonml;

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
 * Test cases for the NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = mock(Node.class);
    when(forNode.getType()).thenReturn(Token.FOR);
    when(forNode.getChildCount()).thenReturn(3);

    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = mock(Node.class);
    when(notForNode.getType()).thenReturn(Token.IF);
    when(notForNode.getChildCount()).thenReturn(2);

    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = mock(Node.class);
    Node parentScript = mock(Node.class);
    Node parentBlock = mock(Node.class);
    Node parentLabel = mock(Node.class);

    when(statementNode.getParent()).thenReturn(parentScript);
    when(parentScript.getType()).thenReturn(Token.SCRIPT);

    assertTrue(NodeUtil.isStatement(statementNode));

    when(statementNode.getParent()).thenReturn(parentBlock);
    when(parentBlock.getType()).thenReturn(Token.BLOCK);

    assertTrue(NodeUtil.isStatement(statementNode));

    when(statementNode.getParent()).thenReturn(parentLabel);
    when(parentLabel.getType()).thenReturn(Token.LABEL);

    assertTrue(NodeUtil.isStatement(statementNode));

    Node notStatementNode = mock(Node.class);
    Node notParent = mock(Node.class);

    when(notStatementNode.getParent()).thenReturn(notParent);
    when(notParent.getType()).thenReturn(Token.ASSIGN);

    assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = mock(Node.class);
    Node parentStatement = mock(Node.class);

    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.isStatement()).thenReturn(true);
    when(functionNode.getParent()).thenReturn(parentStatement);
    when(parentStatement.getType()).thenReturn(Token.SCRIPT);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    when(parentStatement.getType()).thenReturn(Token.BLOCK);

    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = mock(Node.class);
    Node functionDeclaration = mock(Node.class);
    Node scriptNode = mock(Node.class);
    Node functionNode = mock(Node.class);

    when(hoistedFunctionDeclaration.getType()).thenReturn(Token.FUNCTION);
    when(hoistedFunctionDeclaration.isStatement()).thenReturn(true);
    when(hoistedFunctionDeclaration.getParent()).thenReturn(scriptNode);
    when(scriptNode.getType()).thenReturn(Token.SCRIPT);

    when(functionDeclaration.getType()).thenReturn(Token.FUNCTION);
    when(functionDeclaration.isStatement()).thenReturn(true);
    when(functionDeclaration.getParent()).thenReturn(functionNode);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.getParent()).thenReturn(scriptNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    when(scriptNode.getType()).thenReturn(Token.BLOCK);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }

  @Test
  public void testPreconditionsCheckState() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);

    when(node.getParent()).thenReturn(parent);

    checkState(node != null, "node cannot be null");
    checkState(parent != null, "parent cannot be null");
  }
}