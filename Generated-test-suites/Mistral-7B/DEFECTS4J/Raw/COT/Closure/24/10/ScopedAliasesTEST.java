package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testScopedAliases_NoErrors() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = new Node();
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testScopedAliases_WithErrors() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = new Node();
    Node googScopeCall = new Node(Token.CALL, null, null);
    googScopeCall.setFirstChild(new Node("goog", null));
    Node anonymousFunction = new Node(Token.FUNCTION, null, null);
    Node returnStatement = new Node(Token.RETURN, null, null);
    anonymousFunction.addChild(returnStatement);
    Node parameter = new Node(Token.NAME, null, null);
    anonymousFunction.addChild(parameter);
    Node body = new Node(Token.BLOCK, null, null);
    Node assignmentStatement = new Node(Token.ASSIGN, null, null);
    Node aliasReference = new Node(Token.NAME, null, null);
    aliasReference.setString("goog");
    assignmentStatement.setFirstChild(aliasReference);
    assignmentStatement.setLastChild(new Node(Token.NAME, null, null));
    body.addChild(assignmentStatement);
    Node googScopeFunctionCall = new Node(Token.CALL, null, null);
    googScopeFunctionCall.setFirstChild(googScopeCall);
    googScopeFunctionCall.setLastChild(body);
    root.addChild(googScopeFunctionCall);

    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testApplyAlias() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node aliasReference = new Node(Token.NAME, null, null);
    aliasReference.setString("goog.dom");
    Node aliasDefinition = new Node(Token.QUAL_NAME, null, null);
    aliasDefinition.setQualifiedName("dom");

    AliasUsage aliasUsage = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);
    scopedAliases.getAliasUsages().clear();
    scopedAliases.getAliasUsages().add(aliasUsage);
    scopedAliases.applyAlias();

    ArgumentCaptor<Node> aliasDefinitionCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler).replaceChild(aliasReference, aliasDefinitionCaptor.capture());
    assertEquals(aliasDefinition, aliasDefinitionCaptor.getValue());
  }

  @Test(expected = AssertionError.class)
  public void testApplyAlias_MultipleDefinitions() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node aliasReference = new Node(Token.NAME, null, null);
    aliasReference.setString("goog.dom");
    Node aliasDefinition1 = new Node(Token.QUAL_NAME, null, null);
    aliasDefinition1.setQualifiedName("dom1");
    Node aliasDefinition2 = new Node(Token.QUAL_NAME, null, null);
    aliasDefinition2.setQualifiedName("dom2");

    AliasUsage aliasUsage1 = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition1);
    AliasUsage aliasUsage2 = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition2);
    scopedAliases.getAliasUsages().clear();
    scopedAliases.getAliasUsages().add(aliasUsage1);
    scopedAliases.getAliasUsages().add(aliasUsage2);
    scopedAliases.applyAlias();

    ArgumentCaptor<Node> aliasDefinitionCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler, times(2)).replaceChild(aliasReference, aliasDefinitionCaptor.capture());
    assertEquals(aliasDefinition1, aliasDefinitionCaptor.getAllValues().get(0));
    assertEquals(aliasDefinition2, aliasDefinitionCaptor.getAllValues().get(1));
  }

  @Test
  public void testApplyAlias_NoDefinition() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node aliasReference = new Node(Token.NAME, null, null);
    aliasReference.setString("goog.dom");

    scopedAliases.getAliasUsages().clear();
    scopedAliases.applyAlias();

    verify(compiler, never()).replaceChild(aliasReference, any(Node.class));
  }

  @Test
  public void testApplyAlias_NoAliasReference() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node aliasDefinition = new Node(Token.QUAL_NAME, null, null);
    aliasDefinition.setQualifiedName("dom");

    scopedAliases.getAliasDefinitionsInOrder().clear();
    scopedAliases.getAliasDefinitionsInOrder().add(aliasDefinition);
    scopedAliases.applyAlias();

    verify(compiler, never()).replaceChild(any(Node.class), any(Node.class));
  }

  @Test(expected = AssertionError.class)
  public void testApplyAlias_InvalidAliasDefinition() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node aliasDefinition = new Node(Token.NUMBER, null, null);

    scopedAliases.getAliasDefinitionsInOrder().clear();
    scopedAliases.getAliasDefinitionsInOrder().add(aliasDefinition);
    scopedAliases.applyAlias();

    verify(compiler, never()).replaceChild(any(Node.class), any(Node.class));
  }

  @Test
  public void testRemoveAliasDefinition() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node aliasDefinition = new Node(Token.QUAL_NAME, null, null);
    aliasDefinition.setQualifiedName("dom");
    Node parent = new Node(Token.BLOCK, null, null);
    Node aliasDefinitionInParent = new Node(Token.ASSIGN, null, null);
    aliasDefinitionInParent.setFirstChild(aliasDefinition);
    parent.addChild(aliasDefinitionInParent);

    scopedAliases.getAliasDefinitionsInOrder().clear();
    scopedAliases.getAliasDefinitionsInOrder().add(aliasDefinition);
    scopedAliases.process(null, root);

    verify(compiler).detachFromParent(aliasDefinition);
  }

  @Test(expected = AssertionError.class)
  public void testRemoveAliasDefinition_NotFound() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node aliasDefinition = new Node(Token.QUAL_NAME, null, null);
    aliasDefinition.setQualifiedName("dom");
    Node parent = new Node(Token.BLOCK, null, null);
    Node aliasDefinitionInParent = new Node(Token.ASSIGN, null, null);
    aliasDefinitionInParent.setFirstChild(new Node(Token.NAME, null, null));
    parent.addChild(aliasDefinitionInParent);

    scopedAliases.getAliasDefinitionsInOrder().clear();
    scopedAliases.getAliasDefinitionsInOrder().add(aliasDefinition);
    scopedAliases.process(null, root);

    verify(compiler, never()).detachFromParent(aliasDefinition);
  }

  @Test
  public void testCollapseScope() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node googScopeCall = new Node(Token.CALL, null, null);
    Node anonymousFunction = new Node(Token.FUNCTION, null, null);
    Node body = new Node(Token.BLOCK, null, null);
    Node assignmentStatement = new Node(Token.ASSIGN, null, null);
    Node referenceNode = new Node(Token.NAME, null, null);
    referenceNode.setString("goog.dom");
    assignmentStatement.setFirstChild(referenceNode);
    body.addChild(assignmentStatement);
    Node googScopeFunctionCall = new Node(Token.CALL, null, null);
    googScopeFunctionCall.setFirstChild(googScopeCall);
    googScopeFunctionCall.setLastChild(body);
    Node parent = new Node(Token.EXPR_STMT, null, null);
    parent.addChild(googScopeFunctionCall);

    scopedAliases.process(null, root);

    ArgumentCaptor<Node> collapsedScopeCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler).replaceChild(googScopeFunctionCall, collapsedScopeCaptor.capture());
    Node collapsedScope = collapsedScopeCaptor.getValue();
    assertNotNull(collapsedScope);
    assertTrue(collapsedScope.isBlock());
    assertTrue(collapsedScope.getLastChild().isReturn());
  }

  @Test
  public void testCollapseScope_WithErrors() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node googScopeCall = new Node(Token.CALL, null, null);
    Node anonymousFunction = new Node(Token.FUNCTION, null, null);
    Node body = new Node(Token.BLOCK, null, null);
    Node assignmentStatement = new Node(Token.ASSIGN, null, null);
    Node referenceNode = new Node(Token.NAME, null, null);
    referenceNode.setString("goog.dom");
    Node returnStatement = new Node(Token.RETURN, null, null);
    anonymousFunction.addChild(returnStatement);
    body.addChild(assignmentStatement);
    body.addChild(returnStatement);
    Node googScopeFunctionCall = new Node(Token.CALL, null, null);
    googScopeFunctionCall.setFirstChild(googScopeCall);
    googScopeFunctionCall.setLastChild(body);
    Node parent = new Node(Token.EXPR_STMT, null, null);
    parent.addChild(googScopeFunctionCall);

    scopedAliases.process(null, root);

    ArgumentCaptor<Node> collapsedScopeCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler, never()).replaceChild(googScopeFunctionCall, collapsedScopeCaptor.capture());
  }
}