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
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node1));

    Node node2 = new Node(Token.FOR);
    node2.addChildToBack(new Node(Token.EMPTY));
    node2.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node2));

    Node node3 = new Node(Token.FUNCTION);
    node3.addChildToBack(new Node(Token.EMPTY));
    node3.addChildToBack(new Node(Token.EMPTY));
    node3.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(node3));
  }
  
  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node1));

    Node node2 = new Node(Token.BLOCK);
    assertTrue(NodeUtil.isStatement(node2));

    Node node3 = new Node(Token.LABEL);
    assertTrue(NodeUtil.isStatement(node3));

    Node node4 = new Node(Token.EMPTY);
    node4.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isStatement(node4));
    
    Node node5 = new Node(Token.FUNCTION);
    node5.setParent(new Node(Token.SCRIPT));
    assertFalse(NodeUtil.isStatement(node5));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    node1.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isFunctionDeclaration(node1));

    Node node2 = new Node(Token.FUNCTION);
    node2.setParent(new Node(Token.BLOCK));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));

    Node node3 = new Node(Token.EMPTY);
    node3.setParent(new Node(Token.SCRIPT));
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    
    Node functionNode1 = new Node(Token.FUNCTION);
    functionNode1.setParent(scriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode1));

    Node blockNode = new Node(Token.BLOCK);
    scriptNode.addChildToBack(blockNode);
    
    Node functionNode2 = new Node(Token.FUNCTION);
    functionNode2.setParent(blockNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));

    Node functionNode3 = new Node(Token.FUNCTION);
    functionNode3.setParent(blockNode);
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToFront(functionNode3);
    functionNode3.setParent(parent);
    scriptNode.addChildToBack(parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode3));
  }
}