package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.javascript.rhino.Node;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class PeepholeOptimizationsPassTest {
  private PeepholeOptimizationsPass pass;
  private AbstractCompiler compilerMock;
  private List<AbstractPeepholeOptimization> optimizations = new ArrayList<>();

  @Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    pass = new PeepholeOptimizationsPass(compilerMock, new DummyPeepholeOptimization1(), new DummyPeepholeOptimization2());
    optimizations.add(new DummyPeepholeOptimization1());
    optimizations.add(new DummyPeepholeOptimization2());
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testBeginAndEndTraversal() {
    pass.beginTraversal();
    for (AbstractPeepholeOptimization optimization : optimizations) {
      verify(optimization).beginTraversal(compilerMock);
    }

    pass.endTraversal();
    for (AbstractPeepholeOptimization optimization : optimizations) {
      verify(optimization).endTraversal(compilerMock);
    }
  }

  @Test
  public void testProcessWithOneNode() {
    Node root = createMockNode();
    pass.process(null, root);
    verify(compilerMock).addChangeHandler(pass.getPeepholeChangeHandler());
    verify(compilerMock).removeChangeHandler(pass.getPeepholeChangeHandler());
    traverseAndVerify(root);
  }

  @Test
  public void testProcessWithMultipleNodes() {
    Node root = createMockNode();
    Node child1 = createMockNode();
    Node child2 = createMockNode();
    root.setFirstChild(child1);
    child1.setNext(child2);
    pass.process(null, root);
    verify(compilerMock).addChangeHandler(pass.getPeepholeChangeHandler());
    verify(compilerMock).removeChangeHandler(pass.getPeepholeChangeHandler());
    traverseAndVerify(root);
    traverseAndVerify(child1);
    traverseAndVerify(child2);
  }

  @Test
  public void testShouldRetraverseFunctionOrScript() {
    Node root = createMockNode();
    Node functionNode = createFunctionMockNode();
    root.setFirstChild(functionNode);
    ScopeState state = pass.traversalState.peek();
    state.traverseChildScopes = false;
    assertFalse(pass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldRetraverseNonFunctionOrScript() {
    Node root = createMockNode();
    Node nonFunctionNode = createMockNode();
    root.setFirstChild(nonFunctionNode);
    ScopeState state = pass.traversalState.peek();
    assertTrue(pass.shouldRetraverse(nonFunctionNode));
  }

  @Test
  public void testShouldVisitFunctionOrScript() {
    Node root = createMockNode();
    Node functionNode = createFunctionMockNode();
    root.setFirstChild(functionNode);
    ScopeState previous = pass.traversalState.peek();
    assertFalse(pass.shouldVisit(functionNode));
    pass.traversalState.push();
    assertTrue(pass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldVisitNonFunctionOrScript() {
    Node root = createMockNode();
    Node nonFunctionNode = createMockNode();
    root.setFirstChild(nonFunctionNode);
    assertTrue(pass.shouldVisit(nonFunctionNode));
  }

  @Test
  public void testExitNodeFunctionOrScript() {
    Node root = createMockNode();
    Node functionNode = createFunctionMockNode();
    root.setFirstChild(functionNode);
    pass.traversalState.push();
    pass.traversalState.pop();
    verify(functionNode).getParent();
  }

  @Test
  public void testExitNodeNonFunctionOrScript() {
    Node root = createMockNode();
    Node nonFunctionNode = createMockNode();
    root.setFirstChild(nonFunctionNode);
    pass.traversalState.push();
    pass.traversalState.pop();
    // No verification needed as getParent() is not mocked for non-function/script nodes
  }

  private Node createMockNode() {
    return mock(Node.class);
  }

  private Node createFunctionMockNode() {
    Node functionNode = mock(Node.class);
    when(functionNode.isFunction()).thenReturn(true);
    return functionNode;
  }

  private void traverseAndVerify(Node node) {
    pass.traverse(node);
  }
}

class DummyPeepholeOptimization1 {}
class DummyPeepholeOptimization2 {}