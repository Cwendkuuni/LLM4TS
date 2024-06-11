package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PeepholeOptimizationsPassTest {
  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setOptions(new CompilerOptions());
    nodeFactory = compiler.getFactory();
  }

  
  @Test
  public void testPeepholeOptimizationsPassCreation() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    assertNotNull(pass);
  }

  @Test(expected = NullPointerException.class)
  public void testPeepholeOptimizationsPassNullCompiler() {
    new PeepholeOptimizationsPass(null, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
  }

  @Test(expected = NullPointerException.class)
  public void testPeepholeOptimizationsPassNullOptimizations() {
    new PeepholeOptimizationsPass(compiler, null, new NoOpPeepholeOptimization());
  }

  @Test
  public void testProcessNullExterns() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    pass.process(null, nodeFactory.parse("function(){}"));
  }

  @Test
  public void testProcessEmptyAST() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node emptyAST = nodeFactory.parse("{}");
    pass.process(nodeFactory.getExterns(), emptyAST);
  }

  @Test
  public void testProcessValidAST() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node root = nodeFactory.parse("function f() { return 1 + 2; }");
    pass.process(nodeFactory.getExterns(), root);
  }

  @Test
  public void testTraverseNullNode() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    pass.traverse(null);
  }

  @Test
  public void testShouldVisitSimpleNode() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node node = nodeFactory.parse("1");
    assertTrue(pass.shouldVisit(node));
  }

  @Test
  public void testShouldVisitFunctionNode() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node node = nodeFactory.parse("function f() {}");
    assertTrue(pass.shouldVisit(node));
  }

  @Test
  public void testShouldVisitScriptNode() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node node = nodeFactory.parse("function f() {}");
    node.setScript(true);
    assertTrue(pass.shouldVisit(node));
  }

  @Test
  public void testShouldNotVisitNullNodeParent() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node node = nodeFactory.parse("function f() {}");
    node.setParent(null);
    assertFalse(pass.shouldVisit(node));
  }

  @Test
  public void testShouldNotVisitFunctionWithDisabledTraversalChildScopes() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node root = nodeFactory.parse("function f() { g(); } function g() {}");
    pass.traverse(root);
    ScopeState state = pass.traversalState.peek();
    state.traverseChildScopes = false;
    pass.traverse(root);
    assertFalse(pass.traversalState.peek().traverseChildScopes);
  }

  @Test
  public void testShouldVisitFunctionWithEnabledTraversalChildScopes() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node root = nodeFactory.parse("function f() { g(); } function g() {}");
    pass.traverse(root);
    ScopeState state = pass.traversalState.peek();
    assertFalse(state.changed);
    state.traverseChildScopes = false;
    pass.traverse(root);
    assertTrue(state.changed);
  }

  @Test
  public void testVisitSimpleNode() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node currentVersionOfNode = nodeFactory.parse("1");
    Node newVersionOfNode = nodeFactory.parse("2");
    pass.visit(currentVersionOfNode);
    assertNull(currentVersionOfNode.getParent());
    assertNull(currentVersionOfNode);
    assertNotNull(newVersionOfNode.getParent());
    assertNotNull(newVersionOfNode);
  }

  @Test
  public void testVisitFunctionNodeWithNoOptimizations() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node root = nodeFactory.parse("function f() { return 1 + 2; }");
    pass.visit(root);
    Node expected = nodeFactory.parse("function f() { return 3; }");
    assertNotNull(root);
    assertNotNull(root.getParent());
    assertFalse(root.equals(expected));
    assertFalse(root.isFunction());
  }

  @Test
  public void testVisitFunctionNodeWithConstantFoldingOptimization() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFoldingPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node root = nodeFactory.parse("function f() { return 1 + 2; }");
    pass.visit(root);
    Node expected = nodeFactory.parse("function f() { return 3; }");
    assertNotNull(root);
    assertNotNull(root.getParent());
    assertFalse(root.equals(expected));
    assertFalse(root.isFunction());
  }

  @Test(expected = RuntimeException.class)
  public void testVisitFunctionNodeWithInfiniteRecursionOptimization() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new InfiniteRecursionPeepholeOptimization(), new NoOpPeepholeOptimization());
    Node root = nodeFactory.parse("function f() { return f(); }");
    pass.visit(root);
  }

  @Test
  public void testBeginTraversal() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    pass.beginTraversal();
  }

  @Test
  public void testEndTraversal() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(), new NoOpPeepholeOptimization());
    pass.endTraversal();
  }

  

  private static class NoOpPeepholeOptimization implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node node) {
      return node;
    }

    @Override
    public void beginTraversal(Compiler compiler) {

    }

    @Override
    public void endTraversal(Compiler compiler) {

    }
  }

  private static class ConstantFoldingPeepholeOptimization implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node node) {
      if (node.isNumber() && node.isConstant()) {
        Node constant = nodeFactory.makeNumber(node.getNumber().intValue() + 1);
        return constant;
      }
      return node;
    }

    @Override
    public void beginTraversal(Compiler compiler) {

    }

    @Override
    public void endTraversal(Compiler compiler) {

    }
  }

  private static class InfiniteRecursionPeepholeOptimization implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node node) {
      if (node.isCallExpression() && node.getCallTarget().getName().equals("f")) {
        throw new RuntimeException("Infinite recursion detected.");
      }
      return node;
    }

    @Override
    public void beginTraversal(Compiler compiler) {

    }

    @Override
    public void endTraversal(Compiler compiler) {

    }
  }
}