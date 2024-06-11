package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.GraphUtils;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private DeadAssignmentsElimination deadAssignmentsElimination;
  private LiveVariablesAnalysis liveVariablesAnalysis;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope scope;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    liveVariablesAnalysis = new LiveVariablesAnalysis(compiler);
  }

  @Test
  public void testBasicFunctionality() {
    String sourceCode =
        "function foo() {" +
            "var x = 1;" +
            "var y = 2;" +
            "x = y;" +
            "y = 3;" +
            "return x + y;" +
            "}";

    Node root = NodeUtil.parseCompilationUnit(nodeFactory, sourceCode);
    deadAssignmentsElimination.process(null, root);

    DiGraph<Node, Branch> graph = controlFlowGraph.build(root, null);
    Node result = (Node) GraphUtils.getFirstDefinedValue(graph);

    assertEquals(5, result.getDoubleValue());
  }

  @Test
  public void testUnreachableAssignments() {
    String sourceCode =
        "function foo() {" +
            "var x = 1;" +
            "return 1;" +
            "x = 2;" + // Unreachable assignment
            "}";

    Node root = NodeUtil.parseCompilationUnit(nodeFactory, sourceCode);
    deadAssignmentsElimination.process(null, root);

    DiGraph<Node, Branch> graph = controlFlowGraph.build(root, null);
    Node result = (Node) GraphUtils.getFirstDefinedValue(graph);

    assertEquals(1, result.getIntValue());
  }

  @Test
  public void testDeadAssignmentsInCondition() {
    String sourceCode =
        "function foo() {" +
            "var x = 1;" +
            "if (x = 2) {" +
            "  return true;" +
            "}" +
            "return false;" +
            "}";

    Node root = NodeUtil.parseCompilationUnit(nodeFactory, sourceCode);
    deadAssignmentsElimination.process(null, root);

    DiGraph<Node, Branch> graph = controlFlowGraph.build(root, null);
    Node result = (Node) GraphUtils.getFirstDefinedValue(graph);

    // The assignment x = 2 in the condition is dead.
    assertTrue(GraphUtils.isNodeRemoved(root, (Node) NodeUtil.getExpression("x = 2")));
    assertEquals(false, result.getBooleanValue());
  }

  @Test
  public void testDeadAssignmentsInForLoop() {
    String sourceCode =
        "function foo() {" +
            "for (var i = 0; i < 10; i++) {" +
            "  if (i = 10) {" + // Dead assignment
            "    break;" +
            "  }" +
            "  var j = i;" +
            "  j = j + 1;" + // Dead assignment
            "}" +
            "return i;" +
            "}";

    Node root = NodeUtil.parseCompilationUnit(nodeFactory, sourceCode);
    deadAssignmentsElimination.process(null, root);

    DiGraph<Node, Branch> graph = controlFlowGraph.build(root, null);
    Node result = (Node) GraphUtils.getFirstDefinedValue(graph);

    // The assignments i = 10 and j = j + 1 are dead.
    assertTrue(GraphUtils.isNodeRemoved(root, (Node) NodeUtil.getExpression("i = 10")));
    assertTrue(GraphUtils.isNodeRemoved(root, (Node) NodeUtil.getExpression("j = j + 1")));

    assertEquals(10, result.getIntValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullArguments() {
    deadAssignmentsElimination.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRoot() {
    deadAssignmentsElimination.process(null, new Node(Token.PROGRAM, null));
  }

  @Test
  public void testGlobalVariables() {
    String sourceCode =
        "var _global = 1;" +
            "_global = 2;" + // Dead assignment
            "function foo() {" +
            "  return _global;" +
            "}";

    Node root = NodeUtil.parseCompilationUnit(nodeFactory, sourceCode);
    deadAssignmentsElimination.process(null, root);

    DiGraph<Node, Branch> graph = controlFlowGraph.build(root, null);
    Node result = (Node) GraphUtils.getFirstDefinedValue(graph);

    // The dead assignment _global = 2 is not removed because it is a global variable.
    assertFalse(GraphUtils.isNodeRemoved(root, (Node) NodeUtil.getExpression("_global = 2")));
    assertEquals(2, result.getDoubleValue());
  }

  @Test
  public void testFunctionVariables() {
    String sourceCode =
        "function foo() {" +
            "var x = 1;" +
            "function bar() {" +
                "x = 2;" + // Dead assignment
            "}" +
            "return x;" +
            "}";

    Node root = NodeUtil.parseCompilationUnit(nodeFactory, sourceCode);
    deadAssignmentsElimination.process(null, root);

    DiGraph<Node, Branch> graph = controlFlowGraph.build(root, null);
    Node result = (Node) GraphUtils.getFirstDefinedValue(graph);

    // The dead assignment x = 2 is not removed because it is a local variable within a function.
    assertFalse(GraphUtils.isNodeRemoved(root, (Node) NodeUtil.getExpression("x = 2")));
    assertEquals(1, result.getIntValue());
  }

  @Test
  public void testEmptyScope() {
    String sourceCode =
        "function foo() {}";

    Node root = NodeUtil.parseCompilationUnit(nodeFactory, sourceCode);
    deadAssignmentsElimination.process(null, root);

    DiGraph<Node, Branch> graph = controlFlowGraph.build(root, null);
    Node result = (Node) GraphUtils.getFirstDefinedValue(graph);

    assertTrue(GraphUtils.getChildrenCount(root) == 0);
    assertEquals(null, result);
  }

  @Test
  public void testNestedFunctions() {
    String sourceCode =
        "function foo() {" +
            "function bar() {" +
                "function baz() {" +
                    "var x = 1;" +
                    "x = 2;" + // Dead assignment
                    "return x;" +
                "}" +
                "return baz();" +
            "}" +
            "return bar();" +
        "}";

    Node root = NodeUtil.parseCompilationUnit(nodeFactory, sourceCode);
    deadAssignmentsElimination.process(null, root);

    DiGraph<Node, Branch> graph = controlFlowGraph.build(root, null);
    Node result = (Node) GraphUtils.getFirstDefinedValue(graph);

    // The dead assignment x = 2 is not removed because it is a local variable within a nested function.
    assertFalse(GraphUtils.isNodeRemoved(root, (Node) NodeUtil.getExpression("x = 2")));
    assertEquals(2, result.getIntValue());
  }

  @Test
  public void testFunctionWithNoAssignments() {
    String sourceCode =
        "function foo() {}";

    Node root = NodeUtil.parseCompilationUnit(nodeFactory, sourceCode);
    deadAssignmentsElimination.process(null, root);

    DiGraph<Node, Branch> graph = controlFlowGraph.build(root, null);
    Node result = (Node) GraphUtils.getFirstDefinedValue(graph);

    assertTrue(GraphUtils.getChildrenCount(root) == 0);
    assertEquals(null, result);
  }
}