package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{Node.newText("for (var i in arr) {"), Node.newText("}"), Node.newText("")});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeBreak = Node.newNode(Token.BREAK, new int[]{}, Node.NULL);
    assertFalse(NodeUtil.isForIn(nodeBreak));

    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{Node.newText("function foo() {}")});
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{Token.SCRIPT}, new Node[]{Node.newText("var x = 1;")});
    assertTrue(NodeUtil.isStatement(nodeScript.getFirstChild()));

    Node nodeIf = Node.newNode(Token.IF, new int[]{Token.IF}, new Node[]{Node.newText("if (x > 0) {"), Node.newText("}"), Node.newText("")});
    assertTrue(NodeUtil.isStatement(nodeIf.getFirstChild()));

    Node nodeFunctionCall = Node.newNode(Token.CALL, new int[]{Token.IDENT, Token.LPAREN, Token.RPAREN}, new Node[]{Node.newText("foo"), Node.newArray()});
    assertFalse(NodeUtil.isStatement(nodeFunctionCall));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{Node.newText("function foo(x) {"), Node.newText("}"), Node.newText("")});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newNode(Token.ASSIGN, new int[]{Token.IDENT, Token.LPAREN, Token.RPAREN, Token.FUNCTION},
        new Node[]{Node.newText("foo = function(x) {}"), Node.newText("")});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{Token.SCRIPT}, new Node[]{Node.newText("function foo() {}")});
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));

    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
        Node.newNode(Token.IDENT, new int[]{Token.IDENT}, Node.newText("function bar() {")),
        Node.newNode(Token.VAR, new int[]{Token.VAR, Token.IDENT}, new Node[]{Node.newText("var foo = function(x) {}"), Node.newText("}")}),
        Node.newText("}")
    });
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction.getChild(1).getFirstChild()));

    Node nodeFunctionInBlock = Node.newNode(Token.BLOCK, new int[]{Token.BLOCK}, new Node[]{
        Node.newText("function bar() {"),
        Node.newNode(Token.VAR, new int[]{Token.VAR, Token.IDENT}, new Node[]{Node.newText("var foo = function(x) {}"), Node.newText("")}),
        Node.newText("}")
    });
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInBlock.getFirstChild()));
  }
}