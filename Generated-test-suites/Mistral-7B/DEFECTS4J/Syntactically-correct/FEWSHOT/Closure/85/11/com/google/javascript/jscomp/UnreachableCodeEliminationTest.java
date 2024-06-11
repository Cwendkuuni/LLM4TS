package org.apache.commons.cli.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.initTreeBank();
    rootNode = new Node(Token.SCRIPT, 0, null);
  }

  @Test
  public void testRemoveDeadCodeFollowingReturn() {
    Node returnStatement = new Node(Token.RETURN, 1, rootNode);
    Node deadCode = new Node(Token.ASSIGN, 2, rootNode);
    rootNode.setFirstChild(returnStatement);
    returnStatement.setNext(deadCode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    nodeTraversal.traverse(rootNode, elimination);

    DiGraphNode<Node, Branch> graphNode = elimination.curCfg.getDirectedGraphNode(returnStatement);
    assertNull(graphNode);
  }

  @Test
  public void testRemoveDeadCodeWithoutSideEffects() {
    Node expression1 = new Node(Token.ASSIGN, 1, rootNode);
    Node expression2 = new Node(Token.ASSIGN, 2, rootNode);
    Node deadCode = new Node(Token.ASSIGN, 3, rootNode);
    expression1.setNext(expression2);
    expression2.setNext(deadCode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    nodeTraversal.traverse(rootNode, elimination);

    DiGraphNode<Node, Branch> graphNode = elimination.curCfg.getDirectedGraphNode(deadCode);
    assertNull(graphNode);
  }

  @Test
  public void testRemoveDeadCodeWithSideEffects() {
    Node expression1 = new Node(Token.ASSIGN, 1, rootNode);
    Node expression2 = new Node(Token.ASSIGN, 2, rootNode);
    Node deadCode = new Node(Token.ASSIGN, 3, rootNode);
    Node statement = new Node(Token.ASSIGN, 4, rootNode);
    expression1.setNext(expression2);
    expression2.setNext(deadCode);
    deadCode.setNext(statement);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    nodeTraversal.traverse(rootNode, elimination);

    DiGraphNode<Node, Branch> graphNode = elimination.curCfg.getDirectedGraphNode(deadCode);
    assertNull(graphNode);

    assertEquals(4, rootNode.getChildCount());
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    Node ifStatement = new Node(Token.IF, 1, rootNode);
    Node condition = new Node(Token.ASSIGN, 2, rootNode);
    Node trueBranch = new Node(Token.RETURN, 3, rootNode);
    Node falseBranch = new Node(Token.RETURN, 4, rootNode);
    ifStatement.setCondition(condition);
    ifStatement.setNext(trueBranch);
    condition.setNext(falseBranch);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    nodeTraversal.traverse(rootNode, elimination);

    DiGraphNode<Node, Branch> graphNode = elimination.curCfg.getDirectedGraphNode(ifStatement);
    DiGraphNode<Node, Branch> trueBranchNode = elimination.curCfg.getDirectedGraphNode(trueBranch);
    DiGraphNode<Node, Branch> falseBranchNode = elimination.curCfg.getDirectedGraphNode(falseBranch);

    assertEquals(1, elimination.cfgStack.size());
    assertEquals(graphNode, elimination.cfgStack.peek());

    assertNull(graphNode);
    assertNull(trueBranchNode);
    assertNull(falseBranchNode);
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    Node ifStatement = new Node(Token.IF, 1, rootNode);
    Node condition = new Node(Token.ASSIGN, 2, rootNode);
    Node trueBranch = new Node(Token.BREAK, 3, rootNode);
    Node falseBranch = new Node(Token.RETURN, 4, rootNode);
    ifStatement.setCondition(condition);
    ifStatement.setNext(trueBranch);
    condition.setNext(falseBranch);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    nodeTraversal.traverse(rootNode, elimination);

    Node target = elimination.tryRemoveUnconditionalBranching(trueBranch);
    assertEquals(4, rootNode.getChildCount());
    assertEquals(target, elimination.curCfg.getDirectedGraphNode(falseBranch).getValue());
  }
}