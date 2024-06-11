package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForStatement(Token.FOR, Token.Ident("i"), Token.INT_LITERAL(0), Token.INT_LITERAL(10));
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeForIn = Node.newForStatement(Token.FOR, Token.Ident("i"), Token.Ident("arr"), Token.IDENT("arr.length"));
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(10));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript(Token.SCRIPT, Node.newBlock(Token.BLOCK, Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(10))));
    assertTrue(NodeUtil.isStatement(nodeScript.getFirstChild()));

    Node nodeBlock = Node.newBlock(Token.BLOCK, Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(10)));
    assertTrue(NodeUtil.isStatement(nodeBlock.getFirstChild()));

    Node nodeLabel = Node.newLabel(Token.LABEL, "label");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeFunction = Node.newFunction(Token.FUNCTION, Token.IDENT("myFunction"), new Node[0], Node.newBlock(Token.BLOCK, Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(10))));
    assertTrue(NodeUtil.isStatement(nodeFunction.getBody()));

    Node nodeFunctionExpression = Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newFunctionExpression(Token.FUNCTION, Token.IDENT("myFunction"), new Node[0], Node.newBlock(Token.BLOCK, Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(10)))));
    assertTrue(NodeUtil.isStatement(nodeFunctionExpression.getFirstChild()));

    Node nodeIf = Node.newIfStatement(Token.IF, Node.newBinaryOperator(Token.COMPARATOR, Node.newIdent("x"), Token.LT, Node.newIntLiteral(0)), Node.newBlock(Token.BLOCK, Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(1))), Node.newElseStatement(Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(2))));
    assertTrue(NodeUtil.isStatement(nodeIf.getElseStatement().getTruePart()));
    assertTrue(NodeUtil.isStatement(nodeIf.getElseStatement().getFalsePart()));

    Node nodeAssigment = Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newBinaryOperator(Token.ASSIGN, Node.newIdent("x"), Node.newIdent("y")));
    assertTrue(NodeUtil.isStatement(nodeAssigment));

    Node nodeExpression = Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Token.IDENT("y"));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Token.FUNCTION, Token.IDENT("myFunction"), new Node[0], Node.newBlock(Token.BLOCK, Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(10))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newFunctionExpression(Token.FUNCTION, Token.IDENT("myFunction"), new Node[0], Node.newBlock(Token.BLOCK, Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(10))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(Token.SCRIPT,
        Node.newFunction(Token.FUNCTION, Token.IDENT("myFunction"), new Node[0],
            Node.newBlock(Token.BLOCK,
                Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(10)),
                Node.newFunction(Token.FUNCTION, Token.IDENT("myOtherFunction"), new Node[0],
                    Node.newBlock(Token.BLOCK, Node.newExpressionStatement(Token.ASSIGN, Token.Ident("y"), Node.newIntLiteral(20)))))));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild().getFirstChild()));

    Node nodeFunctionInsideAnotherFunction = Node.newFunction(Token.FUNCTION, Token.IDENT("myFunction"), new Node[0],
        Node.newBlock(Token.BLOCK,
            Node.newExpressionStatement(Token.ASSIGN, Token.Ident("x"), Node.newIntLiteral(10)),
            Node.newFunction(Token.FUNCTION, Token.IDENT("myOtherFunction"), new Node[0],
                Node.newBlock(Token.BLOCK, Node.newExpressionStatement(Token.ASSIGN, Token.Ident("y"), Node.newIntLiteral(20))))));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideAnotherFunction));
  }
}