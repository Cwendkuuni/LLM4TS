package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @After
  public void tearDown() {
    compiler = null;
    level = null;
    checkGlobalThis = null;
  }

  @Test
  public void testShouldTraverse_functionConstructor() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_functionThisType() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_functionNoThisAnnotation() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_notAssignmentToPrototype() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.GETPROP));
    parent.getLastChild().addChildToFront(new Node(Token.NAME, "prototype"));

    n.addChildToFront(parent);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_assignmentToNonPrototypeProperty() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.GETPROP));
    parent.getLastChild().addChildToFront(new Node(Token.NAME, "object"));
    parent.getLastChild().addChildToFront(new Node(Token.NAME, "property"));

    n.addChildToFront(parent);

    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testVisit_thisKeywordFound() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);

    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void testVisit_thisKeywordNotFound() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.NAME, "not_this");
    Node parent = new Node(Token.NAME, "parent");

    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void testVisit_assignmentLeftSideIsThis() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);

    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void testVisit_notAssignmentLeftSideIsThis() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.NAME, "not_this");
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);

    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }

}