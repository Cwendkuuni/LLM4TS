package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilerPass;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import java.util.List;

/**
 * Test class for {@link DeadAssignmentsElimination}.
 */
public class DeadAssignmentsEliminationTest {

  private static class TestCompiler extends AbstractCompiler {
    @Override
    public CompilerPass getPass(String id) {
      switch (id) {
        case "DeadAssignmentsElimination":
          return new DeadAssignmentsElimination(this);
        default:
          return null;
      }
    }
  }

  private TestCompiler compiler = new TestCompiler();

  @Test
  public void testRemoveDeadAssignments() {
    String code =
        "function foo() {" +
            "var x = 1;" +
            "x = 2;" + // Dead assignment.
            "x = 3;" + // Dead assignment.
            "return x;" +
        "}";

    Node root = compiler.parse(code, null, null);

    AbstractTraversal traversal = new NodeTraversal(root, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    List<Node> nodes = ImmutableList.copyOf(Iterables.filter(
        root.getDescendantsAndSelf(),
        new Predicate<Node>() {
          @Override
          public boolean apply(Node input) {
            return input != null && NodeUtil.isName(input) && input.getString().equals("x");
          }
        }));

    assertThat(nodes).hasSize(1);
    Node xNode = nodes.get(0);
    Node assignment = Iterables.get(xNode.getDescendantsAndSelf(), 0, null);
    assertThat(assignment).isNull();
  }

  @Test
  public void testRemoveDeadAssignmentInsideIfStatement() {
    String code =
        "function foo() {" +
            "var x = 1;" +
            "if (false) {" +
                "x = 2;" + // Dead assignment.
            "}" +
            "return x;" +
        "}";

    Node root = compiler.parse(code, null, null);

    AbstractTraversal traversal = new NodeTraversal(root, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    List<Node> nodes = Iterables.getInverse(
        Iterables.filter(root.getDescendantsAndSelf(),
            Predicates.<Node>alwaysTrue()),
        new Function<Node, Node>() {
          @Override
          public Node apply(Node input) {
            return input.getParent();
          }
        });

    Node ifStatement = Iterables.get(nodes, 1, null);
    Node xNode = Iterables.get(ifStatement.getDescendantsAndSelf(), 0, null);
    Node assignment = Iterables.get(xNode.getDescendantsAndSelf(), 1, null);

    assertThat(assignment).isNull();
  }

  @Test
  public void testRemoveDeadAssignmentInsideIfStatementWithSideEffect() {
    String code =
        "function foo() {" +
            "var x = 1;" +
            "if (false) {" +
                "++x;" + // Dead assignment.
            "}" +
            "return x;" +
        "}";

    Node root = compiler.parse(code, null, null);

    AbstractTraversal traversal = new NodeTraversal(root, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    List<Node> nodes = Iterables.getInverse(
        Iterables.filter(root.getDescendantsAndSelf(),
            Predicates.<Node>alwaysTrue()),
        new Function<Node, Node>() {
          @Override
          public Node apply(Node input) {
            return input.getParent();
          }
        });

    Node ifStatement = Iterables.get(nodes, 1, null);
    Node xNode = Iterables.get(ifStatement.getDescendantsAndSelf(), 0, null);
    Node increment = Iterables.get(xNode.getDescendantsAndSelf(), 1, null);

    assertEquals(Token.VOID, increment.getType());
  }

  @Test
  public void testRemoveDeadAssignmentInsideWhileStatement() {
    String code =
        "function foo() {" +
            "var x = 1;" +
            "while (true) {" +
                "x = 2;" + // Dead assignment.
            "}" +
            "return x;" +
        "}";

    Node root = compiler.parse(code, null, null);

    AbstractTraversal traversal = new NodeTraversal(root, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    List<Node> nodes = Iterables.getInverse(
        Iterables.filter(root.getDescendantsAndSelf(),
            Predicates.<Node>alwaysTrue()),
        new Function<Node, Node>() {
          @Override
          public Node apply(Node input) {
            return input.getParent();
          }
        });

    Node whileStatement = Iterables.get(nodes, 1, null);
    Node xNode = Iterables.get(whileStatement.getDescendantsAndSelf(), 0, null);
    Node assignment = Iterables.get(xNode.getDescendantsAndSelf(), 1, null);

    assertThat(assignment).isNull();
  }

  @Test
  public void testRemoveDeadAssignmentInsideForStatement() {
    String code =
        "function foo() {" +
            "var x = 1;" +
            "for (var i = 0; i < 1; ++i) {" +
                "x = 2;" + // Dead assignment.
            "}" +
            "return x;" +
        "}";

    Node root = compiler.parse(code, null, null);

    AbstractTraversal traversal = new NodeTraversal(root, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    List<Node> nodes = Iterables.getInverse(
        Iterables.filter(root.getDescendantsAndSelf(),
            Predicates.<Node>alwaysTrue()),
        new Function<Node, Node>() {
          @Override
          public Node apply(Node input) {
            return input.getParent();
          }
        });

    Node forStatement = Iterables.get(nodes, 1, null);
    Node xNode = Iterables.get(forStatement.getDescendantsAndSelf(), 0, null);
    Node assignment = Iterables.get(xNode.getDescendantsAndSelf(), 1, null);

    assertThat(assignment).isNull();
  }

  @Test
  public void testRemoveDeadAssignmentInsideSwitchStatement() {
    String code =
        "function foo() {" +
            "var x = 1;" +
            "switch (1) {" +
                "case 1:" +
                    "x = 2;" + // Dead assignment.
                "}" +
            "return x;" +
        "}";

    Node root = compiler.parse(code, null, null);

    AbstractTraversal traversal = new NodeTraversal(root, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    List<Node> nodes = Iterables.getInverse(
        Iterables.filter(root.getDescendantsAndSelf(),
            Predicates.<Node>alwaysTrue()),
        new Function<Node, Node>() {
          @Override
          public Node apply(Node input) {
            return input.getParent();
          }
        });

    Node switchStatement = Iterables.get(nodes, 1, null);
    Node xNode = Iterables.get(switchStatement.getDescendantsAndSelf(), 0, null);
    Node assignment = Iterables.get(xNode.getDescendantsAndSelf(), 1, null);

    assertThat(assignment).isNull();
  }

  @Test
  public void testRemoveDeadAssignmentInsideReturnStatement() {
    String code =
        "function foo() {" +
            "var x = 1;" +
            "return x = 2;" + // Dead assignment.
        "}";

    Node root = compiler.parse(code, null, null);

    AbstractTraversal traversal = new NodeTraversal(root, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    List<Node> nodes = Iterables.getInverse(
        Iterables.filter(root.getDescendantsAndSelf(),
            Predicates.<Node>alwaysTrue()),
        new Function<Node, Node>() {
          @Override
          public Node apply(Node input) {
            return input.getParent();
          }
        });

    Node returnStatement = Iterables.get(nodes, 1, null);
    Node assignment = Iterables.get(returnStatement.getDescendantsAndSelf(), 0, null);

    assertNull(assignment);
  }

  @Test
  public void testRemoveDeadAssignmentInsideExpressionStatement() {
    String code =
        "function foo() {" +
            "x = 1;" + // Dead assignment, x is not declared.
            "return x;" +
        "}";

    Node root = compiler.parse(code, null, null);

    AbstractTraversal traversal = new NodeTraversal(root, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    List<Node> nodes = Iterables.getInverse(
        Iterables.filter(root.getDescendantsAndSelf(),
            Predicates.<Node>alwaysTrue()),
        new Function<Node, Node>() {
          @Override
          public Node apply(Node input) {
            return input.getParent();
          }
        });

    Node assignment = Iterables.get(nodes, 0, null);

    assertNull(assignment);
  }

  @Test
  public void testRemoveDeadAssignmentInsideVariableDeclarationStatement() {
    String code =
        "function foo() {" +
            "var x = 1, y = 2;" + // x is dead, y is not.
            "return x;" +
        "}";

    Node root = compiler.parse(code, null, null);

    AbstractTraversal traversal = new NodeTraversal(root, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    List<Node> nodes = Iterables.getInverse(
        Iterables.filter(root.getDescendantsAndSelf(),
            Predicates.<Node>alwaysTrue()),
        new Function<Node, Node>() {
          @Override
          public Node apply(Node input) {
            return input.getParent();
          }
        });

    Node variableDeclarationStatement = Iterables.get(nodes, 1, null);
    Node xAssignment = Iterables.get(variableDeclarationStatement.getDescendantsAndSelf(), 0, null);

    assertNull(xAssignment);
  }
}