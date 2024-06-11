package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, null);
    assertTrue(NodeUtil.isForIn(node));

    node = new Node(Token.IF, null);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.PRIMITIVE, null);
    assertFalse(NodeUtil.isStatement(node));

    Node parent = new Node(Token.SCRIPT, null);
    node = new Node(Token.IF, parent);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, null);
    node.setProperty("id", "myFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = new Node(Token.FUNCTION, null);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parentOfFunction = new Node(Token.SCRIPT, null);
    Node functionNode = new Node(Token.FUNCTION, parentOfFunction);
    functionNode.setProperty("id", "myFunction");

    Node grandParent = new Node(Token.FUNCTION, null);
    parentOfFunction.setParent(grandParent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node parentOfFunction2 = new Node(Token.BLOCK, null);
    functionNode = new Node(Token.FUNCTION, parentOfFunction2);
    functionNode.setProperty("id", "myFunction");

    parentOfFunction2.setParent(parentOfFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}