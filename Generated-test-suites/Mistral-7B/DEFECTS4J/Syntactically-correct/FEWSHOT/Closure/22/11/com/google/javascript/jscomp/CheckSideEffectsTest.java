package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {
  private AbstractCompiler compiler;
  private CheckSideEffects checker;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @Test
  public void testNoSideEffects() {
    Node root = createNode("return 1;");
    NodeTraversal.traverse(compiler, root, checker);
    assertFalse(checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.RETURN, problemNode.getType());
  }

  @Test
  public void testEmpty() {
    Node root = createNode("");
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVoidStatement() {
    Node root = createNode(";");
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testCommaStatement() {
    Node root = createNode("x, y;");
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testStatementWithSideEffect() {
    Node root = createNode("document.write('Hello, world!');");
    NodeTraversal.traverse(compiler, root, checker);
    assertFalse(checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());
    assertNull(NodeUtil.getFirstChild(problemNode).getName());
  }

  @Test
  public void testStatementWithSideEffectAndProtectSideEffectFreeCode() {
    CheckSideEffects protectSideEffectsChecker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = createNode("document.write('Hello, world!');");
    NodeTraversal.traverse(compiler, root, protectSideEffectsChecker);
    assertTrue(protectSideEffectsChecker.problemNodes.isEmpty());
  }

  @Test
  public void testSimpleOpStatement() {
    Node root = createNode("x + y;");
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testSimpleOpStatementWithSideEffect() {
    Node root = createNode("x + document.write('Hello, world!');");
    NodeTraversal.traverse(compiler, root, checker);
    assertFalse(checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());
    assertNull(NodeUtil.getFirstChild(problemNode).getName());
  }

  @Test
  public void testStatementWithMultipleSideEffects() {
    Node root = createNode("document.write('Hello, world!'); document.write('Hi!');");
    NodeTraversal.traverse(compiler, root, checker);
    assertFalse(checker.problemNodes.isEmpty());
    assertEquals(2, checker.problemNodes.size());
  }

  @Test
  public void testStatementWithMultipleSideEffectsAndProtectSideEffectFreeCode() {
    CheckSideEffects protectSideEffectsChecker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = createNode("document.write('Hello, world!'); document.write('Hi!');");
    NodeTraversal.traverse(compiler, root, protectSideEffectsChecker);
    assertTrue(protectSideEffectsChecker.problemNodes.isEmpty());
  }

  private Node createNode(String code) {
    CheckerFactory checkerFactory = new CheckerFactory();
    CompilerInput input = new CompilerInput(code, null, null, null);
    Node astRoot = new Compiler().getAST(input, null, null, null, checkerFactory);
    return astRoot;
  }
}