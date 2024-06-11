package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("a = 1; b = a; a = 2;");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertFalse(isNodePresent("a = 2;", root));
    assertTrue(isNodePresent("a = 1; b = a;", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("function foo() { a = 1; b = a; a = 2; }");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertFalse(isNodePresent("a = 2;", root));
    assertTrue(isNodePresent("function foo() { a = 1; b = a; }", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithNoAssignments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("1;");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertTrue(isNodePresent("1;", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithVar() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("var a = 1; a = 2;");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertFalse(isNodePresent("a = 2;", root));
    assertTrue(isNodePresent("var a = 1;", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithComplexCondition() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("if (a = 1 && b = 2) { }");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertFalse(isNodePresent("a = 1 && b = 2", root));
    assertTrue(isNodePresent("if (a = 1 && b = 2) { }", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithForLoop() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("for (var i = 0; i < 10; i++) { i = i + 1; }");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertTrue(isNodePresent("for (var i = 0; i < 10; i++) { i++; }", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithForInLoop() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("for (var i in array) { i = i + 1; }");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertFalse(isNodePresent("i = i + 1;", root));
    assertTrue(isNodePresent("for (var i in array) { }", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithReturnStatement() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("function foo() { return a = 1; }");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertFalse(isNodePresent("return a = 1;", root));
    assertTrue(isNodePresent("function foo() { return 1; }", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithSwitchStatement() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("switch (a) { case 1: a = 2; break; default: }");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertFalse(isNodePresent("a = 2;", root));
    assertTrue(isNodePresent("switch (a) { case 1: break; default: }", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithComplexConditionInSwitchStatement() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("switch (a = 1) { case 1: a = 3; break; default: }");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertFalse(isNodePresent("a = 1", root));
    assertFalse(isNodePresent("switch (a = 1) { case 1: a = 3; break; default: }", root));
    assertTrue(isNodePresent("switch (1) { case 1: a = 3; break; default: }", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithDeadAssignments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("a = 1; a = 1; a = 1; a = 1;");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertFalse(isNodePresent("a = 1; a = 1; a = 1; a = 1;", root));
    assertTrue(isNodePresent("a = 1;", root));
  }

  @Test
  public void testDeadAssignmentsEliminationWithNoDeadAssignments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope scope = new Scope();

    Node root = createNode("a = 1; b = 2; c = 3;");
    NodeTraversal nodeTraversal = new NodeTraversal(root, new DeadAssignmentsElimination(compiler), cfg,
        scope, compiler);

    nodeTraversal.traverse();

    assertTrue(isNodePresent("a = 1; b = 2; c = 3;", root));
  }

  private Node createNode(String code) {
    AbstractNode node = ParserTestUtil.parseJavaScript(code);
    return (Node) node;
  }

  private boolean isNodePresent(String expectedCode, Node node) {
    StringBuilder sb = new StringBuilder();
    NodeTraversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(Node n) {
        sb.append(Parser.sourceName).append(":").append(n.getLineNumber()).append(":").append(n.getColumnNumber())
            .append(": ").append(n.toString());
        if (expectedCode.equals(sb.toString())) {
          return;
        }
        sb.setLength(0);
      }
    }, node, null);
    return false;
  }
}