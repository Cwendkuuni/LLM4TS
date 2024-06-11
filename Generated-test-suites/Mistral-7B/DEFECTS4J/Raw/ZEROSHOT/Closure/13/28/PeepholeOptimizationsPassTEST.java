package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.NodeOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for PeepholeOptimizationsPass class.
 */
public class PeepholeOptimizationsPassTest {
  private AbstractCompiler compiler;
  private AbstractPeepholeOptimization optimization1;
  private AbstractPeepholeOptimization optimization2;

  @org.junit.Before
  public void setUp() {
    compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    optimization1 = new DummyPeepholeOptimization();
    optimization2 = new DummyPeepholeOptimization();
    PeepholeOptimizationsPass pass =
        new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
  }

  @org.junit.Test
  public void testEmptyAST() {
    Node root = Node.NULL();
    pass.process(NodeOperations.createExterns(), root);
    assertTrue(root.isNull());
  }

  @org.junit.Test
  public void testNullNode() {
    Node node = Node.NULL();
    pass.traverse(node);
    assertTrue(node.isNull());
  }

  @org.junit.Test
  public void testShouldVisitFunction() {
    Node functionNode = NodeOperations.createFunction("test", new ArrayList<>(), NodeOperations.createBlock(new Node[0]));
    pass.traverse(functionNode);
    assertTrue(functionNode.isFunction());
  }

  @org.junit.Test
  public void testShouldNotVisitNonFunctionNode() {
    Node nonFunctionNode = NodeOperations.createNumber(1);
    pass.traverse(nonFunctionNode);
    assertFalse(nonFunctionNode.isFunction());
  }

  @org.junit.Test
  public void testShouldVisitScript() {
    Node scriptNode = NodeOperations.createScript("test()");
    pass.traverse(scriptNode);
    assertTrue(scriptNode.isScript());
  }

  @org.junit.Test
  public void testShouldNotVisitNonScriptNode() {
    Node nonScriptNode = NodeOperations.createNumber(1);
    pass.traverse(nonScriptNode);
    assertFalse(nonScriptNode.isScript());
  }

  @org.junit.Test
  public void testShouldRetransverseFunction() {
    Node functionNode = NodeOperations.createFunction("test", new ArrayList<>(), NodeOperations.createBlock(new Node[0]));
    pass.traverse(functionNode);
    ScopeState state = pass.traversalState.peek();
    assertTrue(state.traverseChildScopes);
  }

  @org.junit.Test
  public void testShouldNotRetransverseNonFunctionNode() {
    Node nonFunctionNode = NodeOperations.createNumber(1);
    pass.traverse(nonFunctionNode);
    ScopeState state = pass.traversalState.peek();
    assertFalse(state.traverseChildScopes);
  }

  @org.junit.Test
  public void testShouldChangeNodeWithOptimization1() {
    Node originalNode = NodeOperations.createNumber(1);
    Node optimizedNode = optimization1.optimizeSubtree(originalNode);
    assertFalse(originalNode.equals(optimizedNode));
  }

  @org.junit.Test
  public void testShouldChangeNodeWithOptimization2() {
    Node originalNode = NodeOperations.createNumber(1);
    Node optimizedNode = optimization2.optimizeSubtree(originalNode);
    assertFalse(originalNode.equals(optimizedNode));
  }

  @org.junit.Test
  public void testShouldChangeNodeWithBothOptimization() {
    Node originalNode = NodeOperations.createNumber(1);
    Node optimizedNode = optimization1.optimizeSubtree(optimization2.optimizeSubtree(originalNode));
    assertFalse(originalNode.equals(optimizedNode));
  }

  @org.junit.Test
  public void testShouldNotChangeNodeWithBothOptimization() {
    Node originalNode = NodeOperations.createNumber(1);
    Node optimizedNode1 = optimization1.optimizeSubtree(originalNode);
    Node optimizedNode2 = optimization2.optimizeSubtree(originalNode);
    assertTrue(optimizedNode1.equals(optimizedNode2));
  }

  @org.junit.Test
  public void testShouldReportChangeWithOptimization1() {
    Node originalNode = NodeOperations.createNumber(1);
    PeepholeChangeHandler handler = new PeepholeChangeHandler();
    compiler.addChangeHandler(handler);
    optimization1.optimizeSubtree(originalNode);
    compiler.removeChangeHandler(handler);
    assertTrue(handler.hasReportedChange());
  }

  @org.junit.Test
  public void testShouldNotReportChangeWithOptimization1Alone() {
    Node originalNode = NodeOperations.createNumber(1);
    PeepholeChangeHandler handler = new PeepholeChangeHandler();
    compiler.addChangeHandler(handler);
    optimization1.optimizeSubtree(originalNode);
    optimization1.optimizeSubtree(originalNode);
    compiler.removeChangeHandler(handler);
    assertFalse(handler.hasReportedChange());
  }

  @org.junit.Test
  public void testShouldReportChangeWithOptimization2Alone() {
    Node originalNode = NodeOperations.createNumber(1);
    PeepholeChangeHandler handler = new PeepholeChangeHandler();
    compiler.addChangeHandler(handler);
    optimization2.optimizeSubtree(originalNode);
    compiler.removeChangeHandler(handler);
    assertTrue(handler.hasReportedChange());
  }

  @org.junit.Test
  public void testShouldReportChangeWithBothOptimization() {
    Node originalNode = NodeOperations.createNumber(1);
    PeepholeChangeHandler handler = new PeepholeChangeHandler();
    compiler.addChangeHandler(handler);
    optimization1.optimizeSubtree(optimization2.optimizeSubtree(originalNode));
    compiler.removeChangeHandler(handler);
    assertTrue(handler.hasReportedChange());
  }

  @org.junit.Test
  public void testShouldNotReportChangeWithBothOptimizationAlone() {
    Node originalNode = NodeOperations.createNumber(1);
    PeepholeChangeHandler handler = new PeepholeChangeHandler();
    compiler.addChangeHandler(handler);
    optimization1.optimizeSubtree(originalNode);
    optimization2.optimizeSubtree(originalNode);
    compiler.removeChangeHandler(handler);
    assertFalse(handler.hasReportedChange());
  }

  @org.junit.Test
  public void testShouldNotVisitTwiceFunction() {
    Node functionNode = NodeOperations.createFunction("test", new ArrayList<>(), NodeOperations.createBlock(new Node[0]));
    pass.traverse(functionNode);
    pass.traverse(functionNode);
    ScopeState state = pass.traversalState.peek();
    assertTrue(state.changed);
    assertFalse(state.traverseChildScopes);
  }

  @org.junit.Test
  public void testShouldNotVisitTwiceNonFunctionNode() {
    Node nonFunctionNode = NodeOperations.createNumber(1);
    pass.traverse(nonFunctionNode);
    pass.traverse(nonFunctionNode);
    ScopeState state = pass.traversalState.peek();
    assertFalse(state.changed);
    assertFalse(state.traverseChildScopes);
  }

  @org.junit.Test
  public void testShouldVisitTwiceFunctionAndChange() {
    Node functionNode = NodeOperations.createFunction("test", new ArrayList<>(), NodeOperations.createBlock(new Node[0]));
    Node optimizedNode = optimization1.optimizeSubtree(functionNode);
    pass.traverse(functionNode);
    pass.traverse(functionNode);
    ScopeState state = pass.traversalState.peek();
    assertTrue(state.changed);
    assertFalse(state.traverseChildScopes);
    assertTrue(functionNode.equals(optimizedNode));
  }

  @org.junit.Test
  public void testShouldVisitTwiceNonFunctionNodeAndChange() {
    Node nonFunctionNode = NodeOperations.createNumber(1);
    Node optimizedNode = optimization1.optimizeSubtree(nonFunctionNode);
    pass.traverse(nonFunctionNode);
    pass.traverse(nonFunctionNode);
    ScopeState state = pass.traversalState.peek();
    assertFalse(state.changed);
    assertFalse(state.traverseChildScopes);
    assertTrue(nonFunctionNode.equals(optimizedNode));
  }

  private static class DummyPeepholeOptimization
      implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node node) {
      return NodeOperations.createNumber(2);
    }

    @Override
    public void beginTraversal(AbstractCompiler compiler) {
    }

    @Override
    public void endTraversal(AbstractCompiler compiler) {
    }
  }

  private static class PeepholeChangeHandler implements CodeChangeHandler {
    private boolean hasReportedChange = false;

    @Override
    public void reportChange() {
      hasReportedChange = true;
    }

    public boolean hasReportedChange() {
      return hasReportedChange;
    }

    public void reset() {
      hasReportedChange = false;
    }
  }
}