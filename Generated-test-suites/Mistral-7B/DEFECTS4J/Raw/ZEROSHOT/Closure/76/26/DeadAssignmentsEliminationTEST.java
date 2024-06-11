package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphUtil;
import com.google.javascript.jscomp.GraphVisitor;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;

public class DeadAssignmentsEliminationTest {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(DeadAssignmentsEliminationTest.class);
  }

  @org.junit.Test
  public void testDeadAssignmentsElimination() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Do nothing
      }

      @Override
      public void reportError(JSErrorReporter errors, CompilerOptions options,
          Node node, String message, int lineNumber, int column) {
        fail("Unexpected error in DeadAssignmentsElimination: " + message);
      }

      @Override
      public void reportFatalError(JSErrorReporter errors, CompilerOptions options,
          Node node, String message, int lineNumber, int column) {
        fail("Unexpected fatal error in DeadAssignmentsElimination: " + message);
      }
    };

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    options.setPrintErrorDetails(false);

    String sourceCode = "function foo() {"
        + "  var x = 1, y = 1, z = 1;"
        + "  if (x = y) {"
        + "    y = 2;"
        + "  } else {"
        + "    x = 2;"
        + "  }"
        + "  z = x + y;"
        + "  return z;"
        + "}";

    Node root = NodeUtil.parseJs(sourceCode, null, null);
    Node externs = Node.newRootNode();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(externs, root);

    ControlFlowGraph<Node> controlFlowGraph =
        NodeTraversal.getOrCreateControlFlowGraph(compiler, root);
    DiGraph<Node, Branch> directedGraph = controlFlowGraph.getDirectedGraph();

    GraphVisitor<Node, Void> visitor = new GraphVisitor<Node, Void>() {
      @Override
      public void visitNode(Node node) {
        if (node != null && NodeUtil.isAssign(node)) {
          AbstractNode leftChild = node.getFirstChild();
          AbstractNode rightChild = leftChild.getNext();
          String leftChildName = leftChild.getString();
          String rightChildName = rightChild != null ? rightChild.getString() : "";
          System.out.println("Found dead assignment: " + node);
          System.out.println("  Left child: " + leftChild);
          System.out.println("  Right child: " + rightChild);
          System.out.println("  Left child name: " + leftChildName);
          System.out.println("  Right child name: " + rightChildName);
        }
      }
    };

    directedGraph.traverse(visitor, null);

    List<Node> expectedDeadAssignments = Arrays.asList(
        NodeUtil.findFirstChildByName(root, "y"),
        NodeUtil.findFirstChildByName(root, "x")
    );

    for (Node deadAssignment : expectedDeadAssignments) {
      assertEquals("Dead assignment not removed: " + deadAssignment, null,
          deadAssignment.getParent());
    }
  }
}

class NodeUtil {
  // Adapted from the NodeUtil class in the original DeadAssignmentsElimination
  // code.

  public static Node newRootNode() {
    return new Node(Token.ROOT, null);
  }

  public static Node parseJs(String js, Scope scope, Node parent) {
    return AbstractNode.parse(
        new JsParser(new JsParser.JsParserConfig(), new JsParser.JsParserOptions(),
            new AbstractCompiler() {
              @Override
              public Scope getScope() {
                return scope;
              }

              @Override
              public void reportCodeChange() {
                // Do nothing
              }

              @Override
              public void reportError(JSErrorReporter errors, CompilerOptions options,
                  Node node, String message, int lineNumber, int column) {
                fail("Unexpected error in NodeUtil: " + message);
              }

              @Override
              public void reportFatalError(JSErrorReporter errors, CompilerOptions options,
                  Node node, String message, int lineNumber, int column) {
                fail("Unexpected fatal error in NodeUtil: " + message);
              }
            }), js, parent);
  }

  public static Node findFirstChildByName(Node node, String name) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getString().equals(name)) {
        return child;
      }
    }
    return null;
  }

  public static Node getConditionExpression(Node node) {
    if (node.getType() == Token.IF || node.getType() == Token.WHILE) {
      return NodeUtil.getExpression(node.getFirstChild().getNext());
    }
    return null;
  }

  public static Node getExpression(Node node) {
    if (node.getType() == Token.EXPR_LIST) {
      return node.getLastChild();
    }
    return node;
  }

  public static boolean containsFunction(Node node) {
    if (node.getType() == Token.FUNCTION) {
      return true;
    }
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (containsFunction(child)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isAssignmentOp(Node node) {
    switch (node.getType()) {
      case Token.ASSIGN:
      case Token.ASSIGN_ADD:
      case Token.ASSIGN_SUB:
      case Token.ASSIGN_MUL:
      case Token.ASSIGN_DIV:
      case Token.ASSIGN_MOD:
      case Token.ASSIGN_OR:
      case Token.ASSIGN_AND:
      case Token.ASSIGN_XOR:
      case Token.ASSIGN_LEFT_SHIFT:
      case Token.ASSIGN_RIGHT_SHIFT:
        return true;
      default:
        return false;
    }
  }

  public static boolean isName(Node node) {
    switch (node.getType()) {
      case Token.NAME:
      case Token.VAR:
        return true;
      default:
        return false;
    }
  }

  public static boolean isAssign(Node node) {
    return isAssignmentOp(node) || NodeUtil.isName(node) && node.getParent().getType() == Token.ASSIGN;
  }

  public static boolean isLhs(Node node, Node parent) {
    if (node.getType() == Token.VAR || NodeUtil.isName(node)) {
      if (parent.getType() == Token.ASSIGN) {
        return true;
      }
    }
    return false;
  }

  public static Predicate<Node> matchRemovableAssigns() {
    return new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return (NodeUtil.isAssignmentOp(n) &&
                NodeUtil.isName(n.getFirstChild())) ||
            n.getType() == Token.INC || n.getType() == Token.DEC;
      }
    };
  }

  public static Predicate<Node> alwaysTrue() {
    return Predicates.<Node>alwaysTrue();
  }

  public static void traverse(GraphVisitor<Node, Void> visitor, Node node) {
    if (node != null && visitor != null) {
      visitor.visitNode(node);
      for (Node child = node.getFirstChild(); child != null;
          child = child.getNext()) {
        traverse(visitor, child);
      }
    }
  }
}