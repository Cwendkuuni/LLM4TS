package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class RemoveConstantExpressionsTest {
  private NodeFactory nodeFactory;
  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setup() {
    nodeFactory = new NodeFactory();
    removeConstantExpressions = new RemoveConstantExpressions(null);
  }

  /**
   * Test method for RemoveConstantExpressions class.
   */
  @Test
  public void testRemoveConstantExpressions() {
    Node root = createTestNode();
    Result result = new Result();
    removeConstantExpressions.process(null, root);
    assertFalse(result.changed);
  }

  /**
   * Test method for trySimplify() method with no side effect expression.
   */
  @Test
  public void testTrySimplifyNoSideEffectExpression() {
    Node root = createTestNode();
    Node constantExpression = createConstantExpression();
    Node parent = createExpressionStatement(root);
    Node nodeToReplace = createExpressionStatement(parent, constantExpression);

    removeConstantExpressions.process(null, root);
    assertNull(nodeToReplace);
  }

  /**
   * Test method for trySimplify() method with side effect expression.
   */
  @Test
  public void testTrySimplifySideEffectExpression() {
    Node root = createTestNode();
    Node sideEffectExpression = createAssignmentExpression();
    Node parent = createExpressionStatement(root);
    Node nodeToReplace = createExpressionStatement(parent, sideEffectExpression);

    removeConstantExpressions.process(null, root);
    assertTrue(nodeToReplace.getFirstChild().getText().equals(""));
  }

  /**
   * Test method for getSideEffectNodes() method.
   */
  @Test
  public void testGetSideEffectNodes() {
    Node root = createTestNode();
    Node sideEffectExpression = createAssignmentExpression();
    Node subExpression = createAdditionExpression();
    Node subExpression2 = createMultiplicationExpression();

    List<Node> sideEffectNodes = removeConstantExpressions.new RemoveConstantRValuesCallback().getSideEffectNodes(sideEffectExpression);
    assertTrue(sideEffectNodes.size() > 0);
    assertTrue(sideEffectNodes.get(0).getFirstChild().equals(subExpression));
    assertTrue(sideEffectNodes.get(1).getFirstChild().equals(subExpression2));
  }

  /**
   * Test method for process() method with no nodes to replace.
   */
  @Test
  public void testProcessNoNodesToReplace() {
    Node root = createTestNode();
    Result result = removeConstantExpressions.process(null, root);
    assertFalse(result.changed);
  }

  /**
   * Test method for process() method with nodes to replace.
   */
  @Test
  public void testProcessNodesToReplace() {
    Node root = createTestNode();
    Node constantExpression1 = createConstantExpression();
    Node constantExpression2 = createConstantExpression();
    Node sideEffectExpression = createAssignmentExpression();
    Node parent = createExpressionStatement(root);
    Node nodeToReplace1 = createExpressionStatement(parent, constantExpression1);
    Node nodeToReplace2 = createExpressionStatement(parent, constantExpression2);
    Node nodeToReplace3 = createExpressionStatement(parent, sideEffectExpression);

    result = removeConstantExpressions.process(null, root);
    assertFalse(result.changed);
    assertNull(nodeToReplace1);
    assertNull(nodeToReplace2);
    assertTrue(nodeToReplace3.getFirstChild().getText().equals(""));
  }

  private Node createTestNode() {
    return new Node(Token.ROOT_NODE, null);
  }

  private Node createAssignmentExpression() {
    return new Node(Token.ASSIGN, null);
  }

  private Node createAdditionExpression() {
    Node left = new Node(Token.NAME, "a");
    Node right = new Node(Token.NUMBER, 1);
    return new Node(Token.ADD, null, left, right);
  }

  private Node createMultiplicationExpression() {
    Node left = new Node(Token.NAME, "b");
    Node right = new Node(Token.NUMBER, 2);
    return new Node(Token.MUL, null, left, right);
  }

  private Node createConstantExpression() {
    return new Node(Token.NUMBER, 1);
  }

  private Node createExpressionStatement(Node parent, Node node) {
    Node statement = new Node(Token.EXPR_STATEMENT, null);
    statement.setParent(parent);
    statement.addChild(node);
    return statement;
  }
}