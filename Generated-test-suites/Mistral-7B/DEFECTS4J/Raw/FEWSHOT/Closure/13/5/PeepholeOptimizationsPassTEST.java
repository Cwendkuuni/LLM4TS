package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.ScopeState;
import com.google.javascript.rhino.NodeOperations;

/**
 * Test cases for PeepholeOptimizationsPass class
 */
public class PeepholeOptimizationsPassTest {
  private AbstractCompiler compiler;
  private AbstractPeepholeOptimization optim1;
  private AbstractPeepholeOptimization optim2;
  private PeepholeOptimizationsPass pass;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    optim1 = mock(AbstractPeepholeOptimization.class);
    optim2 = mock(AbstractPeepholeOptimization.class);

    when(optim1.getClass()).thenReturn(optim1.getClass());
    when(optim2.getClass()).thenReturn(optim2.getClass());

    List<AbstractPeepholeOptimization> optimizations = new ArrayList<>();
    optimizations.add(optim1);
    optimizations.add(optim2);

    pass = new PeepholeOptimizationsPass(compiler, optim1, optim2);
  }

  @Test
  public void testProcess() {
    Node root = Node.root();
    pass.process(NodeOperations.EMPTY_NODE, root);
  }

  @Test
  public void testShouldVisitFunction() {
    Node functionNode = mock(Node.class);
    when(functionNode.isFunction()).thenReturn(true);

    ScopeState state = pass.traversalState.peek();

    assertTrue(pass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldVisitScript() {
    Node scriptNode = mock(Node.class);
    when(scriptNode.isScript()).thenReturn(true);

    ScopeState state = pass.traversalState.peek();

    assertTrue(pass.shouldVisit(scriptNode));
  }

  @Test
  public void testShouldNotVisitNonFunctionNonScript() {
    Node nonFunctionNonScriptNode = mock(Node.class);
    when(nonFunctionNonScriptNode.isFunction()).thenReturn(false);
    when(nonFunctionNonScriptNode.isScript()).thenReturn(false);

    assertFalse(pass.shouldVisit(nonFunctionNonScriptNode));
  }

  @Test
  public void testShouldRetransverseFunctionWithChildNodes() {
    Node functionNode = mock(Node.class);
    when(functionNode.isFunction()).thenReturn(true);

    ScopeState state = pass.traversalState.peek();

    pass.traversalState.push();
    assertTrue(pass.shouldRetransverse(functionNode));

    pass.traversalState.pop();
  }

  @Test
  public void testShouldNotRetransverseFunctionWithoutChildNodes() {
    Node functionNode = mock(Node.class);
    when(functionNode.isFunction()).thenReturn(true);

    ScopeState state = pass.traversalState.peek();

    assertFalse(pass.shouldRetransverse(functionNode));
  }

  @Test
  public void testShouldRetransverseScriptWithChildNodes() {
    Node scriptNode = mock(Node.class);
    when(scriptNode.isScript()).thenReturn(true);

    ScopeState state = pass.traversalState.peek();

    pass.traversalState.push();
    assertTrue(pass.shouldRetransverse(scriptNode));

    pass.traversalState.pop();
  }

  @Test
  public void testShouldNotRetransverseScriptWithoutChildNodes() {
    Node scriptNode = mock(Node.class);
    when(scriptNode.isScript()).thenReturn(true);

    ScopeState state = pass.traversalState.peek();

    assertFalse(pass.shouldRetransverse(scriptNode));
  }

  @Test
  public void testShouldRetransverseScopeThatHasBeenChanged() {
    Node functionNode = mock(Node.class);
    when(functionNode.isFunction()).thenReturn(true);

    ScopeState previous = new ScopeState();
    pass.traversalState.push();
    pass.traversalState.peek().changed = true;

    assertTrue(pass.shouldRetransverse(functionNode));
  }

  @Test
  public void testShouldNotRetransverseScopeThatHasNotBeenChanged() {
    Node functionNode = mock(Node.class);
    when(functionNode.isFunction()).thenReturn(true);

    ScopeState previous = new ScopeState();
    pass.traversalState.push();

    assertFalse(pass.shouldRetransverse(functionNode));
  }

  @Test
  public void testVisitNodeWithNoOptimization() {
    Node node = NodeOperations.parseTree("function foo() {}");
    pass.visit(node);
  }

  @Test
  public void testVisitNodeWithOptimization() {
    Node node = NodeOperations.parseTree("function foo() { return 42; }");
    pass.visit(node);
  }

  @Test
  public void testBeginTraversal() {
    for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
      optimization.beginTraversal(compiler);
    }
  }

  @Test
  public void testEndTraversal() {
    for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
      optimization.endTraversal(compiler);
    }
  }
}

class ScopeState {
  boolean changed = false;
  boolean traverseChildScopes = true;
}

class StateStack {
  private ArrayList<ScopeState> states = Lists.newArrayList();
  private int currentDepth = 0;

  StateStack() {
    states.add(new ScopeState());
  }

  ScopeState peek() {
    return states.get(currentDepth);
  }

  void push() {
    currentDepth++;
    if (states.size() <= currentDepth) {
      states.add(new ScopeState());
    } else {
      states.get(currentDepth).reset();
    }
  }

  void pop() {
    currentDepth--;
  }
}

class PeepholeChangeHandler implements CodeChangeHandler {
  @Override
  public void reportChange() {
    traversalState.peek().changed = true;
  }
}