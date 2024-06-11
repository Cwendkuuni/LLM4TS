package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphTestUtils;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link DeadAssignmentsElimination}.
 */
public class DeadAssignmentsEliminationTest {

  private static AbstractCompiler compiler = new NoOpCompiler();

  /**
   * Test for dead assignment elimination.
   */
  @org.junit.Test
  public void testDeadAssignmentsElimination() {
    String jsCode = "function foo() {" +
        "  var a = 1, b = 2, c = 3;" +
        "  if (x > 0) {" +
        "    b = 4;" +
        "  } else {" +
        "    a = 5;" +
        "  }" +
        "  c = a;" +
        "  return c;" +
        "}";

    Node root = NodeTraversal.parseAndTraverse(compiler, jsCode, NodeType.PROGRAM);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);

    ControlFlowGraph<Node> controlFlowGraph = deadAssignmentsElimination.liveness.getGraph();

    List<Node> expected = Arrays.asList(
        NodeFactory.createVariableDeclarationFromString(
            root, "a", Token.VAR, Token.ASSIGN, NodeFactory.createNumberLiteral(5)),
        NodeFactory.createVariableDeclarationFromString(
            root, "b", Token.VAR, Token.ASSIGN, NodeFactory.createNumberLiteral(4)),
        NodeFactory.createNumberLiteral(5));

    List<Node> actual = GraphTestUtils.getPostOrderNodes(controlFlowGraph);

    for (int i = 0; i < expected.size(); i++) {
      Node expectedNode = expected.get(i);
      Node actualNode = actual.get(i);
      assertTrue(
          "Expected node " + expectedNode + " but got " + actualNode,
          NodeUtil.isNodeEqual(expectedNode, actualNode));
    }

    assertFalse(
        "Expected no dead assignments but found one",
        deadAssignmentsElimination.tryRemoveAssignment(
            NodeTraversal.getLastTraversal(compiler),
            NodeUtil.getConditionExpression(
                NodeUtil.getParentOfType(root, Token.IF)),
            deadAssignmentsElimination.liveness.getGraph().getDirectedGraphNodes().get(0).getAnnotation()));
  }

  /**
   * Test for dead assignment elimination in a loop.
   */
  @org.junit.Test
  public void testDeadAssignmentsEliminationInLoop() {
    String jsCode = "function foo() {" +
        "  var a = 1;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    a = i;" +
        "  }" +
        "  return a;" +
        "}";

    Node root = NodeTraversal.parseAndTraverse(compiler, jsCode, NodeType.PROGRAM);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);

    List<Node> expected = Arrays.asList(
        NodeFactory.createVariableDeclarationFromString(
            root, "a", Token.VAR, Token.ASSIGN, NodeFactory.createNumberLiteral(10)));

    List<Node> actual = GraphTestUtils.getPostOrderNodes(
        deadAssignmentsElimination.liveness.getGraph());

    for (int i = 0; i < expected.size(); i++) {
      Node expectedNode = expected.get(i);
      Node actualNode = actual.get(i);
      assertTrue(
          "Expected node " + expectedNode + " but got " + actualNode,
          NodeUtil.isNodeEqual(expectedNode, actualNode));
    }

    assertFalse(
        "Expected no dead assignments but found one",
        deadAssignmentsElimination.tryRemoveAssignment(
            NodeTraversal.getLastTraversal(compiler),
            NodeUtil.getParentOfType(root, Token.FOR),
            deadAssignmentsElimination.liveness.getGraph().getDirectedGraphNodes().get(0).getAnnotation()));
  }

  /**
   * Test for dead assignment elimination with function call.
   */
  @org.junit.Test
  public void testDeadAssignmentsEliminationWithFunctionCall() {
    String jsCode = "function foo() {" +
        "  var a = 1;" +
        "  var b = foo();" +
        "  a = b;" +
        "  return a;" +
        "}" +
        "function bar() {" +
        "  return 1;" +
        "}";

    Node root = NodeTraversal.parseAndTraverse(compiler, jsCode, NodeType.PROGRAM);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);

    List<Node> expected = Arrays.asList(
        NodeFactory.createVariableDeclarationFromString(
            root, "a", Token.VAR, Token.ASSIGN, NodeFactory.createName("b")));

    List<Node> actual = GraphTestUtils.getPostOrderNodes(
        deadAssignmentsElimination.liveness.getGraph());

    for (int i = 0; i < expected.size(); i++) {
      Node expectedNode = expected.get(i);
      Node actualNode = actual.get(i);
      assertTrue(
          "Expected node " + expectedNode + " but got " + actualNode,
          NodeUtil.isNodeEqual(expectedNode, actualNode));
    }

    assertFalse(
        "Expected no dead assignments but found one",
        deadAssignmentsElimination.tryRemoveAssignment(
            NodeTraversal.getLastTraversal(compiler),
            NodeUtil.getFunctionDeclaration(
                NodeUtil.getParentOfType(root, Token.FUNCTION), "foo"),
            deadAssignmentsElimination.liveness.getGraph().getDirectedGraphNodes().get(0).getAnnotation()));
  }

  /**
   * Test for dead assignment elimination with complex control flow.
   */
  @org.junit.Test
  public void testDeadAssignmentsEliminationWithComplexControlFlow() {
    String jsCode = "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  if (x > 0) {" +
        "    if (y > 0) {" +
        "      a = 3;" +
        "    } else {" +
        "      b = 3;" +
        "    }" +
        "  } else {" +
        "    b = 3;" +
        "  }" +
        "  return b;" +
        "}" +
        "function bar() {" +
        "  return 1;" +
        "}";

    Node root = NodeTraversal.parseAndTraverse(compiler, jsCode, NodeType.PROGRAM);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal.traverse(compiler, root, deadAssignmentsElimination);

    List<Node> expected = Arrays.asList(
        NodeFactory.createVariableDeclarationFromString(
            root, "b", Token.VAR, Token.ASSIGN, NodeFactory.createNumberLiteral(1)),
        NodeFactory.createVariableDeclarationFromString(
            root, "a", Token.VAR, Token.ASSIGN, NodeFactory.createNumberLiteral(3)),
        NodeFactory.createNumberLiteral(3));

    List<Node> actual = GraphTestUtils.getPostOrderNodes(
        deadAssignmentsElimination.liveness.getGraph());

    for (int i = 0; i < expected.size(); i++) {
      Node expectedNode = expected.get(i);
      Node actualNode = actual.get(i);
      assertTrue(
          "Expected node " + expectedNode + " but got " + actualNode,
          NodeUtil.isNodeEqual(expectedNode, actualNode));
    }

    assertFalse(
        "Expected no dead assignments but found one",
        deadAssignmentsElimination.tryRemoveAssignment(
            NodeTraversal.getLastTraversal(compiler),
            NodeUtil.getConditionExpression(
                NodeUtil.getParentOfType(root, Token.IF)),
            deadAssignmentsElimination.liveness.getGraph().getDirectedGraphNodes().get(0).getAnnotation()));
  }
}