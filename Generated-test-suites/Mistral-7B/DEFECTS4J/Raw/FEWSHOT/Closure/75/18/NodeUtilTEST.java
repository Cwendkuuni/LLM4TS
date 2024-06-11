package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import org.mockito.Mockito;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = mock(Node.class);
    Node nodeForStmt = mock(Node.class);
    Node nodeForInit = mock(Node.class);
    Node nodeForUpdate = mock(Node.class);

    when(nodeFor.getType()).thenReturn(Token.FOR);
    when(nodeFor.getChildCount()).thenReturn(3);
    when(nodeFor.getChild(0)).thenReturn(nodeForInit);
    when(nodeFor.getChild(1)).thenReturn(nodeForStmt);
    when(nodeFor.getChild(2)).thenReturn(nodeForUpdate);

    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeWhile = mock(Node.class);
    Node nodeIf = mock(Node.class);

    when(nodeWhile.getType()).thenReturn(Token.WHILE);
    when(nodeIf.getType()).thenReturn(Token.IF);

    assertFalse(NodeUtil.isForIn(nodeWhile));
    assertFalse(NodeUtil.isForIn(nodeIf));
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

    Node parentScript = mock(Node.class);
    when(parentScript.getType()).thenReturn(Token.SCRIPT);

    Node parentBlock = mock(Node.class);
    when(parentBlock.getType()).thenReturn(Token.BLOCK);

    Node parentFunction = mock(Node.class);
    when(parentFunction.getType()).thenReturn(Token.FUNCTION);

    when(nodeScript.getParent()).thenReturn(null);
    when(nodeBlock.getParent()).thenReturn(parentBlock);
    when(nodeLabel.getParent()).thenReturn(parentLabel);
    when(nodeFunction.getParent()).thenReturn(parentFunction);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertFalse(NodeUtil.isStatement(nodeLabel));
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeIf = mock(Node.class);
    Node nodeIfStmt = mock(Node.class);

    when(nodeIf.getType()).thenReturn(Token.IF);
    when(nodeIfStmt.getType()).thenReturn(Token.SCRIPT);

    when(nodeIf.getParent()).thenReturn(parentFunction);
    when(nodeIfStmt.getParent()).thenReturn(null);

    assertFalse(NodeUtil.isStatement(nodeIf));
    assertTrue(NodeUtil.isStatement(nodeIfStmt));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = mock(Node.class);
    Node nodeName = mock(Node.class);
    Node nodeBody = mock(Node.class);

    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunction.getChildCount()).thenReturn(2);
    when(nodeFunction.getChild(0)).thenReturn(nodeName);
    when(nodeFunction.getChild(1)).thenReturn(nodeBody);

    Node parentScript = mock(Node.class);
    Node parentBlock = mock(Node.class);

    when(parentScript.getType()).thenReturn(Token.SCRIPT);
    when(parentBlock.getType()).thenReturn(Token.BLOCK);

    when(nodeFunction.getParent()).thenReturn(parentScript);
    when(nodeName.getType()).thenReturn(Token.IDENT);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeBody));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = mock(Node.class);
    Node nodeName = mock(Node.class);
    Node nodeBody = mock(Node.class);
    Node nodeScript = mock(Node.class);

    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunction.getChildCount()).thenReturn(2);
    when(nodeFunction.getChild(0)).thenReturn(nodeName);
    when(nodeFunction.getChild(1)).thenReturn(nodeBody);

    when(nodeName.getType()).thenReturn(Token.IDENT);

    when(nodeScript.getType()).thenReturn(Token.SCRIPT);
    when(nodeScript.getChildCount()).thenReturn(1);
    when(nodeScript.getChild(0)).thenReturn(nodeFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_FunctionInsideFunction() {
    Node nodeFunction = mock(Node.class);
    Node nodeName = mock(Node.class);
    Node nodeBody = mock(Node.class);
    Node nodeFunctionParent = mock(Node.class);

    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunction.getChildCount()).thenReturn(2);
    when(nodeFunction.getChild(0)).thenReturn(nodeName);
    when(nodeFunction.getChild(1)).thenReturn(nodeBody);

    when(nodeName.getType()).thenReturn(Token.IDENT);

    when(nodeFunctionParent.getType()).thenReturn(Token.FUNCTION);
    when(nodeFunctionParent.getChildCount()).thenReturn(1);
    when(nodeFunctionParent.getChild(0)).thenReturn(nodeFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }
}