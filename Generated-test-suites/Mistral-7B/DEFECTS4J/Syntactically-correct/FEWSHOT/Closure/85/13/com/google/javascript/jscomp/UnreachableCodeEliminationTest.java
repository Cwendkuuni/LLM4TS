package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.rhino.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TreePrinter;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tests for {@link UnreachableCodeElimination} class.
 */
@RunWith(JUnit4.class)
public class UnreachableCodeEliminationTest {

  private static final Logger logger =
    Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @Test
  public void testUnreachableCodeElimination() {
    AbstractCompiler compiler = new CompilerOptions();
    UnreachableCodeElimination elimination =
        new UnreachableCodeElimination(compiler, true);

    Node rootNode = TreePrinter.printToNode(
        "function foo() {"
            + "if (true) {"
            + "  return;"
            + "  alert('unreachable');"
            + "}"
            + "console.log('reachable');"
            + "}");

    elimination.process(null, rootNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(elimination);

    Node reachableStatement = nodeTraversal.getRoot().getFirstChild();
    assertEquals("log", reachableStatement.getIdentifier().getName());
    assertEquals("reachable", reachableStatement.toString());

    nodeTraversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(Node n) {
        if (n.getIdentifier() != null && "alert".equals(n.getIdentifier().getName())) {
          assertNull(n);
        }
      }
    });
  }

  @Test
  public void testUnreachableCodeEliminationWithNoOpStatements() {
    AbstractCompiler compiler = new CompilerOptions();
    UnreachableCodeElimination elimination =
        new UnreachableCodeElimination(compiler, true);

    Node rootNode = TreePrinter.printToNode(
        "function foo() {"
            + "var x = a.b.MyClass.prototype.propertyName;"
            + "console.log('reachable');"
            + "return;"
            + "console.log('unreachable');"
            + "}");

    elimination.process(null, rootNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(elimination);

    Node reachableStatement = nodeTraversal.getRoot().getFirstChild();
    assertEquals("log", reachableStatement.getIdentifier().getName());
    assertEquals("reachable", reachableStatement.toString());

    nodeTraversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(Node n) {
        if (n.getIdentifier() != null && "unreachable".equals(n.getIdentifier().getName())) {
          assertNull(n);
        }
      }
    });
  }

  @Test
  public void testUnreachableCodeEliminationWithConditional() {
    AbstractCompiler compiler = new CompilerOptions();
    UnreachableCodeElimination elimination =
        new UnreachableCodeElimination(compiler, true);

    Node rootNode = TreePrinter.printToNode(
        "function foo() {"
            + "if (true) {"
            + "  return;"
            + "  console.log('unreachable1');"
            + "} else {"
            + "  console.log('reachable');"
            + "}"
            + "console.log('unreachable2');"
            + "}");

    elimination.process(null, rootNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(elimination);

    Node reachableStatement = nodeTraversal.getRoot().getFirstChild();
    assertEquals("log", reachableStatement.getIdentifier().getName());
    assertEquals("reachable", reachableStatement.toString());

    nodeTraversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(Node n) {
        if (n.getIdentifier() != null &&
            ("unreachable1".equals(n.getIdentifier().getName()) ||
             "unreachable2".equals(n.getIdentifier().getName()))) {
          assertNull(n);
        }
      }
    });
  }

  @Test
  public void testUnreachableCodeEliminationWithUnconditionalBranch() {
    AbstractCompiler compiler = new CompilerOptions();
    UnreachableCodeElimination elimination =
        new UnreachableCodeElimination(compiler, true);

    Node rootNode = TreePrinter.printToNode(
        "function foo() {"
            + "var x = 1;"
            + "if (x > 1) {"
            + "  return;"
            + "}"
            + "console.log('reachable');"
            + "break;"
            + "console.log('unreachable');"
            + "}");

    elimination.process(null, rootNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(elimination);

    Node reachableStatement = nodeTraversal.getRoot().getFirstChild();
    assertEquals("log", reachableStatement.getIdentifier().getName());
    assertEquals("reachable", reachableStatement.toString());

    nodeTraversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(Node n) {
        if (n.getIdentifier() != null && "unreachable".equals(n.getIdentifier().getName())) {
          assertNull(n);
        }
      }
    });
  }

  @Test
  public void testUnreachableCodeEliminationWithNestedBranches() {
    AbstractCompiler compiler = new CompilerOptions();
    UnreachableCodeElimination elimination =
        new UnreachableCodeElimination(compiler, true);

    Node rootNode = TreePrinter.printToNode(
        "function foo() {"
            + "var x = 1;"
            + "if (x > 1) {"
            + "  console.log('if1');"
            + "  if (x > 2) {"
            + "    return;"
            + "  }"
            + "  console.log('if2');"
            + "}"
            + "console.log('else')"
            + "return;"
            + "console.log('unreachable');"
            + "}");

    elimination.process(null, rootNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
    nodeTraversal.traverse(elimination);

    Node reachableStatement = nodeTraversal.getRoot().getFirstChild();
    assertEquals("log", reachableStatement.getIdentifier().getName());
    assertEquals("else", reachableStatement.toString());

    nodeTraversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(Node n) {
        if (n.getIdentifier() != null && "unreachable".equals(n.getIdentifier().getName())) {
          assertNull(n);
        }
      }
    });
  }
}