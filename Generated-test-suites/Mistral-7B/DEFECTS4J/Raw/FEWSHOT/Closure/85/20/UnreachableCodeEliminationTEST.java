package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.NodeUtil.isTryCatchNodeContainer;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.JSTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;

public class UnreachableCodeEliminationTest extends JSTestCase {
  private AbstractCompiler compiler;

  @Override
  public void setUp() {
    compiler = new Compiler();
  }

  @Test
  public void testEmptyBlock() {
    Node root = parseJs("function foo() {}");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testReturn() {
    Node root = parseJs("function foo() { return; }");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testBreak() {
    Node root = parseJs("function foo() { for (var i = 0; i < 10; i++) { break; } }");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testContinue() {
    Node root = parseJs("function foo() { for (var i = 0; i < 10; i++) { continue; } }");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testSimpleBranching() {
    Node root = parseJs("function foo() { if (1) { return; } else { return; } }");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testComplexBranching() {
    Node root = parseJs("function foo() {" +
        "if (Math.random() < 0.5) {" +
        "return;" +
        "} else {" +
        "if (Math.random() < 0.5) {" +
        "return;" +
        "} else {" +
        "return;" +
        "}" +
        "}");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testNoOpStatements() {
    Node root = parseJs("function foo() {" +
        "var x = 1 + 1;" +
        "return;" +
        "Math.pow(Math.PI, 2);" +
        "}" +
        "");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, root);
    assertEquals(2, root.getChildCount());
    assertEquals("Math.pow(Math.PI, 2)", root.getChild(1).toString());
  }

  @Test
  public void testTryCatchBlock() {
    Node root = parseJs("function foo() {" +
        "try {" +
        "throw new Error('test');" +
        "} catch (e) {" +
        "return;" +
        "}" +
        "}" +
        "");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testTryCatchBlockWithFinally() {
    Node root = parseJs("function foo() {" +
        "try {" +
        "throw new Error('test');" +
        "} catch (e) {" +
        "return;" +
        "}" +
        "finally {" +
        "return;" +
        "}" +
        "}" +
        "");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testDoWhileLoop() {
    Node root = parseJs("function foo() {" +
        "do {" +
        "if (false)" +
        "} while (false);" +
        "}" +
        "");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testComplexControlFlow() {
    Node root = parseJs("function foo() {" +
        "if (Math.random() < 0.5) {" +
        "if (Math.random() < 0.5) {" +
        "return;" +
        "} else {" +
        "if (Math.random() < 0.5) {" +
        "return;" +
        "} else {" +
        "return;" +
        "}" +
        "} else {" +
        "if (Math.random() < 0.5) {" +
        "return;" +
        "} else {" +
        "if (Math.random() < 0.5) {" +
        "return;" +
        "} else {" +
        "return;" +
        "}" +
        "}" +
        "}" +
        "");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testComplexControlFlowWithBreak() {
    Node root = parseJs("function foo() {" +
        "if (Math.random() < 0.5) {" +
        "for (var i = 0; i < 10; i++) {" +
        "if (Math.random() < 0.5) {" +
        "break;" +
        "}" +
        "}" +
        "}" +
        "");
    ControlFlowGraph<Node> cfg = new ControlFlowAnalysis(compiler, false, false)
        .process(null, root).getCfg();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);
    assertNull(root.getFirstChild());
  }

  private Node parseJs(String code) {
    return compiler.parseTree(code, null, null, null);
  }
}