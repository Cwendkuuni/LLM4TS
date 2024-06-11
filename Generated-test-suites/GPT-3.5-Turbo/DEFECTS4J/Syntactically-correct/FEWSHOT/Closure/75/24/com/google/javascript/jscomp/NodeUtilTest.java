package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    assertEquals(true, NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    node.addChildToFront(new Node(Token.EMPTY));
    assertEquals(false, NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.EMPTY);
    parent.addChildToFront(node);
    assertEquals(true, NodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.EMPTY);
    parent.addChildToBack(node);
    assertEquals(false, NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parent.addChildToFront(node);
    assertEquals(true, NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parent.addChildToBack(node);
    assertEquals(false, NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node grandParent = new Node(Token.SCRIPT);
    Node parent = new Node(Token.FUNCTION);
    grandParent.addChildToFront(parent);
    Node node = new Node(Token.FUNCTION);
    parent.addChildToFront(node);
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parent.addChildToFront(node);
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
  }
}