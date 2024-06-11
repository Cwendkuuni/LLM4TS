package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, new Node[0], new Node[0]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeForStatement = Node.newForStatement(1, new Node[0], new Node[0], null, null);
    assertFalse(NodeUtil.isForIn(nodeForStatement));

    Node nodeWhileStatement = Node.newWhileStatement(1, null, null);
    assertFalse(NodeUtil.isForIn(nodeWhileStatement));

    Node nodeIfStatement = Node.newStatementList(1, Node.newStatementList(1, Node.newIfStatement(1, null, null, null)));
    assertFalse(NodeUtil.isForIn(nodeIfStatement));

    Node nodeStatement = Node.newStatementList(1, Node.newExpressionStatement(Node.newIdentifier("x"))).get(0);
    assertFalse(NodeUtil.isForIn(nodeStatement));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript(new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeBlock = Node.newBlock(1, new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeLabel = Node.newLabel(1, "label");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeFunction = Node.newFunction(1, "name", new Node[0], null, null, null, null, new Node[0], null);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeExpression = Node.newExpressionStatement(Node.newNumberLiteral(1));
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeVariableDeclaration = Node.newVariableDeclarationList(1, Node.newVariableDeclaration(1, "x", null, Node.newNumberLiteral(1)));
    assertFalse(NodeUtil.isStatement(nodeVariableDeclaration));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(1, "name", new Node[0], null, null, null, null, new Node[0], null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunctionExpression(1, null, new Node[0], null, null, null, null, new Node[0], null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeVariableDeclaration = Node.newVariableDeclarationList(1, Node.newVariableDeclaration(1, "x", null, Node.newNumberLiteral(1)));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeVariableDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(new Node[] {
      Node.newFunctionDeclarationList(1, Node.newFunctionDeclaration(1, "name", new Node[0], null, null, null, null, new Node[0], null))
    });
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));

    Node nodeFunction = Node.newFunction(1, "name", new Node[0], null, null, null, null, new Node[0], null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInFunction = Node.newFunction(1, "name", new Node[0], null, null, null, null, null, new Node[] {Node.newFunctionDeclarationList(1, Node.newFunctionDeclaration(1, "x", new Node[0], null, null, null, null, new Node[0], null))});
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testNullNode() {
    checkNull(NodeUtil.isForIn((Node) null));
    checkNull(NodeUtil.isStatement((Node) null));
    checkNull(NodeUtil.isFunctionDeclaration((Node) null));
  }

  @Test
  public void testEmptyNode() {
    Node emptyNode = Node.newScript(new Node[0]);
    assertFalse(NodeUtil.isForIn(emptyNode));
    assertFalse(NodeUtil.isStatement(emptyNode));
    assertFalse(NodeUtil.isFunctionDeclaration(emptyNode));
  }
}