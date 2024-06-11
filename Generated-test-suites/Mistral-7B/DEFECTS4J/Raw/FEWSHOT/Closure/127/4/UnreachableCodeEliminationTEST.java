package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.test.util.NodeUtil;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  @Test
  public void testEmptyCode() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new Compiler();
    Node root = Node.newBlock("", null);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractVisitor() {
      @Override
      public void visit(Node n) {
        if (n.isScript()) {
          assertNull(n.getBody());
        }
      }
    });
  }

  @Test
  public void testReturnWithNoCode() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new Compiler();
    Node root = Node.newBlock("return;", null);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractVisitor() {
      @Override
      public void visit(Node n) {
        if (n.isScript()) {
          assertNull(n.getBody());
        }
      }
    });
  }

  @Test
  public void testFunctionWithNoCode() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new Compiler();
    Node root = Node.newBlock(
        "function foo() {" +
        "  return; " +
        "}",
        null);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractVisitor() {
      @Override
      public void visit(Node n) {
        if (n.isFunction()) {
          Node functionNode = n;
          Node body = functionNode.getFirstChild();
          assertNull(body);
        }
      }
    });
  }

  @Test
  public void testFunctionWithCode() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new Compiler();
    Node root = Node.newBlock(
        "function foo() {" +
        "  var x = 1; " +
        "}",
        null);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractVisitor() {
      @Override
      public void visit(Node n) {
        if (n.isFunction()) {
          Node functionNode = n;
          Node body = functionNode.getFirstChild();
          Node statement = body.getFirstChild();
          NodeUtil.assertNodeEquals(compiler, statement, Node.newVar("x", null));
          NodeUtil.assertNodeEquals(compiler, body.getLastChild(), Node.newReturn(null));
        }
      }
    });
  }

  @Test
  public void testFunctionWithNoOpCode() {
    CompilerOptions options = new CompilerOptions();
    options.setRemoveNoOpStatements(true);
    AbstractCompiler compiler = new Compiler();
    Node root = Node.newBlock(
        "function foo() {" +
        "  true; " +
        "}",
        null);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractVisitor() {
      @Override
      public void visit(Node n) {
        if (n.isFunction()) {
          Node functionNode = n;
          Node body = functionNode.getFirstChild();
          assertNull(body);
        }
      }
    });
  }

  @Test
  public void testControlFlowGraph() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new Compiler();
    Node root = Node.newBlock(
        "function foo() {" +
        "  if (true) {" +
        "    return; " +
        "  } else {" +
        "    return; " +
        "  } " +
        "}",
        null);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowAnalysis(compiler, false, false)
        .getCfg();
    DiGraph<Node, ControlFlowGraph.Branch> graph = controlFlowGraph.getDirectedGraph();
    Node getNode = (Node n) -> graph.getDirectedGraphNode(n);
    Node ifNode = NodeUtil.getIfStatement(root).getCondition();
    Node trueBranch = getNode(NodeUtil.getIfStatement(root).getTrueTarget());
    Node falseBranch = getNode(NodeUtil.getIfStatement(root).getFalseTarget());
    Node returnNode = NodeUtil.getReturnStatement(root);
    DiGraphEdge<Node, Branch> trueEdge = graph.getEdge(ifNode, trueBranch);
    DiGraphEdge<Node, Branch> falseEdge = graph.getEdge(ifNode, falseBranch);
    DiGraphEdge<Node, Branch> returnEdge = graph.getEdge(trueBranch, returnNode);
    DiGraphEdge<Node, Branch> falseReturnEdge = graph.getEdge(falseBranch, returnNode);
    NodeUtil.assertGraphEdge(trueEdge, trueBranch, Branch.UNCOND);
    NodeUtil.assertGraphEdge(falseEdge, falseBranch, Branch.COND);
    NodeUtil.assertGraphEdge(returnEdge, returnNode, Branch.UNCOND);
    NodeUtil.assertGraphEdge(falseReturnEdge, returnNode, Branch.UNCOND);
  }

  @Test
  public void testControlFlowGraphWithDeadCode() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new Compiler();
    Node root = Node.newBlock(
        "function foo() {" +
        "  if (true) {" +
        "    return; " +
        "  } else {" +
        "    return; " +
        "    true; " +
        "  } " +
        "}",
        null);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowAnalysis(compiler, false, false)
        .getCfg();
    DiGraph<Node, ControlFlowGraph.Branch> graph = controlFlowGraph.getDirectedGraph();
    Node getNode = (Node n) -> graph.getDirectedGraphNode(n);
    Node ifNode = NodeUtil.getIfStatement(root).getCondition();
    Node trueBranch = getNode(NodeUtil.getIfStatement(root).getTrueTarget());
    Node falseBranch = getNode(NodeUtil.getIfStatement(root).getFalseTarget());
    Node returnNode = NodeUtil.getReturnStatement(root);
    DiGraphEdge<Node, Branch> trueEdge = graph.getEdge(ifNode, trueBranch);
    DiGraphEdge<Node, Branch> falseEdge = graph.getEdge(ifNode, falseBranch);
    DiGraphEdge<Node, Branch> returnEdge = graph.getEdge(trueBranch, returnNode);
    DiGraphEdge<Node, Branch> falseReturnEdge = graph.getEdge(falseBranch, returnNode);
    DiGraphEdge<Node, Branch> deadEdge = graph.getEdge(falseBranch, Node.newBlock("", null));
    NodeUtil.assertGraphEdge(trueEdge, trueBranch, Branch.UNCOND);
    NodeUtil.assertGraphEdge(falseEdge, falseBranch, Branch.COND);
    NodeUtil.assertGraphEdge(returnEdge, returnNode, Branch.UNCOND);
    NodeUtil.assertGraphEdge(falseReturnEdge, returnNode, Branch.UNCOND);
    NodeUtil.assertGraphEdge(deadEdge, falseBranch, Branch.UNCOND);
    NodeUtil.assertNull(graph.getEdge(returnNode, falseBranch));
  }

  @Test
  public void testControlFlowGraphWithDeadBreak() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new Compiler();
    Node root = Node.newBlock(
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i == 5) {" +
        "      break; " +
        "    } " +
        "  } " +
        "}",
        null);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, root);
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowAnalysis(compiler, false, false)
        .getCfg();
    DiGraph<Node, ControlFlowGraph.Branch> graph = controlFlowGraph.getDirectedGraph();
    Node getNode = (Node n) -> graph.getDirectedGraphNode(n);
    Node loopNode = NodeUtil.getForStatement(root).getCondition();
    Node breakNode = NodeUtil.getBreakStatement(root);
    Node continueNode = NodeUtil.getContinueStatement(root);
    Node bodyNode = NodeUtil.getForStatement(root).getBody();
    DiGraphEdge<Node, Branch> loopEdge = graph.getEdge(loopNode, bodyNode);
    DiGraphEdge<Node, Branch> breakEdge = graph.getEdge(bodyNode, breakNode);
    DiGraphEdge<Node, Branch> continueEdge = graph.getEdge(bodyNode, continueNode);
    DiGraphEdge<Node, Branch> returnEdge = graph.getEdge(continueNode, loopNode);
    NodeUtil.assertGraphEdge(loopEdge, loopNode, Branch.UNCOND);
    NodeUtil.assertGraphEdge(breakEdge, breakNode, Branch.UNCOND);
    NodeUtil.assertGraphEdge(continueEdge, continueNode, Branch.UNCOND);
    NodeUtil.assertGraphEdge(returnEdge, continueNode, Branch.COND);
    NodeUtil.assertNull(graph.getEdge(breakNode, bodyNode));
  }
}