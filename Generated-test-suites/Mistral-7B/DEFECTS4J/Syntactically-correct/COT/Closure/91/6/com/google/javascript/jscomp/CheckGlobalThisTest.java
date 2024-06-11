package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckLevel.Warning;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeFactory nodeFactory;
  private NodeTraversal traversal;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions(new TestSourceFile("CheckGlobalThisTest.js"));
    nodeFactory = new NodeFactory(options);
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, Warning);
    traversal = new NodeTraversal(compiler, checker, options);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = nodeFactory.createNodeFromString("function test() { }");
    Node parentNode = nodeFactory.createNodeFromString("var x = function test() { }");

    assertEquals(true, checker.shouldTraverse(any(NodeTraversal.class), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node functionNode = nodeFactory.createNodeFromString("function test() { }");

    JSDocInfo jsDoc = nodeFactory.createJSDocInfo("constructor");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertEquals(false, checker.shouldTraverse(any(NodeTraversal.class), functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisTypeAnnotation() {
    Node functionNode = nodeFactory.createNodeFromString("@this\nfunction test() { }");

    assertEquals(false, checker.shouldTraverse(any(NodeTraversal.class), functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithInterfaceAnnotation() {
    Node functionNode = nodeFactory.createNodeFromString("@interface\nfunction test() { }");

    assertEquals(false, checker.shouldTraverse(any(NodeTraversal.class), functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node functionNode = nodeFactory.createNodeFromString("@override\nfunction test() { }");

    assertEquals(false, checker.shouldTraverse(any(NodeTraversal.class), functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = nodeFactory.createNodeFromString("function test() { }");
    Node objectLiteralNode = nodeFactory.createNodeFromString("var x = {test: function() {}}");

    assertEquals(true, checker.shouldTraverse(any(NodeTraversal.class), functionNode, objectLiteralNode));
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportGlobalThis() {
    Node thisNode = nodeFactory.createNodeFromString("var x = function() { this.test = 1; }");
    Node parentNode = nodeFactory.createNodeFromString("var x = function() { }");

    Node rootNode = nodeFactory.createNodeFromString("x();");

    traversal.traverse(rootNode);

    verify(compiler).report(any(NodeTraversal.Error.class));
  }

  @Test
  public void testShouldNotReportGlobalThisInPrototypeProperty() {
    Node thisNode = nodeFactory.createNodeFromString("var x = {get test() { return this; }};");
    Node parentNode = nodeFactory.createNodeFromString("var x = {};");

    Node rootNode = nodeFactory.createNodeFromString("x.test;");

    traversal.traverse(rootNode);

    verify(compiler, never()).report(any(NodeTraversal.Error.class));
  }

  @Test
  public void testShouldReportGlobalThisInPrototypeSubProperty() {
    Node thisNode = nodeFactory.createNodeFromString("var x = {get y() { return this.z; }};");
    Node parentNode = nodeFactory.createNodeFromString("var x = {z: 1};");

    Node rootNode = nodeFactory.createNodeFromString("x.y;");

    traversal.traverse(rootNode);

    verify(compiler).report(any(NodeTraversal.Error.class));
  }

  @Test
  public void testShouldNotReportGlobalThisInFunctionCall() {
    Node thisNode = nodeFactory.createNodeFromString("function test() { return this.test; }");
    Node parentNode = nodeFactory.createNodeFromString("var x = function test() { }");

    Node rootNode = nodeFactory.createNodeFromString("test();");

    traversal.traverse(rootNode);

    verify(compiler, never()).report(any(NodeTraversal.Error.class));
  }

  @Test
  public void testShouldReportGlobalThisInPropertyAccess() {
    Node thisNode = nodeFactory.createNodeFromString("var x = {get test() { return this; }};");
    Node parentNode = nodeFactory.createNodeFromString("var x = {};");

    Node rootNode = nodeFactory.createNodeFromString("x.test.test;");

    traversal.traverse(rootNode);

    verify(compiler).report(any(NodeTraversal.Error.class));
  }
}