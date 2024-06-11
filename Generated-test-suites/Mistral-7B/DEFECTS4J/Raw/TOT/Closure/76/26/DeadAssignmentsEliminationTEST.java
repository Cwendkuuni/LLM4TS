package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String testCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  if (true) {\n" +
        "    var b = 2;\n" +
        "    a = b;\n" +
        "  } else {\n" +
        "    var c = 3;\n" +
        "    a = c;\n" +
        "  }\n" +
        "  return a;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);

    Node root = AbstractCompiler.parseAndCompile(testCode, compiler);
    pass.process(null, root);

    // Ensure that there are no more dead assignments left.
    for (Node child = root.getFirstChild(); child != null;
        child = child.getNext()) {
      if (child.getType() == Token.ASSIGN) {
        Node lhs = child.getFirstChild();
        if (lhs.getType() == Token.NAME) {
          String name = lhs.getString();
          Scope scope = NodeTraversal.getScopeAt(child);
          Preconditions.checkState(scope.isDeclared(name, false),
              "Variable %s not declared in the test code.", name);
          Var var = scope.getVar(name);
          ControlFlowGraph<Node> cfg = NodeTraversal.getControlFlowGraph(root);
          FlowState<DataFlowAnalysis.LiveVariableLattice> state =
              cfg.getAnnotation(cfg.getEntryNode());
          Preconditions.checkState(state.getOut().isLive(var),
              "Variable %s should be live at the end of the test code.", name);
        }
      }
    }
  }

  @Test
  public void testDeadAssignmentsEliminationWithEmptyStatement() {
    String testCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  if (true) {\n" +
        "  } else {\n" +
        "    var c = 3;\n" +
        "    a = c;\n" +
        "  }\n" +
        "  return a;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);

    Node root = AbstractCompiler.parseAndCompile(testCode, compiler);
    pass.process(null, root);

    // Ensure that there are no more dead assignments left.
    for (Node child = root.getFirstChild(); child != null;
        child = child.getNext()) {
      if (child.getType() == Token.ASSIGN) {
        Node lhs = child.getFirstChild();
        if (lhs.getType() == Token.NAME) {
          String name = lhs.getString();
          Scope scope = NodeTraversal.getScopeAt(child);
          Preconditions.checkState(scope.isDeclared(name, false),
              "Variable %s not declared in the test code.", name);
          Var var = scope.getVar(name);
          ControlFlowGraph<Node> cfg = NodeTraversal.getControlFlowGraph(root);
          FlowState<DataFlowAnalysis.LiveVariableLattice> state =
              cfg.getAnnotation(cfg.getEntryNode());
          Preconditions.checkState(state.getOut().isLive(var),
              "Variable %s should be live at the end of the test code.", name);
        }
      }
    }
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentsEliminationWithUndeclaredVariable() {
    String testCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  if (true) {\n" +
        "    var b = 2;\n" +
        "    a = b;\n" +
        "  } else {\n" +
        "    var c = 3;\n" +
        "    x = c; // x is not declared in the test code.\n" +
        "  }\n" +
        "  return a;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);

    Node root = AbstractCompiler.parseAndCompile(testCode, compiler);
    pass.process(null, root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithConditionInsideLoop() {
    String testCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (true) {\n" +
        "      var b = 2;\n" +
        "      a = b;\n" +
        "    } else {\n" +
        "      var c = 3;\n" +
        "      a = c;\n" +
        "    }\n" +
        "  }\n" +
        "  return a;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);

    Node root = AbstractCompiler.parseAndCompile(testCode, compiler);
    pass.process(null, root);

    // Ensure that there are no more dead assignments left.
    for (Node child = root.getFirstChild(); child != null;
        child = child.getNext()) {
      if (child.getType() == Token.ASSIGN) {
        Node lhs = child.getFirstChild();
        if (lhs.getType() == Token.NAME) {
          String name = lhs.getString();
          Scope scope = NodeTraversal.getScopeAt(child);
          Preconditions.checkState(scope.isDeclared(name, false),
              "Variable %s not declared in the test code.", name);
          Var var = scope.getVar(name);
          ControlFlowGraph<Node> cfg = NodeTraversal.getControlFlowGraph(root);
          FlowState<DataFlowAnalysis.LiveVariableLattice> state =
              cfg.getAnnotation(cfg.getEntryNode());
          Preconditions.checkState(state.getOut().isLive(var),
              "Variable %s should be live at the end of the test code.", name);
        }
      }
    }
  }

  @Test
  public void testDeadAssignmentsEliminationWithNestedConditions() {
    String testCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  if (true) {\n" +
        "    if (true) {\n" +
        "      var b = 2;\n" +
        "      a = b;\n" +
        "    } else {\n" +
        "      var c = 3;\n" +
        "      a = c;\n" +
        "    }\n" +
        "  } else {\n" +
        "    if (true) {\n" +
        "      var d = 4;\n" +
        "      a = d;\n" +
        "    } else {\n" +
        "      var e = 5;\n" +
        "      a = e;\n" +
        "    }\n" +
        "  }\n" +
        "  return a;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);

    Node root = AbstractCompiler.parseAndCompile(testCode, compiler);
    pass.process(null, root);

    // Ensure that there are no more dead assignments left.
    for (Node child = root.getFirstChild(); child != null;
        child = child.getNext()) {
      if (child.getType() == Token.ASSIGN) {
        Node lhs = child.getFirstChild();
        if (lhs.getType() == Token.NAME) {
          String name = lhs.getString();
          Scope scope = NodeTraversal.getScopeAt(child);
          Preconditions.checkState(scope.isDeclared(name, false),
              "Variable %s not declared in the test code.", name);
          Var var = scope.getVar(name);
          ControlFlowGraph<Node> cfg = NodeTraversal.getControlFlowGraph(root);
          FlowState<DataFlowAnalysis.LiveVariableLattice> state =
              cfg.getAnnotation(cfg.getEntryNode());
          Preconditions.checkState(state.getOut().isLive(var),
              "Variable %s should be live at the end of the test code.", name);
        }
      }
    }
  }

  @Test
  public void testDeadAssignmentsEliminationWithLoopCondition() {
    String testCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (true) {\n" +
        "      var b = 2;\n" +
        "      a = b; // This assignment is dead since i is never changed.\n" +
        "    } else {\n" +
        "      var c = 3;\n" +
        "      a = c;\n" +
        "    }\n" +
        "  }\n" +
        "  return a;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);

    Node root = AbstractCompiler.parseAndCompile(testCode, compiler);
    pass.process(null, root);

    // Ensure that there are no more dead assignments left.
    for (Node child = root.getFirstChild(); child != null;
        child = child.getNext()) {
      if (child.getType() == Token.ASSIGN) {
        Node lhs = child.getFirstChild();
        if (lhs.getType() == Token.NAME) {
          String name = lhs.getString();
          Scope scope = NodeTraversal.getScopeAt(child);
          Preconditions.checkState(scope.isDeclared(name, false),
              "Variable %s not declared in the test code.", name);
          Var var = scope.getVar(name);
          ControlFlowGraph<Node> cfg = NodeTraversal.getControlFlowGraph(root);
          FlowState<DataFlowAnalysis.LiveVariableLattice> state =
              cfg.getAnnotation(cfg.getEntryNode());
          Preconditions.checkState(state.getOut().isLive(var),
              "Variable %s should be live at the end of the test code.", name);
        }
      }
    }
  }
}