package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler mockCompiler;
 private AbstractPeepholeOptimization mockOptimization;
 private PeepholeOptimizationsPass pass;

 @Test
 public void testTraversalState() {
  mockCompiler = mock(AbstractCompiler.class);
  mockOptimization = mock(AbstractPeepholeOptimization.class);

  when(mockOptimization.beginTraversal(mockCompiler)).thenReturn(null);
  when(mockOptimization.endTraversal(mockCompiler)).thenReturn(null);

  pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);

  assertTrue(pass.traversalState.states.isEmpty());
  pass.traversalState.push();
  assertTrue(pass.traversalState.states.size() == 1);
  pass.traversalState.pop();
  assertTrue(pass.traversalState.states.isEmpty());
 }

 @Test
 public void testShouldVisitFunctionOrScript() {
  mockCompiler = mock(AbstractCompiler.class);
  mockOptimization = mock(AbstractPeepholeOptimization.class);

  when(mockOptimization.beginTraversal(mockCompiler)).thenReturn(null);
  when(mockOptimization.endTraversal(mockCompiler)).thenReturn(null);

  pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);

  Node node = mock(Node.class);
  when(node.isFunction()).thenReturn(true);

  assertTrue(pass.shouldVisit(node));

  node = mock(Node.class);
  when(node.isScript()).thenReturn(true);

  assertTrue(pass.shouldVisit(node));
 }

 @Test
 public void testShouldVisitOtherNodes() {
  mockCompiler = mock(AbstractCompiler.class);
  mockOptimization = mock(AbstractPeepholeOptimization.class);

  when(mockOptimization.beginTraversal(mockCompiler)).thenReturn(null);
  when(mockOptimization.endTraversal(mockCompiler)).thenReturn(null);

  pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);

  Node node = mock(Node.class);

  assertFalse(pass.shouldVisit(node));
 }

 @Test
 public void testShouldRetraverseFunctionOrScript() {
  mockCompiler = mock(AbstractCompiler.class);
  mockOptimization = mock(AbstractPeepholeOptimization.class);

  when(mockOptimization.beginTraversal(mockCompiler)).thenReturn(null);
  when(mockOptimization.endTraversal(mockCompiler)).thenReturn(null);

  pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);

  Node parentNode = mock(Node.class);
  Node childNode = mock(Node.class);

  when(childNode.isFunction()).thenReturn(true);
  when(childNode.getParent()).thenReturn(parentNode);

  ScopeState state = pass.traversalState.peek();
  assertFalse(state.changed);
  assertFalse(state.traverseChildScopes);

  pass.traverse(parentNode);

  state = pass.traversalState.peek();
  assertTrue(state.changed);
  assertFalse(state.traverseChildScopes);
 }

 @Test
 public void testShouldRetraverseOtherNodes() {
  mockCompiler = mock(AbstractCompiler.class);
  mockOptimization = mock(AbstractPeepholeOptimization.class);

  when(mockOptimization.beginTraversal(mockCompiler)).thenReturn(null);
  when(mockOptimization.endTraversal(mockCompiler)).thenReturn(null);

  pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);

  Node parentNode = mock(Node.class);
  Node childNode = mock(Node.class);

  when(childNode.isFunction()).thenReturn(false);
  when(childNode.getParent()).thenReturn(parentNode);

  ScopeState state = pass.traversalState.peek();
  assertFalse(state.changed);
  assertTrue(state.traverseChildScopes);

  pass.traverse(parentNode);

  state = pass.traversalState.peek();
  assertFalse(state.changed);
  assertTrue(state.traverseChildScopes);
 }

 @Test
 public void testProcess() {
  mockCompiler = mock(AbstractCompiler.class);
  mockOptimization = mock(AbstractPeepholeOptimization.class);

  when(mockOptimization.beginTraversal(mockCompiler)).thenReturn(null);
  when(mockOptimization.endTraversal(mockCompiler)).thenReturn(null);

  pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);

  Node root = mock(Node.class);

  pass.process(null, root);

  verify(mockCompiler).addChangeHandler(argThat(new IsInstanceOf(PeepholeChangeHandler.class)));
  verify(mockCompiler).removeChangeHandler(argThat(new IsInstanceOf(PeepholeChangeHandler.class)));
 }
}

class PeepholeChangeHandler implements CodeChangeHandler {
 // Empty implementation
}