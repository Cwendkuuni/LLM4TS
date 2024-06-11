package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressions() {
    String jsCode = "function foo() { return 1; } function bar() { return 2; } "
        + "function baz() { return 3; } "
        + "1 + foo() + bar() + baz();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = null;
    Node root = new Node(Token.PROGRAM, Lists.newArrayList(), jsCode);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(externs, root);

    Node simplifiedRoot = root;
    Node currentNode = simplifiedRoot;
    while (currentNode.getNextSibling() != null) {
      currentNode = currentNode.getNextSibling();
    }

    // Check if the constant expressions have been removed
    assertTrue(currentNode.getParent().getLastChild() instanceof Node
        && currentNode.getParent().getLastChild().getType() == Token.EXPR_STATEMENT
        && currentNode.getType() == Token.EXPR_STATEMENT);

    Node statement1 = (Node) currentNode.getParent().getLastChild();
    Node statement2 = currentNode;

    List<Node> statement1Children = statement1.getChildren();
    Node callExpression1 = statement1Children.get(0);

    // Check if the expression bodies have been replaced with the side effect nodes
    List<Node> expectedSideEffectNodes = Lists.newArrayList(
        callExpression1.getFirstChild(),
        statement2,
        callExpression1.getLastChild());

    List<Node> actualSideEffectNodes = Lists.newArrayList();
    Node currentSideEffectNode = statement1;

    while (currentSideEffectNode != statement2) {
      actualSideEffectNodes.add(currentSideEffectNode);
      currentSideEffectNode = currentSideEffectNode.getNextSibling();
    }

    NodeTraversal.traverse(null, statement1, new NodeChecker());

    for (int i = 0; i < expectedSideEffectNodes.size(); i++) {
      Node expectedNode = expectedSideEffectNodes.get(i);
      Node actualNode = actualSideEffectNodes.get(i);
      assertEquals("Unexpected node type for expected side effect node " + i,
          expectedNode.getType(), actualNode.getType());
    }

    assertFalse("Unexpected number of side effect nodes",
        actualSideEffectNodes.size() != expectedSideEffectNodes.size());
  }

  private static class NodeChecker extends AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (node.getType() == Token.EXPR_STATEMENT) {
        Node statementNode = (Node) node;
        List<Node> children = statementNode.getChildren();

        if (children.size() > 1 && children.get(0).getType() == Token.CALL_EXPRESSION) {
          Node callExpressionNode = (Node) children.get(0);
          List<Node> callExpressionChildren = callExpressionNode.getChildren();

          if (callExpressionChildren.size() > 1
              && callExpressionChildren.get(0).getType() == Token.IDENTIFIER) {
            String identifier = callExpressionChildren.get(0).getString();
            assertTrue(identifier.equals("foo") || identifier.equals("bar") || identifier.equals("baz"));
          }
        }
      }
    }
  }

  private void assertEquals(String message, int expected, int actual) {
    if (expected != actual) {
      throw new AssertionError(message + " Expected: " + expected + " Actual: " + actual);
    }
  }
}