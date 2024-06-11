package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.ScopeCreator.RedeclarationHandler;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.jscomp.compiler.CompilerOptions;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  private AbstractCompiler compiler;
  private SyntacticScopeCreator scopeCreator;
  private RedeclarationHandler redeclarationHandler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public CompilerOptions getOptions() {
        return new CompilerOptions();
      }
    };
    scopeCreator = new SyntacticScopeCreator(compiler);
    redeclarationHandler = scopeCreator.new DefaultRedeclarationHandler();
  }

  @Test
  public void testCreateScope() {
    Node root = Node.parse("function test() {}");
    Scope parentScope = null;
    Scope scope = scopeCreator.createScope(root, parentScope);
    assertThat(scope.getParent()).isNull();
    assertThat(scope.getVariables().size()).isZero();
  }

  @Test
  public void testCreateScopeWithParent() {
    Node root = Node.parse("function test() {var a = 1; function b() {} var c = 2;}");
    Node functionNode = root.getChildAt(0);
    Scope parentScope = NodeUtil.asScope(root.getFirstChild());
    Scope scope = scopeCreator.createScope(functionNode, parentScope);

    assertThat(scope.getParent()).isEqualTo(parentScope);
    assertThat(scope.getVariables().size()).isEqualTo(3);

    Scope.Var aVar = scope.getVariable("a");
    assertThat(aVar.name).isEqualTo("a");
    assertThat(aVar.type).isNull();
    assertThat(aVar.input).isNull();

    Scope.Var bVar = scope.getVariable("b");
    assertThat(bVar).isNull();

    Scope.Var cVar = scope.getVariable("c");
    assertThat(cVar.name).isEqualTo("c");
    assertThat(cVar.type).isNull();
    assertThat(cVar.input).isNull();
  }

  @Test
  public void testScanRootFunction() {
    Node root = Node.parse("function test(arg1, arg2) {var a = 1; var b = arg1; function c() {} var d = 2; }");
    String sourceName = "test.js";
    Scope parentScope = null;
    Scope scope = scopeCreator.createScope(root, parentScope);

    assertThat(scope.getParent()).isNull();
    assertThat(scope.getVariables().size()).isEqualTo(5);

    Scope.Var aVar = scope.getVariable("a");
    assertThat(aVar.name).isEqualTo("a");
    assertThat(aVar.type).isNull();
    assertThat(aVar.input).isNull();

    Scope.Var arg1Var = scope.getVariable("arg1");
    assertThat(arg1Var).isNull();

    Scope.Var bVar = scope.getVariable("b");
    assertThat(bVar.name).isEqualTo("b");
    assertThat(bVar.type).isNull();
    assertThat(bVar.input).isNotNull();
    assertThat(bVar.input.getName()).isEqualTo("arg1");

    Scope.Var cVar = scope.getVariable("c");
    assertThat(cVar).isNull();

    Scope.Var dVar = scope.getVariable("d");
    assertThat(dVar.name).isEqualTo("d");
    assertThat(dVar.type).isNull();
    assertThat(dVar.input).isNull();
  }

  @Test
  public void testScanRootScript() {
    Node root = Node.parse("var a = 1; var b = 2;");
    String sourceName = "test.js";
    Scope parentScope = null;
    Scope scope = scopeCreator.createScope(root, parentScope);

    assertThat(scope.getParent()).isNull();
    assertThat(scope.getVariables().size()).isEqualTo(2);

    Scope.Var aVar = scope.getVariable("a");
    assertThat(aVar.name).isEqualTo("a");
    assertThat(aVar.type).isNull();
    assertThat(aVar.input).isNull();

    Scope.Var bVar = scope.getVariable("b");
    assertThat(bVar.name).isEqualTo("b");
    assertThat(bVar.type).isNull();
    assertThat(bVar.input).isNull();
  }

  @Test
  public void testScanVarsFunction() {
    Node root = Node.parse("function test() {var a = 1, b = 2, c = 3;}");
    String sourceName = "test.js";
    Scope parentScope = null;
    Scope scope = scopeCreator.createScope(root.getFirstChild(), parentScope);

    assertThat(scope.getParent()).isNull();
    assertThat(scope.getVariables().size()).isEqualTo(3);

    Scope.Var aVar = scope.getVariable("a");
    assertThat(aVar.name).isEqualTo("a");
    assertThat(aVar.type).isNull();
    assertThat(aVar.input).isNull();

    Scope.Var bVar = scope.getVariable("b");
    assertThat(bVar.name).isEqualTo("b");
    assertThat(bVar.type).isNull();
    assertThat(bVar.input).isNull();

    Scope.Var cVar = scope.getVariable("c");
    assertThat(cVar.name).isEqualTo("c");
    assertThat(cVar.type).isNull();
    assertThat(cVar.input).isNull();
  }

  @Test
  public void testScanVarsScript() {
    Node root = Node.parse("var a = 1, b = 2, c = 3;");
    String sourceName = "test.js";
    Scope parentScope = null;
    Scope scope = scopeCreator.createScope(root.getFirstChild(), parentScope);

    assertThat(scope.getParent()).isNull();
    assertThat(scope.getVariables().size()).isEqualTo(3);

    Scope.Var aVar = scope.getVariable("a");
    assertThat(aVar.name).isEqualTo("a");
    assertThat(aVar.type).isNull();
    assertThat(aVar.input).isNull();

    Scope.Var bVar = scope.getVariable("b");
    assertThat(bVar.name).isEqualTo("b");
    assertThat(bVar.type).isNull();
    assertThat(bVar.input).isNull();

    Scope.Var cVar = scope.getVariable("c");
    assertThat(cVar.name).isEqualTo("c");
    assertThat(cVar.type).isNull();
    assertThat(cVar.input).isNull();
  }

  @Test
  public void testRedeclaration() {
    Node root = Node.parse("function test() {var a = 1; var a = 2;}");
    String sourceName = "test.js";
    Scope parentScope = null;
    Scope scope = scopeCreator.createScope(root, parentScope);

    assertThat(scope.getParent()).isNull();
    assertThat(scope.getVariables().size()).isEqualTo(1);

    Scope.Var aVar = scope.getVariable("a");
    assertThat(aVar.name).isEqualTo("a");
    assertThat(aVar.type).isNull();
    assertThat(aVar.input).isNull();

    redeclarationHandler.onRedeclaration(
        scope, "a", root.getChildAt(2), root, null, root);

    Scope.Var newAVar = scope.getVariable("a");
    assertThat(newAVar).isNotNull();
    assertThat(newAVar.name).isEqualTo("a");
    assertThat(newAVar.type).isNull();
    assertThat(newAVar.input).isNull();

    assertThat(newAVar.getLineNumber()).isEqualTo(3);
  }

  @Test
  public void testArgumentsDeclaration() {
    Node root = Node.parse("function test(arg1, arg2) {}");
    String sourceName = "test.js";
    Scope parentScope = null;
    Scope scope = scopeCreator.createScope(root, parentScope);

    Scope.Var arg1Var = scope.getVariable("arg1");
    assertThat(arg1Var).isNotNull();
    assertThat(arg1Var.name).isEqualTo("arg1");
    assertThat(arg1Var.type).isNull();
    assertThat(arg1Var.input).isNull();

    Scope.Var arg2Var = scope.getVariable("arg2");
    assertThat(arg2Var).isNotNull();
    assertThat(arg2Var.name).isEqualTo("arg2");
    assertThat(arg2Var.type).isNull();
    assertThat(arg2Var.input).isNull();
  }

  @Test
  public void testArgumentsShadowing() {
    Node root = Node.parse("function test(arg1) {var arguments = 1;}");
    String sourceName = "test.js";
    Scope parentScope = null;
    Scope scope = scopeCreator.createScope(root, parentScope);

    assertThat(scope.getVariables().size()).isZero();

    redeclarationHandler.onRedeclaration(
        scope, "arguments", root.getChildAt(1), root, null, root);

    Scope.Var argumentsVar = scope.getVariable("arguments");
    assertThat(argumentsVar).isNull();

    compiler.report(root.getChildAt(1).getErrorMessages());
    JSError argumentsError = compiler.getLastError();
    assertThat(argumentsError).isNotNull();
    assertThat(argumentsError.getMessage()).isEqualTo(
        "Shadowing \"arguments\" is not allowed");
  }

  @Test
  public void testDuplicateDeclaration() {
    Node root = Node.parse("function test() {var a = 1; var a = 2;}");
    String sourceName = "test.js";
    Scope parentScope = null;
    Scope scope = scopeCreator.createScope(root, parentScope);

    assertThat(scope.getVariables().size()).isZero();

    redeclarationHandler.onRedeclaration(
        scope, "a", root.getChildAt(3), root, null, root);

    Scope.Var aVar = scope.getVariable("a");
    assertThat(aVar).isNotNull();
    assertThat(aVar.name).isEqualTo("a");
    assertThat(aVar.type).isNull();
    assertThat(aVar.input).isNull();

    JSDocInfo docInfo = root.getFirstChild().getJSDocInfo();
    assertThat(docInfo).isNotNull();
    assertThat(docInfo.getSuppressions().contains("duplicate")).isFalse();

    compiler.report(root.getChildAt(3).getErrorMessages());
    JSError duplicateError = compiler.getLastError();
    assertThat(duplicateError).isNotNull();
    assertThat(duplicateError.getMessage()).isEqualTo(
        "Variable a first declared in test.js");
  }

  @Test
  public void testDuplicateDeclarationWithSuppression() {
    Node root = Node.parse("function test() {/*@suppress duplicates*/ var a = 1; var a = 2;}");
    String sourceName = "test.js";
    Scope parentScope = null;
    Scope scope = scopeCreator.createScope(root, parentScope);

    assertThat(scope.getVariables().size()).isZero();

    redeclarationHandler.onRedeclaration(
        scope, "a", root.getChildAt(3), root, null, root);

    Scope.Var aVar = scope.getVariable("a");
    assertThat(aVar).isNotNull();
    assertThat(aVar.name).isEqualTo("a");
    assertThat(aVar.type).isNull();
    assertThat(aVar.input).isNull();

    JSDocInfo docInfo = root.getFirstChild().getJSDocInfo();
    assertThat(docInfo).isNotNull();
    assertThat(docInfo.getSuppressions().contains("duplicates")).isTrue();

    compiler.report(root.getChildAt(3).getErrorMessages());
    JSError duplicateError = compiler.getLastError();
    assertThat(duplicateError).isNull();
  }
}