package com.google.javascript.jscomp;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static com.google.javascript.jscomp.NodeUtil.isAssign;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.rhino.Token.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

  private static final String TEST_CODE =
      "function f(x, y, z) {" +
      "  var a = 1, b = 2, c = 3, d = 4;" +
      "  if (x && y = a) {" +
      "    z = y + c;" +
      "  } else {" +
      "    y = b;" +
      "  }" +
      "  a = 5;" +
      "  return z;" +
      "}";

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination eliminator;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope scope;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    controlFlowGraph = mock(ControlFlowGraph.class);
    scope = mock(Scope.class);
    eliminator = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testNoDeadAssignments() {
    Node root = Node.parse(TEST_CODE);
    NodeTraversal traversal = new NodeTraversal(root, null, null, null);
    eliminator.process(null, root);
    assertSame(root, traversal.getTree());
  }

  @Test
  public void testSimpleAssignment() {
    Node root = Node.parse(TEST_CODE);
    when(scope.isDeclared("a", false)).thenReturn(true);
    when(scope.isDeclared("b", false)).thenReturn(true);
    when(scope.getVar("a")).thenReturn(new Var("a", -1, 0));
    when(scope.getVar("b")).thenReturn(new Var("b", -1, 1));
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);

    Node expected = new Node(ASSIGN, new Node(NAME, "z"),
        new Node(ADD, new Node(NAME, "y"), new Node(NUMBER, 3)));
    assertTrue(NodeUtil.isNodeEqual(expected, traversal.getTree().getLastChild()));
  }

  @Test
  public void testDeadAssignment() {
    Node root = Node.parse(TEST_CODE);
    when(scope.isDeclared("a", false)).thenReturn(true);
    when(scope.isDeclared("b", false)).thenReturn(true);
    when(scope.getVar("a")).thenReturn(new Var("a", -1, 0));
    when(scope.getVar("b")).thenReturn(new Var("b", -1, 1));
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class),
            mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);

    Node expected = new Node(ASSIGN, new Node(NAME, "y"), new Node(NUMBER, 2));
    assertTrue(NodeUtil.isNodeEqual(expected, traversal.getTree().getLastChild()));
  }

  @Test
  public void testDeadAssignmentInIfStatement() {
    Node root = Node.parse(TEST_CODE);
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.isDeclared("y", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new Var("x", -1, 0));
    when(scope.getVar("y")).thenReturn(new Var("y", -1, 1));
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class),
            mock(DiGraphNode.class), mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);

    Node ifStatement = traversal.getTree().getFirstChild().getFirstChild();
    Node expected = new Node(ASSIGN, new Node(NAME, "y"), new Node(NUMBER, 2));
    assertTrue(NodeUtil.isNodeEqual(expected, ifStatement.getLastChild()));
  }

  @Test
  public void testDeadAssignmentInElseStatement() {
    Node root = Node.parse(TEST_CODE);
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.isDeclared("y", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new Var("x", -1, 0));
    when(scope.getVar("y")).thenReturn(new Var("y", -1, 1));
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class),
            mock(DiGraphNode.class), mock(DiGraphNode.class),
            mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);

    Node elseStatement = traversal.getTree().getFirstChild().getLastChild();
    Node expected = new Node(ASSIGN, new Node(NAME, "y"), new Node(NUMBER, 2));
    assertTrue(NodeUtil.isNodeEqual(expected, elseStatement.getLastChild()));
  }

  @Test
  public void testDeadAssignmentInForStatement() {
    Node root = Node.parse("function f(x) {" +
        "  for (var i = 0; i < 10; i = i + 1) {" +
        "    if (i && (x = 1)) {" +
        "      continue;" +
        "    } else {" +
        "      x = 2;" +
        "    }" +
        "  }" +
        "  return x;" +
        "}");
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.isDeclared("i", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new Var("x", -1, 0));
    when(scope.getVar("i")).thenReturn(new Var("i", -1, 1));
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class),
            mock(DiGraphNode.class), mock(DiGraphNode.class),
            mock(DiGraphNode.class), mock(DiGraphNode.class),
            mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);

    Node forStatement = traversal.getTree().getFirstChild();
    Node expected = new Node(ASSIGN, new Node(NAME, "x"), new Node(NUMBER, 2));
    assertTrue(NodeUtil.isNodeEqual(expected, forStatement.getLastChild()));
  }

  @Test
  public void testDeadAssignmentInIfElseStatementWithMultipleAssignments() {
    Node root = Node.parse("function f() {" +
        "  var x = 1;" +
        "  if (x) {" +
        "    x = 2;" +
        "    x = 3;" +
        "  } else {" +
        "    x = 4;" +
        "  }" +
        "  return x;" +
        "}");
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new Var("x", -1, 0));
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class),
            mock(DiGraphNode.class), mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);

    Node expected = new Node(ASSIGN, new Node(NAME, "x"), new Node(NUMBER, 3));
    assertTrue(NodeUtil.isNodeEqual(expected, traversal.getTree().getLastChild()));
  }

  @Test
  public void testDeadAssignmentInIfElseStatementWithMultipleConditions() {
    Node root = Node.parse("function f() {" +
        "  var x = 1;" +
        "  if (x && x) {" +
        "    x = 2;" +
        "  } else {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}");
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new Var("x", -1, 0));
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class),
            mock(DiGraphNode.class), mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);

    Node expected = new Node(ASSIGN, new Node(NAME, "x"), new Node(NUMBER, 1));
    assertTrue(NodeUtil.isNodeEqual(expected, traversal.getTree().getLastChild()));
  }

  @Test
  public void testDeadAssignmentInIfElseStatementWithMultipleAssignmentsAndConditions() {
    Node root = Node.parse("function f() {" +
        "  var x = 1;" +
        "  if (x && x) {" +
        "    x = 2;" +
        "    x = 3;" +
        "  } else {" +
        "    x = 4;" +
        "    x = 5;" +
        "  }" +
        "  return x;" +
        "}");
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new Var("x", -1, 0));
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class),
            mock(DiGraphNode.class), mock(DiGraphNode.class),
            mock(DiGraphNode.class), mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);

    Node expected = new Node(ASSIGN, new Node(NAME, "x"), new Node(NUMBER, 1));
    assertTrue(NodeUtil.isNodeEqual(expected, traversal.getTree().getLastChild()));
  }

  @Test
  public void testDeadAssignmentInTernaryStatement() {
    Node root = Node.parse("function f() {" +
        "  var x = 1;" +
        "  y = x > 0 ? 1 : (x = 2);" +
        "  return y;" +
        "}");
    when(scope.isDeclared("x", false)).thenReturn(true);
    when(scope.getVar("x")).thenReturn(new Var("x", -1, 0));
    when(scope.isDeclared("y", false)).thenReturn(true);
    when(scope.getVar("y")).thenReturn(new Var("y", -1, 1));
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class),
            mock(DiGraphNode.class), mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);

    Node expected = new Node(ASSIGN, new Node(NAME, "x"), new Node(NUMBER, 2));
    assertTrue(NodeUtil.isNodeEqual(expected, traversal.getTree().getLastChild()));
  }

  @Test(expected = RuntimeException.class)
  public void testAssignmentToNonVariable() {
    Node root = Node.parse("function f() {" +
        "  x = 1;" +
        "}");
    when(scope.isDeclared("x", false)).thenReturn(false);
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);
  }

  @Test(expected = RuntimeException.class)
  public void testAssignmentToNonExistingVariable() {
    Node root = Node.parse("function f() {" +
        "  x = 1;" +
        "}");
    when(scope.isDeclared("x", false)).thenReturn(false);
    when(scope.isDeclared("y", false)).thenReturn(true);
    when(controlFlowGraph.getDirectedGraphNodes())
        .thenReturn(Arrays.asList(mock(DiGraphNode.class),
            mock(DiGraphNode.class)));

    NodeTraversal traversal = new NodeTraversal(root, controlFlowGraph, scope,
        eliminator);
    eliminator.process(null, root);
  }
}