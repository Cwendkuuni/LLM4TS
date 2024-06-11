package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private Compiler compiler;
  private Parser parser;
  private Node root;

  @Before
  public void setUp() {
    CheckerFactory checkerFactory = new CheckerFactory();
    compiler = new Compiler(checkerFactory.getCheckers(), checkerFactory.getCheckers().size());
    parser = new Parser(compiler);
  }

  @Test
  public void testShouldTraversePrototype() {
    String jsCode = "var obj = {prototype: {foo: function() {}}}; obj.prototype.foo();";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() == 0);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    String jsCode = "var a = this;";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() > 0);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    String jsCode = "this.property = 1;";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() > 0);
  }

  @Test
  public void testShouldNotReportThisInsideFunctionWithThisAnnotations() {
    String jsCode = "function foo() {\n" +
      "  this.bar = 1;\n" +
      "}\n" +
      "function fooBar() {\n" +
      "  @constructor\n" +
      "  this.baz = 1;\n" +
      "}";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() == 0);
  }

  @Test
  public void testShouldNotReportThisInsidePrototypeMethod() {
    String jsCode = "function Foo() {}\n" +
      "Foo.prototype.bar = function() {\n" +
      "  this.baz = 1;\n" +
      "}";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() == 0);
  }

  @Test
  public void testShouldNotReportThisInsideFunctionWithNoAnnotation() {
    String jsCode = "function foo() { this.bar = 1; }";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() == 0);
  }

  @Test
  public void testShouldNotReportThisInsideConstructor() {
    String jsCode = "function Foo() {\n" +
      "  this.bar = 1;\n" +
      "}";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() == 0);
  }

  @Test
  public void testShouldNotReportThisInsideFunctionWithNoLogicalPlaceForAnnotation() {
    String jsCode = "function foo() {\n" +
      "  var a = 1;\n" +
      "  this.bar = a;\n" +
      "}";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() == 0);
  }

  @Test
  public void testShouldReportThisInsideFunctionWithNoLogicalPlaceForAnnotationAndNoParent() {
    String jsCode = "function foo() {\n" +
      "  this.bar = 1;\n" +
      "}";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node rootNode = root;
    new NodeTraversal(rootNode, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() > 0);
  }

  @Test
  public void testShouldNotReportThisInsideFunctionWithNoLogicalPlaceForAnnotationAndParentIsAssign() {
    String jsCode = "function foo() {\n" +
      "  this.bar = 1;\n" +
      "} var a = foo();";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() > 0);
  }

  @Test
  public void testShouldNotReportThisInsideFunctionWithNoLogicalPlaceForAnnotationAndParentIsGetProperty() {
    String jsCode = "var obj = {foo: function() {\n" +
      "  this.bar = 1;\n" +
      "}}; obj.foo();";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() > 0);
  }

  @Test
  public void testShouldNotReportThisInsideFunctionWithNoLogicalPlaceForAnnotationAndParentIsGetPropertyWithPrototype() {
    String jsCode = "var obj = {prototype: {get foo() {\n" +
      "  this.bar = 1;\n" +
      "}}}; obj.foo();";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() > 0);
  }

  @Test
  public void testShouldNotReportThisInsideFunctionWithNoLogicalPlaceForAnnotationAndParentIsSetProperty() {
    String jsCode = "var obj = {}; Object.defineProperty(obj, \"foo\", {\n" +
      "  get: function() {\n" +
      "    this.bar = 1;\n" +
      "  }\n" +
      "}); obj.foo();";
    root = parser.parse(jsCode, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, checker).traverse();

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.getNumErrors() > 0);
  }
}