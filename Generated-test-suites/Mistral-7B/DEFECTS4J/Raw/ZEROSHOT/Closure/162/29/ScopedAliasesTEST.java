package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformations(
        Lists.newArrayList(
            new AliasTransformation("goog.dom", "DOM"),
            new AliasTransformation("goog.dom.TagName", "TagName")));
    AliasTransformationHandler handler = new AliasTransformationHandler() {};
    Compiler compiler = new Compiler(options, handler);

    String code = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    Node root = new Node(code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    assertEquals("DOM.createElement(DOM.TagName.DIV);", root.toString());

    // Check errors were reported.
    assertEquals(4, compiler.getDiagnostics().size());
    DiagnosticType[] types = {
      DiagnosticType.error("JSC_GOOG_SCOPE_USED_IMPROPERLY"),
      DiagnosticType.error("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS"),
      DiagnosticType.error("JSC_GOOG_SCOPE_REFERENCES_THIS"),
      DiagnosticType.error("JSC_GOOG_SCOPE_USES_RETURN")
    };
    for (int i = 0; i < 4; i++) {
      DiagnosticType type = types[i];
      assertNotNull(compiler.getDiagnostics().get(i).getErrorType());
      assertEquals(type, compiler.getDiagnostics().get(i).getErrorType());
    }
  }

  @Test
  public void testScopedAliasesWithJSDocs() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformations(
        Lists.newArrayList(
            new AliasTransformation("goog.dom", "DOM"),
            new AliasTransformation("goog.dom.TagName", "TagName")));
    AliasTransformationHandler handler = new AliasTransformationHandler() {};
    Compiler compiler = new Compiler(options, handler);

    String code = "/**\n" +
        " * Test function.\n" +
        " * @return {number}\n" +
        " */\n" +
        "goog.scope(function() {" +
        "  /**\n" +
        "   * Test function.\n" +
        "   * @return {number}\n" +
        "   */\n" +
        "  function testFunction() {" +
        "    return 42;" +
        "  }" +
        "\n" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "  return testFunction();" +
        "});";

    Node root = new Node(code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    assertEquals("/**\n" +
        " * Test function.\n" +
        " * @return {number}\n" +
        " */\n" +
        "DOM.createElement(DOM.TagName.DIV);\n" +
        "return testFunction();", root.toString());

    // Check errors were reported.
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testScopedAliasesWithNestedScope() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformations(
        Lists.newArrayList(
            new AliasTransformation("goog.dom", "DOM"),
            new AliasTransformation("goog.dom.TagName", "TagName")));
    AliasTransformationHandler handler = new AliasTransformationHandler() {};
    Compiler compiler = new Compiler(options, handler);

    String code = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    var DIV = dom.TagName.DIV;" +
        "  });" +
        "});";

    Node root = new Node(code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    assertEquals("goog.scope(function() {});", root.toString());

    // Check errors were reported.
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_USED_IMPROPERLY"),
        compiler.getDiagnostics().get(0).getErrorType());
  }

  @Test
  public void testScopedAliasesWithReturn() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformations(
        Lists.newArrayList(
            new AliasTransformation("goog.dom", "DOM"),
            new AliasTransformation("goog.dom.TagName", "TagName")));
    AliasTransformationHandler handler = new AliasTransformationHandler() {};
    Compiler compiler = new Compiler(options, handler);

    String code = "goog.scope(function() {" +
        "  return 42;" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});";

    Node root = new Node(code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    assertEquals("goog.scope(function() { return 42; });", root.toString());

    // Check errors were reported.
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_USES_RETURN"),
        compiler.getDiagnostics().get(0).getErrorType());
  }

  @Test
  public void testScopedAliasesWithThrow() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformations(
        Lists.newArrayList(
            new AliasTransformation("goog.dom", "DOM"),
            new AliasTransformation("goog.dom.TagName", "TagName")));
    AliasTransformationHandler handler = new AliasTransformationHandler() {};
    Compiler compiler = new Compiler(options, handler);

    String code = "goog.scope(function() {" +
        "  throw new Error('Test')" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});";

    Node root = new Node(code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    assertEquals("goog.scope(function() { throw new Error('Test'); });", root.toString());

    // Check errors were reported.
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_USES_THROW"),
        compiler.getDiagnostics().get(0).getErrorType());
  }

  @Test
  public void testScopedAliasesWithThis() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformations(
        Lists.newArrayList(
            new AliasTransformation("goog.dom", "DOM"),
            new AliasTransformation("goog.dom.TagName", "TagName")));
    AliasTransformationHandler handler = new AliasTransformationHandler() {};
    Compiler compiler = new Compiler(options, handler);

    String code = "goog.scope(function() {" +
        "  this.test = 'Test';" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});";

    Node root = new Node(code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    assertEquals("goog.scope(function() { this.test = 'Test'; });", root.toString());

    // Check errors were reported.
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_REFERENCES_THIS"),
        compiler.getDiagnostics().get(0).getErrorType());
  }

  @Test
  public void testScopedAliasesWithVarDeclaration() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformations(
        Lists.newArrayList(
            new AliasTransformation("goog.dom", "DOM"),
            new AliasTransformation("goog.dom.TagName", "TagName")));
    AliasTransformationHandler handler = new AliasTransformationHandler() {};
    Compiler compiler = new Compiler(options, handler);

    String code = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var test = 42;" +
        "});";

    Node root = new Node(code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    assertEquals("goog.scope(function() {" +
        "  var test = 42;" +
        "  DOM.createElement(DOM.TagName.DIV);" +
        "});", root.toString());

    // Check errors were reported.
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL"),
        compiler.getDiagnostics().get(0).getErrorType());
  }

  @Test
  public void testScopedAliasesWithRedefinedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformations(
        Lists.newArrayList(
            new AliasTransformation("goog.dom", "DOM"),
            new AliasTransformation("goog.dom.TagName", "TagName")));
    AliasTransformationHandler handler = new AliasTransformationHandler() {};
    Compiler compiler = new Compiler(options, handler);

    String code = "goog.scope(function() {" +
        "  var DOM = 'Test';" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});";

    Node root = new Node(code);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, handler);
    scopedAliases.process(null, root);

    assertEquals("goog.scope(function() {" +
        "  var DOM = 'Test';" +
        "  DOM = goog.dom;" +
        "  var DIV = DOM.TagName.DIV;" +
        "});", root.toString());

    // Check errors were reported.
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.error("JSC_GOOG_SCOPE_ALIAS_REDEFINED"),
        compiler.getDiagnostics().get(0).getErrorType());
  }
}