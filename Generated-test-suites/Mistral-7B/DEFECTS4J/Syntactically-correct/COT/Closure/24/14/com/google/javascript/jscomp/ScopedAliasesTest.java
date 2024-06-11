package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_EmptyScript() {
    Node externs = Node.NULL;
    Node root = Node.NULL;

    scopedAliases.process(externs, root);
    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_SimpleScopedAlias() {
    Node externs = Node.NULL;
    Node root = Node.newBlock("function test() { " +
        "goog.scope(function() { var dom = goog.dom; }); ");

    scopedAliases.process(externs, root);

    ArgumentCaptor<AliasTransformation> transformationCaptor =
        ArgumentCaptor.forClass(AliasTransformation.class);
    Mockito.verify(transformationHandler, Mockito.times(1)).logAliasTransformation(
        Mockito.eq("test.js"), Mockito.capture(transformationCaptor));

    AliasTransformation transformation = transformationCaptor.getValue();
    assertEquals("dom", transformation.getAliasName());
    assertEquals("goog.dom", transformation.getAliasValue());

    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_InvalidScopedAlias() {
    Node externs = Node.NULL;
    Node root = Node.newBlock("function test() { " +
        "goog.scope(function() { " +
        "this.test = 1; " +
        "}); " +
        "goog.scope(function() { " +
        "}); " +
        "}");

    scopedAliases.process(externs, root);

    ArgumentCaptor<DiagnosticType> errorCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.atLeastOnce(
            Mockito.and(
                Mockito.eq(root.getParent()),
                Mockito.eq(errorCaptor.capture()))));

    DiagnosticType error = errorCaptor.getValue();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", error.getTypeName());

    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_ScopedAliasWithReturn() {
    Node externs = Node.NULL;
    Node root = Node.newBlock("function test() { " +
        "goog.scope(function() { return 1; }); " +
        "}");

    scopedAliases.process(externs, root);

    ArgumentCaptor<DiagnosticType> errorCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.atLeastOnce(
            Mockito.and(
                Mockito.eq(root.getParent()),
                Mockito.eq(errorCaptor.capture()))));

    DiagnosticType error = errorCaptor.getValue();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", error.getTypeName());

    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_ScopedAliasWithThrow() {
    Node externs = Node.NULL;
    Node root = Node.newBlock("function test() { " +
        "goog.scope(function() { throw new Error('Test'); }); " +
        "}");

    scopedAliases.process(externs, root);

    ArgumentCaptor<DiagnosticType> errorCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.atLeastOnce(
            Mockito.and(
                Mockito.eq(root.getParent()),
                Mockito.eq(errorCaptor.capture()))));

    DiagnosticType error = errorCaptor.getValue();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", error.getTypeName());

    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_ScopedAliasWithRedefinedAlias() {
    Node externs = Node.NULL;
    Node root = Node.newBlock("function test() { " +
        "goog.scope(function() { var dom = goog.dom; }); " +
        "goog.scope(function() { var dom = goog.dom; }); " +
        "}");

    scopedAliases.process(externs, root);

    ArgumentCaptor<DiagnosticType> errorCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.atLeastOnce(
            Mockito.and(
                Mockito.eq(root.getParent()),
                Mockito.eq(errorCaptor.capture()))));

    DiagnosticType error = errorCaptor.getValue();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getTypeName());

    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_ScopedAliasWithNonAliasLocal() {
    Node externs = Node.NULL;
    Node root = Node.newBlock("function test() { " +
        "goog.scope(function() { var dom = goog.dom; var domElement = dom.createElement(TagName.DIV); }); " +
        "}");

    ArgumentCaptor<DiagnosticType> errorCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    scopedAliases.process(externs, root);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.atLeastOnce(
            Mockito.and(
                Mockito.eq(root.getParent()),
                Mockito.eq(errorCaptor.capture()))));

    DiagnosticType error = errorCaptor.getValue();
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", error.getTypeName());

    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_ScopedAliasWithInvalidParameters() {
    Node externs = Node.NULL;
    Node root = Node.newBlock("function test() { " +
        "goog.scope(function(a, b) { }); " +
        "}");

    ArgumentCaptor<DiagnosticType> errorCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    scopedAliases.process(externs, root);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.atLeastOnce(
            Mockito.and(
                Mockito.eq(root.getParent()),
                Mockito.eq(errorCaptor.capture()))));

    DiagnosticType error = errorCaptor.getValue();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", error.getTypeName());

    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_ScopedAliasWithInvalidAnonymousFunction() {
    Node externs = Node.NULL;
    Node root = Node.newBlock("function test() { " +
        "goog.scope(1); " +
        "}");

    ArgumentCaptor<DiagnosticType> errorCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    scopedAliases.process(externs, root);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.atLeastOnce(
            Mockito.and(
                Mockito.eq(root.getParent()),
                Mockito.eq(errorCaptor.capture()))));

    DiagnosticType error = errorCaptor.getValue();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", error.getTypeName());

    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_ScopedAliasWithDeepNestedFunction() {
    Node externs = Node.NULL;
    Node root = Node.newBlock("function test() { " +
        "function outer() { " +
        "goog.scope(function() { " +
        "function inner() { } " +
        "}); " +
        "} " +
        "}");

    scopedAliases.process(externs, root);

    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testHotSwapScript() {
    Node externs = Node.NULL;
    Node root = Node.newBlock("function test() { " +
        "goog.scope(function() { var dom = goog.dom; dom.createElement(TagName.DIV); }); " +
        "}");

    scopedAliases.hotSwapScript(root, null);

    ArgumentCaptor<AliasTransformation> transformationCaptor =
        ArgumentCaptor.forClass(AliasTransformation.class);
    Mockito.verify(transformationHandler, Mockito.times(1)).logAliasTransformation(
        Mockito.eq("test.js"), Mockito.capture(transformationCaptor));

    AliasTransformation transformation = transformationCaptor.getValue();
    assertEquals("dom", transformation.getAliasName());
    assertEquals("goog.dom", transformation.getAliasValue());

    ArgumentCaptor<Node> aliasDefinitionCaptor =
        ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<Node> scopeCallCaptor = ArgumentCaptor.forClass(Node.class);
    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    Mockito.verify(compiler, Mockito.atLeastOnce(
        Mockito.and(
            Mockito.eq(root),
            Mockito.eq(aliasDefinitionCaptor.capture()),
            Mockito.eq(scopeCallCaptor.capture()))));

    Node aliasDefinition = aliasDefinitionCaptor.getValue();
    Node scopeCall = scopeCallCaptor.getValue();

    assertEquals("goog.dom", aliasDefinition.getQualifiedName());
    assertEquals("dom", scopeCall.getLastChild().getLastChild().getString());

    Mockito.verify(preprocessorSymbolTable, Mockito.times(1)).addReference(
        Mockito.eq(scopeCall.getFirstChild()));
  }
}