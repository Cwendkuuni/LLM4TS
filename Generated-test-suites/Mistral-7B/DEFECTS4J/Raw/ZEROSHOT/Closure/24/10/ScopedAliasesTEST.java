package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

/**
 * Tests for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliasesProcessing() {
    // Setup mocks.
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    // Create ScopedAliases instance.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test processing.
    Node rootNode = Node.newCompilationUnit("test.js");
    scopedAliases.process(null, rootNode);

    // Assert no errors.
    assertEquals(0, scopedAliases.getTraversal().getDiagnostics().size());
  }

  @Test
  public void testImproperlyUsedGoogScope() {
    // Setup mocks.
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    // Create ScopedAliases instance.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Create test root node.
    Node rootNode = Node.newCompilationUnit("test.js");

    // Add improperly used goog.scope.
    Node googScopeCall = Node.newCall(
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        "scope",
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        Node.newList(
            Node.newFunction(
                Node.newList(
                    Node.newName("var1", Node.NO_SOURCE_POSITION),
                    Node.newName("var2", Node.NO_SOURCE_POSITION)
                ),
                Node.NEW_EXPR,
                "function() {",
                "  // Do something with var1 and var2.",
                "}",
                Node.NO_SOURCE_POSITION
            )
        )
    );

    rootNode.addChild(googScopeCall);

    // Process.
    scopedAliases.process(null, rootNode);

    // Assert errors.
    Collection<DiagnosticType> diagnostics = scopedAliases.getTraversal().getDiagnostics();
    assertNotNull(diagnostics);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.iterator().next().getType());
  }

  @Test
  public void testGoogScopeWithBadParameters() {
    // Setup mocks.
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    // Create ScopedAliases instance.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Create test root node.
    Node rootNode = Node.newCompilationUnit("test.js");

    // Add goog.scope with bad parameters.
    Node googScopeCall = Node.newCall(
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        "scope",
        Node.newList(
            Node.newName("var1", Node.NO_SOURCE_POSITION),
            Node.newFunction(
                Node.newList(
                    Node.newName("param1", Node.NO_SOURCE_POSITION),
                    Node.newName("param2", Node.NO_SOURCE_POSITION)
                ),
                Node.NEW_EXPR,
                "function(p1, p2) {",
                "  // Do something with p1 and p2.",
                "}",
                Node.NO_SOURCE_POSITION
            )
        )
    );

    rootNode.addChild(googScopeCall);

    // Process.
    scopedAliases.process(null, rootNode);

    // Assert errors.
    Collection<DiagnosticType> diagnostics = scopedAliases.getTraversal().getDiagnostics();
    assertNotNull(diagnostics);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.iterator().next().getType());
  }

  @Test
  public void testGoogScopeReferencesThis() {
    // Setup mocks.
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    // Create ScopedAliases instance.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Create test root node.
    Node rootNode = Node.newCompilationUnit("test.js");

    // Add goog.scope that references 'this'.
    Node googScopeCall = Node.newCall(
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        "scope",
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        Node.newList(
            Node.newFunction(
                Node.NO_LIST,
                Node.NEW_EXPR,
                "function() {",
                "  this.var1 = 1;",
                "}",
                Node.NO_SOURCE_POSITION
            )
        )
    );

    rootNode.addChild(googScopeCall);

    // Process.
    scopedAliases.process(null, rootNode);

    // Assert errors.
    Collection<DiagnosticType> diagnostics = scopedAliases.getTraversal().getDiagnostics();
    assertNotNull(diagnostics);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.iterator().next().getType());
  }

  @Test
  public void testGoogScopeUsesReturn() {
    // Setup mocks.
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    // Create ScopedAliases instance.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Create test root node.
    Node rootNode = Node.newCompilationUnit("test.js");

    // Add goog.scope that uses 'return'.
    Node googScopeCall = Node.newCall(
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        "scope",
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        Node.newList(
            Node.newFunction(
                Node.NO_LIST,
                Node.NEW_EXPR,
                "function() {",
                "  return 1;",
                "}",
                Node.NO_SOURCE_POSITION
            )
        )
    );

    rootNode.addChild(googScopeCall);

    // Process.
    scopedAliases.process(null, rootNode);

    // Assert errors.
    Collection<DiagnosticType> diagnostics = scopedAliases.getTraversal().getDiagnostics();
    assertNotNull(diagnostics);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.iterator().next().getType());
  }

  @Test
  public void testGoogScopeUsesThrow() {
    // Setup mocks.
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    // Create ScopedAliases instance.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Create test root node.
    Node rootNode = Node.newCompilationUnit("test.js");

    // Add goog.scope that uses 'throw'.
    Node googScopeCall = Node.newCall(
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        "scope",
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        Node.newList(
            Node.newFunction(
                Node.NO_LIST,
                Node.NEW_EXPR,
                "function() {",
                "  throw new Error('test');",
                "}",
                Node.NO_SOURCE_POSITION
            )
        )
    );

    rootNode.addChild(googScopeCall);

    // Process.
    scopedAliases.process(null, rootNode);

    // Assert errors.
    Collection<DiagnosticType> diagnostics = scopedAliases.getTraversal().getDiagnostics();
    assertNotNull(diagnostics);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.iterator().next().getType());
  }

  @Test
  public void testAliasesRedefined() {
    // Setup mocks.
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    // Create ScopedAliases instance.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Create test root node.
    Node rootNode = Node.newCompilationUnit("test.js");

    // Add scoped aliases with redefined alias.
    Node googScopeCall = Node.newCall(
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        "scope",
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        Node.newList(
            Node.newFunction(
                Node.NO_LIST,
                Node.NEW_EXPR,
                "function() {",
                "  var dom = goog.dom; // First alias definition.",
                "  var DIV = goog.dom.TagName.DIV; // Second alias definition.",
                "}",
                Node.NO_SOURCE_POSITION
            )
        )
    );

    rootNode.addChild(googScopeCall);

    // Process.
    scopedAliases.process(null, rootNode);

    // Assert errors.
    Collection<DiagnosticType> diagnostics = scopedAliases.getTraversal().getDiagnostics();
    assertNotNull(diagnostics);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.iterator().next().getType());
  }

  @Test
  public void testNonAliasLocalVariableInGoogScope() {
    // Setup mocks.
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    // Create ScopedAliases instance.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Create test root node.
    Node rootNode = Node.newCompilationUnit("test.js");

    // Add goog.scope with non-alias local variable.
    Node googScopeCall = Node.newCall(
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        "scope",
        Node.newName("goog", Node.NO_SOURCE_POSITION),
        Node.newList(
            Node.newFunction(
                Node.NO_LIST,
                Node.NEW_EXPR,
                "function() {",
                "  var var1 = 1; // Non-alias local variable.",
                "}",
                Node.NO_SOURCE_POSITION
            )
        )
    );

    rootNode.addChild(googScopeCall);

    // Process.
    scopedAliases.process(null, rootNode);

    // Assert errors.
    Collection<DiagnosticType> diagnostics = scopedAliases.getTraversal().getDiagnostics();
    assertNotNull(diagnostics);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.iterator().next().getType());
  }

  // Getters and setters for private fields.
  private ScopedAliases.Traversal getTraversal(ScopedAliases scopedAliases) {
    return scopedAliases.getTraversal();
  }

}