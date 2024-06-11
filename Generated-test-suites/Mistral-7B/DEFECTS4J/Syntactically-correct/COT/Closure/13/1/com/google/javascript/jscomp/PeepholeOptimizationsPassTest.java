package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;

public class PeepholeOptimizationsPassTest {

  @Test
  public void testPeepholeOptimizationsPassConstruction() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);
    assertNotNull(pass);
  }

  @Test
  public void testProcessNormalFlow() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    Node root = Node.objectLiteral();
    pass.process(Node.newTopLevelEnvironment(), root);
  }

  @Test(expected = NullPointerException.class)
  public void testProcessNullCompiler() {
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null, optimizations);
    Node root = Node.objectLiteral();
    pass.process(Node.newTopLevelEnvironment(), root);
  }

  @Test(expected = NullPointerException.class)
  public void testProcessNullOptimizations() {
    Compiler compiler = new Compiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, null);
    Node root = Node.objectLiteral();
    pass.process(Node.newTopLevelEnvironment(), root);
  }

  @Test(expected = NullPointerException.class)
  public void testProcessNullExterns() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);
    Node root = Node.objectLiteral();
    pass.process(null, root);
  }

  @Test(expected = NullPointerException.class)
  public void testProcessNullRoot() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);
    pass.process(Node.newTopLevelEnvironment(), null);
  }

  @Test
  public void testShouldVisitFunction() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    Node node = Node.function("test", new ArrayList<Node>());
    assertTrue(pass.shouldVisit(node));
  }

  @Test
  public void testShouldVisitScript() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    Node node = Node.script("test()");
    assertTrue(pass.shouldVisit(node));
  }

  @Test
  public void testShouldNotVisitNonFunctionNonScript() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    Node node = Node.numberLiteral(1);
    assertFalse(pass.shouldVisit(node));
  }

  @Test
  public void testShouldNotVisitTraversalChildScopesFalse() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    Node functionNode = Node.function("test", new ArrayList<Node>());
    pass.traversalState.push();
    pass.traversalState.peek().traverseChildScopes = false;

    Node nestedFunctionNode = Node.function("innerTest", new ArrayList<Node>());
    Node parentNode = Node.objectLiteral();
    parentNode.addChild(functionNode);
    parentNode.addChild(nestedFunctionNode);

    assertTrue(pass.shouldVisit(parentNode));
    assertFalse(pass.shouldVisit(nestedFunctionNode));
  }

  @Test
  public void testShouldRetraverseFunction() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    Node parentFunctionNode = Node.function("parentTest", new ArrayList<Node>());
    Node childFunctionNode = Node.function("childTest", new ArrayList<Node>());
    Node parentNode = Node.objectLiteral();
    parentNode.addChild(parentFunctionNode);
    parentNode.addChild(childFunctionNode);

    pass.traverse(parentNode);
    assertFalse(pass.shouldRetraverse(childFunctionNode));

    pass.traversalState.peek().changed = true;
    assertTrue(pass.shouldRetraverse(childFunctionNode));
  }

  @Test
  public void testShouldRetraverseScript() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    Node parentNode = Node.objectLiteral();
    Node scriptNode = Node.script("test()");
    parentNode.addChild(scriptNode);

    pass.traverse(parentNode);
    assertFalse(pass.shouldRetraverse(scriptNode));

    pass.traversalState.peek().changed = true;
    assertTrue(pass.shouldRetraverse(scriptNode));
  }

  @Test
  public void testVisitFunction() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    Node functionNode = Node.function("test", new ArrayList<Node>());
    Node currentVersionOfNode = functionNode;

    pass.visit(functionNode);
    assertNull(currentVersionOfNode);
  }

  @Test
  public void testVisitScript() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    Node scriptNode = Node.script("test()");
    Node currentVersionOfNode = scriptNode;

    pass.visit(scriptNode);
    assertNull(currentVersionOfNode);
  }

  @Test
  public void testBeginTraversal() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    pass.beginTraversal();
  }

  @Test
  public void testEndTraversal() {
    Compiler compiler = new Compiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    pass.endTraversal();
  }
}