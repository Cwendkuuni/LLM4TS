package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test cases for NodeUtil class.
 */
public class NodeUtilTest {
  private Node mockNode;
  private Node mockParentNode;

  @Before
  public void setUp() {
    mockNode = mock(Node.class);
    mockParentNode = mock(Node.class);
  }

  @Test
  public void testIsForIn() {
    when(mockNode.getType()).thenReturn(Token.FOR);
    when(mockNode.getChildCount()).thenReturn(3);
    assertTrue(NodeUtil.isForIn(mockNode));

    when(mockNode.getType()).thenReturn(Token.IF);
    assertFalse(NodeUtil.isForIn(mockNode));
  }

  @Test
  public void testIsStatement() {
    when(mockParentNode.getType()).thenReturn(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(mockNode));

    when(mockParentNode.getType()).thenReturn(Token.EXPRESSION);
    assertFalse(NodeUtil.isStatement(mockNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    when(mockNode.getType()).thenReturn(Token.FUNCTION);
    when(mockParentNode.getType()).thenReturn(Token.BLOCK);
    assertTrue(NodeUtil.isFunctionDeclaration(mockNode));

    when(mockParentNode.getType()).thenReturn(Token.EXPRESSION);
    assertFalse(NodeUtil.isFunctionDeclaration(mockNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    when(mockNode.getType()).thenReturn(Token.FUNCTION);
    when(mockParentNode.getType()).thenReturn(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(mockNode));

    when(mockParentNode.getType()).thenReturn(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(mockNode));
  }
}