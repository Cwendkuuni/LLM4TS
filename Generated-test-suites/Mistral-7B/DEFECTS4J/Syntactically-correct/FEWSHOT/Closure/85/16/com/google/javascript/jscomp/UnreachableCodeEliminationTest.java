package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DiGraphEdge;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.CompilerPass;
import com.google.javascript.jscomp.compiler.ScopedCallback;
import com.google.javascript.rhino.Token;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

  private static final Logger logger =
    Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @Test
  public void testUnreachableCodeElimination() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newBlock(null, null);
    root.addChild(Node.newReturn(null, null, null, null));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("alert"), null), null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    elimination.process(null, root);

    assertTrue(NodeUtil.isEmpty(root));
  }

  @Test
  public void testRemoveNoOpStatements() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newBlock(null, null);
    root.addChild(Node.newExpressionStatement(Node.newIdent("a").getInfo(), Node.IDENT("a")));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("console"), "log"), null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    elimination.process(null, root);

    assertTrue(NodeUtil.isEmpty(root.getChild(0).getNext()));
  }

  @Test
  public void testRemoveUnreachableCodeFollowingReturn() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newBlock(null, null);
    root.addChild(Node.newReturn(null, null, null, null));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("alert"), null), null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    elimination.process(null, root);

    assertTrue(NodeUtil.isEmpty(root));
  }

  @Test
  public void testRemoveUnreachableCodeFollowingBreak() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newBlock(null, null);
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newBlock(null, null));
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newReturn(null, null, null, null));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("alert"), null), null));
    root.addChild(Node.newLabel(null, null));

    root.getChild(2).addChild(Node.newBreak(null, null, null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    elimination.process(null, root);

    assertTrue(NodeUtil.isEmpty(root.getChild(2)));
  }

  @Test
  public void testRemoveUnreachableCodeFollowingContinue() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newBlock(null, null);
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newBlock(null, null));
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newReturn(null, null, null, null));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("alert"), null), null));
    root.addChild(Node.newLabel(null, null));

    Node loop = root.addChild(Node.newBlock(null, null));
    loop.addChild(Node.newLabel(null, null));
    loop.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("console"), "log", Node.newString("i"), null), null));
    loop.addChild(Node.newContinueStatement(null, null, null));
    loop.addChild(Node.newLabel(null, null));
    loop.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("console"), "log", Node.newString("j"), null), null));
    loop.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("console"), "log", Node.newString("k"), null), null));
    loop.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("console"), "log", Node.newString("l"), null), null));
    loop.addChild(Node.newLabel(null, null));

    root.addChild(Node.newLabel(null, null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    elimination.process(null, root);

    Node logI = root.getChild(1).getChild(1);
    Node logJ = root.getChild(1).getChild(4);
    Node logK = root.getChild(1).getChild(7);
    Node logL = root.getChild(1).getChild(10);

    assertFalse(NodeUtil.isNullOrEmpty(logI.getParent().getNext()));
    assertFalse(NodeUtil.isNullOrEmpty(logJ.getParent().getNext()));
    assertFalse(NodeUtil.isNullOrEmpty(logK.getParent().getNext()));
    assertFalse(NodeUtil.isNullOrEmpty(logL.getParent().getNext()));

    assertTrue(NodeUtil.isEmpty(root.getChild(3)));
    assertTrue(NodeUtil.isEmpty(root.getChild(5)));
  }

  @Test
  public void testRemoveConditionalBranch() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newBlock(null, null);
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newBlock(null, null));
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("console"), "log", Node.newString("A"), null), null));
    root.addChild(Node.newIfStatement(null, Node.newIdent("x").getInfo(), Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("console"), "log", Node.newString("B"), null), null), null));
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("console"), "log", Node.newString("C"), null), null));
    root.addChild(Node.newReturn(null, null, null, null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    elimination.process(null, root);

    assertTrue(NodeUtil.isEmpty(root.getChild(3)));
    assertTrue(NodeUtil.isEmpty(root.getChild(5)));
  }

  @Test
  public void testRemoveUnreachableCodeWithParentFunction() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newFunction(null, null, Node.newBlock(null, null));
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newReturn(null, null, null, null));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("alert"), null), null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    elimination.process(null, root);

    assertTrue(NodeUtil.isEmpty(root.getChild(1)));
  }

  @Test
  public void testRemoveUnreachableCodeWithParentScript() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newScript(null);
    root.addChild(Node.newReturn(null, null, null, null));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("alert"), null), null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    elimination.process(null, root);

    assertTrue(NodeUtil.isEmpty(root.getChild(1)));
  }

  @Test
  public void testRemoveUnreachableCodeWithParentTry() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newFunction(null, null, Node.newBlock(null, null));
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newTryStatement(null, Node.newBlock(null, null), Node.newLabel(null, null), null));
    root.addChild(Node.newReturn(null, null, null, null));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("alert"), null), null));
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newCatchClause(null, null, null, null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    elimination.process(null, root);

    assertTrue(NodeUtil.isEmpty(root.getChild(3)));
    assertTrue(NodeUtil.isEmpty(root.getChild(5)));
  }

  @Test
  public void testRemoveUnreachableCodeWithParentTryCatch() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newFunction(null, null, Node.newBlock(null, null));
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newTryStatement(null, Node.newBlock(null, null), Node.newLabel(null, null), null));
    root.addChild(Node.newLabel(null, null));
    root.addChild(Node.newTryCatchBlock(Node.newLabel(null, null), null, null, null));
    root.addChild(Node.newReturn(null, null, null, null));
    root.addChild(Node.newExpressionStatement(Node.newCall(null, null, Node.IDENT("alert"), null), null));
    root.addChild(Node.newLabel(null, null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    elimination.process(null, root);

    assertTrue(NodeUtil.isEmpty(root.getChild(3)));
    assertTrue(NodeUtil.isEmpty(root.getChild(5)));
  }

  private static class Compiler extends AbstractCompiler {
  }
}