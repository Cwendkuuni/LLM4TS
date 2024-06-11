package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.abstractShallowCallback;
import static com.google.javascript.jscomp.testing.CodeResource.getTestSource;
import static com.google.javascript.rhino.Token.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.jscomp.testing.CodeResource;

import org.junit.Test;

import java.util.List;
import java.util.logging.Level;

public class UnreachableCodeEliminationTest {
  private static final Logger logger =
      Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @Test
  public void testUnreachableCodeElimination() {
    String sourceCode = getTestSource("testcases/unreachableCodeElimination.js");

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass compilerPass = new UnreachableCodeElimination(compiler, true);
    Node externs = null;
    Node root = NodePrinter.parse(sourceCode, null, null);
    compilerPass.process(externs, root);
    String expectedCode = getTestSource("testcases/unreachableCodeEliminationExpected.js");
    String actualCode = NodePrinter.print(root);
    logger.log(Level.FINE, "Expected:\n{0}", expectedCode);
    logger.log(Level.FINE, "Actual:\n{0}", actualCode);
    assertTrue("Unexpected code change.", expectedCode.equals(actualCode));
    assertFalse("Code change should not have been reported.", compiler.shouldReportCodeChange());
  }

  @Test
  public void testUnreachableCodeEliminationNoOp() {
    String sourceCode = getTestSource("testcases/unreachableCodeEliminationNoOp.js");

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass compilerPass = new UnreachableCodeElimination(compiler, false);
    Node externs = null;
    Node root = NodePrinter.parse(sourceCode, null, null);
    compilerPass.process(externs, root);
    String expectedCode = getTestSource("testcases/unreachableCodeEliminationNoOpExpected.js");
    String actualCode = NodePrinter.print(root);
    logger.log(Level.FINE, "Expected:\n{0}", expectedCode);
    logger.log(Level.FINE, "Actual:\n{0}", actualCode);
    assertTrue("Unexpected code change.", expectedCode.equals(actualCode));
    assertFalse("Code change should not have been reported.", compiler.shouldReportCodeChange());
  }

  @Test
  public void testUnreachableCodeEliminationWithBreaksAndContinues() {
    String sourceCode = getTestSource("testcases/unreachableCodeEliminationWithBreaksAndContinues.js");

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass compilerPass = new UnreachableCodeElimination(compiler, true);
    Node externs = null;
    Node root = NodePrinter.parse(sourceCode, null, null);
    compilerPass.process(externs, root);
    String expectedCode = getTestSource("testcases/unreachableCodeEliminationWithBreaksAndContinuesExpected.js");
    String actualCode = NodePrinter.print(root);
    logger.log(Level.FINE, "Expected:\n{0}", expectedCode);
    logger.log(Level.FINE, "Actual:\n{0}", actualCode);
    assertTrue("Unexpected code change.", expectedCode.equals(actualCode));
    assertFalse("Code change should not have been reported.", compiler.shouldReportCodeChange());
  }

  @Test
  public void testUnreachableCodeEliminationWithControlFlowGraph() {
    String sourceCode = getTestSource("testcases/unreachableCodeEliminationWithControlFlowGraph.js");

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass compilerPass = new UnreachableCodeElimination(compiler, true);
    Node externs = null;
    Node root = NodePrinter.parse(sourceCode, null, null);
    compilerPass.process(externs, root);
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    cfa.process(null, root);
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    DiGraph<Node, DiGraphEdge<Node, Branch>> graph = cfg.getControlFlowGraph();

    // The entry node should be reachable.
    DiGraphNode<Node, Branch> entryNode = graph.getEntry();
    assertTrue("The entry node should be reachable.", entryNode.getAnnotation() == GraphReachability.REACHABLE);

    // The last return statement should be unreachable.
    DiGraphNode<Node, Branch> lastReturnNode =
        graph.getNodeForValue(root.getLastChild());
    assertTrue("The last return statement should be unreachable.", lastReturnNode.getAnnotation() == GraphReachability.UNREACHABLE);

    // The following code should be eliminated.
    DiGraphNode<Node, Branch> commentNode = graph.getNodeForValue(
        findChildByType(root, Token.COMMENT));
    assertTrue("The comment node should be eliminated.", commentNode == null);
  }

  @Test
  public void testUnreachableCodeEliminationWithControlFlowGraphAndBranches() {
    String sourceCode = getTestSource("testcases/unreachableCodeEliminationWithControlFlowGraphAndBranches.js");

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass compilerPass = new UnreachableCodeElimination(compiler, true);
    Node externs = null;
    Node root = NodePrinter.parse(sourceCode, null, null);
    compilerPass.process(externs, root);
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    cfa.process(null, root);
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    DiGraph<Node, DiGraphEdge<Node, Branch>> graph = cfg.getControlFlowGraph();

    // The first return statement should be eliminated.
    DiGraphNode<Node, Branch> firstReturnNode =
        graph.getNodeForValue(findChildByType(root, Token.RETURN));
    assertTrue("The first return statement should be eliminated.", firstReturnNode == null);

    // The second return statement should be reachable and not eliminated.
    DiGraphNode<Node, Branch> secondReturnNode =
        graph.getNodeForValue(findChildByType(root, Token.RETURN, 1));
    assertTrue("The second return statement should be reachable.", secondReturnNode.getAnnotation() == GraphReachability.REACHABLE);
  }

  @Test
  public void testUnreachableCodeEliminationWithControlFlowGraphAndNoOp() {
    String sourceCode = getTestSource("testcases/unreachableCodeEliminationWithControlFlowGraphAndNoOp.js");

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass compilerPass = new UnreachableCodeElimination(compiler, false);
    Node externs = null;
    Node root = NodePrinter.parse(sourceCode, null, null);
    compilerPass.process(externs, root);
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    cfa.process(null, root);
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    DiGraph<Node, DiGraphEdge<Node, Branch>> graph = cfg.getControlFlowGraph();

    // The no-op statement should be eliminated.
    DiGraphNode<Node, Branch> noOpNode =
        graph.getNodeForValue(findChildByType(root, Token.EXPRESSION_STATEMENT, 1));
    assertTrue("The no-op statement should be eliminated.", noOpNode == null);
  }

  @Test
  public void testUnreachableCodeEliminationWithControlFlowGraphAndNoOpAndBreak() {
    String sourceCode = getTestSource("testcases/unreachableCodeEliminationWithControlFlowGraphAndNoOpAndBreak.js");

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass compilerPass = new UnreachableCodeElimination(compiler, false);
    Node externs = null;
    Node root = NodePrinter.parse(sourceCode, null, null);
    compilerPass.process(externs, root);
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    cfa.process(null, root);
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    DiGraph<Node, DiGraphEdge<Node, Branch>> graph = cfg.getControlFlowGraph();

    // The no-op statement should be eliminated.
    DiGraphNode<Node, Branch> noOpNode =
        graph.getNodeForValue(findChildByType(root, Token.EXPRESSION_STATEMENT, 1));
    assertTrue("The no-op statement should be eliminated.", noOpNode == null);

    // The break statement should be reachable and not eliminated.
    DiGraphNode<Node, Branch> breakNode =
        graph.getNodeForValue(findChildByType(root, Token.BREAK, 1));
    assertTrue("The break statement should be reachable.", breakNode.getAnnotation() == GraphReachability.REACHABLE);
  }

  private static Node findChildByType(Node node, int... types) {
    for (Node child : node.getChildren()) {
      if (NodeUtil.isNodeType(child, types)) {
        return child;
      }
    }
    return null;
  }

  private static Node findChildByType(Node node, int type) {
    return findChildByType(node, type);
  }

  private static Node findChildByType(Node node, int type, int index) {
    int indexCounter = 0;
    for (Node child : node.getChildren()) {
      if (NodeUtil.isNodeType(child, type)) {
        if (indexCounter == index) {
          return child;
        }
        indexCounter++;
      }
    }
    throw new RuntimeException("Node not found.");
  }

  private static Node findChildByType(Node node, int type, int... types) {
    for (Node child : node.getChildren()) {
      if (NodeUtil.isNodeType(child, types)) {
        if (NodeUtil.isNodeType(child, type)) {
          return child;
        }
      }
    }
    throw new RuntimeException("Node not found.");
  }
}

class ControlFlowAnalysis extends AbstractCompiler {
  // ...
}

class ControlFlowGraph<NodeType extends Node> {
  // ...
}

class GraphReachability<NodeType extends Node, BranchType extends Branch> {
  // ...
}