package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;


public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.ASSIGN);
    Node child3 = new Node(Token.NAME);
    node1.addChildToFront(child1);
    node1.addChildAfter(child2, child1);
    node1.addChildAfter(child3, child2);
    
    Node node2 = new Node(Token.FOR);
    Node child4 = new Node(Token.NAME);
    Node child5 = new Node(Token.ASSIGN);
    node2.addChildToFront(child4);
    node2.addChildAfter(child5, child4);
    
    assertTrue(NodeUtil.isForIn(node1));
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.BLOCK);
    Node node3 = new Node(Token.LABEL);
    Node node4 = new Node(Token.NAME);
    
    assertTrue(NodeUtil.isStatement(node1));
    assertTrue(NodeUtil.isStatement(node2));
    assertTrue(NodeUtil.isStatement(node3));
    assertFalse(NodeUtil.isStatement(node4));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToFront(node1);
    
    Node node2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.BLOCK);
    parent2.addChildToFront(node2);
    
    Node node3 = new Node(Token.FUNCTION);
    Node parent3 = new Node(Token.LABEL);
    parent3.addChildToFront(node3);
    
    Node node4 = new Node(Token.NAME);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
    assertFalse(NodeUtil.isFunctionDeclaration(node4));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node grandParent1 = new Node(Token.SCRIPT);
    grandParent1.addChildToFront(parent1);
    parent1.addChildToFront(node1);
    
    Node node2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.SCRIPT);
    Node grandParent2 = new Node(Token.FUNCTION);
    grandParent2.addChildToFront(parent2);
    parent2.addChildToFront(node2);
    
    Node node3 = new Node(Token.FUNCTION);
    Node parent3 = new Node(Token.BLOCK);
    parent3.addChildToFront(node3);
    
    Node node4 = new Node(Token.NAME);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
  }
}