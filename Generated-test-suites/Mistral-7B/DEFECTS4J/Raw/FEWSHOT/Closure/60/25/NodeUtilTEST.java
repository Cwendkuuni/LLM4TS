package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {0, 0, 0});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {0, 0, 0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {0, 0, 0});
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {0, 0, 0});
    Node nodeIf = Node.newNode(Token.IF, new int[] {0, 0, 0});
    Node nodeAssignment = Node.newNode(Token.ASSIGN, new int[] {0, 0, 0}, new Node[] {Node.newString("x"), Node.newNumber(42)});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeAssignment));
    assertFalse(NodeUtil.isStatement(Node.newNode(Token.ADD, new int[] {0, 0, 0}, new Node[] {Node.newNumber(2), Node.newNumber(3)})));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0, 0, 0}, new Node[] {Node.newIdentifier("myFunction")});
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {0, 0, 0}, new Node[] {Node.newIdentifier("myFunction"), Node.newBlock(new int[] {0, 0, 0})});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeTopLevelFunction = Node.newNode(Token.FUNCTION, new int[] {0, 0, 0}, new Node[] {Node.newIdentifier("myFunction")});
    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, new int[] {0, 0, 0}, new Node[] {Node.newIdentifier("myFunction"), Node.newBlock(new int[] {0, 0, 0})}, Node.newNode(Token.FUNCTION, new int[] {0, 0, 0}, new Node[] {Node.newIdentifier("outerFunction")}));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeTopLevelFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testNullParent() {
    Node node = Node.newNode(Token.SCRIPT, new int[] {0, 0, 0});
    checkState(node.getParent() == null);
    assertFalse(NodeUtil.isStatement(node));
  }
}