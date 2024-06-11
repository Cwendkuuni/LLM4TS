package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class PeepholeOptimizationsPassTest {
  private AbstractCompiler compiler;
  private PeepholeOptimizationsPass peepholeOptimizationsPass;
  private StateStack traversalState;
  private AbstractPeepholeOptimization optimization1;
  private AbstractPeepholeOptimization optimization2;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    traversalState = new StateStack();
    optimization1 = mock(AbstractPeepholeOptimization.class);
    optimization2 = mock(AbstractPeepholeOptimization.class);

    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
  }

  @Test
  public void testProcess_rootNodeVisited() {
    Node root = new Node();
    peepholeOptimizationsPass.process(null, root);

    verify(optimization1).beginTraversal(compiler);
    verify(optimization2).beginTraversal(compiler);
    verify(optimization1).optimizeSubtree(root);
    verify(optimization2).optimizeSubtree(root);
    verify(optimization1).endTraversal(compiler);
    verify(optimization2).endTraversal(compiler);
  }

  @Test
  public void testProcess_emptyAST() {
    Node root = null;
    peepholeOptimizationsPass.process(null, root);

    verify(optimization1).beginTraversal(compiler);
    verify(optimization2).beginTraversal(compiler);
    verify(optimization1).endTraversal(compiler);
    verify(optimization2).endTraversal(compiler);
  }

  @Test
  public void testShouldVisit_functionNode() {
    Node functionNode = new Node();
    assertTrue(peepholeOptimizationsPass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldVisit_nonFunctionNode() {
    Node nonFunctionNode = new Node();
    assertTrue(peepholeOptimizationsPass.shouldVisit(nonFunctionNode));
  }

  @Test
  public void testShouldRetraverse_functionNodeWithChangedScope() {
    Node functionNode = new Node();
    traversalState.push();
    traversalState.peek().changed = true;

    assertTrue(peepholeOptimizationsPass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldRetraverse_nonFunctionNode() {
    Node nonFunctionNode = new Node();
    assertFalse(peepholeOptimizationsPass.shouldRetraverse(nonFunctionNode));
  }

  @Test
  public void testShouldRetraverse_functionNodeWithNoChangedScope() {
    Node functionNode = new Node();
    traversalState.push();

    assertFalse(peepholeOptimizationsPass.shouldRetraverse(functionNode));
  }

  @Test
  public void testTraverse_emptyAST() {
    Node root = null;
    peepholeOptimizationsPass.traverse(root);
  }

  @Test
  public void testTraverse_functionNodeWithNoChildren() {
    Node functionNode = new Node();
    peepholeOptimizationsPass.traverse(functionNode);
  }

  @Test
  public void testTraverse_functionNodeWithOneChild() {
    Node functionNode = new Node();
    Node childNode = new Node();
    functionNode.setFirstChild(childNode);

    peepholeOptimizationsPass.traverse(functionNode);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    verify(optimization1).optimizeSubtree(nodeCaptor.capture());
    verify(optimization2).optimizeSubtree(nodeCaptor.capture());

    assertTrue(childNode.equals(nodeCaptor.getValue()));
  }

  @Test
  public void testTraverse_functionNodeWithMultipleChildren() {
    Node functionNode = new Node();
    Node child1 = new Node();
    Node child2 = new Node();
    functionNode.setFirstChild(child1);
    child1.setNext(child2);

    peepholeOptimizationsPass.traverse(functionNode);

    ArgumentCaptor<Node> nodeCaptor1 = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> nodeCaptor2 = ArgumentCaptor.forClass(Node.class);

    verify(optimization1).optimizeSubtree(nodeCaptor1.capture());
    verify(optimization2).optimizeSubtree(nodeCaptor2.capture());

    assertTrue(child1.equals(nodeCaptor1.getValue()));
    assertTrue(child2.equals(nodeCaptor2.getValue()));
  }

  @Test
  public void testVisit_nullNode() {
    Node nullNode = null;
    peepholeOptimizationsPass.visit(nullNode);
  }

  @Test
  public void testVisit_functionNode() {
    Node functionNode = new Node();
    peepholeOptimizationsPass.visit(functionNode);
  }

  @Test
  public void testVisit_nodeWithoutSubtreeOptimization() {
    Node node = new Node();
    peepholeOptimizationsPass.visit(node);
  }

  @Test
  public void testVisit_nodeWithSubtreeOptimization() {
    Node node = new Node();
    Node optimizedNode = new Node();

    when(optimization1.optimizeSubtree(node)).thenReturn(optimizedNode);

    peepholeOptimizationsPass.visit(node);

    verify(optimization1).optimizeSubtree(node);
    verify(optimization2).optimizeSubtree(node);
    verify(node).setFirstChild(optimizedNode);
  }

  @Test
  public void testBeginTraversal() {
    peepholeOptimizationsPass.beginTraversal();

    for (AbstractPeepholeOptimization optimization : peepholeOptimizationsPass.peepholeOptimizations) {
      verify(optimization).beginTraversal(compiler);
    }
  }

  @Test
  public void testEndTraversal() {
    peepholeOptimizationsPass.endTraversal();

    for (AbstractPeepholeOptimization optimization : peepholeOptimizationsPass.peepholeOptimizations) {
      verify(optimization).endTraversal(compiler);
    }
  }
}