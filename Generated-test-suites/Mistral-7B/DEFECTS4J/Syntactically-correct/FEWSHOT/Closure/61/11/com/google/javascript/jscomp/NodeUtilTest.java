package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpression("1 + 1");;
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpression("var x = 1;");
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression("1 + 1");;
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("foo", new Node[0], Node.newBlock(Node.newExpression("1")));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression("function foo() {}");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = Node.newFunction("foo", new Node[0], Node.newBlock(Node.newExpression("1")));
    Node parentScript = Node.newScript(new Node[0]);
    parentScript.appendChild(nodeFunctionDeclarationInScript);
    Node functionDeclarationInScript = NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript);
    assertTrue(functionDeclarationInScript == nodeFunctionDeclarationInScript);

    Node nodeFunctionDeclarationInFunction = Node.newFunction("function foo() {}", Node.newFunction("bar", new Node[0], Node.newBlock(Node.newExpression("1"))));
    Node parentFunction = Node.newFunction("parent", new Node[0], Node.newBlock(Node.newExpression("1")));
    parentFunction.appendChild(nodeFunctionDeclarationInFunction);
    Node functionDeclarationInFunction = NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction);
    assertTrue(functionDeclarationInFunction == nodeFunctionDeclarationInFunction);

    Node nodeNotFunctionDeclaration = Node.newExpression("1 + 1");;
    Node functionDeclarationNotParent = Node.newFunction("foo", new Node[0], Node.newBlock(Node.newExpression("1")));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(nodeNotFunctionDeclaration));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNotParent));
  }

  @Test
  public void testIsFunctionExpression() {
    Node nodeFunctionExpression = Node.newExpression("function foo() {}");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpression));
    assertTrue(NodeUtil.isFunctionExpression(nodeFunctionExpression));

    Node nodeFunctionDeclaration = Node.newFunction("foo", new Node[0], Node.newBlock(Node.newExpression("1")));
    assertFalse(NodeUtil.isFunctionExpression(nodeFunctionDeclaration));
  }

  @Test
  public void testIsStatementParentScript() {
    Node nodeStatement = Node.newExpression("var x = 1;");
    Node parentScript = Node.newScript(new Node[0]);
    parentScript.appendChild(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeStatement.getParent()));
  }

  @Test
  public void testIsStatementParentBlock() {
    Node nodeStatement = Node.newExpression("var x = 1;");
    Node parentBlock = Node.newBlock(new Node[0]);
    parentBlock.appendChild(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeStatement.getParent()));
  }

  @Test
  public void testIsStatementParentLabel() {
    Node nodeStatement = Node.newExpression("var x = 1;");
    Node parentLabel = Node.newLabel("label");
    parentLabel.appendChild(nodeStatement);
    assertFalse(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeStatement.getParent()));
  }

  @Test
  public void testIsStatementParentFunction() {
    Node nodeStatement = Node.newExpression("var x = 1;");
    Node parentFunction = Node.newFunction("function foo() {}", new Node[0]);
    parentFunction.appendChild(nodeStatement);
    assertFalse(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeStatement.getParent()));
  }
}