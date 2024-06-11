package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newNode(Token.FOR, new int[]{}, new Node[3]);
    assertTrue(NodeUtil.isForIn(node));

    node = Node.newNode(Token.IF, new int[]{}, new Node[0]);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = Node.newNode(Token.IF, new int[]{}, new Node[0]);
    assertFalse(NodeUtil.isStatement(node));

    node = Node.newNode(Token.SCRIPT, new int[]{}, new Node[0]);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[1]);
    functionDeclaration.setProperty("id", "testFunction");
    functionDeclaration.setChild(0, Node.newNode(Token.IDENT, new int[]{}, "testFunction"));

    Node functionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new Node[1]);
    functionExpression.setProperty("id", "testFunction");
    functionExpression.setChild(0, Node.newNode(Token.IDENT, new int[]{}, "testFunction"));

    Node block = Node.newNode(Token.BLOCK, new int[]{}, new Node[0]);

    Node script = Node.newNode(Token.SCRIPT, new int[]{}, new Node[0]);

    Node function = Node.newNode(Token.FUNCTION, new int[]{}, new Node[2]);
    function.setProperty("id", "testFunction");
    function.setChild(0, Node.newNode(Token.IDENT, new int[]{}, "testFunction"));
    function.setChild(1, Node.newNode(Token.BLOCK, new int[]{}, new Node[0]));

    Node functionInScript = Node.newNode(Token.FUNCTION, new int[]{}, new Node[2]);
    functionInScript.setProperty("id", "testFunction");
    functionInScript.setChild(0, Node.newNode(Token.IDENT, new int[]{}, "testFunction"));
    functionInScript.setChild(1, script);

    Node blockInFunction = Node.newNode(Token.BLOCK, new int[]{}, new Node[0]);
    Node functionInBlock = Node.newNode(Token.FUNCTION, new int[]{}, new Node[2]);
    functionInBlock.setProperty("id", "testFunction");
    functionInBlock.setChild(0, Node.newNode(Token.IDENT, new int[]{}, "testFunction"));
    functionInBlock.setChild(1, blockInFunction);

    assertEquals(false, NodeUtil.isFunctionDeclaration(functionExpression));
    assertEquals(true, NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertEquals(true, NodeUtil.isFunctionDeclaration(function));
    assertEquals(true, NodeUtil.isFunctionDeclaration(functionInScript));
    assertEquals(false, NodeUtil.isFunctionDeclaration(functionInBlock));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[1]);
    functionDeclaration.setProperty("id", "testFunction");
    functionDeclaration.setChild(0, Node.newNode(Token.IDENT, new int[]{}, "testFunction"));

    Node functionInScript = Node.newNode(Token.FUNCTION, new int[]{}, new Node[2]);
    functionInScript.setProperty("id", "testFunction");
    functionInScript.setChild(0, Node.newNode(Token.IDENT, new int[]{}, "testFunction"));
    functionInScript.setChild(1, Node.newNode(Token.SCRIPT, new int[]{}, new Node[0]));

    Node scriptWithFunction = Node.newNode(Token.SCRIPT, new int[]{}, new Node[1]);
    scriptWithFunction.setChild(0, functionDeclaration);

    Node scriptWithFunctionInScript = Node.newNode(Token.SCRIPT, new int[]{}, new Node[2]);
    scriptWithFunctionInScript.setChild(0, functionInScript);
    scriptWithFunctionInScript.setChild(1, scriptWithFunction);

    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(functionInScript));
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(scriptWithFunction));
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(scriptWithFunctionInScript));
  }

  @Test
  public void testCheckStateWithNull() {
    Node nullNode = null;
    Node parentNode = Node.newNode(Token.SCRIPT, new int[]{}, new Node[0]);

    try {
      NodeUtil.checkState(nullNode != null);
    } catch (RuntimeException e) {
      assertEquals("Argument 'condition' cannot be null", e.getMessage());
    }

    try {
      NodeUtil.checkState(parentNode == null);
    } catch (RuntimeException e) {
      assertEquals("Argument 'condition' is false", e.getMessage());
    }

    try {
      NodeUtil.checkState(parentNode != null && nullNode != null);
    } catch (RuntimeException e) {
      fail("Unexpected error: " + e.getMessage());
    }
  }

  @Test
  public void testCheckArgumentWithNegativeSize() {
    Node node = Node.newNode(Token.ARRAY_LITERAL, new int[]{}, new Node[1]);

    try {
      NodeUtil.checkArgument(node.getChildCount() > 0);
    } catch (RuntimeException e) {
      assertEquals("Argument 'condition' is false", e.getMessage());
    }
  }
}