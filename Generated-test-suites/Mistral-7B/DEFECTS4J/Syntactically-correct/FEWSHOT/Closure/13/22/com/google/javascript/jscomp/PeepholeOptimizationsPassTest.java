package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeOperations;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

public class PeepholeOptimizationsPassTest {

  private AbstractCompiler compiler;
  private Node rootNode;
  private PeepholeOptimizationsPass peepholeOptimizationsPass;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    rootNode = NodeOperations.newNode("root");
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, new DummyPeepholeOptimization());
  }

  @Test
  public void testProcessWithoutChildren() {
    peepholeOptimizationsPass.process(null, rootNode);
    Mockito.verify(compiler, Mockito.never()).addChangeHandler(Mockito.any(CodeChangeHandler.class));
  }

  @Test
  public void testProcessWithChildren() {
    Node childNode = NodeOperations.newNode("child");
    rootNode.setFirstChild(childNode);
    when(compiler.addChangeHandler(Mockito.any(CodeChangeHandler.class))).thenReturn(true);

    peepholeOptimizationsPass.process(null, rootNode);

    ArgumentCaptor<CodeChangeHandler> changeHandlerCaptor = ArgumentCaptor.forClass(CodeChangeHandler.class);
    Mockito.verify(compiler).addChangeHandler(changeHandlerCaptor.capture());

    CodeChangeHandler changeHandler = changeHandlerCaptor.getValue();
    Mockito.verify(changeHandler).reportChange();
  }

  @Test
  public void testTraverseFunctionWithChildren() {
    Node childNode1 = NodeOperations.newNode("child1");
    Node childNode2 = NodeOperations.newNode("child2");
    rootNode.setFirstChild(NodeOperations.newNode("function"));
    ((Node) rootNode.getFirstChild()).setFirstChild(childNode1);
    childNode1.setFirstChild(childNode2);

    peepholeOptimizationsPass.traverse(rootNode);

    ScopeState state = peepholeOptimizationsPass.traversalState.peek();
    assertThat(state.changed).isFalse();
    assertThat(state.traverseChildScopes).isFalse();
  }

  @Test
  public void testTraverseFunctionWithoutChildren() {
    rootNode.setFirstChild(NodeOperations.newNode("function"));

    peepholeOptimizationsPass.traverse(rootNode);

    ScopeState state = peepholeOptimizationsPass.traversalState.peek();
    assertThat(state.changed).isFalse();
    assertThat(state.traverseChildScopes).isTrue();
  }

  @Test
  public void testTraverseScript() {
    rootNode.setIsScript(true);

    peepholeOptimizationsPass.traverse(rootNode);

    ScopeState state = peepholeOptimizationsPass.traversalState.peek();
    assertThat(state.changed).isFalse();
    assertThat(state.traverseChildScopes).isFalse();
  }

  @Test
  public void testTraverseRoot() {
    peepholeOptimizationsPass.traverse(null);

    ScopeState state = peepholeOptimizationsPass.traversalState.peek();
    assertThat(state.changed).isFalse();
    assertThat(state.traverseChildScopes).isFalse();
  }

  @Test
  public void testShouldVisitFunctionWithChildren() {
    Node childNode1 = NodeOperations.newNode("child1");
    Node childNode2 = NodeOperations.newNode("child2");
    rootNode.setFirstChild(NodeOperations.newNode("function"));
    ((Node) rootNode.getFirstChild()).setFirstChild(childNode1);
    childNode1.setFirstChild(childNode2);

    boolean result = peepholeOptimizationsPass.shouldVisit(rootNode);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldVisitFunctionWithoutChildren() {
    rootNode.setFirstChild(NodeOperations.newNode("function"));

    boolean result = peepholeOptimizationsPass.shouldVisit(rootNode);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldVisitScript() {
    rootNode.setIsScript(true);

    boolean result = peepholeOptimizationsPass.shouldVisit(rootNode);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldVisitRoot() {
    boolean result = peepholeOptimizationsPass.shouldVisit(null);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldRetransverseFunctionWithChangedChildren() {
    Node childNode1 = NodeOperations.newNode("child1");
    Node childNode2 = NodeOperations.newNode("child2");
    rootNode.setFirstChild(NodeOperations.newNode("function"));
    ((Node) rootNode.getFirstChild()).setFirstChild(childNode1);
    childNode1.setFirstChild(childNode2);

    ScopeState state = peepholeOptimizationsPass.traversalState.peek();
    state.changed = true;

    boolean result = peepholeOptimizationsPass.shouldRetransverse(rootNode);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldRetransverseFunctionWithoutChangedChildren() {
    Node childNode1 = NodeOperations.newNode("child1");
    Node childNode2 = NodeOperations.newNode("child2");
    rootNode.setFirstChild(NodeOperations.newNode("function"));
    ((Node) rootNode.getFirstChild()).setFirstChild(childNode1);
    childNode1.setFirstChild(childNode2);

    boolean result = peepholeOptimizationsPass.shouldRetransverse(rootNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldRetransverseScript() {
    rootNode.setIsScript(true);

    boolean result = peepholeOptimizationsPass.shouldRetransverse(rootNode);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldRetransverseRoot() {
    boolean result = peepholeOptimizationsPass.shouldRetransverse(null);

    assertThat(result).isFalse();
  }

  @Test
  public void testVisitFunctionWithChildren() {
    Node childNode1 = NodeOperations.newNode("child1");
    Node childNode2 = NodeOperations.newNode("child2");
    rootNode.setFirstChild(NodeOperations.newNode("function"));
    ((Node) rootNode.getFirstChild()).setFirstChild(childNode1);
    childNode1.setFirstChild(childNode2);

    peepholeOptimizationsPass.visit(rootNode);
  }

  @Test
  public void testVisitFunctionWithoutChildren() {
    rootNode.setFirstChild(NodeOperations.newNode("function"));

    peepholeOptimizationsPass.visit(rootNode);
  }

  @Test
  public void testVisitScript() {
    rootNode.setIsScript(true);

    peepholeOptimizationsPass.visit(rootNode);
  }

  @Test
  public void testVisitRoot() {
    peepholeOptimizationsPass.visit(null);
  }

  @Test
  public void testBeginTraversal() {
    when(compiler.addChangeHandler(Mockito.any(CodeChangeHandler.class))).thenReturn(true);

    peepholeOptimizationsPass.beginTraversal();

    for (AbstractPeepholeOptimization optimization : peepholeOptimizationsPass.peepholeOptimizations) {
      Mockito.verify(optimization).beginTraversal(compiler);
    }
  }

  @Test
  public void testEndTraversal() {
    for (AbstractPeepholeOptimization optimization : peepholeOptimizationsPass.peepholeOptimizations) {
      Mockito.verify(optimization).endTraversal(compiler);
    }

    peepholeOptimizationsPass.endTraversal();
  }

  static class DummyPeepholeOptimization
      implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node subtree) {
      return subtree;
    }

    @Override
    public void beginTraversal(Compiler compiler) {
    }

    @Override
    public void endTraversal(Compiler compiler) {
    }
  }
}