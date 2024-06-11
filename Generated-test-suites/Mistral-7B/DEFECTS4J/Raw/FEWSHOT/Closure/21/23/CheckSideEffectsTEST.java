package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Nodeops;

import java.util.List;

/**
 * Test class for CheckSideEffects.
 */
public class CheckSideEffectsTest {

  private AbstractCompiler mCompiler;
  private CheckSideEffects mCheckSideEffects;

  @org.junit.Before
  public void setUp() {
    mCompiler = mock(AbstractCompiler.class);
    mCheckSideEffects = new CheckSideEffects(mCompiler, CheckLevel.WARNING, false);
  }

  @org.junit.Test
  public void testvisitVoidNode() {
    Node node = Nodeops.createNode(Token.VOID);
    mCheckSideEffects.visit(null, node, null);
  }

  @org.junit.Test
  public void testvisitCommaNode() {
    Node parent = Nodeops.createNode(Token.EXPR_RESULT);
    Node node = Nodeops.createNode(Token.COMMA);
    mCheckSideEffects.visit(null, node, parent);
  }

  @org.junit.Test
  public void testvisitBlockNode() {
    Node parent = Nodeops.createNode(Token.BLOCK);
    Node node = Nodeops.createNode(Token.EXPR_RESULT);
    mCheckSideEffects.visit(null, node, parent);
  }

  @org.junit.Test
  public void testvisitNonResultUsed() {
    Node parent = Nodeops.createNode(Token.EXPR_RESULT);
    Node node = Nodeops.createNode(Token.ASSIGN);
    NodeUtil.setLeftHandSide(node, Nodeops.createName("x"));
    mCheckSideEffects.visit(null, node, parent);
  }

  @org.junit.Test
  public void testvisitSimpleOp() {
    Node parent = Nodeops.createNode(Token.EXPR_RESULT);
    Node node = Nodeops.createNode(Token.ADD);
    mCheckSideEffects.visit(null, node, parent);
  }

  @org.junit.Test
  public void testvisitNonSimpleOp() {
    Node parent = Nodeops.createNode(Token.EXPR_RESULT);
    Node node = Nodeops.createNode(Token.CALL, Nodeops.createName("foo"));
    mCheckSideEffects.visit(null, node, parent);
  }

  @org.junit.Test
  public void testvisitCallWithProtectorFn() {
    Node parent = Nodeops.createNode(Token.EXPR_RESULT);
    Node node = Nodeops.createNode(Token.CALL, Nodeops.createName("JSCOMPILER_PRESERVE"), Lists.newArrayList(Nodeops.createString("arg")));
    mCheckSideEffects.visit(null, node, parent);
  }

  @org.junit.Test
  public void testprocess() {
    Node root = Nodeops.createNode(Token.BLOCK);
    Node node = Nodeops.createNode(Token.ASSIGN, Nodeops.createName("x"), Nodeops.createString(""));
    Nodeops.addChild(root, node);
    mCheckSideEffects.process(null, root);
  }

  @org.junit.Test
  public void testprocessWithProtectedCode() {
    Node root = Nodeops.createNode(Token.BLOCK);
    Node node = Nodeops.createNode(Token.CALL, Nodeops.createName("JSCOMPILER_PRESERVE"), Lists.newArrayList(Nodeops.createString("arg")));
    Nodeops.addChild(root, node);
    mCheckSideEffects.process(null, root, false);
  }

  @org.junit.Test
  public void testhotSwapScript() {
    Node root = Nodeops.createNode(Token.BLOCK);
    mCheckSideEffects.hotSwapScript(null, root);
  }

  @org.junit.Test
  public void testprotectSideEffects() {
    List<Node> problemNodes = Lists.newArrayList();
    when(mCompiler.getSynthesizedExternsInput()).thenReturn(new CompilerInput());
    mCheckSideEffects.protectSideEffects();
    assertEquals(1, problemNodes.size());
  }

  @org.junit.Test
  public void testStripProtection() {
    Node root = Nodeops.createNode(Token.BLOCK);
    Node callNode = Nodeops.createNode(Token.CALL, Nodeops.createName("JSCOMPILER_PRESERVE"), Lists.newArrayList(Nodeops.createString("arg")));
    Nodeops.addChild(root, callNode);

    StripProtection stripProtection = new StripProtection(mCompiler);
    stripProtection.process(null, root);

    assertNull(Nodeops.getFirstChild(callNode));
  }
}