package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void shouldReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(n);
    assertTrue(checkGlobalThis.shouldReportThis(n));
  }

  @Test
  public void shouldNotReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.CALL);
    parent.addChildToFront(n);
    assertFalse(checkGlobalThis.shouldReportThis(n));
  }

  @Test
  public void visitThisNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    assertFalse(compiler.hasErrors());
    checkGlobalThis.visit(t, n, null);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void visitAssignLhsNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.THIS);
    n.addChildToFront(lhs);
    checkGlobalThis.assignLhsChild = lhs;
    assertFalse(compiler.hasErrors());
    checkGlobalThis.visit(t, lhs, n);
    assertTrue(compiler.hasErrors());
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void shouldTraverseWithParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void shouldTraverseWithoutParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void getFunctionJsDocInfoWithJSDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }

  @Test
  public void getFunctionJsDocInfoWithParentAssign() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }

  @Test
  public void getFunctionJsDocInfoWithParentVar() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    parent.addChildToFront(n);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }

  @Test
  public void getFunctionJsDocInfoWithoutJSDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node(Token.FUNCTION);
    assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
  }

}