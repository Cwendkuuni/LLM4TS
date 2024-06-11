package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {
  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = new CheckLevel(WARNING);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node1 = new Node(Token.THIS, 0, 0);
    Node node2 = new Node(Token.ASSIGN, 0, 0);
    Node node3 = new Node(Token.FUNCTION, 0, 0);
    Node node4 = new Node(Token.SCRIPT, 0, 0);
    Node node5 = new Node(Token.BLOCK, 0, 0);
    Node node6 = new Node(Token.NAME, 0, 0);
    Node node7 = new Node(Token.ASSIGN, 0, 0);
    Node node8 = new Node(Token.GETPROP, 0, 0);
    Node node9 = new Node(Token.STRING, 0, 0);
    Node node10 = new Node(Token.NUMBER, 0, 0);
    Node parent1 = node3;
    Node parent2 = node5;
    Node parent3 = node6;
    Node parent4 = node7;
    Node parent5 = node8;

    when(compiler.report(traversal.makeError(node1, level, DiagnosticType.warning(null, null)))).thenReturn(true);

    assertTrue(checker.shouldTraverse(traversal, node1, parent1));
    assertFalse(checker.shouldTraverse(traversal, node2, parent2));
    assertFalse(checker.shouldTraverse(traversal, node3, null));
    assertFalse(checker.shouldTraverse(traversal, node3, parent1));
    assertFalse(checker.shouldTraverse(traversal, node3, parent3));
    assertFalse(checker.shouldTraverse(traversal, node4, null));
    assertTrue(checker.shouldTraverse(traversal, node5, null));
    assertTrue(checker.shouldTraverse(traversal, node6, parent3));
    assertTrue(checker.shouldTraverse(traversal, node7, parent4));
    assertFalse(checker.shouldTraverse(traversal, node8, parent5));
    assertFalse(checker.shouldTraverse(traversal, node9, null));
    assertFalse(checker.shouldTraverse(traversal, node10, null));
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = new CheckLevel(WARNING);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node1 = new Node(Token.THIS, 0, 0);
    Node node2 = new Node(Token.ASSIGN, 0, 0);
    Node node3 = new Node(Token.FUNCTION, 0, 0);
    Node node4 = new Node(Token.SCRIPT, 0, 0);
    Node node5 = new Node(Token.BLOCK, 0, 0);
    Node node6 = new Node(Token.NAME, 0, 0);
    Node node7 = new Node(Token.ASSIGN, 0, 0);
    Node node8 = new Node(Token.GETPROP, 0, 0);
    Node node9 = new Node(Token.STRING, 0, 0);
    Node node10 = new Node(Token.NUMBER, 0, 0);
    Node parent1 = node3;
    Node parent2 = node5;
    Node parent3 = node6;
    Node parent4 = node7;
    Node parent5 = node8;
    Node leftSide = node5.getFirstChild();

    when(compiler.report(traversal.makeError(node1, level, DiagnosticType.warning(null, null)))).thenReturn(true);

    checker.visit(traversal, node1, parent1);
    checker.visit(traversal, node2, parent2);
    checker.visit(traversal, node3, null);
    checker.visit(traversal, node4, null);
    checker.visit(traversal, node5, null);
    checker.visit(traversal, node6, parent3);
    checker.visit(traversal, node7, parent4);
    checker.visit(traversal, node8, parent5);

    assertTrue(leftSide.getFirstChild().equals(node3));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, null);

    Node node1 = new Node(Token.ASSIGN, 0, 0);
    Node node2 = new Node(Token.NAME, 0, 0);
    Node node3 = new Node(Token.FUNCTION, 0, 0);
    Node node4 = new Node(Token.VAR, 0, 0);
    Node node5 = new Node(Token.FUNCTION, 0, 0);

    JSDocInfo jsDoc1 = checker.getFunctionJsDocInfo(node1);
    JSDocInfo jsDoc2 = checker.getFunctionJsDocInfo(node2);
    JSDocInfo jsDoc3 = checker.getFunctionJsDocInfo(node3);
    JSDocInfo jsDoc4 = checker.getFunctionJsDocInfo(node4);
    JSDocInfo jsDoc5 = checker.getFunctionJsDocInfo(node5);

    assertNull(jsDoc1);
    assertNull(jsDoc2);
    assertNull(jsDoc4);
    assertNull(jsDoc5);
    assertTrue(jsDoc3 != null);
  }

  @Test
  public void testShouldReportThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, null);

    Node node1 = new Node(Token.THIS, 0, 0);
    Node node2 = new Node(Token.ASSIGN, 0, 0);
    Node node3 = new Node(Token.GETPROP, 0, 0);

    assertTrue(checker.shouldReportThis(node1, null));
    assertFalse(checker.shouldReportThis(node2, node1));
    assertTrue(checker.shouldReportThis(node3, node2));
  }
}