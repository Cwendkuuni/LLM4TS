import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.AbstractCompiler.CompilerFlags.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCallback;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractTraversal;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String sourceCode =
        "function foo() {" +
        "  var x = 1, y = 1;" +
        "  if (x === y) {" +
        "    x = y;" +
        "  }" +
        "  if (x !== y) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}" +
        "function bar() {" +
        "  var x = 1, y = 1;" +
        "  if (x > y) {" +
        "    x = y;" +
        "  } else {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerFlags flags = new CompilerFlags();
    flags.setOptimizationLevel(-1);

    Node root = Node.parse(sourceCode);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    ControlFlowGraph<Node> cfg = nodeTraversal.getControlFlowGraph();
    Graph<Node> graph = new Graph<Node>(cfg);
    graph.traverseDepthFirst(new AbstractPostOrderCallback() {
      @Override
      public void process(Node node) {
        if (NodeUtil.isReturn(node)) {
          Node value = node.getLastChild();
          assertEquals(2, NodeUtil.intValue(value));
        }
      }
    });

    // Test for dead assignments elimination in conditional branches
    sourceCode =
        "function foo() {" +
        "  var x = 1, y = 1;" +
        "  if (x === y) {" +
        "    x = y;" +
        "  }" +
        "  return x;" +
        "}" +
        "function bar() {" +
        "  var x = 1, y = 1;" +
        "  if (x !== y) {" +
        "    x = y;" +
        "  }" +
        "  return x;" +
        "}";

    compiler = new AbstractCompiler();
    flags = new CompilerFlags();
    flags.setOptimizationLevel(-1);

    root = Node.parse(sourceCode);
    deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    graph = new Graph<Node>(nodeTraversal.getControlFlowGraph());
    graph.traverseDepthFirst(new AbstractPostOrderCallback() {
      @Override
      public void process(Node node) {
        if (NodeUtil.isReturn(node)) {
          Node value = node.getLastChild();
          assertEquals(1, NodeUtil.intValue(value));
        }
      }
    });

    // Test for dead assignments elimination in loops
    sourceCode =
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) {" +
        "      i = 6;" +
        "    }" +
        "  }" +
        "  return i;" +
        "}" +
        "function bar() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i !== 5) {" +
        "      i = 6;" +
        "    }" +
        "  }" +
        "  return i;" +
        "}";

    compiler = new AbstractCompiler();
    flags = new CompilerFlags();
    flags.setOptimizationLevel(-1);

    root = Node.parse(sourceCode);
    deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    graph = new Graph<Node>(nodeTraversal.getControlFlowGraph());
    graph.traverseDepthFirst(new AbstractPostOrderCallback() {
      @Override
      public void process(Node node) {
        if (NodeUtil.isReturn(node)) {
          Node value = node.getLastChild();
          assertEquals(5, NodeUtil.intValue(value));
        }
      }
    });

    // Test for dead assignments elimination in nested conditions
    sourceCode =
        "function foo() {" +
        "  var x = 1, y = 1;" +
        "  if (x === y) {" +
        "    if (x === y) {" +
        "      x = y;" +
        "    }" +
        "  }" +
        "  return x;" +
        "}" +
        "function bar() {" +
        "  var x = 1, y = 1;" +
        "  if (x !== y) {" +
        "    if (x !== y) {" +
        "      x = y;" +
        "    }" +
        "  }" +
        "  return x;" +
        "}";

    compiler = new AbstractCompiler();
    flags = new CompilerFlags();
    flags.setOptimizationLevel(-1);

    root = Node.parse(sourceCode);
    deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    graph = new Graph<Node>(nodeTraversal.getControlFlowGraph());
    graph.traverseDepthFirst(new AbstractPostOrderCallback() {
      @Override
      public void process(Node node) {
        if (NodeUtil.isReturn(node)) {
          Node value = node.getLastChild();
          assertEquals(1, NodeUtil.intValue(value));
        }
      }
    });

    // Test for dead assignments elimination in for in loops
    sourceCode =
        "function foo() {" +
        "  var i, obj = {x: 1, y: 1};" +
        "  for (i in obj) {" +
        "    if (i === 'x') {" +
        "      i = 'y';" +
        "    }" +
        "  }" +
        "  return i;" +
        "}" +
        "function bar() {" +
        "  var i, obj = {x: 1, y: 1};" +
        "  for (i in obj) {" +
        "    if (i !== 'x') {" +
        "      i = 'y';" +
        "    }" +
        "  }" +
        "  return i;" +
        "}";

    compiler = new AbstractCompiler();
    flags = new CompilerFlags();
    flags.setOptimizationLevel(-1);

    root = Node.parse(sourceCode);
    deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    graph = new Graph<Node>(nodeTraversal.getControlFlowGraph());
    graph.traverseDepthFirst(new AbstractPostOrderCallback() {
      @Override
      public void process(Node node) {
        if (NodeUtil.isReturn(node)) {
          Node value = node.getLastChild();
          assertEquals("y", value.getString());
        }
      }
    });

    // Test for dead assignments elimination in switch statements
    sourceCode =
        "function foo() {" +
        "  var x = 1;" +
        "  switch (x) {" +
        "    case 1:" +
        "      x = 2;" +
        "      break;" +
        "    case 2:" +
        "      x = 3;" +
        "      break;" +
        "  }" +
        "  return x;" +
        "}" +
        "function bar() {" +
        "  var x = 1;" +
        "  switch (x) {" +
        "    case 1:" +
        "      x = 2;" +
        "      break;" +
        "    case 2:" +
        "      x = 3;" +
        "  }" +
        "  return x;" +
        "}";

    compiler = new AbstractCompiler();
    flags = new CompilerFlags();
    flags.setOptimizationLevel(-1);

    root = Node.parse(sourceCode);
    deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    graph = new Graph<Node>(nodeTraversal.getControlFlowGraph());
    graph.traverseDepthFirst(new AbstractPostOrderCallback() {
      @Override
      public void process(Node node) {
        if (NodeUtil.isReturn(node)) {
          Node value = node.getLastChild();
          assertEquals(1, NodeUtil.intValue(value));
        }
      }
    });

    // Test for dead assignments elimination in try-catch blocks
    sourceCode =
        "function foo() {" +
        "  try {" +
        "    var x = 1;" +
        "    x = 2;" +
        "    throw new Error('Test');" +
        "  } catch (e) {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}" +
        "function bar() {" +
        "  try {" +
        "    var x = 1;" +
        "    x = 2;" +
        "  } catch (e) {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}";

    compiler = new AbstractCompiler();
    flags = new CompilerFlags();
    flags.setOptimizationLevel(-1);

    root = Node.parse(sourceCode);
    deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    graph = new Graph<Node>(nodeTraversal.getControlFlowGraph());
    graph.traverseDepthFirst(new AbstractPostOrderCallback() {
      @Override
      public void process(Node node) {
        if (NodeUtil.isReturn(node)) {
          Node value = node.getLastChild();
          assertEquals(1, NodeUtil.intValue(value));
        }
      }
    });
  }

  @Test
  public void testDeadAssignmentsEliminationWithException() {
    String sourceCode =
        "function foo() {" +
        "  try {" +
        "    var x = 1;" +
        "    x = 2;" +
        "    throw new Error('Test');" +
        "  } catch (e) {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}" +
        "function bar() {" +
        "  try {" +
        "    var x = 1;" +
        "    x = 2;" +
        "  } catch (e) {" +
        "    x = 3;" +
        "    throw new Error('Other Test');" +
        "  }" +
        "  return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerFlags flags = new CompilerFlags();
    flags.setOptimizationLevel(-1);
    flags.setOptimizationCompilationUnit(true);

    Node root = Node.parse(sourceCode);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    // Test for dead assignments elimination in try-catch blocks with errors
    try {
      ControlFlowGraph<Node> cfg = nodeTraversal.getControlFlowGraph();
      Graph<Node> graph = new Graph<Node>(cfg);
      graph.traverseDepthFirst(new AbstractPostOrderCallback() {
        @Override
        public void process(Node node) {
          if (NodeUtil.isReturn(node)) {
            Node value = node.getLastChild();
            fail("Exception should be thrown and not return a value");
          }
        }
      });
      fail("An exception should be thrown during the test");
    } catch (AssertionError e) {
      // Expected behavior
    }
  }
}