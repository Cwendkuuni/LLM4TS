package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testEmpty() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Node.ROOT();
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);
    assertTrue(NodeUtil.isNullOrEmpty(root.getFirstChild()));
  }

  @Test
  public void testUnreachableCodeAfterReturn() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Node.newBlock(
        Node.newReturn(Token.RETURN, null, Node.newIntLiteral(1)),
        Node.newExpressionStatement(Node.newCall(
            Token.CALL,
            Node.newIdentifier("alert"),
            Node.newStringLiteral("unreachable"))));
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    assertTrue(root.isReturn());
    assertFalse(root.getLastChild().isDefined());
  }

  @Test
  public void testUnreachableCodeAfterBreak() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Node.newBlock(
        Node.newLabel(),
        Node.newExpressionStatement(Node.newBreakStatement(Node.newIdentifier("label"))),
        Node.newLabel("label"),
        Node.newExpressionStatement(Node.newCall(
            Token.CALL,
            Node.newIdentifier("alert"),
            Node.newStringLiteral("unreachable"))));
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    Node label = NodeUtil.getChildByKind(root, Node.LABEL);
    assertTrue(label != null);
    assertTrue(label.getNext().isReturn());
    assertFalse(root.getLastChild().isDefined());
  }

  @Test
  public void testUnreachableCodeAfterContinue() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Node.newBlock(
        Node.newLabel(),
        Node.newExpressionStatement(Node.newContinueStatement(Node.newIdentifier("label"))),
        Node.newLabel("label"),
        Node.newExpressionStatement(Node.newCall(
            Token.CALL,
            Node.newIdentifier("alert"),
            Node.newStringLiteral("unreachable"))));
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    Node label = NodeUtil.getChildByKind(root, Node.LABEL);
    assertTrue(label != null);
    assertTrue(label.getNext().isFunction());
    assertFalse(root.getLastChild().isDefined());
  }

  @Test
  public void testRemoveNoOpStatements() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Node.newBlock(
        Node.newExpressionStatement(Node.newCall(
            Token.CALL,
            Node.newIdentifier("console.log"),
            Node.newStringLiteral("foo"))),
        Node.newExpressionStatement(Node.newCall(
            Token.CALL,
            Node.newIdentifier("console.log"),
            Node.newStringLiteral("bar"))));
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node logFoo = NodeUtil.getChildByKind(root, Node.EXPR_STMT);
    assertTrue(logFoo != null);
    assertTrue(logFoo.getNext() != null);

    Node logBar = NodeUtil.getChildByKind(root, Node.EXPR_STMT);
    assertTrue(logBar != null);
    assertFalse(root.getLastChild().isDefined());
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Node.newBlock(
        Node.newLabel(),
        Node.newExpressionStatement(Node.newBreakStatement(Node.newIdentifier("label"))),
        Node.newLabel("label"),
        Node.newExpressionStatement(Node.newBreakStatement(Node.newIdentifier("label"))));
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    Node label = NodeUtil.getChildByKind(root, Node.LABEL);
    assertTrue(label != null);
    assertTrue(label.getNext().isReturn());
  }

  @Test
  public void testRemoveDeadBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Node.newBlock(
        Node.newBlock(
            Node.newLabel(),
            Node.newExpressionStatement(Node.newCall(
                Token.CALL,
                Node.newIdentifier("console.log"),
                Node.newStringLiteral("foo")))),
        Node.newLabel("exit"));
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    assertTrue(root.getFirstChild().isDefined());
    assertFalse(root.getLastChild().isDefined());
  }

  @Test
  public void testRemoveDeadFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Node.newFunction(
        Node.newIdentifier("foo"),
        Node.newBlock(
            Node.newLabel(),
            Node.newExpressionStatement(Node.newCall(
                Token.CALL,
                Node.newIdentifier("console.log"),
                Node.newStringLiteral("foo"))),
            Node.newLabel("exit")));
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    assertTrue(root.getFirstChild().isDefined());
    assertFalse(root.getLastChild().isDefined());
  }
}