package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n1 = new Node(Token.FOR, null, null, 0, 0);
    Node n2 = new Node(Token.SCRIPT, null, null, 0, 0);
    Node n3 = new Node(Token.BLOCK, null, null, 0, 0);
    Node n4 = new Node(Token.IF, null, null, 0, 0);
    Node n5 = new Node(Token.FOR, null, new Node[] {new Node(Token.IDENT, "i", null, 0, 0)}, 0, 0);
    Node n6 = new Node(Token.FOR, null, new Node[] {new Node(Token.IDENT, "i", null, 0, 0), new Node(Token.IDENT, "j", null, 0, 0)}, 0, 0);

    assertTrue(NodeUtil.isForIn(n5));
    assertTrue(NodeUtil.isForIn(n6));
    assertFalse(NodeUtil.isForIn(n1));
    assertFalse(NodeUtil.isForIn(n2));
    assertFalse(NodeUtil.isForIn(n3));
    assertFalse(NodeUtil.isForIn(n4));
  }

  @Test
  public void testIsStatement() {
    Node n1 = new Node(Token.FOR, null, null, 0, 0);
    Node n2 = new Node(Token.SCRIPT, null, null, 0, 0);
    Node n3 = new Node(Token.BLOCK, null, null, 0, 0);
    Node n4 = new Node(Token.LABEL, "label", null, 0, 0);
    Node n5 = new Node(Token.FUNCTION, null, null, 0, 0);
    Node n6 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0), new Node(Token.PUNCTUATION, ".", 0, 0), new Node(Token.NUMBER, "42", null, 0, 0)});
    Node n7 = new Node(Token.IF, null, null, 0, 0);

    assertTrue(NodeUtil.isStatement(n2));
    assertTrue(NodeUtil.isStatement(n3));
    assertTrue(NodeUtil.isStatement(n4));
    assertTrue(NodeUtil.isStatement(n5));
    assertFalse(NodeUtil.isStatement(n1));
    assertFalse(NodeUtil.isStatement(n6));
    assertFalse(NodeUtil.isStatement(n7));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION, null, null, 0, 0);
    Node n2 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0)});
    Node n3 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0), new Node(Token.PUNCTUATION, "(", 0, 0)});
    Node n4 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0), new Node(Token.PUNCTUATION, "(", 0, 0), new Node(Token.PUNCTUATION, ")", 0, 0)});

    assertTrue(NodeUtil.isFunctionDeclaration(n1));
    assertTrue(NodeUtil.isFunctionDeclaration(n2));
    assertFalse(NodeUtil.isFunctionDeclaration(n3));
    assertTrue(NodeUtil.isFunctionDeclaration(n4));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n1 = new Node(Token.SCRIPT, null, null, 0, 0);
    Node n2 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0)});
    Node n3 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0), new Node(Token.PUNCTUATION, "(", 0, 0)});
    Node n4 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0), new Node(Token.PUNCTUATION, "(", 0, 0), new Node(Token.PUNCTUATION, ")", 0, 0)});
    Node n5 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0), new Node(Token.PUNCTUATION, "(", 0, 0), new Node(Token.BLOCK, null, null, 0, 0), new Node(Token.PUNCTUATION, ")", 0, 0)});

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n4));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n5));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentFunction() {
    Node parent = new Node(Token.FUNCTION, null, null, 0, 0, null);
    Node n1 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0)});
    Node n2 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0), new Node(Token.PUNCTUATION, "(", 0, 0)});
    Node n3 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0), new Node(Token.PUNCTUATION, "(", 0, 0), new Node(Token.PUNCTUATION, ")", 0, 0)});
    Node n4 = new Node(Token.FUNCTION, null, null, 0, 0, new Node[] {new Node(Token.IDENT, "foo", null, 0, 0), new Node(Token.PUNCTUATION, "(", 0, 0), new Node(Token.BLOCK, null, null, 0, 0), new Node(Token.PUNCTUATION, ")", 0, 0)});

    checkState(parent != null);

    NodeUtil.setParent(n1, parent);
    NodeUtil.setParent(n2, parent);
    NodeUtil.setParent(n3, parent);
    NodeUtil.setParent(n4, parent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n3));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n4));
  }
}