package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for the PeepholeOptimizationsPass class.
 */
public class PeepholeOptimizationsPassTest {

  private AbstractCompiler compiler;
  private PeepholeOptimizationsPass pass;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);

    AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);

    pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
  }

  @Test
  public void testProcess() {
    Node root = Node.newObject();

    pass.process(Node.newObject(), root);
  }

  @Test
  public void testShouldVisitFunctionNode() {
    Node functionNode = Node.newFunction("func", new ArrayList<Node>(), Node.newObject(), Node.newObject(), null);

    assertTrue(pass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldVisitScriptNode() {
    Node scriptNode = Node.newScript("script");

    assertTrue(pass.shouldVisit(scriptNode));
  }

  @Test
  public void testShouldNotVisitNonFunctionNonScriptNode() {
    Node node = Node.newNumber(1);

    assertFalse(pass.shouldVisit(node));
  }

  @Test
  public void testShouldNotVisitFunctionWithPreviousTraverseChildScopesFalse() {
    Node functionNode = Node.newFunction("func", new ArrayList<Node>(), Node.newObject(), Node.newObject(), null);

    ScopeState previous = new ScopeState();
    previous.traverseChildScopes = false;

    traversalState = new StateStack();
    traversalState.peek = () -> previous;

    assertFalse(pass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldVisitFunctionWithPreviousTraverseChildScopesTrue() {
    Node functionNode = Node.newFunction("func", new ArrayList<Node>(), Node.newObject(), Node.newObject(), null);

    ScopeState previous = new ScopeState();
    previous.traverseChildScopes = true;

    traversalState = new StateStack();
    traversalState.peek = () -> previous;

    assertTrue(pass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldRetraverseFunctionWithChangedParent() {
    Node functionNode = Node.newFunction("func", new ArrayList<Node>(), Node.newObject(), Node.newObject(), null);
    Node parentNode = Node.newObject();

    functionNode.setParent(parentNode);

    ScopeState state = new ScopeState();
    state.changed = true;

    traversalState = new StateStack();
    traversalState.peek = () -> state;

    assertTrue(pass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldNotRetraverseFunctionWithUnchangedParent() {
    Node functionNode = Node.newFunction("func", new ArrayList<Node>(), Node.newObject(), Node.newObject(), null);
    Node parentNode = Node.newObject();

    functionNode.setParent(parentNode);

    ScopeState state = new ScopeState();

    traversalState = new StateStack();
    traversalState.peek = () -> state;

    assertFalse(pass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldNotRetraverseNonFunctionNode() {
    Node node = Node.newNumber(1);

    ScopeState state = new ScopeState();
    state.changed = true;

    traversalState = new StateStack();
    traversalState.peek = () -> state;

    assertFalse(pass.shouldRetraverse(node));
  }

  @Test
  public void testVisit() {
    Node node = Node.newObject();

    pass.visit(node);
  }

  @Test
  public void testVisitWithOptimizationChange() {
    Node node = Node.newObject();
    Node newNode = Node.newNumber(42);

    ArgumentCaptor<Node> captor = ArgumentCaptor.forClass(Node.class);

    when(pass.peepholeOptimizations[0].optimizeSubtree(node)).thenReturn(newNode);

    pass.visit(node);

    assertTrue(pass.compiler.hasChange());
  }

  @Test
  public void testVisitWithoutOptimizationChange() {
    Node node = Node.newObject();

    pass.visit(node);

    assertFalse(pass.compiler.hasChange());
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