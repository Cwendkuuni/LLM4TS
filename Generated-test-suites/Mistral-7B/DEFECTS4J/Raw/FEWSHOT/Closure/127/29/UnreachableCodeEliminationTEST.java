package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.List;

public class UnreachableCodeEliminationTest {
  private static AbstractCompiler compiler;
  private static ControlFlowGraph<Node> cfg;
  private static boolean codeChanged;

  @org.junit.BeforeClass
  public static void setUp() {
    compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(compiler.getExterns(), compiler.getToplevel());
    cfg = elimination.getCfa().getCfg();
  }

  @org.junit.Test
  public void testRemoveDeadReturnStatements() {
    Node root = compiler.getToplevel();
    Node returnNode = root.getChildAtIndex(0);
    assertNull(cfg.getDirectedGraphNode(returnNode));
  }

  @org.junit.Test
  public void testRemoveDeadStatementsWithNoSideEffects() {
    Node root = compiler.getToplevel();
    Node expressionNodeA = root.getChildAtIndex(1);
    Node expressionNodeB = root.getChildAtIndex(2);
    Node statementNode = root.getChildAtIndex(3);

    assertNull(cfg.getDirectedGraphNode(expressionNodeB));
    Node targetNode = cfg.getDirectedGraphNode(expressionNodeA);
    List<DiGraphEdge<Node, Branch>> outEdges = targetNode.getOutEdges();
    assertEquals(1, outEdges.size());
    Branch branch = outEdges.get(0).getValue();
    assertEquals(Branch.UNCOND, branch);

    Node successorNode = outEdges.get(0).getDestination().getValue();
    assertEquals(statementNode, successorNode);

    removeDeadExprStatementSafely(statementNode);

    assertNull(cfg.getDirectedGraphNode(statementNode));
    assertEquals(1, root.getChildCount());
  }

  @org.junit.Test
  public void testRemoveDeadFunction() {
    Node functionNode = compiler.getToplevel().getFirstChild();
    Node returnNode = functionNode.getLastChild();
    assertNull(cfg.getDirectedGraphNode(returnNode));

    removeDeadFunction(functionNode);

    assertEquals(0, functionNode.getChildCount());
  }

  @org.junit.Test
  public void testRemoveDeadBlock() {
    Node functionNode = compiler.getToplevel().getFirstChild();
    Node blockNode = functionNode.getChildAtIndex(1);
    assertNull(cfg.getDirectedGraphNode(blockNode));

    removeDeadBlock(blockNode);

    assertEquals(1, functionNode.getChildCount());
  }

  @org.junit.Test
  public void testRemoveDeadBreakStatement() {
    Node functionNode = compiler.getToplevel().getFirstChild();
    Node ifStatementNode = functionNode.getChildAtIndex(1);
    Node breakStatementNode = ifStatementNode.getLastChild();
    assertNull(cfg.getDirectedGraphNode(breakStatementNode));

    Node successorNode = ifStatementNode.getChildAtIndex(0);
    List<DiGraphEdge<Node, Branch>> outEdges = cfg.getDirectedGraphNode(ifStatementNode).getOutEdges();
    assertEquals(1, outEdges.size());
    Branch branch = outEdges.get(0).getValue();
    assertEquals(Branch.BRK, branch);
    Node targetNode = outEdges.get(0).getDestination().getValue();
    assertEquals(successorNode, targetNode);

    removeDeadBreakStatement(breakStatementNode);

    assertEquals(1, functionNode.getChildCount());
  }

  @org.junit.Test
  public void testRemoveDeadContinueStatement() {
    Node functionNode = compiler.getToplevel().getFirstChild();
    Node whileStatementNode = functionNode.getChildAtIndex(1);
    Node continueStatementNode = whileStatementNode.getLastChild();
    assertNull(cfg.getDirectedGraphNode(continueStatementNode));

    Node successorNode = functionNode.getChildAtIndex(2);
    List<DiGraphEdge<Node, Branch>> outEdges = cfg.getDirectedGraphNode(whileStatementNode).getOutEdges();
    assertEquals(1, outEdges.size());
    Branch branch = outEdges.get(0).getValue();
    assertEquals(Branch.CONT, branch);
    Node targetNode = outEdges.get(0).getDestination().getValue();
    assertEquals(successorNode, targetNode);

    removeDeadContinueStatement(continueStatementNode);

    assertEquals(1, functionNode.getChildCount());
  }

  @org.junit.Test
  public void testRemoveDeadReturnStatementFromFunction() {
    Node functionNode = compiler.getToplevel().getFirstChild();
    Node ifStatementNode = functionNode.getChildAtIndex(1);
    Node returnStatementNode = ifStatementNode.getLastChild();
    assertNull(cfg.getDirectedGraphNode(returnStatementNode));

    List<DiGraphEdge<Node, Branch>> outEdges = ifStatementNode.getOutEdges();
    assertEquals(1, outEdges.size());
    Branch branch = outEdges.get(0).getValue();
    assertEquals(Branch.BRK, branch);
    Node targetNode = outEdges.get(0).getDestination().getValue();
    assertEquals(functionNode, targetNode);

    removeDeadReturnStatementFromFunction(returnStatementNode);

    assertEquals(1, functionNode.getChildCount());
  }

  @org.junit.Test
  public void testRemoveDeadBlockWithSideEffects() {
    Node functionNode = compiler.getToplevel().getFirstChild();
    Node ifStatementNode = functionNode.getChildAtIndex(1);
    Node blockNode = ifStatementNode.getChildAtIndex(0);
    assertNotNull(cfg.getDirectedGraphNode(blockNode));

    Node successorNode = ifStatementNode.getChildAtIndex(1);
    List<DiGraphEdge<Node, Branch>> outEdges = cfg.getDirectedGraphNode(ifStatementNode).getOutEdges();
    assertEquals(1, outEdges.size());
    Branch branch = outEdges.get(0).getValue();
    assertEquals(Branch.BRK, branch);
    Node targetNode = outEdges.get(0).getDestination().getValue();
    assertEquals(successorNode, targetNode);

    removeDeadBlock(blockNode);

    assertEquals(1, functionNode.getChildCount());
  }

  @org.junit.Test
  public void testRemoveDeadFunctionWithSideEffects() {
    Node functionNode = compiler.getToplevel().getFirstChild();
    assertNotNull(cfg.getDirectedGraphNode(functionNode));

    removeDeadFunction(functionNode);

    assertEquals(0, compiler.getToplevel().getChildCount());
  }

  private void assertEquals(int expectedSize, Node node) {
    assertEquals(expectedSize, node.getChildCount());
  }

  private void removeDeadExprStatementSafely(Node n) {
    new EliminationPass(cfg).visit(null, n, null);
  }

  private void removeDeadFunction(Node n) {
    NodeTraversal.traverse(compiler, n, new EliminationPass(cfg));
  }

  private void removeDeadBlock(Node n) {
    Node parent = n.getParent();
    NodeUtil.removeChild(parent, n);
  }

  private void removeDeadBreakStatement(Node n) {
    List<Node> children = Arrays.asList(n.getPreviousSibling(), n.getNextSibling());
    Node parent = n.getParent();
    NodeUtil.removeChildren(parent, children);
  }

  private void removeDeadContinueStatement(Node n) {
    List<Node> children = Arrays.asList(n.getPreviousSibling(), n.getNextSibling());
    Node parent = n.getParent();
    NodeUtil.removeChildren(parent, children);
  }

  private void removeDeadReturnStatementFromFunction(Node n) {
    Node functionNode = n.getParent();
    NodeUtil.removeChild(functionNode, n);
  }
}