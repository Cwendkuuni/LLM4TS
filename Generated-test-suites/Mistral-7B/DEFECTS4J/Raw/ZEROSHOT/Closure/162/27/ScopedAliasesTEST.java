package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CompilerOptions.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.ErrorList;
import com.google.javascript.jscomp.CompilerOptions.SourceFile;
import com.google.javascript.jscomp.CompilerOptions.TypeChecker;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.collect.CollectingErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testValidScopedAlias() {
    // Arrange
    SourceFile input = new SourceFile("test.js", "test");
    Node root = input.getAST();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    options.setSourceMap(false);
    options.setEmitWarning(false);
    options.setErrorReporter(new CollectingErrorReporter());

    // Act
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    // Assert
    ErrorList errors = options.getErrors();
    assertEquals(0, errors.size());
    TypeChecker typeChecker = options.getTypeChecker();

    // Check that alias was introduced
    Var aliasVar = typeChecker.getVariable("scopeAlias");
    assertEquals("dom", aliasVar.getName());
    assertEquals("goog.dom", aliasVar.getInitialValue().toString());

    // Check that alias was used
    Node node = NodeTraversal.findFirstDescendantWithName(root, "createElement");
    assertEquals("goog.dom.createElement", node.toString());
  }

  @Test
  public void testInvalidScopedAlias() {
    // Arrange
    SourceFile input = new SourceFile("test.js", "test");
    Node root = input.getAST();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    options.setSourceMap(false);
    options.setEmitWarning(false);
    options.setErrorReporter(new CollectingErrorReporter());

    // Act
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    // Assert
    ErrorList errors = options.getErrors();
    List<JSErrorReporter.DiagnosticType> errorTypes =
        ImmutableList.of(
            JSC_GOOG_SCOPE_USED_IMPROPERLY,
            JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS,
            JSC_GOOG_SCOPE_REFERENCES_THIS,
            JSC_GOOG_SCOPE_USES_RETURN,
            JSC_GOOG_SCOPE_USES_THROW,
            JSC_GOOG_SCOPE_ALIAS_REDEFINED,
            JSC_GOOG_SCOPE_NON_ALIAS_LOCAL);
    for (JSErrorReporter.DiagnosticType errorType : errorTypes) {
      assertFalse("Unexpected error", errors.containsError(errorType));
    }
  }

  @Test
  public void testInvalidScopedAliasParameter() {
    // Arrange
    SourceFile input = new SourceFile("test.js", "test");
    Node root = input.getAST();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    options.setSourceMap(false);
    options.setEmitWarning(false);
    options.setErrorReporter(new CollectingErrorReporter());

    // Act
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    // Assert
    ErrorList errors = options.getErrors();
    List<JSErrorReporter.DiagnosticType> errorTypes =
        ImmutableList.of(JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS);
    for (JSErrorReporter.DiagnosticType errorType : errorTypes) {
      Node node = NodeTraversal.findFirstDescendantWithName(root, "googScopeWithParam");
      assertEquals(1, node.getChildCount());
      Node paramNode = node.getChildAtIndex(0);
      assertEquals("function(p)", paramNode.toString());
      assertFalse("Unexpected error", errors.containsError(errorType));
    }
  }

  @Test
  public void testInvalidScopedAliasReturn() {
    // Arrange
    SourceFile input = new SourceFile("test.js", "test");
    Node root = input.getAST();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    options.setSourceMap(false);
    options.setEmitWarning(false);
    options.setErrorReporter(new CollectingErrorReporter());

    // Act
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    // Assert
    ErrorList errors = options.getErrors();
    List<JSErrorReporter.DiagnosticType> errorTypes =
        ImmutableList.of(JSC_GOOG_SCOPE_USES_RETURN);
    for (JSErrorReporter.DiagnosticType errorType : errorTypes) {
      Node node = NodeTraversal.findFirstDescendantWithName(root, "googScopeWithReturn");
      assertEquals("function() { return; }", node.toString());
      assertFalse("Unexpected error", errors.containsError(errorType));
    }
  }

  @Test
  public void testInvalidScopedAliasThrow() {
    // Arrange
    SourceFile input = new SourceFile("test.js", "test");
    Node root = input.getAST();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    options.setSourceMap(false);
    options.setEmitWarning(false);
    options.setErrorReporter(new CollectingErrorReporter());

    // Act
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    // Assert
    ErrorList errors = options.getErrors();
    List<JSErrorReporter.DiagnosticType> errorTypes =
        ImmutableList.of(JSC_GOOG_SCOPE_USES_THROW);
    for (JSErrorReporter.DiagnosticType errorType : errorTypes) {
      Node node = NodeTraversal.findFirstDescendantWithName(root, "googScopeWithThrow");
      assertEquals("function() { throw new Error('Test'); }", node.toString());
      assertFalse("Unexpected error", errors.containsError(errorType));
    }
  }

  @Test
  public void testInvalidScopedAliasThis() {
    // Arrange
    SourceFile input = new SourceFile("test.js", "test");
    Node root = input.getAST();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    options.setSourceMap(false);
    options.setEmitWarning(false);
    options.setErrorReporter(new CollectingErrorReporter());

    // Act
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    // Assert
    ErrorList errors = options.getErrors();
    List<JSErrorReporter.DiagnosticType> errorTypes =
        ImmutableList.of(JSC_GOOG_SCOPE_REFERENCES_THIS);
    for (JSErrorReporter.DiagnosticType errorType : errorTypes) {
      Node node = NodeTraversal.findFirstDescendantWithName(root, "googScopeWithThis");
      assertEquals("function() { this; }", node.toString());
      assertFalse("Unexpected error", errors.containsError(errorType));
    }
  }

  @Test
  public void testDuplicateAlias() {
    // Arrange
    SourceFile input = new SourceFile("test.js", "test");
    Node root = input.getAST();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    options.setSourceMap(false);
    options.setEmitWarning(false);
    options.setErrorReporter(new CollectingErrorReporter());

    // Act
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    // Assert
    ErrorList errors = options.getErrors();
    List<JSErrorReporter.DiagnosticType> errorTypes =
        ImmutableList.of(JSC_GOOG_SCOPE_ALIAS_REDEFINED);
    for (JSErrorReporter.DiagnosticType errorType : errorTypes) {
      Node node = NodeTraversal.findFirstDescendantWithName(root, "alias");
      assertEquals("function() { return alias; }", node.toString());
      Node aliasNode = NodeTraversal.findFirstDescendantWithName(root, "alias");
      assertEquals("function() { return alias; }", aliasNode.toString());
      assertTrue("Unexpected error", errors.containsError(errorType));
      assertEquals(1, errors.size());
    }
  }

  @Test
  public void testGlobalAlias() {
    // Arrange
    SourceFile input = new SourceFile("test.js", "test");
    Node root = input.getAST();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    options.setSourceMap(false);
    options.setEmitWarning(false);
    options.setErrorReporter(new CollectingErrorReporter());

    // Act
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    // Assert
    ErrorList errors = options.getErrors();
    List<JSErrorReporter.DiagnosticType> errorTypes =
        ImmutableList.of(JSC_GOOG_SCOPE_USED_IMPROPERLY);
    for (JSErrorReporter.DiagnosticType errorType : errorTypes) {
      Node node = NodeTraversal.findFirstDescendantWithName(root, "globalAlias");
      assertEquals("function() { return globalAlias; }", node.toString());
      assertFalse("Unexpected error", errors.containsError(errorType));
    }
  }

  @Test
  public void testNonAliasLocalVariable() {
    // Arrange
    SourceFile input = new SourceFile("test.js", "test");
    Node root = input.getAST();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    options.setSourceMap(false);
    options.setEmitWarning(false);
    options.setErrorReporter(new CollectingErrorReporter());

    // Act
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    // Assert
    ErrorList errors = options.getErrors();
    List<JSErrorReporter.DiagnosticType> errorTypes =
        ImmutableList.of(JSC_GOOG_SCOPE_NON_ALIAS_LOCAL);
    for (JSErrorReporter.DiagnosticType errorType : errorTypes) {
      Node node = NodeTraversal.findFirstDescendantWithName(root, "nonAliasLocalVariable");
      assertEquals("var nonAliasLocalVariable = 42;", node.toString());
      assertTrue("Unexpected error", errors.containsError(errorType));
      assertEquals(1, errors.size());
    }
  }

  @Test
  public void testMultipleAliases() {
    // Arrange
    SourceFile input = new SourceFile("test.js", "test");
    Node root = input.getAST();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    options.setSourceMap(false);
    options.setEmitWarning(false);
    options.setErrorReporter(new CollectingErrorReporter());

    // Act
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    // Assert
    ErrorList errors = options.getErrors();
    List<JSErrorReporter.DiagnosticType> errorTypes =
        ImmutableList.of(JSC_GOOG_SCOPE_ALIAS_REDEFINED);
    for (JSErrorReporter.DiagnosticType errorType : errorTypes) {
      Node node = NodeTraversal.findFirstDescendantWithName(root, "alias1");
      assertEquals("function() { return alias1; }", node.toString());
      Node aliasNode1 = NodeTraversal.findFirstDescendantWithName(root, "alias2");
      assertEquals("function() { return alias2; }", aliasNode1.toString());
      Node aliasNode2 = NodeTraversal.findFirstDescendantWithName(root, "alias1");
      assertEquals("function() { return alias1; }", aliasNode2.toString());
      assertTrue("Unexpected error", errors.containsError(errorType));
      assertEquals(1, errors.size());
    }
  }
}