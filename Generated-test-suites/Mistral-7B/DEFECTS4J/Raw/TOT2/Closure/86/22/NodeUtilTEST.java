
package com.google.javascript.jscomp.jsonml;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node node = new Node(); // create an empty Node object for testing

    // Basic test case: Check if a valid FOR-IN loop node is identified correctly
    node.setType(Token.FOR);
    node.setChildCount(3);
    assertTrue(NodeUtil.isForIn(node));

    // Edge case 1: Check if a node that is not a FOR-IN loop is identified correctly
    node.setType(Token.IF);
    assertFalse(NodeUtil.isForIn(node));

    // Edge case 2: Check if a FOR-IN loop node with less than three children is identified correctly
    node.setChildCount(2);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node();
    Node parentNode = new Node();

    // Basic test case: Check if a statement node is identified correctly
    node.setType(Token.PRINT);
    parentNode.setType(Token.SCRIPT);
    parentNode.addChild(node);
    assertTrue(NodeUtil.isStatement(node));

    // Edge case 1: Check if a non-statement node is identified correctly
    node.setType(Token.VAR);
    assertFalse(NodeUtil.isStatement(node));

    // Edge case 2: Check if a node without a parent is identified correctly
    node.setParent(null);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node();
    Node parentNode = new Node();

    // Basic test case: Check if a function declaration node is identified correctly
    node.setType(Token.FUNCTION);
    node.setProperty("id", "myFunction");
    parentNode.setType(Token.BLOCK);
    parentNode.addChild(node);
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    // Edge case 1: Check if a non-function declaration node is identified correctly
    node.setType(Token.IF);
    assertFalse(NodeUtil.isFunctionDeclaration(node));

    // Edge case 2: Check if a function declaration node without a name is identified correctly
    node.removeProperty("id");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node();
    Node parentNode = new Node();
    Node grandParentNode = new Node();

    // Basic test case: Check if a hoisted function declaration node is identified correctly
    node.setType(Token.FUNCTION);
    node.setProperty("id", "myFunction");
    parentNode.setType(Token.SCRIPT);
    parentNode.addChild(node);
    grandParentNode.setType(Token.FUNCTION);
    grandParentNode.addChild(parentNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    // Edge case 1: Check if a non-hoisted function declaration node is identified correctly
    parentNode.setType(Token.BLOCK);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

    // Edge case 2: Check if a function declaration node without a name is identified correctly
    node.removeProperty("id");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}