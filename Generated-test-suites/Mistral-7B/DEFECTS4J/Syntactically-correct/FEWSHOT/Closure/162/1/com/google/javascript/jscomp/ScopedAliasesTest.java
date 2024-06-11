package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    Node root = createNode(
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "});");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_GOOG_SCOPE);
    AbstractCompiler compiler = new AbstractCompiler(options);

    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(
          String fileName, SourcePosition position, String alias, String qualifiedName) {
        System.out.println("Alias " + alias + " transformed to " + qualifiedName);
      }
    };

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that the alias definitions were applied and the goog.scope functions were removed.
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", root.toString());

    // Check that the aliases were reported correctly.
    List<AliasTransformation> transformations = transformationHandler.getTransformations();
    assertEquals(1, transformations.size());
    AliasTransformation transformation = transformations.get(0);
    assertEquals("DIV", transformation.getAlias());
    assertEquals("goog.dom.TagName.DIV", transformation.getQualifiedName());
  }

  @Test
  public void testScopedAliasesWithInvalidCalls() {
    Node root = createNode(
        "goog.scope(function() {" +
            "goog.scope(function() {});" + // Call to goog.scope inside a goog.scope
            "goog.scope(function(x, y) {});" + // Call to goog.scope with arguments
            "function googScope() {}" + // Function declaration inside a goog.scope
            "var x = goog.scope(function() {});" + // goog.scope as a variable name
            "goog.scope(function() { return 5; });" + // goog.scope returns a value
            "goog.scope(function() { throw new Error(); });" + // goog.scope throws an error
            "goog.scope(function() { this = 5; });" + // goog.scope cannot use 'this'
            "});");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that all errors were reported.
    assertFalse(scopedAliases.getErrors().isEmpty());
    assertEquals(12, scopedAliases.getErrors().size());

    List<Node> errors = Lists.newArrayList();
    for (DiagnosticType error : scopedAliases.getErrors()) {
      errors.add(error.getNode());
    }

    // Check that the errors are in the correct order.
    List<String> expectedErrorMessages = Lists.newArrayList(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        "JSC_GOOG_SCOPE_USES_RETURN",
        "JSC_GOOG_SCOPE_USES_THROW",
        "JSC_GOOG_SCOPE_REFERENCES_THIS",
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED"
    );

    for (int i = 0; i < errors.size(); i++) {
      assertEquals(expectedErrorMessages.get(i), errors.get(i).getString());
    }
  }

  @Test
  public void testScopedAliasesWithInvalidScopeCall() {
    Node root = createNode("goog.scope();");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that an error was reported for an empty goog.scope call.
    assertTrue(scopedAliases.getErrors().size() > 0);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", scopedAliases.getErrors().get(0).getType());
  }

  @Test
  public void testScopedAliasesWithValidCalls() {
    Node root = createNode(
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "goog.scope(function() { dom.createElement(goog.dom.TagName.DIV); });" +
            "});");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that no errors were reported.
    assertFalse(scopedAliases.getErrors().isEmpty());

    // Check that the alias definitions were applied and the goog.scope functions were removed.
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", root.toString());

    // Check that no aliases were reported.
    assertEquals(0, transformationHandler.getTransformations().size());
  }

  @Test
  public void testScopedAliasesWithValidCallsAndAliasUsages() {
    Node root = createNode(
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "goog.scope(function() { dom.createElement(goog.dom.TagName.DIV); });" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "});");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that no errors were reported.
    assertFalse(scopedAliases.getErrors().isEmpty());

    // Check that the alias definitions were applied and the goog.scope functions were removed.
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", root.toString());

    // Check that the alias usage was reported.
    List<AliasTransformation> transformations = transformationHandler.getTransformations();
    assertEquals(1, transformations.size());
    AliasTransformation transformation = transformations.get(0);
    assertEquals("DIV", transformation.getAlias());
    assertEquals("goog.dom.TagName.DIV", transformation.getQualifiedName());
  }

  @Test
  public void testScopedAliasesWithInvalidAliasDefinition() {
    Node root = createNode(
        "goog.scope(function() {" +
            "goog.scope(function() { var DIV = 5; });" +
            "});");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that an error was reported for an alias definition with a non-alias value.
    assertTrue(scopedAliases.getErrors().size() > 0);
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", scopedAliases.getErrors().get(0).getType());
  }

  @Test
  public void testScopedAliasesWithRedefinedAlias() {
    Node root = createNode(
        "goog.scope(function() {" +
            "goog.scope(function() { var DIV = goog.dom.TagName.DIV; });" +
            "goog.scope(function() { var DIV = 5; });" +
            "});");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that an error was reported for a redefined alias.
    assertTrue(scopedAliases.getErrors().size() > 0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", scopedAliases.getErrors().get(0).getType());
  }

  @Test
  public void testScopedAliasesWithAliasTransformation() {
    Node root = createNode(
        "goog.scope(function() {" +
            "goog.scope(function() { var DIV = goog.dom.TagName.DIV; });" +
            "goog.scope(function() { var DIV = goog.dom.createElement; });" +
            "});");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public void logAliasTransformation(
          String fileName, SourcePosition position, String alias, String qualifiedName) {
        System.out.println("Alias " + alias + " transformed to " + qualifiedName);
      }
    };

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that an error was reported for an alias definition with a non-alias value.
    assertTrue(scopedAliases.getErrors().isEmpty());

    // Check that the alias transformations were reported correctly.
    List<AliasTransformation> transformations = transformationHandler.getTransformations();
    assertEquals(2, transformations.size());

    AliasTransformation divAliasTransformation = transformations.get(0);
    assertEquals("DIV", divAliasTransformation.getAlias());
    assertEquals("goog.dom.TagName.DIV", divAliasTransformation.getQualifiedName());

    AliasTransformation createAliasTransformation = transformations.get(1);
    assertEquals("createElement", createAliasTransformation.getAlias());
    assertEquals("goog.dom.createElement", createAliasTransformation.getQualifiedName());

    // Check that the alias definitions were applied and the goog.scope functions were removed.
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", root.toString());
  }

  @Test
  public void testScopedAliasesWithJSDocInfo() {
    Node root = createNode(
        "/** @constructor */\n" +
        "goog.scope(function() {" +
        "  /** @type {number} */\n" +
        "  var x = goog.scope(function() { return 5; });" +
        "});");

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Check that no errors were reported.
    assertFalse(scopedAliases.getErrors().isEmpty());

    // Check that the alias definitions were applied and the goog.scope functions were removed.
    assertEquals("return 5", root.toString());

    // Check that the JSDoc info was not affected.
    JSDocInfo jsDocInfo = root.getFirstChild().getJSDocInfo();
    assertNotNull(jsDocInfo);
    assertEquals("@constructor", jsDocInfo.getTags().get(0).getName());
  }

  private Node createNode(String code) {
    RhinoRuntime runtime = new RhinoRuntime();
    runtime.eval(code);
    return runtime.getRootNode();
  }

  private void assertNull(Node node) {
    if (node != null) {
      fail("Expected null, but got: " + node.toString());
    }
  }
}