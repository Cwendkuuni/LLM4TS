package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;

public class PeepholeOptimizationsPassTest {

  @SuppressWarnings("unchecked")
  private static <T extends AbstractPeepholeOptimization> T createMockOptimization(String optimizationName) {
    T optimization = mock(AbstractPeepholeOptimization.class);
    when(optimization.getName()).thenReturn(optimizationName);
    return optimization;
  }

  @Test
  public void testPeepholeOptimizationsPass() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    List<AbstractPeepholeOptimization> optimizations = new ArrayList<>();
    optimizations.add(createMockOptimization("optimization1"));
    optimizations.add(createMockOptimization("optimization2"));

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations.toArray(new AbstractPeepholeOptimization[0]));

    Node root = NodeOperations.newNode("function() {}");
    pass.process(null, root);

    for (AbstractPeepholeOptimization optimization : optimizations) {
      optimization.verifyTraversal(compiler);
    }
  }

  @Test
  public void testShouldVisit() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);

    Node root1 = NodeOperations.newNode("function f() {}");
    Node root2 = NodeOperations.newNode("function g() { f(); }");

    pass.traverse(root1);
    pass.traverse(root2);

    ScopeState state1 = pass.traversalState.peek();
    ScopeState state2 = pass.traversalState.peek();

    assertThat(state1.traverseChildScopes).isTrue();
    assertThat(state2.traverseChildScopes).isFalse();
  }

  @Test
  public void testShouldRetraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);

    Node functionNode = NodeOperations.newNode("function f() {}");
    Node scriptNode = NodeOperations.newNode("(function() {})()");

    pass.traverse(functionNode);
    pass.traverse(scriptNode);

    Node child = NodeOperations.newNode("return 1;");
    Node parentFunction = NodeOperations.newNode("function g() { return " + child.toString() + "; }");

    pass.traverse(parentFunction);

    ScopeState state = pass.traversalState.peek();
    assertThat(state.changed).isFalse();

    Node updatedChild = NodeOperations.newNode("return 2;");
    Node updatedParentFunction = NodeOperations.newNode("function g() { return " + updatedChild.toString() + "; }");

    pass.traverse(parentFunction);

    state = pass.traversalState.peek();
    assertThat(state.changed).isTrue();
  }

  @Test
  public void testProcess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);

    Node root = NodeOperations.newNode("function() { return 1; }");

    pass.process(null, root);

    pass.beginTraversal();
    pass.endTraversal();
  }
}