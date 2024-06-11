package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.EMPTY);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.EMPTY);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);
    
    assertEquals(true, NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_Script() {
    Node node = new Node(Token.SCRIPT);
    
    assertEquals(true, NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_Block() {
    Node node = new Node(Token.BLOCK);
    
    assertEquals(true, NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_Label() {
    Node node = new Node(Token.LABEL);
    
    assertEquals(true, NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_Other() {
    Node node = new Node(Token.NAME);
    
    assertEquals(false, NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    
    assertEquals(true, NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node node = new Node(Token.NAME);
    
    assertEquals(false, NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_Script() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);
    
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_Function() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.SCRIPT);
    parent.setParent(grandParent);
    node.setParent(parent);
    
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    node.setParent(parent);
    
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
  }
}