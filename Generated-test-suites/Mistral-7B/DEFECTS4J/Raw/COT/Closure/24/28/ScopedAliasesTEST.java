package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test class for ScopedAliases.
 */
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

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testProcess_NoGoogScopeCalls() {
    Node root = Node.createTree("function foo() {}");
    scopedAliases.process(null, root);
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testProcess_SingleGoogScopeCall() {
    String source = "goog.scope(function() { var a = 42; })";
    Node root = Node.createTree(source);

    when(compiler.report(mock(NodeTraversal.class).makeError(Mockito.any(),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))).thenReturn(null);

    scopedAliases.process(null, root);

    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testProcess_MultipleGoogScopeCalls() {
    String source = "goog.scope(function() { var a = 1; }) " +
        "goog.scope(function() { var a = 2; })";
    Node root = Node.createTree(source);

    when(compiler.report(mock(NodeTraversal.class).makeError(Mockito.any(),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))).thenReturn(null);

    scopedAliases.process(null, root);

    assertEquals(2, scopedAliases.getAliasUsages().size());
    assertEquals(2, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(2, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testProcess_ImproperlyUsedGoogScopeCall() {
    String source = "goog.scope(function() { return 42; })";
    Node root = Node.createTree(source);

    when(compiler.report(mock(NodeTraversal.class).makeError(Mockito.any(),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))).thenReturn(null);

    scopedAliases.process(null, root);

    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testProcess_GoogScopeCallWithBadParameters() {
    String source = "goog.scope(function() {}, 1)";
    Node root = Node.createTree(source);

    when(compiler.report(mock(NodeTraversal.class).makeError(Mockito.any(),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))).thenReturn(null);

    scopedAliases.process(null, root);

    assertThrows(AssertionError.class, () -> {
      scopedAliases.getScopeCalls().get(0);
    });
  }

  @Test
  public void testProcess_GoogScopeCallWithThisKeyword() {
    String source = "goog.scope(function() { this.a = 42; })";
    Node root = Node.createTree(source);

    when(compiler.report(mock(NodeTraversal.class).makeError(Mockito.any(),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))).thenReturn(null);

    scopedAliases.process(null, root);

    assertThrows(AssertionError.class, () -> {
      scopedAliases.getAliasUsages().get(0).applyAlias();
    });
  }

  @Test
  public void testProcess_GoogScopeCallWithReturnStatement() {
    String source = "goog.scope(function() { return 42; })";
    Node root = Node.createTree(source);

    when(compiler.report(mock(NodeTraversal.class).makeError(Mockito.any(),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))).thenReturn(null);

    scopedAliases.process(null, root);

    assertThrows(AssertionError.class, () -> {
      scopedAliases.getScopeCalls().get(0);
    });
  }

  @Test
  public void testProcess_GoogScopeCallWithThrowStatement() {
    String source = "goog.scope(function() { throw new Error(); })";
    Node root = Node.createTree(source);

    when(compiler.report(mock(NodeTraversal.class).makeError(Mockito.any(),
        Mockito.any(DiagnosticType.class), Mockito.anyString()))).thenReturn(null);

    scopedAliases.process(null, root);

    assertThrows(AssertionError.class, () -> {
      scopedAliases.getScopeCalls().get(0);
    });
  }

  @Test
  public void testProcess_AliasesWithSameName() {
    String source = "goog.scope(function() { var a = 1; }) " +
        "goog.scope(function() { var a = 2; })";
    Node root = Node.createTree(source);

    when(compiler.report(mock(NodeTraversal.class).makeError(Mockito.any(),
        GOOG_SCOPE_ALIAS_REDEFINED, Mockito.anyString()))).thenReturn(null);

    scopedAliases.process(null, root);

    assertThrows(AssertionError.class, () -> {
      scopedAliases.getAliasDefinitionsInOrder().get(1);
    });
  }

  @Test
  public void testProcess_AliasesWithNonAliasLocalVariable() {
    String source = "goog.scope(function() { var a = 1; var b = a; })";
    Node root = Node.createTree(source);

    when(compiler.report(mock(NodeTraversal.class).makeError(Mockito.any(),
        GOOG_SCOPE_NON_ALIAS_LOCAL, Mockito.anyString()))).thenReturn(null);

    scopedAliases.process(null, root);

    assertThrows(AssertionError.class, () -> {
      scopedAliases.getAliasDefinitionsInOrder().get(1);
    });
  }

  @Test
  public void testApplyAlias() {
    String source = "goog.scope(function() { var a = 1; })";
    Node root = Node.createTree(source);

    scopedAliases.process(null, root);

    Node aliasDefinition = scopedAliases.getAliasDefinitionsInOrder().get(0);

    ((AliasedNode) scopedAliases.getAliasUsages().get(0)).applyAlias();

    assertEquals("a", aliasDefinition.getIdent().getName());
  }

  @Test
  public void testApplyAliasType() {
    String source = "goog.scope(function() { var a = new Foo(); })";
    Node root = Node.createTree(source);

    scopedAliases.process(null, root);

    Node aliasDefinition = scopedAliases.getAliasDefinitionsInOrder().get(0);

    ((AliasedTypeNode) scopedAliases.getAliasUsages().get(0)).applyAlias();

    assertEquals("Foo", aliasDefinition.getIdent().getName());
  }

  @Test
  public void testHotSwapScript() {
    String source = "function foo() {}";
    Node root = Node.createTree(source);

    Node oldRoot = root.cloneTree();

    scopedAliases.process(null, root);

    assertFalse(scopedAliases.hasErrors());

    scopedAliases.hotSwapScript(oldRoot, root);

    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testHotSwapScriptWithGoogScopeCalls() {
    String source = "goog.scope(function() { var a = 1; })";
    Node root = Node.createTree(source);

    Node oldRoot = root.cloneTree();

    scopedAliases.process(null, root);

    assertFalse(scopedAliases.hasErrors());

    scopedAliases.hotSwapScript(oldRoot, root);

    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testApplyAliases() {
    String source = "function foo(a) { return a * 2; } goog.scope(function() { var a = 3; })";
    Node root = Node.createTree(source);

    scopedAliases.process(null, root);

    assertFalse(scopedAliases.hasErrors());

    scopedAliases.applyAliases();

    assertEquals("a", scopedAliases.getAliasDefinitionsInOrder().get(0).getIdent().getName());

    Node functionNode = root.getLastChild();
    Node newFunctionNode = functionNode.getLastChild();

    Node updatedFunctionNode = newFunctionNode.getLastChild();

    assertEquals(3, updatedFunctionNode.getDottedMember(0).getInt());
    assertEquals(6, functionNode.getExpression().calculateNearlyIntValue(null, null));
  }

  @Test
  public void testApplyAliasesWithMultipleAliases() {
    String source = "function foo(a, b) { return a + b; } goog.scope(function() { var a = 3, b = 4; })";
    Node root = Node.createTree(source);

    scopedAliases.process(null, root);

    assertFalse(scopedAliases.hasErrors());

    scopedAliases.applyAliases();

    assertEquals("a", scopedAliases.getAliasDefinitionsInOrder().get(0).getIdent().getName());
    assertEquals("b", scopedAliases.getAliasDefinitionsInOrder().get(1).getIdent().getName());

    Node functionNode = root.getLastChild();
    Node newFunctionNode = functionNode.getLastChild();

    Node updatedFunctionNode = newFunctionNode.getLastChild();

    assertEquals(3, updatedFunctionNode.getDottedMember(0).getInt());
    assertEquals(3, updatedFunctionNode.getDottedMember(1).getInt());
    assertEquals(7, functionNode.getExpression().calculateNearlyIntValue(null, null));
  }
}