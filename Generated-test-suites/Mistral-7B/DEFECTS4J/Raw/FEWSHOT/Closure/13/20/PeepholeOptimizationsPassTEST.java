package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassTest;
import com.google.javascript.rhino.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for PeepholeOptimizationsPass.
 */
public class PeepholeOptimizationsPassTest extends CompilerPassTest {

  @Override
  protected CompilerPass createPass() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);
    return new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
  }

  @Test
  public void testProcess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

    Node root = createNode("root");
    pass.process(null, root);

    // This method should not have any side effects.
    // No changes should have been made to the node.
    assertNodeEquals("root", root);
  }

  @Test
  public void testTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

    Node root = createNode("root");
    Node child1 = createNode("child1");
    Node child2 = createNode("child2");

    root.addChild(child1);
    root.addChild(child2);

    when(optimization1.optimizeSubtree(anyNode())).thenReturn(null);
    when(optimization2.optimizeSubtree(anyNode())).thenReturn(createNode("optimizedNode"));

    pass.traverse(root);

    // The traversal should have reached the deepest node in the subtree.
    Node optimizedNode = findNodeWithValue("optimizedNode", root);
    assertNotNull(optimizedNode);
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

    Node root = createNode("root");
    Node child1 = createNode("child1");
    Node child2 = createNode("child2");

    root.addChild(child1);
    root.addChild(child2);

    Node originalRoot = root;

    when(optimization1.optimizeSubtree(anyNode())).thenReturn(null);
    when(optimization2.optimizeSubtree(eq(child1))).thenReturn(createNode("optimizedChild1"));
    when(optimization2.optimizeSubtree(eq(child2))).thenReturn(createNode("optimizedChild2"));

    pass.visit(root);

    // Only optimization2 was called, so only its optimization should have been applied.
    Node optimizedChild1 = findNodeWithValue("optimizedChild1", root);
    Node optimizedChild2 = findNodeWithValue("optimizedChild2", root);

    assertNotNull(optimizedChild1);
    assertNotNull(optimizedChild2);
    assertNodeEquals("root", originalRoot);
    assertNodeEquals("root", root);
  }

  @Test
  public void testBeginAndEndTraversal() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

    when(optimization1.beginTraversal(any())).thenReturn(voidValue());
    when(optimization1.endTraversal(any())).thenReturn(voidValue());
    when(optimization2.beginTraversal(any())).thenReturn(voidValue());
    when(optimization2.endTraversal(any())).thenReturn(voidValue());

    pass.beginTraversal();
    pass.endTraversal();
  }

  @Test
  public void testShouldVisitFunctionOrScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

    Node functionNode = createNode("function() {}");
    Node scriptNode = createNode("function() {}");

    assertThat(pass.shouldVisit(functionNode)).isTrue();
    assertThat(pass.shouldVisit(scriptNode)).isTrue();
  }

  @Test
  public void testShouldNotVisitNonFunctionOrScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

    Node numberNode = createNode("1");

    assertThat(pass.shouldVisit(numberNode)).isFalse();
  }

  @Test
  public void testShouldRetraverseFunctionOrScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

    Node functionNode = createNode("function() {}");
    Node scriptNode = createNode("function() {}");
    Node childFunctionNode = createNode("function() {}");

    when(optimization.shouldRetraverse(functionNode)).thenReturn(true);
    when(optimization.shouldRetraverse(scriptNode)).thenReturn(true);
    when(optimization.shouldRetraverse(childFunctionNode)).thenReturn(true);

    pass.traverse(functionNode);
    pass.traverse(scriptNode);
    pass.traverse(childFunctionNode);

    ScopeState state = pass.traversalState.peek();
    assertThat(state.changed).isFalse();
    assertThat(state.traverseChildScopes).isFalse();
  }

  @Test
  public void testShouldNotRetraverseNonFunctionOrScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

    Node numberNode = createNode("1");

    assertThat(pass.shouldRetraverse(numberNode)).isFalse();
  }
}