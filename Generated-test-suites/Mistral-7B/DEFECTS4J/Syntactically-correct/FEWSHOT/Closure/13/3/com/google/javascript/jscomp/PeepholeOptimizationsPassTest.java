package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;

public class PeepholeOptimizationsPassTest {
  private AbstractCompiler compiler;
  private Node root;
  private PeepholeOptimizationsPass pass;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    root = mock(Node.class);
    pass = new PeepholeOptimizationsPass(compiler, new DummyPeepholeOptimization(), new DummyPeepholeOptimization());
  }

  @Test
  public void testProcess() {
    pass.process(null, root);
    verify(compiler).addChangeHandler(argThat(isA(PeepholeChangeHandler.class)));
    verify(compiler, times(2)).removeChangeHandler(argThat(isA(PeepholeChangeHandler.class)));
  }

  @Test
  public void testTraverse() {
    pass.traverse(root);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler).visit(nodeCaptor.capture());
    assertEquals(root, nodeCaptor.getValue());

    pass.traverse(root);
    verify(compiler, times(2)).visit(nodeCaptor.capture());
    assertEquals(root, nodeCaptor.getValue());
  }

  @Test
  public void testVisit() {
    when(compiler.visit(any(Node.class))).thenReturn(null);

    pass.visit(root);

    for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
      verify(optimization).optimizeSubtree(root);
    }
  }

  @Test
  public void testShouldVisitFunction() {
    when(root.isFunction()).thenReturn(true);

    ScopeState state = pass.traversalState.peek();
    assertTrue(pass.shouldVisit(root));
    assertFalse(state.changed);
  }

  @Test
  public void testShouldVisitScript() {
    when(root.isScript()).thenReturn(true);

    ScopeState state = pass.traversalState.peek();
    assertTrue(pass.shouldVisit(root));
    assertFalse(state.changed);
  }

  @Test
  public void testShouldNotVisitNodeWithTraverseChildScopesFalse() {
    ScopeState state = pass.traversalState.peek();
    state.reset();
    state.traverseChildScopes = false;

    when(root.isFunction()).thenReturn(true);

    assertFalse(pass.shouldVisit(root));
  }

  @Test
  public void testShouldRetraverseNodeWithChangedParent() {
    Node functionNode = mock(Node.class);
    when(functionNode.getParent()).thenReturn(root);
    when(functionNode.isFunction()).thenReturn(true);

    ScopeState previousState = pass.traversalState.peek();
    previousState.changed = true;

    assertTrue(pass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldRetraverseNodeWithUnchangedParent() {
    Node functionNode = mock(Node.class);
    when(functionNode.getParent()).thenReturn(root);
    when(functionNode.isFunction()).thenReturn(true);

    ScopeState previousState = pass.traversalState.peek();

    assertFalse(pass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldRetraverseNodeWithChangedSelf() {
    Node functionNode = mock(Node.class);
    when(functionNode.getParent()).thenReturn(root);
    when(functionNode.isFunction()).thenReturn(true);

    ScopeState previousState = pass.traversalState.peek();
    previousState.changed = false;

    Node previousVersionOfNode = mock(Node.class);
    when(functionNode.getNodeType()).thenReturn(Node.CALL_EXPR);
    when(functionNode.getFirstChild()).thenReturn(previousVersionOfNode);

    when(previousVersionOfNode.getNodeType()).thenReturn(Node.IDENT);
    when(previousVersionOfNode.toString(null)).thenReturn("oldFunction");

    Node newVersionOfNode = mock(Node.class);
    when(functionNode.optimizeSubtree(previousVersionOfNode)).thenReturn(newVersionOfNode);

    Node currentVersionOfNode = functionNode;

    assertTrue(pass.shouldRetraverse(functionNode));

    assertEquals(newVersionOfNode, currentVersionOfNode);
  }

  @Test
  public void testShouldNotRetraverseNodeWithUnchangedSelf() {
    Node functionNode = mock(Node.class);
    when(functionNode.getParent()).thenReturn(root);
    when(functionNode.isFunction()).thenReturn(true);

    Node previousVersionOfNode = mock(Node.class);
    when(functionNode.getNodeType()).thenReturn(Node.CALL_EXPR);
    when(functionNode.getFirstChild()).thenReturn(previousVersionOfNode);

    when(previousVersionOfNode.getNodeType()).thenReturn(Node.IDENT);
    when(previousVersionOfNode.toString(null)).thenReturn("oldFunction");

    when(functionNode.optimizeSubtree(previousVersionOfNode)).thenReturn(functionNode);

    Node currentVersionOfNode = functionNode;

    assertFalse(pass.shouldRetraverse(functionNode));

    assertEquals(functionNode, currentVersionOfNode);
  }

  @Test
  public void testBeginTraversal() {
    for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
      optimization.beginTraversal(compiler);
    }

    verify(compiler, times(pass.peepholeOptimizations.length)).beginTraversal(any());
  }

  @Test
  public void testEndTraversal() {
    for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
      optimization.endTraversal(compiler);
    }

    verify(compiler, times(pass.peepholeOptimizations.length)).endTraversal(any());
  }
}

class DummyPeepholeOptimization implements AbstractPeepholeOptimization {
  @Override
  public Node optimizeSubtree(Node node) {
    return null;
  }

  @Override
  public void beginTraversal(Compiler compiler) {
  }

  @Override
  public void endTraversal(Compiler compiler) {
  }
}