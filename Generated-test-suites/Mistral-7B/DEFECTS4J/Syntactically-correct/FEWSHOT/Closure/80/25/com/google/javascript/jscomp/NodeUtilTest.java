package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(Token.FOR, 1, null, 1, 1, new Node[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(Token.IF, 1, null, 1, 1, new Node[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(Token.IF, 1, null, 1, 1, new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = createNode(Token.ADD, 1, new Node[] { createNode(Token.NUMBER, 1, null, 1, 1, null), createNode(Token.NUMBER, 1, null, 1, 1, null) }, 1, 2, new Node[0]);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION, 1, null, 1, 1, new Node[1]);
    nodeFunctionDeclaration.setProperty("name", "myFunction");

    Node nodeNotFunctionDeclaration = createNode(Token.IF, 1, null, 1, 1, new Node[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));

    Node nodeFunctionDeclarationWithName = createNode(Token.FUNCTION, 1, null, 1, 1, new Node[1]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationWithName));

    Node nodeFunctionDeclarationWithBody = createNode(Token.FUNCTION, 1, null, 1, 1, new Node[2]);
    nodeFunctionDeclarationWithBody.setProperty("body", createNode(Token.BLOCK, 1, null, 1, 1, new Node[0]));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationWithBody));

    Node nodeFunctionDeclarationWithNameAndBody = createNode(Token.FUNCTION, 1, null, 1, 1, new Node[2]);
    nodeFunctionDeclarationWithNameAndBody.setProperty("name", "myFunction");
    nodeFunctionDeclarationWithNameAndBody.setProperty("body", createNode(Token.BLOCK, 1, null, 1, 1, new Node[0]));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationWithNameAndBody));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION, 1, null, 1, 1, new Node[1]);
    nodeFunctionDeclaration.setProperty("name", "myFunction");

    Node nodeScript = createNode(Token.SCRIPT, 1, null, 1, 1, new Node[0]);
    nodeFunctionDeclaration.setParent(nodeScript);

    Node nodeHoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration);
    assertTrue(nodeHoistedFunctionDeclaration == nodeFunctionDeclaration);

    Node nodeFunctionDeclarationInFunction = createNode(Token.FUNCTION, 1, null, 1, 1, new Node[1]);
    nodeFunctionDeclarationInFunction.setProperty("name", "myFunction");
    Node nodeFunction = createNode(Token.FUNCTION, 1, null, 1, 1, new Node[1]);
    nodeFunction.setProperty("body", nodeScript);
    nodeFunctionDeclarationInFunction.setParent(nodeFunction);

    Node nodeNotHoistedFunctionDeclaration = createNode(Token.FUNCTION, 1, null, 1, 1, new Node[1]);
    nodeNotHoistedFunctionDeclaration.setProperty("name", "myFunction");
    nodeNotHoistedFunctionDeclaration.setParent(createNode(Token.BLOCK, 1, null, 1, 1, new Node[1]));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  private Node createNode(int type, int line, Node parent, int start, int end, Node[] children) {
    Node node = new Node();
    node.setType(type);
    node.setLine(line);
    node.setParent(parent);
    node.setStart(start);
    node.setEnd(end);
    node.setChildren(children);
    return node;
  }

  private void assertFalse(boolean condition) {
    checkNotNull(condition);
    assertFalse("Expected false but was: " + condition, condition);
  }
}