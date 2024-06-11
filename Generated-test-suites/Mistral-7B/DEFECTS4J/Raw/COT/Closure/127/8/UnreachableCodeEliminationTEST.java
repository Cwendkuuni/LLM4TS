package com.google.javascript.jscomp; // Import necessary classes

import static org.junit.Assert.*;
import static com.google.javascript.jscomp.UnreachableCodeElimination.*;
import static com.google.javascript.rhino.NodeUtil.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractShallowCallback;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.DiGraphEdge;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

/**
 * Test Class for UnreachableCodeElimination
 *
 */
@RunWith(JUnit4.class)
public class UnreachableCodeEliminationTest {
  // Test case for the constructor
  @Test
  public void testUnreachableCodeEliminationConstruction() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    assertNotNull(eliminator);
  }

  // Test case for process method with removeNoOpStatements set to false
  @Test
  public void testProcessWithoutRemoveNoOpStatements() {
    // Setup
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    Node node = createTestNode();

    // Execution
    eliminator.process(null, node);

    // Verification
    assertTrue(NodeUtil.isFunction(node));
    assertTrue(NodeUtil.getFunctionBody(node).hasChildren());
  }

  // Test case for process method with removeNoOpStatements set to true
  @Test
  public void testProcessWithRemoveNoOpStatements() {
    // Setup
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    Node node = createTestNode();

    // Execution
    eliminator.process(null, node);

    // Verification
    assertTrue(NodeUtil.isFunction(node));
    assertFalse(NodeUtil.getFunctionBody(node).hasChildren());
  }

  // Test case for EliminationPass - removeDeadExprStatementSafely method
  @Test
  public void testRemoveDeadExprStatementSafely() {
    // Setup
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    Node unreachableNode = createUnreachableNode();
    AbstractShallowCallback callback = eliminator.new EliminationPass(null);

    // Execution
    NodeTraversal.traverse(compiler, unreachableNode, callback);

    // Verification
    assertTrue(unreachableNode.isBlock());
    assertFalse(unreachableNode.hasChildren());
  }

  // Test case for EliminationPass - tryRemoveUnconditionalBranching method
  @Test
  public void testTryRemoveUnconditionalBranching() {
    // Setup
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    Node unconditionalBreak = createUnconditionalBreakNode();

    // Execution
    eliminator.new EliminationPass(null).visit(null, unconditionalBreak, null);

    // Verification
    assertTrue(unconditionalBreak.isBlock());
    assertFalse(unconditionalBreak.hasChildren());
  }

  // Test case for EliminationPass - removeNode method
  @Test
  public void testRemoveNode() {
    // Setup
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    Node deadNode = createDeadNode();

    // Execution
    eliminator.new EliminationPass(null).removeNode(deadNode);

    // Verification
    assertTrue(deadNode.isBlock());
    assertFalse(deadNode.getParent().hasChild(deadNode));
  }

  // Helper methods
  private Node createTestNode() {
    Node node = new Node(Token.FUNCTION_DECLARATION);
    node.setLineNumber(1);
    node.setColumnNumber(1);
    node.setName("testFunction");
    node.setIsStrict(false);
    node.setIsDeclared(true);
    node.setHasBody(true);
    node.setBody(createNode(BLOCK));
    return node;
  }

  private Node createUnreachableNode() {
    Node node = new Node(TOKEN_RETURN);
    node.setParent(createTestNode().getBody());
    node.setLineNumber(2);
    node.setColumnNumber(2);
    node.setNext(new Node(TOKEN_ALERT));
    node.setNext().setLineNumber(2);
    node.setNext().setColumnNumber(3);
    return node;
  }

  private Node createUnconditionalBreakNode() {
    Node node = new Node(TOKEN_BREAK);
    node.setParent(createTestNode().getBody());
    node.setLineNumber(1);
    node.setColumnNumber(1);
    node.setNext(new Node(TOKEN_BREAK));
    node.setNext().setLineNumber(1);
    node.setNext().setColumnNumber(1);
    return node;
  }

  private Node createDeadNode() {
    Node node = new Node(TOKEN_IF);
    node.setCond(new Node(TOKEN_TRUE));
    node.setParent(createTestNode().getBody());
    node.setLineNumber(1);
    node.setColumnNumber(1);
    node.setNext(new Node(TOKEN_RETURN));
    node.setNext().setLineNumber(1);
    node.setNext().setColumnNumber(2);
    return node;
  }
}