package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  @Test
  public void testProcess() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node rootNode = Node.newJsonObject();
    Node externsNode = Node.newJsonObject();
    scopedAliases.process(externsNode, rootNode);

    // Then
    // Perform assertions on the rootNode and externsNode as needed.
    // For example, to check if certain nodes were created or removed,
    // or if errors were reported.
  }

  @Test
  public void testHotSwapScript() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Node.newJsonObject();
    Node originalRootNode = Node.newJsonObject();

    // When
    scopedAliases.hotSwapScript(rootNode, originalRootNode);

    // Then
    // Perform assertions on the rootNode and externsNode as needed.
  }

  @Test
  public void testApplyAlias() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node aliasReferenceNode = Node.newName("dom");
    Node aliasDefinitionNode = Node.newName("goog.dom");

    // When
    AliasUsage aliasUsage = scopedAliases.new AliasedNode(aliasReferenceNode, aliasDefinitionNode);
    aliasUsage.applyAlias();

    // Then
    assertEquals("goog.dom", aliasReferenceNode.getQualifiedName());
  }

  @Test
  public void testApplyCorrectedType() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node aliasReferenceNode = Node.newName("divElement");
    String correctedType = "goog.dom.DivElement";

    // When
    AliasUsage aliasUsage = scopedAliases.new AliasedTypeNode(aliasReferenceNode, correctedType);
    aliasUsage.applyAlias();

    // Then
    assertEquals(correctedType, aliasReferenceNode.getString());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidScopeCall() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node invalidScopeCallNode = Node.newCall(
        Node.newName("goog.scope"),
        Node.newArray(Node.newName("divElement")));

    // When
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()),
        null);
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        invalidScopeCallNode,
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()));

    // Then
    // The test fails due to the expected AssertionError.
  }

  @Test(expected = AssertionError.class)
  public void testDuplicateAliasDefinition() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node firstAliasDefinitionNode = Node.newName("divElement");
    Node secondAliasDefinitionNode = Node.newName("divElement");

    // When
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()),
        null);
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        firstAliasDefinitionNode,
        Node.newAssignment(Node.newName("divElement"), firstAliasDefinitionNode));
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        secondAliasDefinitionNode,
        Node.newAssignment(Node.newName("divElement"), secondAliasDefinitionNode));

    // Then
    // The test fails due to the expected AssertionError.
  }

  @Test
  public void testEmptyScopeCall() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node emptyScopeCallNode = Node.newCall(
        Node.newName("goog.scope"),
        Node.newFunction());

    // When
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()),
        null);
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        emptyScopeCallNode,
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()));

    // Then
    // The scopeCalls list should be empty.
    assertEquals(ImmutableList.of(), scopedAliases.getTraversal().getScopeCalls());
  }

  @Test
  public void testScopeCallWithParameters() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node scopeCallNode = Node.newCall(
        Node.newName("goog.scope"),
        Node.newFunction(),
        Node.newArray(Node.newName("arg1"), Node.newName("arg2")));

    // When
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()),
        null);
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        scopeCallNode,
        Node.newCall(Node.newName("goog.scope"), Node.newFunction(),
            Node.newArray(Node.newName("arg1"), Node.newName("arg2"))));

    // Then
    // The scopeCalls list should contain one node.
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(ImmutableList.of(scopeCallNode), ImmutableList.copyOf(scopeCalls));
  }

  @Test
  public void testScopeCallWithFunctionName() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node scopeCallNode = Node.newCall(
        Node.newQualifiedName(Node.newName("goog"), "scope"),
        Node.newFunction(),
        Node.newArray());

    // When
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        Node.newCall(Node.newQualifiedName(Node.newName("goog"), "scope"), Node.newFunction()),
        null);
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        scopeCallNode,
        Node.newCall(Node.newQualifiedName(Node.newName("goog"), "scope"), Node.newFunction(),
            Node.newArray()));

    // Then
    // The scopeCalls list should contain one node.
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(ImmutableList.of(scopeCallNode), ImmutableList.copyOf(scopeCalls));
  }

  @Test
  public void testInvalidAliasDefinition() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node invalidAliasDefinitionNode = Node.newAssignment(
        Node.newName("divElement"),
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()));

    // When
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()),
        null);
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        invalidAliasDefinitionNode,
        Node.newAssignment(Node.newName("divElement"), Node.newCall(
            Node.newName("goog.scope"), Node.newFunction())));

    // Then
    // The aliasDefinitionsInOrder list should be empty.
    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(ImmutableList.of(), aliasDefinitionsInOrder);
  }

  @Test
  public void testInvalidAliasUsage() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node invalidAliasUsageNode = Node.newCall(
        Node.newName("goog.scope"),
        Node.newName("this"));

    // When
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()),
        null);
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        invalidAliasUsageNode,
        Node.newCall(Node.newName("goog.scope"), Node.newFunction(),
            Node.newName("this")));

    // Then
    // The aliasUsages list should be empty.
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(ImmutableList.of(), aliasUsages);
  }

  @Test
  public void testInvalidAliasUsageWithReturnStatement() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node invalidAliasUsageNodeWithReturnStatement = Node.newExpressionStatement(
        Node.newReturnStatement(Node.newCall(
            Node.newName("goog.scope"), Node.newFunction())));

    // When
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()),
        null);
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        invalidAliasUsageNodeWithReturnStatement,
        Node.newExpressionStatement(Node.newReturnStatement(
            Node.newCall(Node.newName("goog.scope"), Node.newFunction()))));

    // Then
    // The aliasUsages list should be empty.
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(ImmutableList.of(), aliasUsages);
  }

  @Test
  public void testInvalidAliasUsageWithThrowStatement() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node invalidAliasUsageNodeWithThrowStatement = Node.newExpressionStatement(
        Node.newThrowStatement(Node.newNull()));

    // When
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        Node.newCall(Node.newName("goog.scope"), Node.newFunction()),
        null);
    scopedAliases.getTraversal().traverse(
        new NodeTraversal() {},
        invalidAliasUsageNodeWithThrowStatement,
        Node.newExpressionStatement(Node.newThrowStatement(Node.newNull())));

    // Then
    // The aliasUsages list should be empty.
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(ImmutableList.of(), aliasUsages);
  }
}