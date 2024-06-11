package com.example.junit;

import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR);
    Node forNodeWithThreeChildren = Node.newNode(Token.FOR, Node.newNode(Token.IDENTIFIER, "i"), Node.newNode(Token.KEYWORD, "in"), Node.newNode(Token.IDENTIFIER, "myArray"));
    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"))));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IDENTIFIER, "myStatement");
    Node expressionNode = Node.newNode(Token.IDENTIFIER, "myExpression");
    Node functionNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"));
    Node scriptNode = Node.newNode(Token.SCRIPT, Node.newNode(Token.BLOCK, Node.newNode(Token.IDENTIFIER, "myBlock"), statementNode));
    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node notFunctionNode = Node.newNode(Token.IDENTIFIER, "myIdentifier");
    Node nonStatementFunctionNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))));
    Node functionDeclarationWithoutName = Node.newNode(Token.FUNCTION, Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))));
    Node functionDeclarationNotInScopeRoot = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))), Node.newNode(Token.SCRIPT, Node.newNode(Token.BLOCK, Node.newNode(Token.IDENTIFIER, "myBlock"))));

    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(nonStatementFunctionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionDeclarationWithoutName));
    assertFalse(NodeUtil.isFunctionDeclaration(functionDeclarationNotInScopeRoot));

    Node hoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))));
    Node nonHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))), Node.newNode(Token.BLOCK, Node.newNode(Token.IDENTIFIER, "myBlock")));

    assertTrue(NodeUtil.isFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nonHoistedFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nonFunctionNode = Node.newNode(Token.IDENTIFIER, "myIdentifier");
    Node nonFunctionDeclaration = Node.newNode(Token.IDENTIFIER, "myIdentifier");
    Node functionNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))));
    Node functionDeclarationNotInScopeRoot = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))), Node.newNode(Token.SCRIPT, Node.newNode(Token.BLOCK, Node.newNode(Token.IDENTIFIER, "myBlock"))));
    Node hoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))), Node.newNode(Token.SCRIPT, Node.newNode(Token.BLOCK)));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonFunctionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNotInScopeRoot));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }

  @Test
  public void testIsForInWithEmptyNode() {
    Node emptyNode = Node.newNode();
    assertFalse(NodeUtil.isForIn(emptyNode));
  }

  // Add other tests for edge cases and exception handling scenarios here

  // ...
}