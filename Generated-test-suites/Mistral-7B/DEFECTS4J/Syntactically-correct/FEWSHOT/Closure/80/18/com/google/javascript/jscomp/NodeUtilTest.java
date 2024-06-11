package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNodeForForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNodeNotForIn();
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNodeStatement();
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = createNodeNotStatement();
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNodeFunctionDeclaration();
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNodeFunctionExpression();
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = createNodeHoistedFunctionDeclaration();
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = createNodeNotHoistedFunctionDeclaration();
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  private Node createNodeForForIn() {
    Node forToken = new Node(Token.FOR);
    Node init = new Node(Token.VAR);
    init.setProperty("id", "i");
    init.setProperty("value", new Node(Token.INT_LITERAL, 0));
    Node condition = new Node(Token.IDENTIFIER, "i");
    condition.setProperty("value", "i < 10");
    Node increment = new Node(Token.ASSIGN, new Node(Token.IDENTIFIER, "i"), new Node(Token.INT_LITERAL, 1));
    forToken.setProperty("init", init);
    forToken.setProperty("condition", condition);
    forToken.setProperty("increment", increment);
    forToken.setProperty("statements", new Node(Token.BLOCK, new Node[0]));
    return forToken;
  }

  private Node createNodeNotForIn() {
    Node ifToken = new Node(Token.IF);
    Node condition = new Node(Token.IDENTIFIER, "i");
    condition.setProperty("value", "i < 10");
    Node statements = new Node(Token.BLOCK, new Node[0]);
    ifToken.setProperty("condition", condition);
    ifToken.setProperty("statements", statements);
    return ifToken;
  }

  private Node createNodeStatement() {
    Node statement = new Node(Token.ASSIGN, new Node(Token.IDENTIFIER, "x"), new Node(Token.INT_LITERAL, 42));
    return statement;
  }

  private Node createNodeNotStatement() {
    Node expression = new Node(Token.ASSIGN, new Node(Token.IDENTIFIER, "x"), new Node(Token.IDENTIFIER, "y"));
    return expression;
  }

  private Node createNodeFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION, new Node[0], new Node[0]);
    Node name = new Node(Token.IDENTIFIER, "myFunction");
    function.setProperty("id", name.getProperty("name"));
    name.setParent(function);
    Node body = new Node(Token.BLOCK, new Node[0]);
    function.setProperty("body", body);
    return function;
  }

  private Node createNodeFunctionExpression() {
    Node expression = new Node(Token.FUNCTION, new Node[0], new Node[0]);
    Node name = new Node(Token.IDENTIFIER, "myFunction");
    Node body = new Node(Token.BLOCK, new Node[0]);
    expression.setProperty("id", name.getProperty("name"));
    name.setParent(expression);
    expression.setProperty("body", body);
    Node call = new Node(Token.CALL, expression, new Node[0]);
    return call;
  }

  private Node createNodeHoistedFunctionDeclaration() {
    Node script = new Node(Token.SCRIPT, new Node[0]);
    Node functionDeclaration = createNodeFunctionDeclaration();
    script.setProperty("body", functionDeclaration);
    return functionDeclaration;
  }

  private Node createNodeNotHoistedFunctionDeclaration() {
    Node function = createNodeFunctionDeclaration();
    Node statement = new Node(Token.ASSIGN, new Node(Token.IDENTIFIER, "x"), new Node(Token.INT_LITERAL, 42));
    Node block = new Node(Token.BLOCK, new Node[0]);
    block.setProperty("statements", statement);
    function.setProperty("body", block);
    Node script = new Node(Token.SCRIPT, new Node[0]);
    script.setProperty("body", function);
    return function;
  }
}