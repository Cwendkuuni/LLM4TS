package com.google.javascript.jscomp;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination elimination;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    compiler = new Compiler();
    elimination = new DeadAssignmentsElimination(compiler);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testDeadAssignmentsElimination_Simple() {
    String source = "function f() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  return x;" +
        "}";

    Node root = NodeTraversal.parse(source, null);
    elimination.process(nodeFactory.getExterns(), root);
    Node result = root;
    // The dead assignment x = 2 should be removed.
    assertThat(result.getFirstChild().getString()).isEqualTo("function f() {" +
        "  var x = 1;" +
        "  return x;" +
        "}");
  }

  @Test
  public void testDeadAssignmentsElimination_Complex() {
    String source = "function f() {" +
        "  var x = 1;" +
        "  if (a) {" +
        "    x = 2;" +
        "  } else {" +
        "    x = 3;" +
        "  }" +
        "  if (b) {" +
        "    x = 4;" +
        "  }" +
        "  return x;" +
        "}";

    Node root = NodeTraversal.parse(source, null);
    elimination.process(nodeFactory.getExterns(), root);
    Node result = root;
    // Dead assignment x = 2 should be removed when a is true.
    // Dead assignment x = 3 should be removed when a is false and b is true.
    // Dead assignment x = 4 should be removed when a is false and b is false.
    assertThat(result.getFirstChild().getString()).isEqualTo("function f() {" +
        "  var x = 1;" +
        "  if (a) {" +
        "  } else {" +
        "    if (b) {" +
        "    } else {" +
        "    }" +
        "    return x;" +
        "  }" +
        "}");
  }

  @Test
  public void testDeadAssignmentsElimination_NoDeadAssignments() {
    String source = "function f() {" +
        "  var x = 1;" +
        "  return x;" +
        "}";

    Node root = NodeTraversal.parse(source, null);
    elimination.process(nodeFactory.getExterns(), root);
    Node result = root;
    // There are no dead assignments, so nothing should be removed.
    assertThat(result.getFirstChild().getString()).isEqualTo("function f() {" +
        "  var x = 1;" +
        "  return x;" +
        "}");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeadAssignmentsElimination_NullRoot() {
    elimination.process(nodeFactory.getExterns(), null);
  }
}