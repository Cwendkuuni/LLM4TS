package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Diagnostics diagnostics = compiler.compile(Node.fromString(code), null, null);

    assertEquals(0, diagnostics.getNumberOfErrors());

    String transformedCode = compiler.getSourceFile("test.js").getContent();
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV);", transformedCode);
  }

  @Test
  public void testScopedAliasesWithErrors() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement('DIV');" + // invalid argument
        "});";

    Diagnostics diagnostics = compiler.compile(Node.fromString(code), null, null);

    List<DiagnosticType> expectedErrors = Lists.newArrayList(
        DiagnosticType.error("JSC_GOOG_SCOPE_USED_IMPROPERLY"),
        DiagnosticType.error("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS")
    );

    assertEquals(expectedErrors.size(), diagnostics.getNumberOfErrors());
    assertEquals(expectedErrors, diagnostics.getDiagnostics());
  }

  @Test
  public void testScopedAliasesWithThisKeyword() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String code =
        "goog.scope(function() {" +
        "  this.dom = goog.dom;" +
        "});";

    Diagnostics diagnostics = assertThrows(
        RuntimeException.class,
        () -> compiler.compile(Node.fromString(code), null, null)
    );

    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_REFERENCES_THIS"),
        diagnostics.getDiagnostics().get(0));
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String code =
        "goog.scope(function() {" +
        "  return 'foo';" +
        "});";

    Diagnostics diagnostics = assertThrows(
        RuntimeException.class,
        () -> compiler.compile(Node.fromString(code), null, null)
    );

    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_USES_RETURN"),
        diagnostics.getDiagnostics().get(0));
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String code =
        "goog.scope(function() {" +
        "  throw new Error('foo');" +
        "});";

    Diagnostics diagnostics = assertThrows(
        RuntimeException.class,
        () -> compiler.compile(Node.fromString(code), null, null)
    );

    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_USES_THROW"),
        diagnostics.getDiagnostics().get(0));
  }

  @Test
  public void testScopedAliasesWithRedefinedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String code =
        "goog.scope(function() {" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var DIV = 'bar';" +
        "});";

    Diagnostics diagnostics = compiler.compile(Node.fromString(code), null, null);

    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_ALIAS_REDEFINED"),
        diagnostics.getDiagnostics().get(0));
  }

  @Test
  public void testScopedAliasesWithNonAliasLocalVariable() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String code =
        "goog.scope(function() {" +
        "  var x = 1;" +
        "});";

    Diagnostics diagnostics = compiler.compile(Node.fromString(code), null, null);

    assertEquals(1, diagnostics.getNumberOfErrors());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL"),
        diagnostics.getDiagnostics().get(0));
  }

  @Test
  public void testScopedAliasesWithNestedScopes() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String code =
        "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var DIV = goog.dom.TagName.DIV;" +
        "  });" +
        "});";

    Diagnostics diagnostics = compiler.compile(Node.fromString(code), null, null);

    assertEquals(0, diagnostics.getNumberOfErrors());
  }

  @Test
  public void testScopedAliasesWithAliasDefinitionOrder() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String code =
        "goog.scope(function() {" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var BUTTON = goog.dom.TagName.BUTTON;" +
        "});";

    Diagnostics diagnostics = compiler.compile(Node.fromString(code), null, null);

    assertEquals(0, diagnostics.getNumberOfErrors());

    String transformedCode = compiler.getSourceFile("test.js").getContent();
    assertEquals(
        "goog.dom.TagName.BUTTON, goog.dom.TagName.DIV;", transformedCode);
  }
}