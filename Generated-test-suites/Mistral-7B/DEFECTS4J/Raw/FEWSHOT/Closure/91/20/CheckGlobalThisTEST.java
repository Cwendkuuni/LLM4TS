package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private Checker checker;
  private NodeTraversal traverser;
  private Parser parser;
  private AbstractCompiler compiler;

  @org.junit.Before
  public void setUp() throws Exception {
    checker = mock(Checker.class);
    parser = mock(Parser.class);
    compiler = mock(AbstractCompiler.class);

    when(parser.parseCompilationUnit(anyString())).thenReturn(new Node());
    when(compiler.getChecker()).thenReturn(checker);

    traverser = new NodeTraversal(parser.parseCompilationUnit(""), compiler);
  }

  @org.junit.Test
  public void shouldNotTraverseNonGlobalContexts() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    assertFalse(checker.shouldTraverse(traverser, new Node(), null));
  }

  @org.junit.Test
  public void shouldTraverseFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION);

    assertTrue(checker.shouldTraverse(traverser, functionNode, null));
  }

  @org.junit.Test
  public void shouldTraverseFunctionWithNoAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION);

    Node parentNode = new Node();
    parentNode.setType(Token.NAME);
    functionNode.setParent(parentNode);

    assertTrue(checker.shouldTraverse(traverser, functionNode, parentNode));
  }

  @org.junit.Test
  public void shouldTraverseFunctionWithPrototypeProperty() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION);

    Node prototypeNode = new Node();
    prototypeNode.setType(Token.GETPROP);
    prototypeNode.setLastChild(new Node().setType(Token.STRING).setString("prototype"));

    Node parentNode = new Node();
    parentNode.setType(Token.ASSIGN);
    parentNode.setFirstChild(functionNode);
    parentNode.setLastChild(prototypeNode);

    assertFalse(checker.shouldTraverse(traverser, functionNode, parentNode));
  }

  @org.junit.Test
  public void shouldTraversePropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node getPropNode = new Node();
    getPropNode.setType(Token.GETPROP);

    Node parentNode = new Node();
    parentNode.setType(Token.ASSIGN);
    parentNode.setFirstChild(new Node().setType(Token.THIS));
    parentNode.setLastChild(getPropNode);

    assertTrue(checker.shouldTraverse(traverser, getPropNode, parentNode));
  }

  @org.junit.Test
  public void shouldTraverseFunctionWithThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION);

    functionNode.setJSDocInfo(new JSDocInfo().setThisType(true));

    assertTrue(checker.shouldTraverse(traverser, functionNode, null));
  }

  @org.junit.Test
  public void shouldReportThisKeyword() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node thisNode = new Node().setType(Token.THIS);
    Node parentNode = new Node();
    parentNode.setType(Token.ASSIGN);
    parentNode.setFirstChild(thisNode);

    traverser.traverse(checker, parentNode);

    assertTrue(checker.compiler.hasErrors());
  }

  @org.junit.Test
  public void shouldNotReportThisKeywordInFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION);

    Node parentNode = new Node();
    parentNode.setType(Token.ASSIGN);
    parentNode.setFirstChild(new Node().setType(Token.THIS));
    parentNode.setLastChild(functionNode);

    traverser.traverse(checker, functionNode);

    assertFalse(checker.compiler.hasErrors());
  }

  @org.junit.Test
  public void shouldNotReportThisKeywordInPrototypeProperty() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node getPropNode = new Node();
    getPropNode.setType(Token.GETPROP);
    getPropNode.setLastChild(new Node().setType(Token.STRING).setString("prototype"));

    Node parentNode = new Node();
    parentNode.setType(Token.ASSIGN);
    parentNode.setFirstChild(new Node().setType(Token.THIS));
    parentNode.setLastChild(getPropNode);

    traverser.traverse(checker, getPropNode);

    assertFalse(checker.compiler.hasErrors());
  }
}