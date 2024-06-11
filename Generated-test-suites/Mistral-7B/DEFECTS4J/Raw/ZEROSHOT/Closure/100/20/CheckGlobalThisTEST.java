package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckerFactory checkerFactory;
  private Parser parser;

  @Before
  public void setUp() {
    checkerFactory = mock(CheckerFactory.class);
    compiler = new AbstractCompiler(new CompilerOptions(), checkerFactory);
    parser = new Parser(compiler);
  }

  @Test
  public void testNoWarningForConstructor() {
    String code = "function Foo() {}";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testWarningForFunctionWithNoAnnotation() {
    String code = "function evil() { this.x = 1; }";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
  }

  @Test
  public void testWarningForFunctionWithThisAnnotation() {
    String code = "function evil() { @this this.x = 1; }";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testWarningForFunctionWithPrototypeProperty() {
    String code = "function evil() { this.prototype.x = 1; }";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
  }

  @Test
  public void testWarningForFunctionWithNestedPrototypeProperty() {
    String code = "function evil() { this.a.prototype.x = 1; }";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
  }

  @Test
  public void testNoWarningForAssignment() {
    String code = "var a = this;";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testNoWarningForAssignmentWithPropertyAccess() {
    String code = "var a = { x: this.y };";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testNoWarningForProtoMethod() {
    String code = "function Foo() {} Foo.prototype.bar = function() { this.x = 1; }";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testWarningForProtoMethodWithErrorOnCall() {
    String code = "function Foo() {} Foo.prototype.bar = function() { this.call(); }";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
  }

  @Test
  public void testNoWarningForMethodCall() {
    String code = "function Foo() {} var f = new Foo(); f.bar();";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testWarningForMethodCallWithErrorOnCall() {
    String code = "function Foo() {} var f = new Foo(); f.call();";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
  }

  @Test
  public void testWarningForCallWithErrorOnCall() {
    String code = "function evil() { this.call(); }";

    Node node = parser.parse(code, null, null).getRootNode();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(compiler, node, checker);
    traverser.traverse();
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
  }
}