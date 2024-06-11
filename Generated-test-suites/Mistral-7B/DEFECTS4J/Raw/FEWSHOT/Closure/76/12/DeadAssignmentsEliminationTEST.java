package org.example.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.ControlFlowGraph;
import org.apache.commons.cli.DeadAssignmentsElimination;
import org.apache.commons.cli.DiGraphNode;
import org.apache.commons.cli.FlowState;
import org.apache.commons.cli.LiveVariablesAnalysis;
import org.apache.commons.cli.Node;
import org.apache.commons.cli.NodeTraversal;
import org.apache.commons.cli.Scope;
import org.apache.commons.cli.compiler.AbstractCompiler;
import org.apache.commons.cli.compiler.CompilerPass;
import org.apache.commons.cli.compiler.NodeUtil;
import org.apache.commons.cli.compiler.ScopedCallback;
import org.apache.commons.cli.compiler.graph.DiGraph;
import org.apache.commons.cli.compiler.graph.DiGraphNode;
import org.apache.commons.cli.compiler.graph.Node;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Test empty code
    Node externs = Node.newBlock("empty");
    Node root = Node.newBlock("empty");
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(new NoopCompiler());
    deadAssignmentsElimination.process(externs, root);

    // Test no removable assignments
    String codeNoRemovableAssignments =
        "function foo() {}";
    Node externsNoRemovableAssignments =
            Node.newBlock("function foo() {");
    externsNoRemovableAssignments.addChild(Node.newText("}"));
    Node rootNoRemovableAssignments = Node.parse(codeNoRemovableAssignments);
    DeadAssignmentsElimination deadAssignmentsEliminationNoRemovableAssignments =
        new DeadAssignmentsElimination(new NoopCompiler());
    deadAssignmentsEliminationNoRemovableAssignments.process(externsNoRemovableAssignments, rootNoRemovableAssignments);
    NodeTraversal nodeTraversal = new NodeTraversal(rootNoRemovableAssignments);
    NodeUtil.traverse(nodeTraversal);
    assertFalse(nodeTraversal.hasVisited());

    // Test simple dead assignment elimination
    String codeSimpleDeadAssignment =
        "function foo() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  return x;" +
        "}";
    Node externsSimpleDeadAssignment =
            Node.newBlock("function foo() {");
    externsSimpleDeadAssignment.addChild(Node.newText("}"));
    Node rootSimpleDeadAssignment = Node.parse(codeSimpleDeadAssignment);
    DeadAssignmentsElimination deadAssignmentsEliminationSimpleDeadAssignment =
        new DeadAssignmentsElimination(new NoopCompiler());
    deadAssignmentsEliminationSimpleDeadAssignment.process(externsSimpleDeadAssignment, rootSimpleDeadAssignment);
    NodeTraversal nodeTraversalSimpleDeadAssignment = new NodeTraversal(rootSimpleDeadAssignment);
    NodeUtil.traverse(nodeTraversalSimpleDeadAssignment);
    assertTrue(nodeTraversalSimpleDeadAssignment.hasVisited());
    assertNull(nodeTraversalSimpleDeadAssignment.getCurrentNode().getFirstChild());

    // Test dead assignment elimination within if statement
    String codeDeadAssignmentWithIf =
        "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";
    Node externsDeadAssignmentWithIf =
            Node.newBlock("function foo() {");
    externsDeadAssignmentWithIf.addChild(Node.newText("}"));
    Node rootDeadAssignmentWithIf = Node.parse(codeDeadAssignmentWithIf);
    DeadAssignmentsElimination deadAssignmentsEliminationDeadAssignmentWithIf =
        new DeadAssignmentsElimination(new NoopCompiler());
    deadAssignmentsEliminationDeadAssignmentWithIf.process(externsDeadAssignmentWithIf, rootDeadAssignmentWithIf);
    NodeTraversal nodeTraversalDeadAssignmentWithIf = new NodeTraversal(rootDeadAssignmentWithIf);
    NodeUtil.traverse(nodeTraversalDeadAssignmentWithIf);
    assertTrue(nodeTraversalDeadAssignmentWithIf.hasVisited());
    assertNull(nodeTraversalDeadAssignmentWithIf.getCurrentNode().getFirstChild());

    // Test dead assignment elimination within for statement
    String codeDeadAssignmentWithFor =
        "function foo() {" +
        "  var x = 1;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    x = i;" +
        "  }" +
        "  return x;" +
        "}";
    Node externsDeadAssignmentWithFor =
            Node.newBlock("function foo() {");
    externsDeadAssignmentWithFor.addChild(Node.newText("}"));
    Node rootDeadAssignmentWithFor = Node.parse(codeDeadAssignmentWithFor);
    DeadAssignmentsElimination deadAssignmentsEliminationDeadAssignmentWithFor =
        new DeadAssignmentsElimination(new NoopCompiler());
    deadAssignmentsEliminationDeadAssignmentWithFor.process(externsDeadAssignmentWithFor, rootDeadAssignmentWithFor);
    NodeTraversal nodeTraversalDeadAssignmentWithFor = new NodeTraversal(rootDeadAssignmentWithFor);
    NodeUtil.traverse(nodeTraversalDeadAssignmentWithFor);
    assertTrue(nodeTraversalDeadAssignmentWithFor.hasVisited());
    assertNull(nodeTraversalDeadAssignmentWithFor.getCurrentNode().getFirstChild());

    // Test dead assignment elimination within switch statement
    String codeDeadAssignmentWithSwitch =
        "function foo() {" +
        "  var x = 1;" +
        "  switch (1) {" +
        "    case 1:" +
        "      x = 2;" +
        "  }" +
        "  return x;" +
        "}";
    Node externsDeadAssignmentWithSwitch =
            Node.newBlock("function foo() {");
    externsDeadAssignmentWithSwitch.addChild(Node.newText("}"));
    Node rootDeadAssignmentWithSwitch = Node.parse(codeDeadAssignmentWithSwitch);
    DeadAssignmentsElimination deadAssignmentsEliminationDeadAssignmentWithSwitch =
        new DeadAssignmentsElimination(new NoopCompiler());
    deadAssignmentsEliminationDeadAssignmentWithSwitch.process(externsDeadAssignmentWithSwitch, rootDeadAssignmentWithSwitch);
    NodeTraversal nodeTraversalDeadAssignmentWithSwitch = new NodeTraversal(rootDeadAssignmentWithSwitch);
    NodeUtil.traverse(nodeTraversalDeadAssignmentWithSwitch);
    assertTrue(nodeTraversalDeadAssignmentWithSwitch.hasVisited());
    assertNull(nodeTraversalDeadAssignmentWithSwitch.getCurrentNode().getFirstChild());

    // Test dead assignment elimination within while statement
    String codeDeadAssignmentWithWhile =
        "function foo() {" +
        "  var x = 1;" +
        "  while (true) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";
    Node externsDeadAssignmentWithWhile =
            Node.newBlock("function foo() {");
    externsDeadAssignmentWithWhile.addChild(Node.newText("}"));
    Node rootDeadAssignmentWithWhile = Node.parse(codeDeadAssignmentWithWhile);
    DeadAssignmentsElimination deadAssignmentsEliminationDeadAssignmentWithWhile =
        new DeadAssignmentsElimination(new NoopCompiler());
    deadAssignmentsEliminationDeadAssignmentWithWhile.process(externsDeadAssignmentWithWhile, rootDeadAssignmentWithWhile);
    NodeTraversal nodeTraversalDeadAssignmentWithWhile = new NodeTraversal(rootDeadAssignmentWithWhile);
    NodeUtil.traverse(nodeTraversalDeadAssignmentWithWhile);
    assertTrue(nodeTraversalDeadAssignmentWithWhile.hasVisited());
    assertNull(nodeTraversalDeadAssignmentWithWhile.getCurrentNode().getFirstChild());

    // Test dead assignment elimination within do-while statement
    String codeDeadAssignmentWithDoWhile =
        "function foo() {" +
        "  var x = 1;" +
        "  do {" +
        "    x = 2;" +
        "  } while (true);" +
        "  return x;" +
        "}";
    Node externsDeadAssignmentWithDoWhile =
            Node.newBlock("function foo() {");
    externsDeadAssignmentWithDoWhile.addChild(Node.newText("}"));
    Node rootDeadAssignmentWithDoWhile = Node.parse(codeDeadAssignmentWithDoWhile);
    DeadAssignmentsElimination deadAssignmentsEliminationDeadAssignmentWithDoWhile =
        new DeadAssignmentsElimination(new NoopCompiler());
    deadAssignmentsEliminationDeadAssignmentWithDoWhile.process(externsDeadAssignmentWithDoWhile, rootDeadAssignmentWithDoWhile);
    NodeTraversal nodeTraversalDeadAssignmentWithDoWhile = new NodeTraversal(rootDeadAssignmentWithDoWhile);
    NodeUtil.traverse(nodeTraversalDeadAssignmentWithDoWhile);
    assertTrue(nodeTraversalDeadAssignmentWithDoWhile.hasVisited());
    assertNull(nodeTraversalDeadAssignmentWithDoWhile.getCurrentNode().getFirstChild());
  }

  /**
   * A no-op compiler for testing purposes.
   */
  private static class NoopCompiler implements AbstractCompiler {

    @Override
    public ControlFlowGraph<Node> getControlFlowGraph(
        Node root, Scope scope, CompilerOptions options) {
        return new ControlFlowGraph<Node>(
            new DiGraph<Node, Branch>(new DiGraphNode<Node, Branch>(root)),
            new NodeTraversal<Node>(root));
    }

    @Override
    public void reportCodeChange() {
      // Do nothing.
    }

    @Override
    public void reportError(Node t, String msg, int line, int lineOffset) {
      // Do nothing.
    }
  }
}