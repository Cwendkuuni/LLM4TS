package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String sourceCode = "function foo() {" +
            "var x = 1, y = 2;" +
            "x = 3; // dead assignment to x" +
            "y = x; // dead assignment to y, since y is not used after this" +
            "}";

    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = AbstractNodePrinter.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, null, null, 0);
    NodeUtil.traverseAndApply(traversal, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("x")) {
          assertTrue(n.getParent() == null);
        }
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("y")) {
          assertFalse(n.getParent() == null);
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunction() {
    String sourceCode = "function foo() {" +
            "function bar() {" +
            "var x = 1, y = 2;" +
            "x = 3; // dead assignment to x" +
            "y = x; // dead assignment to y, since y is not used after this" +
            "}" +
            "}" +
            "foo();";

    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = AbstractNodePrinter.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, null, null, 0);
    NodeUtil.traverseAndApply(traversal, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("x")) {
          assertTrue(n.getParent() == null);
        }
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("y")) {
          assertFalse(n.getParent() == null);
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithLocalVariableReadBeforeWrite() {
    String sourceCode = "function foo() {" +
            "var x = 1, y = 2;" +
            "x = 3; // dead assignment to x" +
            "y = x; // dead assignment to y, since y is read before it is written" +
            "print(x);" +
            "}" +
            "foo();";

    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = AbstractNodePrinter.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, null, null, 0);
    NodeUtil.traverseAndApply(traversal, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("x")) {
          assertTrue(n.getParent() == null);
        }
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("y")) {
          assertFalse(n.getParent() == null);
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithVariableNotDead() {
    String sourceCode = "function foo() {" +
            "var x = 1, y = 2;" +
            "x = 3; // not dead assignment to x" +
            "y = x; // dead assignment to y, since y is not used after this" +
            "print(x);" +
            "}" +
            "foo();";

    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = AbstractNodePrinter.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, null, null, 0);
    NodeUtil.traverseAndApply(traversal, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("x")) {
          assertFalse(n.getParent() == null);
        }
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("y")) {
          assertFalse(n.getParent() == null);
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithVariableStillLive() {
    String sourceCode = "function foo() {" +
            "var x = 1, y = 2;" +
            "x = 3; // dead assignment to x" +
            "y = x; // dead assignment to y, since y is still live after this" +
            "print(y);" +
            "}" +
            "foo();";

    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = AbstractNodePrinter.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, null, null, 0);
    NodeUtil.traverseAndApply(traversal, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("x")) {
          assertTrue(n.getParent() == null);
        }
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("y")) {
          assertFalse(n.getParent() == null);
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithVariableReadAfterWrite() {
    String sourceCode = "function foo() {" +
            "var x = 1, y = 2;" +
            "x = 3; // dead assignment to x" +
            "y = x; // y is read after x is assigned, so the assignment to y is not dead" +
            "print(y);" +
            "}" +
            "foo();";

    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = AbstractNodePrinter.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, null, null, 0);
    NodeUtil.traverseAndApply(traversal, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("x")) {
          assertTrue(n.getParent() == null);
        }
        if (n != null && n.getType() == Token.ASSIGN &&
            n.getFirstChild() != null &&
            n.getFirstChild().getType() == Token.NAME &&
            n.getString().equals("y")) {
          assertFalse(n.getParent() == null);
        }
      }
    });
  }
}