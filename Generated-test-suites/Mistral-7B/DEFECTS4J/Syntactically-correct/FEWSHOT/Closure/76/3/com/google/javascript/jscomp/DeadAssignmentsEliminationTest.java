package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeUtil;

public class DeadAssignmentsEliminationTest {
  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass deadAssignmentsPass = new DeadAssignmentsElimination(compiler);

    String code = "function foo() {"
        + "var a = 1, b = 2, c = 3;"
        + "if (false) {"
        + "  a = b;"
        + "}"
        + "var d = a + b;"
        + "}";

    Node root = NodeUtil.parseJsonString(code);
    NodeTraversal traversal = new NodeTraversal(root, null);
    deadAssignmentsPass.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.VAR) {
          String name = n.getString();
          Var var = ((Scope) parent.getScope()).getVar(name);
          if (var != null) {
            assertEquals(0, var.getUsageCount());
          }
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass deadAssignmentsPass = new DeadAssignmentsElimination(compiler);

    String code = "function foo(x) {"
        + "var a = x;"
        + "function bar() {"
        + "  a = 1;"
        + "}"
        + "bar();"
        + "}"
        + "foo(1);";

    Node root = NodeUtil.parseJsonString(code);
    NodeTraversal traversal = new NodeTraversal(root, null);
    deadAssignmentsPass.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.VAR) {
          String name = n.getString();
          Var var = ((Scope) parent.getScope()).getVar(name);
          if (var != null) {
            assertEquals(1, var.getUsageCount());
          }
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithCycle() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass deadAssignmentsPass = new DeadAssignmentsElimination(compiler);

    String code = "function foo() {"
        + "var a = 1, b = 2;"
        + "a = b;"
        + "b = a;"
        + "}";

    Node root = NodeUtil.parseJsonString(code);
    NodeTraversal traversal = new NodeTraversal(root, null);
    deadAssignmentsPass.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.VAR) {
          String name = n.getString();
          Var var = ((Scope) parent.getScope()).getVar(name);
          if (var != null) {
            assertNull(var);
          }
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithComplexControlFlow() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass deadAssignmentsPass = new DeadAssignmentsElimination(compiler);

    String code = "function foo() {"
        + "var a = 1, b = 2;"
        + "if (true) {"
        + "  if (false) {"
        + "    a = b;"
        + "  } else {"
        + "    b = a;"
        + "  }"
        + "} else {"
        + "  a = 3;"
        + "}"
        + "}";

    Node root = NodeUtil.parseJsonString(code);
    NodeTraversal traversal = new NodeTraversal(root, null);
    deadAssignmentsPass.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.VAR) {
          String name = n.getString();
          Var var = ((Scope)parent.getScope()).getVar(name);
          if (var != null) {
            if (name.equals("a")) {
              assertEquals(1, var.getUsageCount());
            } else if (name.equals("b")) {
              assertEquals(1, var.getUsageCount());
            }
          }
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithComplexControlFlowAndFunctionCall() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass deadAssignmentsPass = new DeadAssignmentsElimination(compiler);

    String code = "function foo() {"
        + "var a = 1, b = 2;"
        + "function bar() {"
        + "  var c = 3;"
        + "  a = b;"
        + "  b = c;"
        + "}"
        + "if (true) {"
        + "  bar();"
        + "} else {"
        + "  a = 3;"
        + "}"
        + "}";

    Node root = NodeUtil.parseJsonString(code);
    NodeTraversal traversal = new NodeTraversal(root, null);
    deadAssignmentsPass.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.VAR) {
          String name = n.getString();
          Var var = ((Scope)parent.getScope()).getVar(name);
          if (var != null) {
            if (name.equals("a")) {
              assertEquals(1, var.getUsageCount());
            } else if (name.equals("b")) {
              assertEquals(1, var.getUsageCount());
            } else if (name.equals("c")) {
              assertEquals(1, var.getUsageCount());
            }
          }
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithComplexControlFlowAndFunctionDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass deadAssignmentsPass = new DeadAssignmentsElimination(compiler);

    String code = "function foo() {"
        + "var a = 1, b = 2;"
        + "function bar(x) {"
        + "  a = b;"
        + "  b = x;"
        + "}"
        + "if (true) {"
        + "  bar(1);"
        + "} else {"
        + "  a = 3;"
        + "}"
        + "}";

    Node root = NodeUtil.parseJsonString(code);
    NodeTraversal traversal = new NodeTraversal(root, null);
    deadAssignmentsPass.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.VAR) {
          String name = n.getString();
          Var var = ((Scope)parent.getScope()).getVar(name);
          if (var != null) {
            if (name.equals("a")) {
              assertEquals(1, var.getUsageCount());
            } else if (name.equals("b")) {
              assertEquals(2, var.getUsageCount());
            }
          }
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithComplexControlFlowAndFunctionCallWithReturn() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass deadAssignmentsPass = new DeadAssignmentsElimination(compiler);

    String code = "function foo() {"
        + "var a = 1, b = 2;"
        + "function bar() {"
        + "  a = b;"
        + "  b = 3;"
        + "  return b;"
        + "}"
        + "if (true) {"
        + "  b = bar();"
        + "} else {"
        + "  a = 3;"
        + "}"
        + "}";

    Node root = NodeUtil.parseJsonString(code);
    NodeTraversal traversal = new NodeTraversal(root, null);
    deadAssignmentsPass.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.VAR) {
          String name = n.getString();
          Var var = ((Scope)parent.getScope()).getVar(name);
          if (var != null) {
            if (name.equals("a")) {
              assertEquals(1, var.getUsageCount());
            } else if (name.equals("b")) {
              assertEquals(2, var.getUsageCount());
            }
          }
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithComplexControlFlowAndFunctionCallWithReturnAndAssignment() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass deadAssignmentsPass = new DeadAssignmentsElimination(compiler);

    String code = "function foo() {"
        + "var a = 1, b = 2;"
        + "function bar() {"
        + "  a = b;"
        + "  b = 3;"
        + "  return b;"
        + "}"
        + "if (true) {"
        + "  b = bar();"
        + "  a = b;"
        + "} else {"
        + "  a = 3;"
        + "}"
        + "}";

    Node root = NodeUtil.parseJsonString(code);
    NodeTraversal traversal = new NodeTraversal(root, null);
    deadAssignmentsPass.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.VAR) {
          String name = n.getString();
          Var var = ((Scope)parent.getScope()).getVar(name);
          if (var != null) {
            if (name.equals("a")) {
              assertEquals(1, var.getUsageCount());
            } else if (name.equals("b")) {
              assertEquals(2, var.getUsageCount());
            }
          }
        }
      }
    });
  }
}