package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_Function_Constructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_Function_HasThisType() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType("myType");
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_Function_IsOverride() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_Function_ParentIsNotBlockScriptNameAssign() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.ADD);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ParentIsNull() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertTrue(result);
  }

  @Test
  public void testVisit_This_ShouldReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void testVisit_This_ShouldNotReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }
}