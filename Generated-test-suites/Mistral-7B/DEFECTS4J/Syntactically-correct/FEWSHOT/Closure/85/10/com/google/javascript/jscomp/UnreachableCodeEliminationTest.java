package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testEmptyMethod() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newBlock(1);
    elimination.process(Node.NULL, root);
    assertTrue(root.hasChildren());
  }

  @Test
  public void testRemoveEmptyBlock() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newBlock(1);
    root.addChild(Node.newBlock(2));
    elimination.process(Node.NULL, root);
    assertFalse(root.hasChildren());
  }

  @Test
  public void testRemoveEmptyStatement() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newBlock(1);
    root.addChild(Node.newExpressionStatement(Node.newLiteral(1)));
    elimination.process(Node.NULL, root);
    assertFalse(root.hasChildren());
  }

  @Test
  public void testRemoveUnconditionalBreak() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newBlock(1);
    Node loop = Node.newBlock(2);
    loop.addChild(Node.newExpressionStatement(Node.newLiteral(1)));
    loop.addChild(Node.newStatement(Node.BREAKSTATEMENT, Node.NULL));
    root.addChild(Node.newStatement(Node.DOSTATEMENT, Node.NULL, loop));
    elimination.process(Node.NULL, root);
    assertFalse(root.hasChildren());
  }

  @Test
  public void testRemoveUnconditionalContinue() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newBlock(1);
    Node loop = Node.newBlock(2);
    loop.addChild(Node.newExpressionStatement(Node.newLiteral(1)));
    loop.addChild(Node.newStatement(Node.CONTINUESTATEMENT, Node.NULL));
    root.addChild(Node.newStatement(Node.DOSTATEMENT, Node.NULL, loop));
    elimination.process(Node.NULL, root);
    assertFalse(root.hasChildren());
  }

  @Test
  public void testRemoveUnconditionalReturn() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newBlock(1);
    root.addChild(Node.newStatement(Node.RETURNSTATEMENT, Node.NULL));
    elimination.process(Node.NULL, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testRemoveConditionalReturn() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newBlock(1);
    Node condition = Node.newExpressionStatement(Node.newExpressionBoolean(true, Node.nullLiteral()));
    root.addChild(condition);
    root.addChild(Node.newStatement(Node.RETURNSTATEMENT, Node.NULL));
    elimination.process(Node.NULL, root);
    assertFalse(root.hasChildren());
  }

  @Test
  public void testRemoveDeadFunction() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newBlock(1);
    Node function = Node.newFunctionDeclaration("test", Node.VOID, Node.NULL, Node.nullLiteral(), Node.newBlock(1));
    root.addChild(function);
    elimination.process(Node.NULL, root);
    assertFalse(root.hasChildren());
  }

  @Test
  public void testRemoveDeadScript() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = Node.newBlock(1);
    Node script = Node.newScript(Node.newExpressionStatement(Node.newLiteral(1)));
    root.addChild(script);
    elimination.process(Node.NULL, root);
    assertFalse(root.hasChildren());
  }
}