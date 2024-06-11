package com.google.javascript.jscomp.jsonml;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.ID);
    Node child3 = new Node(Token.BLOCK);
    node.addChildToFront(child1);
    node.addChildBefore(child2, child3);
    assertTrue(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
    Node node = new Node(Token.BLOCK);
    assertFalse(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_WhenNodeIsPartOfAST_ReturnsTrue() {
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);
    assertTrue(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_WhenNodeIsNotPartOfAST_ReturnsFalse() {
    Node node = new Node(Token.NAME);
    assertFalse(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);
    assertTrue(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.NAME);
    assertFalse(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
    Node function = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    parent1.addChildToBack(function);
    parent2.addChildToBack(parent1);
    function.setParent(parent1);
    parent1.setParent(parent2);
    assertTrue(nodeUtil.isHoistedFunctionDeclaration(function));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.FUNCTION);
    assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
  }
}