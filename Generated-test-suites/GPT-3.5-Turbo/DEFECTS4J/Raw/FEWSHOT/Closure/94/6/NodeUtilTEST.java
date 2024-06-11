
package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
  
  //Test for isForIn method
  @Test
  public void testIsForIn() {
    // Test when node is for-in loop
    Node node1 = new Node(Token.FOR);
    Node node2 = new Node(Token.SCRIPT);
    Node node3 = new Node(Token.FOR);
    node1.addChildToFront(new Node(Token.FOR));
    node1.addChildToFront(new Node(Token.VAR));
    node1.addChildToFront(new Node(Token.IN));
    assertTrue(NodeUtil.isForIn(node1));
    
    // Test when node is not for-in loop
    Node node4 = new Node(Token.WHILE);
    Node node5 = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isForIn(node2));
  }
  
  //Test for isStatement method
  @Test
  public void testIsStatement() {
    // Test when node is part of a statement
    Node node1 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.BLOCK);
    Node node3 = new Node(Token.LABEL);
    assertTrue(NodeUtil.isStatement(node1));
    
    // Test when node is not part of a statement
    Node node4 = new Node(Token.IF);
    Node node5 = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isStatement(node4));
  }
  
  // Test for isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    // Test when node is a function declaration
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.BLOCK);
    Node parent3 = new Node(Token.LABEL);
    Node node1 = new Node(Token.FUNCTION);
    parent1.addChildToBack(node1);
    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    
    // Test when node is not a function declaration
    Node node2 = new Node(Token.VAR);
    Node node3 = new Node(Token.IF);
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  }
  
  // Test for isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test when node is a hoisted function declaration
    Node script1 = new Node(Token.SCRIPT);
    Node script2 = new Node(Token.SCRIPT);
    Node function1 = new Node(Token.FUNCTION);
    script1.addChildToBack(function1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function1));
    
    // Test when node is not a hoisted function declaration
    Node function2 = new Node(Token.FUNCTION);
    Node var1 = new Node(Token.VAR);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(function2));
  }
}
