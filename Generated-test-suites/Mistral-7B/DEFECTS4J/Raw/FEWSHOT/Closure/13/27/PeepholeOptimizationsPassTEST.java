package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPasses;
import com.google.javascript.jscomp.CompilerPasses.CompilationLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.ScopeState;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PeepholeOptimizationsPassTest {

  @Mock
  private CompilerPasses passes;
  @Mock
  private Node rootNode;
  @Mock
  private Node externsNode;

  private AbstractCompiler compiler;
  private PeepholeOptimizationsPass pass;
  private List<AbstractPeepholeOptimization> optimizations = Lists.newArrayList();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    pass = new PeepholeOptimizationsPass(compiler, optimizations.toArray(new AbstractPeepholeOptimization[0]));

    when(compiler.getPassManager()).thenReturn(passes);
    when(compiler.getSourceName()).thenReturn(SourceName.getScriptSourceName(""));
    when(compiler.getCompilationLevel()).thenReturn(CompilationLevel.ADVANCED);

    pass.beginTraversal();
  }

  @Test
  public void testProcess() {
    pass.process(externsNode, rootNode);
    pass.endTraversal();
  }

  @Test
  public void testTraverse() {
    Node node1 = mock(Node.class);
    Node node2 = mock(Node.class);

    when(node1.isFunction()).thenReturn(true);
    when(node1.isScript()).thenReturn(false);
    when(node1.getFirstChild()).thenReturn(node2);

    when(node2.isFunction()).thenReturn(false);
    when(node2.isScript()).thenReturn(false);
    when(node2.getFirstChild()).thenReturn(null);

    pass.traverse(node1);

    ScopeState state = traversalState.peek();
    Preconditions.checkState(state.changed, "should have changed a node");
  }

  @Test
  public void testShouldVisit() {
    Node node = mock(Node.class);
    when(node.isFunction()).thenReturn(false);
    when(node.isScript()).thenReturn(false);

    traversalState.push();
    boolean result = pass.shouldVisit(node);
    traversalState.pop();

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldRetraverse() {
    Node node = mock(Node.class);
    when(node.isFunction()).thenReturn(false);
    when(node.isScript()).thenReturn(false);

    traversalState.push();
    boolean result = pass.shouldRetraverse(node);
    traversalState.pop();

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldVisitFunction() {
    Node node = mock(Node.class);
    when(node.isFunction()).thenReturn(true);
    when(node.isScript()).thenReturn(false);

    traversalState.push();
    boolean result = pass.shouldVisit(node);
    traversalState.pop();

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldVisitScript() {
    Node node = mock(Node.class);
    when(node.isFunction()).thenReturn(false);
    when(node.isScript()).thenReturn(true);

    traversalState.push();
    boolean result = pass.shouldVisit(node);
    traversalState.pop();

    assertThat(result).isFalse();
  }

  @Test
  public void testVisit() {
    Node node = mock(Node.class);
    when(node.isFunction()).thenReturn(false);
    when(node.isScript()).thenReturn(false);

    OptimizingRewriter optimizingRewriter = new OptimizingRewriter();
    optimizations.add(optimizingRewriter);

    when(optimizingRewriter.optimizeSubtree(node)).thenReturn(node);
    pass.visit(node);

    Preconditions.checkState(node.getParent() != null, "node should have a parent");
  }
}

class OptimizingRewriter implements AbstractPeepholeOptimization {
  @Override
  public Node optimizeSubtree(Node node) {
    return node;
  }
}

class StateStack {
  private ArrayList<ScopeState> states = Lists.newArrayList();
  private int currentDepth = 0;

  StateStack() {
  }

  ScopeState peek() {
    return states.get(currentDepth);
  }

  void push() {
    currentDepth++;
  }

  void pop() {
    currentDepth--;
  }
}

class ScopeState {
  boolean changed;
  boolean traverseChildScopes;

  void reset() {
    changed = false;
    traverseChildScopes = true;
  }
}