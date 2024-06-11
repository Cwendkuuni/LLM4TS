package com.google.javascript.jscomp;

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

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = mock(Node.class);
    Node n1 = mock(Node.class);
    Node n2 = mock(Node.class);

    when(n1.getType()).thenReturn(Token.FUNCTION);
    when(n1.getParent()).thenReturn(parent);
    when(n2.getType()).thenReturn(Token.ASSIGN);
    when(n2.getParent()).thenReturn(parent);
    when(n2.getFirstChild()).thenReturn(n1);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertTrue(checker.shouldTraverse(traversal, n1, parent));
    assertTrue(checker.shouldTraverse(traversal, n2, parent));

    Node n3 = mock(Node.class);
    when(n3.getType()).thenReturn(Token.GETPROP);
    when(n3.getString()).thenReturn("prototype");
    when(n3.getParent()).thenReturn(parent);

    assertFalse(checker.shouldTraverse(traversal, n3, parent));

    Node n4 = mock(Node.class);
    when(n4.getType()).thenReturn(Token.FUNCTION);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(true);
    when(n4.getJSDocInfo()).thenReturn(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, n4, parent));
  }

  @Test
  public void testShouldReportThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n1 = mock(Node.class);
    Node parent = mock(Node.class);

    when(n1.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.ASSIGN);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertTrue(checker.shouldReportThis(n1, parent));

    Node n2 = mock(Node.class);
    when(n2.getType()).thenReturn(Token.GETPROP);

    assertFalse(checker.shouldReportThis(n1, n2));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n1 = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);

    when(n1.getType()).thenReturn(Token.FUNCTION);
    when(n1.getParent()).thenReturn(parent);
    when(parent.getType()).thenReturn(Token.NAME);

    when(n1.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getJSDocInfo()).thenReturn(null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertEquals(jsDoc, checker.getFunctionJsDocInfo(n1));

    when(parent.getType()).thenReturn(Token.ASSIGN);
    assertNull(checker.getFunctionJsDocInfo(n1));
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n1 = mock(Node.class);
    Node n2 = mock(Node.class);
    Node parent = mock(Node.class);

    when(n1.getType()).thenReturn(Token.THIS);
    when(n2.getType()).thenReturn(Token.ASSIGN);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    when(n1.getParent()).thenReturn(parent);
    when(n2.getParent()).thenReturn(parent);
    when(n2.getFirstChild()).thenReturn(n1);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    when(compiler.report(traversal.makeError(n1, level, DiagnosticType.warning(null, "")))).thenReturn(null);

    checker.visit(traversal, n1, parent);
    checker.visit(traversal, n2, parent);
  }
}