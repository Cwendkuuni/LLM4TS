package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Tests for ScopedAliases class.
 */
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private AliasTransformationHandler transformationHandler;
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
  }

  @Test
  public void testScopedAliases() {
    // Given
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.createString("goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  goog.dom.createElement(DIV);\n" +
            "});");

    scopedAliases.process(null, root);

    // Then
    ArgumentCaptor<AliasTransformation> transformationArgumentCaptor =
        ArgumentCaptor.forClass(AliasTransformation.class);
    AliasTransformation transformation = Mockito.capture(transformationHandler)
        .logAliasTransformation(Mockito.anyString(), Mockito.eq(null));
    assertEquals(1, transformation.getAliases().size());
    assertEquals("dom", transformation.getAliases().get(0).getName());
    assertEquals("goog.dom", transformation.getAliases().get(0).getValue());
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithImproperUsage() {
    // Given
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.createString("goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  goog.dom.createElement(DIV);\n" +
            "  goog.scope(function() {\n" +
            "    goog.dom.createElement(DIV);\n" +
            "  });\n" +
            "});");

    // Then
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithBadParameters() {
    // Given
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.createString("goog.scope(function googScopeFunction() {\n" +
            "  goog.scope(function() {\n" +
            "  });\n" +
            "});");

    // Then
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithThisReference() {
    // Given
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.createString("goog.scope(function() {\n" +
            "  this.test = 'test';\n" +
            "});");

    // Then
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithReturnStatement() {
    // Given
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.createString("goog.scope(function() {\n" +
            "  return 42;\n" +
            "});");

    // Then
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithThrowStatement() {
    // Given
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.createString("goog.scope(function() {\n" +
            "  throw new Error('Test Error');\n" +
            "});");

    // Then
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithAliasRedefined() {
    // Given
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.createString("goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  var DIV = 'DIV';\n" +
            "  goog.dom.createElement(DIV);\n" +
            "});");

    // Then
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithNonAliasLocalVariable() {
    // Given
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.createString("goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var test = dom.TagName.DIV;\n" +
            "});");

    // Then
    scopedAliases.process(null, root);
  }
}