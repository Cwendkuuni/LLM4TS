package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckLevel.Warning;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    SourceFile sourceFile = Parser.parseJavaScript("function evil() { this.useful = undefined; }", "test.js");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel checkLevel = new CheckLevel(Warning.UNSAFE_GLOBAL_THIS);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, checkLevel);

    NodeTraversal traversal = new NodeTraversal(sourceFile.ast, checker);
    traversal.traverseAll();

    int diagnosticCount = compiler.getNumDiagnostics();
    assertThat(diagnosticCount).isGreaterThan(0);
    Node diagnostic = compiler.getDiagnostic(0);
    DiagnosticType diagnosticType = diagnostic.getType();
    String diagnosticMessage = diagnostic.getMessage();
    assertThat(diagnosticType).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
    assertThat(diagnosticMessage).contains("dangerous use of the global 'this' object");
  }

  @Test
  public void testCheckGlobalThis_PrototypePropertyAccess() {
    SourceFile sourceFile = Parser.parseJavaScript("function evil() { Object.prototype.useful = undefined; }", "test.js");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel checkLevel = new CheckLevel(Warning.UNSAFE_GLOBAL_THIS);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, checkLevel);

    NodeTraversal traversal = new NodeTraversal(sourceFile.ast, checker);
    traversal.traverseAll();

    int diagnosticCount = compiler.getNumDiagnostics();
    assertThat(diagnosticCount).isEqualTo(0);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    SourceFile sourceFile = Parser.parseJavaScript("/** @this {Object} */ function evil() { this.useful = undefined; }", "test.js");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel checkLevel = new CheckLevel(Warning.UNSAFE_GLOBAL_THIS);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, checkLevel);

    NodeTraversal traversal = new NodeTraversal(sourceFile.ast, checker);
    traversal.traverseAll();

    int diagnosticCount = compiler.getNumDiagnostics();
    assertThat(diagnosticCount).isEqualTo(0);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithConstructorAnnotation() {
    SourceFile sourceFile = Parser.parseJavaScript("function evil() { /** @constructor */ this.useful = undefined; }", "test.js");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel checkLevel = new CheckLevel(Warning.UNSAFE_GLOBAL_THIS);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, checkLevel);

    NodeTraversal traversal = new NodeTraversal(sourceFile.ast, checker);
    traversal.traverseAll();

    int diagnosticCount = compiler.getNumDiagnostics();
    assertThat(diagnosticCount).isEqualTo(0);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    SourceFile sourceFile = Parser.parseJavaScript("function evil() { this.useful = undefined; }", "test.js");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel checkLevel = new CheckLevel(Warning.UNSAFE_GLOBAL_THIS);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, checkLevel);

    NodeTraversal traversal = new NodeTraversal(sourceFile.ast, checker);
    traversal.traverseAll();

    int diagnosticCount = compiler.getNumDiagnostics();
    JSDocInfo functionJsDocInfo = sourceFile.getFunction(0).getJSDocInfo();
    boolean hasThisAnnotation = functionJsDocInfo != null && functionJsDocInfo.hasThisType();
    assertThat(diagnosticCount).isGreaterThanOrEqualTo(hasThisAnnotation ? 0 : 1);
  }

  @Test
  public void testCheckGlobalThis_AssignmentLeftSide() {
    SourceFile sourceFile = Parser.parseJavaScript("var a = this;", "test.js");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel checkLevel = new CheckLevel(Warning.UNSAFE_GLOBAL_THIS);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, checkLevel);

    NodeTraversal traversal = new NodeTraversal(sourceFile.ast, checker);
    traversal.traverseAll();

    int diagnosticCount = compiler.getNumDiagnostics();
    assertThat(diagnosticCount).isEqualTo(0);
  }

  @Test
  public void testCheckGlobalThis_AssignmentRightSideWithPropertyAccess() {
    SourceFile sourceFile = Parser.parseJavaScript("var a = {}; a.this = this;", "test.js");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel checkLevel = new CheckLevel(Warning.UNSAFE_GLOBAL_THIS);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, checkLevel);

    NodeTraversal traversal = new NodeTraversal(sourceFile.ast, checker);
    traversal.traverseAll();

    int diagnosticCount = compiler.getNumDiagnostics();
    assertThat(diagnosticCount).isGreaterThan(0);
  }

  @Test
  public void testCheckGlobalThis_AssignmentRightSideWithThisAnnotatedFunction() {
    SourceFile sourceFile = Parser.parseJavaScript(
        "var a = function evil() { this.useful = undefined; }();", "test.js");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel checkLevel = new CheckLevel(Warning.UNSAFE_GLOBAL_THIS);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, checkLevel);

    NodeTraversal traversal = new NodeTraversal(sourceFile.ast, checker);
    traversal.traverseAll();

    int diagnosticCount = compiler.getNumDiagnostics();
    assertThat(diagnosticCount).isGreaterThan(0);
  }

  @Test
  public void testCheckGlobalThis_EmptyFunction() {
    SourceFile sourceFile = Parser.parseJavaScript("function(){}", "test.js");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel checkLevel = new CheckLevel(Warning.UNSAFE_GLOBAL_THIS);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, checkLevel);

    NodeTraversal traversal = new NodeTraversal(sourceFile.ast, checker);
    traversal.traverseAll();

    int diagnosticCount = compiler.getNumDiagnostics();
    assertThat(diagnosticCount).isEqualTo(0);
  }
}