package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions. checksState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class NodeUtilTest {

  Node rootNode;
  Node scriptNode;
  Node functionNode;
  Node blockNode;
  Node labelNode;
  Node forNode;

  @Before
  public void setUp() {
    rootNode = mock(Node.class);
    when(rootNode.getType()).thenReturn(Token.SCRIPT);

    scriptNode = mock(Node.class);
    when(scriptNode.getType()).thenReturn(Token.SCRIPT);

    functionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(functionNode.getParent()).thenReturn(rootNode);

    blockNode = mock(Node.class);
    when(blockNode.getType()).thenReturn(Token.BLOCK);

    labelNode = mock(Node.class);
    when(labelNode.getType()).thenReturn(Token.LABEL);

    forNode = mock(Node.class);
    when(forNode.getType()).thenReturn(Token.FOR);
    when(forNode.getParent()).thenReturn(rootNode);
    when(forNode.getChildCount()).thenReturn(3);
  }

  @Test
  public void testIsForIn() {
    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(functionNode));
  }

  @Test
  public void testIsStatement() {
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(functionNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));

    assertFalse(NodeUtil.isStatement(rootNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(scriptNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    checksState(NodeUtil.isFunctionDeclaration(functionNode), "functionNode must be a function declaration");

    when(functionNode.getParent().getType()).thenReturn(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    when(functionNode.getParent().getType()).thenReturn(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}