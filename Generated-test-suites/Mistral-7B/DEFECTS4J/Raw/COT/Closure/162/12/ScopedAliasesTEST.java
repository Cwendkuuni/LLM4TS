package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ScopedAliasesTest {
  private AliasTransformationHandler transformationHandler;
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    transformationHandler = mock(AliasTransformationHandler.class);
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);

    when(compiler.report(Mockito.any(DiagnosticType.class), Mockito.any(Node.class),
        Mockito.anyString()))
        .thenReturn(null);

    when(compiler.reportCodeChange()).thenReturn(null);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testProcess() {
    Node root = Node.newNode("function main() {}");
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testHotSwapScript_FunctionWithoutScopingMethodCall() {
    Node root = Node.newNode("function main() { return 42; }");
    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testHotSwapScript_CallToScopingMethodWithNoArgument() {
    Node root = Node.newNode(
        "goog.scope(function() { var foo = 42; });");
    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testHotSwapScript_CallToScopingMethodWithArgument() {
    Node root = Node.newNode(
        "goog.scope(function(bar) { var foo = 42; });");
    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test(expected = AssertionError.class)
  public void testHotSwapScript_CallToScopingMethodWithMultipleArguments() {
    Node root = Node.newNode(
        "goog.scope(function(foo, bar) { var baz = 42; });");
    scopedAliases.hotSwapScript(root, null);
  }

  @Test
  public void testHotSwapScript_CallToScopingMethodWithReturnStatement() {
    Node root = Node.newNode(
        "goog.scope(function() { return 42; });");
    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test(expected = AssertionError.class)
  public void testHotSwapScript_CallToScopingMethodWithThrowStatement() {
    Node root = Node.newNode(
        "goog.scope(function() { throw new Error(); });");
    scopedAliases.hotSwapScript(root, null);
  }

  @Test
  public void testHotSwapScript_CallToScopingMethodWithThisKeyword() {
    Node root = Node.newNode(
        "goog.scope(function() { this.foo = 42; });");
    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testHotSwapScript_CallToScopingMethodWithMultipleStatements() {
    Node root = Node.newNode(
        "goog.scope(function() { var foo = 42; var bar = 43; });");
    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testHotSwapScript_AliasDefinition() {
    Node root = Node.newNode(
        "goog.scope(function() { var goog = { dom: 42 }; });");
    scopedAliases.hotSwapScript(root, null);
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitions.size());
    Node aliasDefinition = aliasDefinitions.get(0);
    assertEquals("goog", aliasDefinition.getFirstChild().getString());
    assertEquals("{ dom: 42 }", aliasDefinition.getLastChild().getString());
  }

  @Test
  public void testHotSwapScript_AliasUsage() {
    Node root = Node.newNode(
        "goog.scope(function() { var foo = goog.dom; });");
    scopedAliases.hotSwapScript(root, null);
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasedNode aliasedNode = (AliasedNode) aliasUsages.get(0);
    assertEquals("goog.dom", aliasedNode.aliasDefinition.getString());
    assertEquals("goog", aliasedNode.aliasReference);
  }

  @Test
  public void testHotSwapScript_AliasUsageInsideFunction() {
    Node root = Node.newNode(
        "function foo() { goog.scope(function() { var bar = goog.dom; }); }");
    scopedAliases.hotSwapScript(root, null);
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasedNode aliasedNode = (AliasedNode) aliasUsages.get(0);
    Node functionNode = aliasedNode.aliasReference.getParent();
    assertEquals("foo", functionNode.getName());
  }

  @Test
  public void testHotSwapScript_AliasUsageWithQualifiedName() {
    Node root = Node.newNode(
        "goog.scope(function() { var foo = goog.dom.createElement; });");
    scopedAliases.hotSwapScript(root, null);
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasedTypeNode aliasedNode = (AliasedTypeNode) aliasUsages.get(0);
    assertEquals("goog.dom.createElement", aliasedNode.correctedType);
    assertEquals("goog.dom.createElement", aliasedNode.aliasReference.getString());
  }

  @Test
  public void testHotSwapScript_ApplyAlias() {
    Node root = Node.newNode(
        "goog.scope(function() { var goog = { dom: 42 }; var foo = goog.dom; });");
    scopedAliases.hotSwapScript(root, null);
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    Node aliasDefinition = aliasDefinitions.get(0);
    assertEquals("goog", aliasDefinition.getFirstChild().getString());
    assertEquals("{ dom: 42 }", aliasDefinition.getLastChild().getString());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    AliasedNode aliasedNode = (AliasedNode) aliasUsages.get(0);
    assertEquals("goog.dom", aliasedNode.aliasDefinition.getString());
    assertEquals("goog", aliasedNode.aliasReference);

    assertEquals("goog.dom", aliasedNode.applyAlias().getString());
  }

  @Test
  public void testHotSwapScript_RemoveAliasDefinition() {
    Node root = Node.newNode(
        "goog.scope(function() { var goog = { dom: 42 }; var foo = goog.dom; });");
    scopedAliases.hotSwapScript(root, null);
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    Node aliasDefinition = aliasDefinitions.get(0);

    scopedAliases.hotSwapScript(root, null);
    aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitions.isEmpty());
  }

  @Test
  public void testHotSwapScript_RemoveAliasUsage() {
    Node root = Node.newNode(
        "goog.scope(function() { var goog = { dom: 42 }; var foo = goog.dom; goog.dom = 43; });");
    scopedAliases.hotSwapScript(root, null);
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    Node aliasDefinition = aliasDefinitions.get(0);

    scopedAliases.hotSwapScript(root, null);
    aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitions.isEmpty());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    AliasedNode aliasedNode = (AliasedNode) aliasUsages.get(0);
    assertEquals("goog.dom", aliasedNode.aliasDefinition.getString());
    assertEquals("goog", aliasedNode.aliasReference);

    scopedAliases.hotSwapScript(root, null);
    aliasUsages = scopedAliases.getAliasUsages();
    assertFalse(aliasUsages.isEmpty());
    assertNull(aliasUsages.get(0));
  }

  @Test
  public void testHotSwapScript_CollapseScopes() {
    Node root = Node.newNode(
        "goog.scope(function() { var goog = { dom: 42 }; var foo = goog.dom.createElement; });");
    scopedAliases.hotSwapScript(root, null);
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    Node aliasDefinition = aliasDefinitions.get(0);

    Node aliasDefinitionAfterCollapse = aliasDefinition.getLastChild();
    assertEquals("goog.dom.createElement", aliasDefinitionAfterCollapse.getString());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    ArgumentCaptor<Node> scopeCallArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    assertEquals(1, scopeCalls.size());
    transformationHandler.logAliasTransformation(
        Mockito.capture(AliasTransformation.class), scopeCallArgumentCaptor.capture());
    AliasTransformation aliasTransformation = transformationHandler.getAliasTransformation();
    assertEquals(1, aliasTransformation.getAliases().size());
    assertEquals("goog", aliasTransformation.getAliases().get(0).getName());
    assertEquals("goog.dom.createElement", aliasTransformation.getAliases().get(0).getAlias());
  }
}