package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.AbstractShallowCallbackType.VOID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {
  private static final Logger logger =
      Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @Test
  public void testEliminationPass() {
    String sourceCode =
        "function f() {" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    var x = 42;" +
        "    x = x + 1;" +
        "  }" +
        "}" +
        "function g() {" +
        "  var x = 42;" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    x = x + 1;" +
        "  }" +
        "}" +
        "function h() {" +
        "  return;" +
        "}";

    CompilationTestHelper.runTest(sourceCode, new Runnable() {
      @Override
      public void run() {
        DiGraph graph = CompilationTestHelper.getControlFlowGraph(null, sourceCode);
        Preconditions.checkState(graph != null, "CFG should not be null.");

        GraphReachability<Node, Branch> reachability =
            new GraphReachability<Node, Branch>(graph);
        reachability.compute(graph.getEntry().getValue());

        NodeTraversal.traverse(
            CompilationTestHelper.getCompiler(),
            null,
            new EliminationPassTest(graph));

        Node fNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("f"));
        Node xNode = fNode.getValue().getFirstChild();
        Node gNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("g"));
        Node hNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("h"));

        assertTrue(fNode.getAnnotation() == GraphReachability.REACHABLE);
        assertTrue(gNode.getAnnotation() == GraphReachability.REACHABLE);
        assertFalse(hNode.getAnnotation() == GraphReachability.REACHABLE);

        assertTrue(xNode.isDefinition());
        assertFalse(xNode.getAnnotation() == GraphReachability.REACHABLE);

        logger.fine("Pass finished.");
      }
    });
  }

  @Test
  public void testUnconditionalBranching() {
    String sourceCode =
        "function f() {" +
        "  if (true) {" +
        "    break foo;" +
        "  }" +
        "  return;" +
        "  foo:" +
        "}" +
        "function foo() {" +
        "  continue foo;" +
        "}" +
        "function g() {" +
        "  return;" +
        "}" +
        "function h() {" +
        "  break foo;" +
        "  foo:" +
        "}" +
        "function bar() {" +
        "  continue foo;" +
        "  foo:" +
        "}" +
        "function foo() {" +
        "  continue g;" +
        "  g:" +
        "}" +
        "function main() {" +
        "  f();" +
        "  h();" +
        "  g();" +
        "  bar();" +
        "}" +
        "main();";

    CompilationTestHelper.runTest(sourceCode, new Runnable() {
      @Override
      public void run() {
        DiGraph graph = CompilationTestHelper.getControlFlowGraph(null, sourceCode);
        Preconditions.checkState(graph != null, "CFG should not be null.");

        GraphReachability<Node, Branch> reachability =
            new GraphReachability<Node, Branch>(graph);
        reachability.compute(graph.getEntry().getValue());

        NodeTraversal.traverse(
            CompilationTestHelper.getCompiler(),
            null,
            new EliminationPassTest(graph));

        Node fNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("f"));
        Node hNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("h"));
        Node gNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("g"));
        Node barNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("bar"));
        Node fooNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("foo"));

        assertTrue(fNode.getAnnotation() == GraphReachability.REACHABLE);
        assertTrue(hNode.getAnnotation() == GraphReachability.REACHABLE);
        assertTrue(gNode.getAnnotation() == GraphReachability.REACHABLE);
        assertTrue(barNode.getAnnotation() == GraphReachability.REACHABLE);
        assertTrue(fooNode.getAnnotation() == GraphReachability.REACHABLE);

        logger.fine("Pass finished.");
      }
    });
  }

  @Test
  public void testRemoveNoOpStatement() {
    String sourceCode =
        "function f() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  // Do nothing" +
        "}" +
        "function g() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  // Do nothing" +
        "  true;" +
        "}" +
        "function h() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  true;";

    CompilationTestHelper.runTest(sourceCode, new Runnable() {
      @Override
      public void run() {
        DiGraph graph = CompilationTestHelper.getControlFlowGraph(null, sourceCode);
        Preconditions.checkState(graph != null, "CFG should not be null.");

        GraphReachability<Node, Branch> reachability =
            new GraphReachability<Node, Branch>(graph);
        reachability.compute(graph.getEntry().getValue());

        NodeTraversal.traverse(
            CompilationTestHelper.getCompiler(),
            null,
            new EliminationPassTest(graph, true));

        Node fNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("f"));
        Node gNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("g"));
        Node hNode = graph.getDirectedGraphNode(CompilationTestHelper.getNodeByName("h"));

        assertTrue(fNode.getAnnotation() == GraphReachability.REACHABLE);
        assertTrue(gNode.getAnnotation() == GraphReachability.REACHABLE);
        assertTrue(hNode.getAnnotation() == GraphReachability.REACHABLE);

        assertFalse(fNode.getLastChild().isDefinition());
        assertFalse(gNode.getLastChild().isDefinition());
        assertTrue(hNode.getLastChild().isDefinition());

        logger.fine("Pass finished.");
      }
    });
  }

  private static class EliminationPassTest extends UnreachableCodeElimination.EliminationPass {
    private final DiGraph graph;
    private final boolean removeNoOpStatements;

    EliminationPassTest(DiGraph graph) {
      this.graph = graph;
      this.removeNoOpStatements = false;
    }

    EliminationPassTest(DiGraph graph, boolean removeNoOpStatements) {
      this.graph = graph;
      this.removeNoOpStatements = removeNoOpStatements;
    }

    @Override
    public NodeUtil.CallbackType getCallbackType() {
      return VOID;
    }
  }
}