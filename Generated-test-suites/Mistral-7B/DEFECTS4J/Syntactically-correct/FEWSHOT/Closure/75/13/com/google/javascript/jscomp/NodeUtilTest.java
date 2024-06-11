package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
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
    Node nodeFor = new Node(Token.FOR, null, 0, 0);
    Node nodeForIn = new Node(Token.FOR, new Node[] {new Node(Token.IDENT, "i"), new Node(Token.KEYWORD, "in"), new Node(Token.IDENT, "arr")}, 0, 0);
    Node nodeIf = new Node(Token.IF, new Node[] {new Node(Token.IDENT, "x")}, 0, 0);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeFunction = new Node(Token.FUNCTION, null, 0, 0);
    Node nodeExpression = new Node(Token.IDENT, "x", 0, 0);
    Node nodeIf = new Node(Token.IF, new Node[] {nodeExpression}, 0, 0);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertFalse(NodeUtil.isStatement(nodeExpression));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENT, "foo")}, 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, 0, 0);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeFunction = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENT, "foo")}, 0, 0);
    Node nodeFunctionInsideFunction = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENT, "bar")}, nodeFunction, 0);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));

    Node nodeScriptInsideFunction = new Node(Token.SCRIPT, null, nodeFunction, 0);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScriptInsideFunction));
  }

  @Test
  public void testCheckArgument() {
    try {
      checkArgument(1 != 1, "Expected false");
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Expected false", e.getMessage());
    }

    try {
      checkArgument(1 == 1, "Expected true");
      fail();
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  @Test
  public void testPreconditionsCheckState() {
    Node parent = null;
    try {
      Preconditions.checkState(parent != null, "Expected parent to not be null");
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Expected parent to not be null", e.getMessage());
    }

    Node node = new Node(Token.IDENT, "x", 0, 0);
    Node parentNode = new Node(Token.SCRIPT, null, 0, 0);
    Preconditions.checkState(parentNode != null, "Expected parent to not be null");
    assertSame(parentNode, node.getParent());
  }
}