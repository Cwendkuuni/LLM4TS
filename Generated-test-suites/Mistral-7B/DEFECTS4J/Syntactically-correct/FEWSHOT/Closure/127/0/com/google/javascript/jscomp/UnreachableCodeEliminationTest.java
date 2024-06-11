package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback;
import com.google.javascript.jscomp.NodeTraversal.FunctionCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

  @Test
  public void testRemoveDeadCodeAfterReturn() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.RETURN, null));
    root.addChild(new Node(Token.PRINT, null, "unreachable"));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.getLastChild().isMissing());
  }

  @Test
  public void testRemoveEmptyBlock() {
    Node root = new Node(Token.BLOCK, null);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.isMissing());
  }

  @Test
  public void testRemoveVarDeclarationWithoutInitialization() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.VAR, null, "x"));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.isMissing());
  }

  @Test
  public void testRemoveSideEffectFreeExpression() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.ASSIGN, null, null, new Node(Token.IDENT, "a"), new Node(Token.IDENT, "b")));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, true);
    elimination.process(null, root);

    assertTrue(root.isMissing());
  }

  @Test
  public void testRemoveEmptyFunction() {
    Node root = new Node(Token.FUNCTION, new Node[0], null, null, null);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.isMissing());
  }

  @Test
  public void testRemoveUnconditionalBreak() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.LABEL, "loop"));
    root.addChild(new Node(Token.BREAK, null, "loop"));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.getChild(1).isMissing());
  }

  @Test
  public void testRemoveUnconditionalContinue() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.LABEL, "loop"));
    root.addChild(new Node(Token.CONTINUE, null, "loop"));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.getChild(1).isMissing());
  }

  @Test
  public void testRemoveUnreachableReturn() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.RETURN, null));
    root.addChild(new Node(Token.RETURN, null));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.getChild(0).isMissing());
  }

  @Test
  public void testRemoveUnreachableFunction() {
    Node root = new Node(Token.FUNCTION, new Node[0], null, null, null);
    root.addChild(new Node(Token.RETURN, null));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.isMissing());
  }

  @Test
  public void testRemoveUnreachableBlockInsideFunction() {
    Node root = new Node(Token.FUNCTION, new Node[0], null, null, null);
    root.addChild(new Node(Token.BLOCK, null));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.getChild(0).isMissing());
  }

  @Test
  public void testRemoveUnreachableBlockInsideBlock() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.BLOCK, null));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.getChild(1).isMissing());
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.BREAK, null, "loop"));
    root.addChild(new Node(Token.LABEL, "loop"));
    root.addChild(new Node(Token.BREAK, null, "loop"));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertFalse(root.getChild(1).isMissing());
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithFollowingBreak() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.BREAK, null, "loop1"));
    root.addChild(new Node(Token.LABEL, "loop1"));
    root.addChild(new Node(Token.BREAK, null, "loop2"));
    root.addChild(new Node(Token.LABEL, "loop2"));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertFalse(root.getChild(2).isMissing());
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithFollowingReturn() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.RETURN, null));
    root.addChild(new Node(Token.BREAK, null, "loop"));
    root.addChild(new Node(Token.LABEL, "loop"));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertFalse(root.getChild(2).isMissing());
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithFollowingUnreachableFunction() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.FUNCTION, new Node[0], null, null, null));
    root.addChild(new Node(Token.BREAK, null, "loop"));
    root.addChild(new Node(Token.LABEL, "loop"));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.getChild(2).isMissing());
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithFollowingUnreachableBlock() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.BLOCK, null));
    root.addChild(new Node(Token.BREAK, null, "loop"));
    root.addChild(new Node(Token.LABEL, "loop"));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertTrue(root.getChild(2).isMissing());
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithFollowingUnconditionalBranching() {
    Node root = new Node(Token.BLOCK, null);
    root.addChild(new Node(Token.BREAK, null, "loop1"));
    root.addChild(new Node(Token.LABEL, "loop1"));
    root.addChild(new Node(Token.BREAK, null, "loop2"));
    root.addChild(new Node(Token.LABEL, "loop2"));

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    elimination.process(null, root);

    assertFalse(root.getChild(3).isMissing());
  }
}