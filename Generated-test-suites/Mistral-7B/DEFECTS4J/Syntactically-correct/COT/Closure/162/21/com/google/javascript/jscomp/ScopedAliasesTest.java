package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;
  @Mock
  private AliasTransformationHandler transformationHandler;

  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess() {
    // Given
    Node root = Node.objectLiteral();

    // When
    scopedAliases.process(null, root);

    // Then
    verify(compiler, never()).report(any());
  }

  @Test
  public void testHotSwapScript() {
    // Given
    Node root = Node.objectLiteral();
    Node externs = Node.objectLiteral();

    when(preprocessorSymbolTable.addReference(any(Node.class)))
        .thenReturn(null);

    // When
    scopedAliases.hotSwapScript(externs, root);

    // Then
    verify(compiler, never()).report(any());
  }

  @Test
  public void testApplyAlias() {
    // Given
    Node aliasDefinition = Node.name("goog.dom");
    Node aliasReference = Node.name("goog.dom");

    Node aliasUsage = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);

    // When
    aliasUsage.applyAlias();

    // Then
    assertEquals(aliasDefinition, aliasReference.getParent());
  }

  @Test
  public void testApplyAliasType() {
    // Given
    Node aliasDefinition = Node.name("goog.dom").append("Element");
    Node aliasReference = Node.string("Element");

    Node aliasUsage = new ScopedAliases.AliasedTypeNode(aliasReference, "goog.dom.Element");

    // When
    aliasUsage.applyAlias();

    // Then
    assertEquals("goog.dom.Element", aliasReference.getString());
  }

  @Test(expected = AssertionError.class)
  public void testApplyAliasTwice() {
    // Given
    Node aliasDefinition = Node.name("goog.dom");
    Node aliasReference = Node.name("goog.dom");

    Node aliasUsage = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);

    // When
    aliasUsage.applyAlias();
    aliasUsage.applyAlias();

    // Then
    fail("Alias should not be applied twice.");
  }

  @Test(expected = AssertionError.class)
  public void testApplyAliasNonAliasLocal() {
    // Given
    Node aliasDefinition = Node.name("foo");
    Node aliasReference = Node.name("foo");

    Node aliasUsage = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);

    // When
    aliasUsage.applyAlias();

    // Then
    fail("Non-alias local should not be replaced.");
  }

  @Test(expected = AssertionError.class)
  public void testApplyAliasInFunction() {
    // Given
    Node aliasDefinition = Node.name("goog.dom");
    Node aliasReference = Node.name("goog.dom");
    Node functionNode = Node.functionLiteral();

    Node aliasUsage = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);

    // When
    functionNode.setFirstChild(aliasUsage);
    scopedAliases.hotSwapScript(null, functionNode);

    // Then
    fail("Alias should not be applied inside a function.");
  }

  @Test(expected = AssertionError.class)
  public void testApplyAliasInScopeCall() {
    // Given
    Node aliasDefinition = Node.name("goog.dom");
    Node aliasReference = Node.name("goog.dom");
    Node scopeCall = Node.name("goog.scope").call(null);

    Node aliasUsage = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);

    scopeCall.setLastChild(aliasUsage);

    // When
    scopedAliases.hotSwapScript(null, scopeCall);

    // Then
    fail("Alias should not be applied inside a goog.scope call.");
  }

  @Test(expected = AssertionError.class)
  public void testApplyAliasInRootScope() {
    // Given
    Node aliasDefinition = Node.name("goog.dom");
    Node aliasReference = Node.name("goog.dom");

    Node rootNode = Node.objectLiteral();

    Node aliasUsage = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);

    rootNode.setFirstChild(aliasUsage);

    // When
    scopedAliases.hotSwapScript(null, rootNode);

    // Then
    fail("Alias should not be applied in the root scope.");
  }

  @Test
  public void testTraversal() {
    // Given
    Node root = Node.objectLiteral();

    Node functionNode1 = Node.functionLiteral();
    Node functionNode2 = Node.functionLiteral();

    Node scopeCall = Node.name("goog.scope").call(null);

    Node aliasDefinition1 = Node.name("goog.dom");
    Node aliasDefinition2 = Node.name("goog.elem");

    Node aliasReference1 = Node.name("goog.dom");
    Node aliasReference2 = Node.name("goog.elem");

    Node aliasUsage1 = new ScopedAliases.AliasedNode(aliasReference1, aliasDefinition1);
    Node aliasUsage2 = new ScopedAliases.AliasedNode(aliasReference2, aliasDefinition2);

    functionNode1.setFirstChild(aliasUsage1);
    functionNode2.setFirstChild(aliasUsage2);
    scopeCall.setLastChild(functionNode1);
    root.setFirstChild(scopeCall);

    // When
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, root, traversal);

    // Then
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(2, traversal.getAliasUsages().size());
  }

  @Test
  public void testTraversalWithErrors() {
    // Given
    Node root = Node.objectLiteral();

    Node functionNode1 = Node.functionLiteral();
    Node functionNode2 = Node.functionLiteral();

    Node scopeCall = Node.name("goog.scope").call(null);

    Node aliasDefinition1 = Node.name("goog.dom");
    Node aliasDefinition2 = Node.name("goog.elem");

    Node aliasReference1 = Node.name("goog.dom");
    Node aliasReference2 = Node.name("goog.elem");

    Node aliasUsage1 = new ScopedAliases.AliasedNode(aliasReference1, aliasDefinition1);
    Node aliasUsage2 = new ScopedAliases.AliasedNode(aliasReference2, aliasDefinition2);

    functionNode1.setFirstChild(aliasUsage1);
    functionNode2.setFirstChild(aliasUsage2);
    functionNode1.addChild(Node.name("return"));
    scopeCall.setLastChild(functionNode1);
    root.setFirstChild(scopeCall);

    // When
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, root, traversal);

    // Then
    assertTrue(traversal.hasErrors());
    verify(compiler, times(2)).report(any());
  }

  @Test
  public void testTransformation() {
    // Given
    Node root = Node.objectLiteral();

    Node functionNode = Node.functionLiteral();

    Node aliasDefinition = Node.name("goog.dom");
    Node aliasReference = Node.name("goog.dom");

    Node aliasUsage = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);
    functionNode.setFirstChild(aliasUsage);

    Node scopeCall = Node.name("goog.scope").call(null);
    scopeCall.setLastChild(functionNode);

    root.setFirstChild(scopeCall);

    AliasTransformation transformation = mock(AliasTransformation.class);

    when(transformationHandler.logAliasTransformation(eq(root.getSourceFileName()), any()))
        .thenReturn(transformation);

    // When
    scopedAliases.hotSwapScript(null, root);

    // Then
    verify(transformationHandler, times(1)).logAliasTransformation(eq(root.getSourceFileName()), any());
    assertEquals("goog.dom", transformation.getAliasName());
    assertEquals("com.google.javascript.jscomp.dom.DOM", transformation.getAliasValue());
  }
}