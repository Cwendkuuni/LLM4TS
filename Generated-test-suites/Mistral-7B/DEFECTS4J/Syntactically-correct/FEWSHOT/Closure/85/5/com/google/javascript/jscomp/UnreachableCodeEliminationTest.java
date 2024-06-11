package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DiGraph;
import com.google.javascript.jscomp.DiGraphEdge;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.NodeTraversalType;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test class for UnreachableCodeElimination class.
 */
public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    cfg = new ControlFlowGraph<>();
  }

  @Test
  public void testUnreachableCodeElimination() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Scope scope = new Scope(null);
    Node root = new Node(Token.SCRIPT, 0, null, null);
    NodeTraversal traversal = new NodeTraversal(NodeTraversalType.PREORDER, null, null, null, null);
    NodeUtil.traverse(traversal, root);
    elimination.process(null, root);

    assertEquals(0, cfg.size());
  }

  @Test
  public void testEnterScope() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Scope scope = mock(Scope.class);
    when(scope.getRootNode()).thenReturn(new Node(Token.SCRIPT, 0, null, null));

    elimination.enterScope(mock(NodeTraversal.class));

    assertEquals(1, cfg.size());
    verify(compiler, times(1)).analyze(eq(scope), eq(true), eq(false));
  }

  @Test
  public void testExitScope() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    ControlFlowGraph<Node> oldCfg = new ControlFlowGraph<>();
    oldCfg.addNode(new Node(Token.SCRIPT, 0, null, null));
    elimination.cfgStack.push(oldCfg);

    elimination.exitScope(mock(NodeTraversal.class));

    assertEquals(0, elimination.cfgStack.size());
  }

  @Test
  public void testVisit() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    Node root = new Node(Token.SCRIPT, 0, null, null);

    DiGraphNode<Node, Branch> node1 = new DiGraphNode<>(new Node(Token.VAR, 0, null, null));
    DiGraphNode<Node, Branch> node2 = new DiGraphNode<>(new Node(Token.RETURN, 0, null, null));

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    cfg.addNode(node1);
    cfg.addNode(node2);
    cfg.addEdge(node1, node2, Branch.UNCOND);

    elimination.curCfg = cfg;

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = mock(Node.class);

    when(traversal.getScope()).thenReturn(mock(Scope.class));
    when(traversal.getParent()).thenReturn(parent);

    elimination.visit(traversal, node1, parent);

    verify(compiler, never()).reportCodeChange();

    elimination.visit(traversal, node2, parent);

    verify(compiler, times(1)).reportCodeChange();
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node returnNode = new Node(Token.RETURN, 0, null, null);
    Node breakNode = new Node(Token.BREAK, 0, null, null);
    Node continueNode = new Node(Token.CONTINUE, 0, null, null);

    DiGraphNode<Node, Branch> node1 = new DiGraphNode<>(breakNode);
    DiGraphNode<Node, Branch> node2 = new DiGraphNode<>(returnNode);
    DiGraphNode<Node, Branch> node3 = new DiGraphNode<>(continueNode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    cfg.addNode(node1);
    cfg.addNode(node2);
    cfg.addNode(node3);
    cfg.addEdge(node1, node2, Branch.UNCOND);
    cfg.addEdge(node1, node3, Branch.UNCOND);
    cfg.addEdge(node3, node2, Branch.UNCOND);

    elimination.curCfg = cfg;

    Node testNode = new Node(Token.IF, 0, null, null);
    testNode.setNext(breakNode);

    assertEquals(breakNode, elimination.tryRemoveUnconditionalBranching(testNode));
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node deadStatement = new Node(Token.ASSIGN, 0, null, null);
    Node parent = new Node(Token.BLOCK, 0, null, null);
    parent.setFirstChild(deadStatement);

    elimination.removeDeadExprStatementSafely(deadStatement);

    assertTrue(parent.hasNoChildren());
  }

  @Test
  public void testRemoveUnreachableCodeInDeepBranch() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = new Node(Token.IF, 0, null, null);
    Node condition = new Node(Token.LOGICAL_OR, 0, null, null);
    Node block1 = new Node(Token.BLOCK, 0, null, null);
    Node block2 = new Node(Token.BLOCK, 0, null, null);

    root.setFirstChild(condition);
    condition.setFirstChild(new Node(Token.LOGICAL_OR, 0, null, null));
    condition.getFirstChild().setFirstChild(new Node(Token.RETURN, 0, null, null));
    condition.getFirstChild().setNext(new Node(Token.IF, 0, null, null));
    condition.getLastChild().setNext(block1);

    Node ifStatement = condition.getLastChild();
    Node innerCondition = ifStatement.getFirstChild();
    Node innerIfStatement = innerCondition.getFirstChild();

    innerCondition.setNext(new Node(Token.RETURN, 0, null, null));
    innerIfStatement.setFirstChild(new Node(Token.RETURN, 0, null, null));

    Scope scope = new Scope(null);
    NodeTraversal traversal = new NodeTraversal(NodeTraversalType.PREORDER, null, null, null, scope);

    elimination.process(null, root);

    NodeUtil.traverse(traversal, root);

    List<Node> remainingNodes = new ArrayList<>();
    Node currentNode = root;
    while (currentNode != null) {
      remainingNodes.add(currentNode);
      currentNode = currentNode.getNext();
    }

    assertEquals(2, remainingNodes.size());
    assertFalse(remainingNodes.get(0).getType() == Token.RETURN);
    assertFalse(remainingNodes.get(1).getType() == Token.RETURN);
  }
}