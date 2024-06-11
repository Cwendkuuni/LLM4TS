package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test.testType.TestUnreachableCodeEliminationRoot;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {
  private static final Logger logger = Logger.getLogger(
      UnreachableCodeEliminationTest.class.getName());
  private AbstractCompiler compiler;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
  }

  @org.junit.Test
  public void testRemoveUnreachableCodeAfterReturn() {
    TestUnreachableCodeEliminationRoot root = new TestUnreachableCodeEliminationRoot();
    compiler.process(root);
    NodeTraversal.traverse(compiler, root);
    Node returnStatement = root.getFirstChild().getLastChild();
    Node followingStatement = returnStatement.getNext();
    assertTrue(followingStatement == null);
  }

  @org.junit.Test
  public void testRemoveDeadCodeWithNoSideEffects() {
    TestUnreachableCodeEliminationRoot root = new TestUnreachableCodeEliminationRoot();
    compiler.process(root);
    NodeTraversal.traverse(compiler, root);
    Node deadCode = root.getChildNodes().get(1);
    assertNull(deadCode.getNext());
  }

  @org.junit.Test
  public void testRemoveDeadCodeWithSideEffects() {
    TestUnreachableCodeEliminationRoot root = new TestUnreachableCodeEliminationRoot();
    compiler.process(root);
    NodeTraversal.traverse(compiler, root);
    Node sideEffect = root.getChildNodes().get(2);
    assertFalse(NodeUtil.mayHaveSideEffects(sideEffect, compiler));
    assertTrue(sideEffect.getNext() != null);
  }

  @org.junit.Test
  public void testRemoveBranchingStatements() {
    TestUnreachableCodeEliminationRoot root = new TestUnreachableCodeEliminationRoot();
    compiler.process(root);
    NodeTraversal.traverse(compiler, root);
    Node breakStatement = root.getChildNodes().get(3);
    Node continueStatement = root.getChildNodes().get(4);
    Node returnStatement = root.getChildNodes().get(5);
    assertNull(breakStatement.getNext());
    assertNull(continueStatement.getNext());
    assertNull(returnStatement.getNext());
  }

  @org.junit.Test
  public void testRemoveDeadBlock() {
    TestUnreachableCodeEliminationRoot root = new TestUnreachableCodeEliminationRoot();
    compiler.process(root);
    NodeTraversal.traverse(compiler, root);
    Node deadBlock = root.getLastChild();
    assertNull(deadBlock.getFirstChild());
    assertNull(deadBlock.getNext());
  }

  @org.junit.Test
  public void testRemoveDeadCatchBlock() {
    TestUnreachableCodeEliminationRoot root = new TestUnreachableCodeEliminationRoot();
    compiler.process(root);
    NodeTraversal.traverse(compiler, root);
    Node catchBlock = root.getChildNodes().get(1).getLastChild();
    assertNull(catchBlock.getFirstChild());
    assertNull(catchBlock.getNext());
  }

  @org.junit.Test
  public void testRemoveDeadDOStatement() {
    TestUnreachableCodeEliminationRoot root = new TestUnreachableCodeEliminationRoot();
    compiler.process(root);
    NodeTraversal.traverse(compiler, root);
    Node doStatement = root.getChildNodes().get(0).getLastChild();
    assertFalse(doStatement.isDO());
    Node loop = doStatement.getNext();
    assertTrue(loop.isBlock());
    Node body = loop.getFirstChild();
    Node condition = body.getLastChild();
    assertTrue(condition.isIf());
    Node whileCondition = condition.getCondition();
    assertTrue(whileCondition.isLiteral());
    assertTrue(whileCondition.isFalse());
  }

  @org.junit.Test
  public void testRemoveDeadVariableDeclaration() {
    TestUnreachableCodeEliminationRoot root = new TestUnreachableCodeEliminationRoot();
    compiler.process(root);
    NodeTraversal.traverse(compiler, root);
    Node variableDeclaration = root.getChildNodes().get(1).getLastChild();
    assertNull(variableDeclaration.getNext());
  }
}

class TestUnreachableCodeEliminationRoot extends Node {
  TestUnreachableCodeEliminationRoot() {
    setType(Token.BLOCK);
    Node returnStatement = new Node(Token.RETURN);
    Node deadCode1 = new Node(Token.IDENT, "deadCode1");
    Node deadCode2 = new Node(Token.NUMBER, "42");
    Node deadCode3 = new Node(Token.IDENT, "deadCode3");
    Node deadCode4 = new Node(Token.NUMBER, "3.14");
    Node deadCode5 = new Node(Token.RETURN);
    Node breakStatement = new Node(Token.BREAK);
    Node continueStatement = new Node(Token.CONTINUE);
    Node deadBlock = new Node(Token.BLOCK);
    Node deadCatchBlock = new Node(Token.BLOCK);
    Node doStatement = new Node(Token.DO);
    Node loop = new Node(Token.WHILE);
    Node condition = new Node(Token.IF);
    Node whileCondition = new Node(Token.BOOLEAN, false);

    addChild(returnStatement);
    addChild(deadCode1);
    addChild(deadCode2);
    addChild(deadCode3);
    addChild(deadCode4);
    addChild(deadCode5);
    addChild(breakStatement);
    addChild(continueStatement);
    addChild(deadBlock);
    addChild(deadCatchBlock);
    addChild(doStatement);
    doStatement.addChild(loop);
    loop.addChild(condition);
    condition.addChild(whileCondition);
  }
}