package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isAssignmentOp;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.jscomp.NodeUtil.isFunction;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isAssign;
import static com.google.javascript.jscomp.NodeUtil.containsFunction;
import static com.google.javascript.jscomp.NodeUtil.getConditionExpression;
import static com.google.javascript.jscomp.NodeUtil.getFirstChild;
import static com.google.javascript.jscomp.NodeUtil.getLastChild;
import static com.google.javascript.jscomp.NodeUtil.getNext;
import static com.google.javascript.jscomp.NodeUtil.getOpFromAssignmentOp;
import static com.google.javascript.jscomp.NodeUtil.has;
import static com.google.javascript.jscomp.NodeUtil.isExpressionNode;
import static com.google.javascript.jscomp.NodeUtil.isLhs;
import static com.google.javascript.jscomp.NodeUtil.isVar;
import static com.google.javascript.jscomp.NodeUtil.isVariableLiveness;
import static com.google.javascript.jscomp.NodeUtil.isVariableReadBeforeKill;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractVisitor;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DiGraph;
import com.google.javascript.jscomp.FlowState;
import com.google.javascript.jscomp.GraphVisitor;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link DeadAssignmentsElimination}.
 */
@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    CompilerOptions options = new CompilerOptions();
    options.setPrintWarningMessages(true);
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIRKS);

    String sourceCode = "function deadAssignmentsEliminationTest() {" +
        "  var x = 1;" +
        "  var y = 1;" +
        "  var z = 1;" +
        "  x = y;" +
        "  y = z;" +
        "  z = 1;" +
        "  if (x == y) {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.compile(options, sourceCode, "deadAssignmentsEliminationTest");

    Node root = compiler.getEntryNode();
    ControlFlowGraph<Node> cfg = NodeTraversal.getOrCreateControlFlowGraph(root);
    LiveVariablesAnalysis liveness = new LiveVariablesAnalysis(cfg, null, compiler);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.process(cfg.getExterns(), root);

    // Check that the control flow graph has not been changed by the elimination.
    List<DiGraphNode<Node, Branch>> nodes = cfg.getDirectedGraphNodes();
    DiGraphNode<Node, Branch> firstNode = Iterables.getFirst(nodes, null);
    assertNotNull(firstNode);
    Node firstNodeValue = firstNode.getValue();
    assertEquals("deadAssignmentsEliminationTest", firstNodeValue.getString());

    DiGraphNode<Node, Branch> xNode = findNodeWithName(cfg, "x");
    assertNotNull(xNode);
    Node x = xNode.getValue();
    assertEquals(Token.VAR, x.getType());
    Node xAssignment = findAssignmentToNode(cfg, x);
    assertNotNull(xAssignment);
    assertEquals(3, xAssignment.getFirstChild().getNumber().doubleValueExact());

    DiGraphNode<Node, Branch> yNode = findNodeWithName(cfg, "y");
    assertNotNull(yNode);
    Node y = yNode.getValue();
    assertEquals(Token.VAR, y.getType());
    Node yAssignment = findAssignmentToNode(cfg, y);
    assertNull(yAssignment);

    DiGraphNode<Node, Branch> zNode = findNodeWithName(cfg, "z");
    assertNotNull(zNode);
    Node z = zNode.getValue();
    assertEquals(Token.VAR, z.getType());
    Node zAssignment = findAssignmentToNode(cfg, z);
    assertEquals(1, zAssignment.getFirstChild().getNumber().doubleValueExact());
  }

  private Node findNodeWithName(ControlFlowGraph<Node> cfg, String name) {
    for (DiGraphNode<Node, Branch> node : cfg.getDirectedGraphNodes()) {
      Node n = node.getValue();
      if (isName(n) && n.getString().equals(name)) {
        return node;
      }
    }
    return null;
  }

  private Node findAssignmentToNode(ControlFlowGraph<Node> cfg, Node node) {
    for (Node child = node.getFirstChild(); child != null;
         child = getNext(child)) {
      if (isAssignmentOp(child) &&
          NodeUtil.isName(child.getFirstChild()) &&
          child.getFirstChild().getString().equals(node.getString())) {
        return child;
      }
    }
    return null;
  }
}

class NodeUtil extends AbstractVisitor<Node, Void> {

  public static boolean isAssignmentOp(Node n) {
    return n.getType() == Token.ASSIGN ||
        n.getType() == Token.ASSIGN_OP;
  }

  public static boolean isForIn(Node n) {
    return n.getType() == Token.FOR &&
        NodeUtil.isName(n.getFirstChild()) &&
        n.getFirstChild().getString().equals("in");
  }

  public static boolean isFunction(Node n) {
    return n.getType() == Token.FUNCTION;
  }

  public static boolean isName(Node n) {
    return n.getType() == Token.NAME;
  }

  public static boolean isAssign(Node n) {
    return isAssignmentOp(n) || n.getType() == Token.INC || n.getType() == Token.DEC;
  }

  public static boolean isLhs(Node lhs, Node op) {
    return lhs.getType() == Token.NAME &&
        lhs.getString().equals(op.getFirstChild().getString());
  }

  public static boolean isVar(Node n) {
    return n.getType() == Token.VAR;
  }

  public static boolean containsFunction(Node parent, Predicate<Node> predicate) {
    return NodeUtil.has(parent, predicate, Predicates.<Node>alwaysTrue());
  }

  public static Node getConditionExpression(Node n) {
    while (n != null && n.getType() != Token.IF && n.getType() != Token.WHILE &&
           n.getType() != Token.DO && n.getType() != Token.SWITCH &&
           n.getType() != Token.RETURN) {
      n = getNext(n);
    }
    return n;
  }

  public static Node getFirstChild(Node node) {
    return node.getFirstChild();
  }

  public static Node getLastChild(Node node) {
    Node last = node;
    while (last.getNext() != null) {
      last = last.getNext();
    }
    return last;
  }

  public static Node getNext(Node node) {
    return node.getNext();
  }

  public static Node getOpFromAssignmentOp(Node n) {
    return n.getPrevious();
  }

  public static Node has(Node node, Predicate<Node> predicate, Predicate<Node> nextPredicate) {
    for (Node child = node.getFirstChild(); child != null;
         child = getNext(child)) {
      if (predicate.apply(child)) {
        return child;
      } else if (nextPredicate.apply(child)) {
        continue;
      }
    }
    return null;
  }

  public static List<Node> getChildren(Node node) {
    List<Node> children = Lists.newArrayList();
    for (Node child = node.getFirstChild(); child != null;
         child = getNext(child)) {
      children.add(child);
    }
    return children;
  }

  public static int getNumChildren(Node node) {
    int numChildren = 0;
    for (Node child = node.getFirstChild(); child != null;
         child = getNext(child)) {
      numChildren++;
    }
    return numChildren;
  }

  public static boolean isExpressionNode(Node node) {
    return node.getType() != Token.IF &&
        node.getType() != Token.WHILE &&
        node.getType() != Token.DO &&
        node.getType() != Token.SWITCH &&
        node.getType() != Token.RETURN &&
        node.getType() != Token.FOR &&
        !isFunction(node) &&
        node.getType() != Token.VAR &&
        node.getType() != Token.ASSIGN &&
        node.getType() != Token.ASSIGN_OP &&
        node.getType() != Token.INC &&
        node.getType() != Token.DEC;
  }

  public static boolean isVariableLiveness(Node node) {
    return node.getType() == Token.VARIABLE_LIVENESS;
  }

  public static VariableLiveness isVariableReadBeforeKill(
      Node n, String variable) {
    // TODO(user): Implement the logic here.
    throw new UnsupportedOperationException();
  }

  public static String join(Iterable<String> strings, String separator) {
    Joiner joiner = Joiner.on(separator);
    return joiner.join(strings);
  }

  public static List<Node> getAssignments(Node node) {
    List<Node> assignments = Lists.newArrayList();
    for (Node child = node; child != null;
         child = getNext(child)) {
      if (isAssignmentOp(child)) {
        assignments.add(child);
      }
    }
    return assignments;
  }

  public static boolean hasAssignments(Node node) {
    return !Collections.isEmpty(getAssignments(node));
  }

  public static boolean hasAssignmentsToVariable(Node node, String variable) {
    List<Node> assignments = getAssignments(node);
    for (Node assignment : assignments) {
      if (isLhs(assignment.getFirstChild(), getNameNode(variable))) {
        return true;
      }
    }
    return false;
  }

  public static Node getNameNode(String name) {
    return new Node(Token.NAME, name);
  }

  @Override
  public Void visit(Node node, Void unused) {
    return null;
  }
}

class Lists {
  public static <T> List<T> newArrayList(T... elements) {
    return Arrays.asList(elements);
  }
}