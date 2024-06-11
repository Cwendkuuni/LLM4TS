package com.google.javascript.jscomp; // Make sure the test file is in the same package as the PeepholeOptimizationsPass class

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.NodeFactory;

public class PeepholeOptimizationsPassTest {
  // Mock Compiler instance
  private Compiler compiler;

  // Mock list of peephole optimizations
  private List<AbstractPeepholeOptimization> peepholeOptimizations;

  // Mock NodeFactory instance
  private NodeFactory nodeFactory;

  // PeepholeOptimizationsPass instance under test
  private PeepholeOptimizationsPass pass;

  @Before
  public void setUp() {
    compiler = Mockito.mock(Compiler.class);
    peepholeOptimizations = Arrays.asList(mock(AbstractPeepholeOptimization.class), mock(AbstractPeepholeOptimization.class));
    nodeFactory = Mockito.mock(NodeFactory.class);

    pass = new PeepholeOptimizationsPass(compiler, peepholeOptimizations.toArray(new AbstractPeepholeOptimization[0]));
  }

  @Test
  public void testGetCompiler() {
    assertEquals(compiler, pass.getCompiler());
  }

  @Test
  public void testProcess_NullExterns() {
    when(compiler.getFactory()).thenReturn(nodeFactory);

    pass.process(null, Mockito.mock(Node.class));

    verify(compiler).reportError(anyString());
  }

  @Test
  public void testProcess_NullRoot() {
    when(compiler.getFactory()).thenReturn(nodeFactory);

    pass.process(Mockito.mock(Node.class), null);

    verify(compiler).reportError(anyString());
  }

  @Test
  public void testProcess() {
    when(compiler.getFactory()).thenReturn(nodeFactory);

    Node root = nodeFactory.createScript("function foo() { return 42; }");

    pass.process(Mockito.mock(Node.class), root);

    // Verify that each optimization has been called
    for (AbstractPeepholeOptimization optimization : peepholeOptimizations) {
      verify(optimization).optimizeSubtree(root);
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_TraversalError() {
    when(compiler.getFactory()).thenReturn(nodeFactory);

    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);
    doThrow(new IllegalStateException("Traversal error")).when(optimization).optimizeSubtree(Mockito.any(Node.class));

    peepholeOptimizations.set(0, optimization);

    Node root = nodeFactory.createScript("function foo() { return 42; }");

    pass.process(Mockito.mock(Node.class), root);

    verify(compiler, times(2)).reportError(anyString());
  }

  @Test
  public void testShouldVisit_FunctionNode() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    ScopeState state = new ScopeState();
    StateStack traversalState = new StateStack();
    traversalState.push();
    traversalState.peek().traverseChildScopes = false;

    assertTrue(pass.shouldVisit(createNode("function foo() { return 42; }")));

    assertEquals(1, traversalState.states.size());
    assertEquals(state, traversalState.states.get(0));
    assertEquals(false, state.traverseChildScopes);

    traversalState.pop();
  }

  @Test
  public void testShouldVisit_FunctionNode_TraverseChildScopes() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    ScopeState state = new ScopeState();
    StateStack traversalState = new StateStack();
    traversalState.push();

    assertTrue(pass.shouldVisit(createNode("function foo() { return 42; }")));

    assertEquals(1, traversalState.states.size());
    assertEquals(state, traversalState.states.get(0));
    assertEquals(true, state.traverseChildScopes);

    traversalState.pop();
  }

  @Test
  public void testShouldVisit_ScriptNode() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    assertTrue(pass.shouldVisit(createNode("function foo() { return 42; }")));

    assertTrue(pass.shouldVisit(createNode("(function() { return 42; })()")));
  }

  @Test
  public void testShouldRetraverse_FunctionNode_Changed() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    ScopeState state = new ScopeState();
    StateStack traversalState = new StateStack();

    Node node = createNode("function foo() { return 42; }");
    pass.traverse(node);

    state.changed = true;

    assertTrue(pass.shouldRetraverse(node));

    assertEquals(1, traversalState.states.size());
    assertEquals(state, traversalState.states.get(0));
    assertEquals(false, state.traverseChildScopes);
  }

  @Test
  public void testShouldRetraverse_FunctionNode_Unchanged() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    ScopeState state = new ScopeState();
    StateStack traversalState = new StateStack();

    Node node = createNode("function foo() { return 42; }");
    pass.traverse(node);

    assertFalse(pass.shouldRetraverse(node));

    assertEquals(1, traversalState.states.size());
    assertEquals(state, traversalState.states.get(0));
    assertEquals(true, state.traverseChildScopes);
  }

  @Test
  public void testShouldRetraverse_ScriptNode() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    assertFalse(pass.shouldRetraverse(createNode("function foo() { return 42; }")));
  }

  @Test
  public void testExitNode() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    Node node = createNode("function foo() { return 42; }");
    pass.traverse(node);
    pass.exitNode(node);

    assertEquals(0, traversalState.states.size());
  }

  @Test
  public void testVisit_FunctionNode() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    Node newVersionOfNode = createNode("function foo() { return 43; }");

    when(optimization.optimizeSubtree(Mockito.any(Node.class))).thenReturn(newVersionOfNode);

    Node node = createNode("function foo() { return 42; }");

    pass.visit(node);

    assertEquals(newVersionOfNode, node);
  }

  @Test
  public void testVisit_ScriptNode() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    Node newVersionOfNode = createNode("function foo() { return 43; }");

    when(optimization.optimizeSubtree(Mockito.any(Node.class))).thenReturn(newVersionOfNode);

    Node node = createNode("function foo() { return 42; }");

    pass.visit(node);

    assertEquals(newVersionOfNode, node);
  }

  @Test
  public void testBeginTraversal() {
    for (AbstractPeepholeOptimization optimization : peepholeOptimizations) {
      pass.beginTraversal();
      verify(optimization).beginTraversal(compiler);
    }
  }

  @Test
  public void testEndTraversal() {
    for (AbstractPeepholeOptimization optimization : peepholeOptimizations) {
      pass.endTraversal();
      verify(optimization).endTraversal(compiler);
    }
  }

  private Node createNode(String script) {
    return nodeFactory.createScript(script);
  }
}