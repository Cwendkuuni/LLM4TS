package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = createNodeForForIn();
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeIf = createNodeForIfStatement();
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeFunction = createNodeForFunctionDeclaration();
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = createNodeForScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunction = createNodeForFunctionExpression();
    assertFalse(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNodeForFunctionDeclaration();
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNodeForFunctionExpression();
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = createNodeForFunctionDeclarationInScript();
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = createNodeForFunctionDeclarationInFunction();
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  private Node createNodeForFunctionDeclaration() {
    return createNode(Token.FUNCTION, "foo", new Node[0], null);
  }

  private Node createNodeForFunctionExpression() {
    return createNode(Token.FUNCTION, null, new Node[0], new Node(Token.ASSIGN, null, new Node[0]));
  }

  private Node createNodeForForIn() {
    return createNode(Token.FOR, null, new Node[0], new Node(Token.FOR_IN, null, new Node[0], new Node[0], new Node[0]));
  }

  private Node createNodeForIfStatement() {
    return createNode(Token.IF, null, new Node[0], new Node(Token.BLOCK, null, new Node[0]));
  }

  private Node createNodeForScript() {
    return createNode(Token.SCRIPT, null, new Node[0], null);
  }

  private Node createNodeForLabel() {
    return createNode(Token.LABEL, "foo", new Node[0], null);
  }

  private Node createNodeForFunctionDeclarationInScript() {
    Node nodeFunctionDeclaration = createNodeForFunctionDeclaration();
    Node nodeScript = createNode(Token.SCRIPT, null, new Node[0], null);
    nodeScript.addChild(nodeFunctionDeclaration);
    return nodeFunctionDeclaration;
  }

  private Node createNodeForFunctionDeclarationInFunction() {
    Node nodeFunctionDeclaration = createNodeForFunctionDeclaration();
    Node nodeFunction = createNode(Token.FUNCTION, null, new Node[0], null);
    nodeFunction.addChild(nodeFunctionDeclaration);
    return nodeFunctionDeclaration;
  }

  private Node createNode(int type, String name, Node[] children, Node parent) {
    Node node = new Node();
    node.setType(type);
    if (name != null) {
      node.setName(name);
    }
    node.setParent(parent);
    if (children != null) {
      for (Node child : children) {
        node.addChild(child);
      }
    }
    return node;
  }

  @Test
  public void testPreconditions() {
    checkArgument(true);
    checkState(true);
  }
}