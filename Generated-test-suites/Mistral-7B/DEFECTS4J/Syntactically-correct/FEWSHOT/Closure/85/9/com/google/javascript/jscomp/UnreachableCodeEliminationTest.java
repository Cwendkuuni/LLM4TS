package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.ROOT;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private Node rootNode;
  private ControlFlowGraph<Node> controlFlowGraph;

  @Before
  public void setUp() {
    compiler = new CompilerPassConfig().getCompiler();
    rootNode = new Node(Token.SCRIPT);
    rootNode.setParent(null);
    controlFlowGraph = new ControlFlowAnalysis(compiler, false, false).process(null, rootNode);
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    Node deadStatement = new Node(Token.ASSIGN);
    deadStatement.setParent(rootNode);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(compiler, null, null, ROOT, elimination);
    traversal.traverse(rootNode);

    assertEquals(0, rootNode.getChildCount());
    assertNull(findNodeByValue(controlFlowGraph, deadStatement));
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    Node returnStatement = new Node(Token.RETURN);
    returnStatement.setParent(rootNode);

    Node unconditionalBreak = new Node(Token.BREAK);
    unconditionalBreak.setParent(returnStatement);

    Node target = new Node(Token.RETURN);
    target.setParent(rootNode);

    Node unconditionalReturn = new Node(Token.RETURN);
    unconditionalReturn.setParent(rootNode);

    Node unconditionalContinue = new Node(Token.CONTINUE);
    unconditionalContinue.setParent(returnStatement);

    Node unreachableStatement = new Node(Token.ASSIGN);
    unreachableStatement.setParent(target);

    Node conditionalBreak = new Node(Token.BREAK);
    conditionalBreak.setParent(new Node(Token.IF, rootNode));
    conditionalBreak.getLastChild().setParent(conditionalBreak);

    Node conditionalReturn = new Node(Token.RETURN);
    conditionalReturn.setParent(new Node(Token.IF, rootNode));
    conditionalReturn.getLastChild().setParent(conditionalReturn);

    Node conditionalContinue = new Node(Token.CONTINUE);
    conditionalContinue.setParent(new Node(Token.IF, rootNode));
    conditionalContinue.getLastChild().setParent(conditionalContinue);

    Node block = new Node(Token.BLOCK, null);
    block.setParent(returnStatement);

    Node blockReturn = new Node(Token.RETURN);
    blockReturn.setParent(block);

    List<Node> children = new ArrayList<>();
    children.add(unconditionalBreak);
    children.add(returnStatement);
    returnStatement.setChildren(children);

    List<Node> children2 = new ArrayList<>();
    children2.add(unconditionalReturn);
    returnStatement.setChildren(children2);

    List<Node> children3 = new ArrayList<>();
    children3.add(unconditionalContinue);
    returnStatement.setChildren(children3);

    List<Node> children4 = new ArrayList<>();
    children4.add(conditionalBreak);
    children4.add(unreachableStatement);
    target.setChildren(children4);

    List<Node> children5 = new ArrayList<>();
    children5.add(conditionalReturn);
    Node ifNode = new Node(Token.IF, rootNode);
    ifNode.setChildren(children5);
    rootNode.addChild(ifNode);

    List<Node> children6 = new ArrayList<>();
    children6.add(conditionalContinue);
    ifNode.setChildren(children6);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(compiler, null, null, ROOT, elimination);
    traversal.traverse(rootNode);

    Node unreachableNode = findNodeByValue(controlFlowGraph, unreachableStatement);
    assertNull(unreachableNode);

    Node targetNode = findNodeByValue(controlFlowGraph, target);
    assertEquals(returnStatement, targetNode.getParent());

    Node breakNode = findNodeByValue(controlFlowGraph, unconditionalBreak);
    assertEquals(returnStatement, breakNode.getParent());

    Node returnNode = findNodeByValue(controlFlowGraph, returnStatement);
    assertEquals(null, returnNode.getParent());

    Node conditionalReturnNode = findNodeByValue(controlFlowGraph, conditionalReturn);
    assertEquals(ifNode, conditionalReturnNode.getParent());

    Node conditionalContinueNode = findNodeByValue(controlFlowGraph, conditionalContinue);
    assertEquals(ifNode, conditionalContinueNode.getParent());

    Node conditionalBreakNode = findNodeByValue(controlFlowGraph, conditionalBreak);
    assertEquals(ifNode, conditionalBreakNode.getParent());

    Node blockNode = findNodeByValue(controlFlowGraph, block);
    assertEquals(returnStatement, blockNode.getParent());

    Node blockReturnNode = findNodeByValue(controlFlowGraph, blockReturn);
    assertEquals(block, blockReturnNode.getParent());

    Node unconditionalReturnNode = findNodeByValue(controlFlowGraph, unconditionalReturn);
    assertEquals(null, unconditionalReturnNode.getParent());

    Node unconditionalContinueNode = findNodeByValue(controlFlowGraph, unconditionalContinue);
    assertEquals(returnStatement, unconditionalContinueNode.getParent());
  }

  private Node findNodeByValue(ControlFlowGraph<Node> controlFlowGraph, Node node) {
    for (DiGraphNode<Node, Branch> graphNode : controlFlowGraph.nodes().values()) {
      if (graphNode.getValue().equals(node)) {
        return graphNode.getValue();
      }
    }
    return null;
  }
}