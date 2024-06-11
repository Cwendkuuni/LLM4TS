package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    
    // When
    Node root = new Node("goog.scope(function() { var dom = goog.dom; dom.createElement(goog.dom.TagName.DIV); })");
    scopedAliases.process(null, root);

    // Then
    AliasTransformation transformation = transformationHandler.getLoggedAliasTransformations().get(0);
    assertEquals("goog.dom", transformation.getName());
    assertEquals("dom", transformation.getValue());
    
    // Given
    root = new Node("goog.scope(function() { function dom() {} dom.createElement('DIV') })");
    scopedAliases.process(null, root);

    // Then
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCallNode = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());
    assertEquals(1, scopeCallNode.getChildCount());
    Node anonymousFnNode = scopeCallNode.getChildAtIndex(1);
    assertEquals(1, anonymousFnNode.getChildCount());
    assertEquals("dom", anonymousFnNode.getChildAtIndex(0).getString());
    assertEquals("function dom() {}\n  dom.createElement('DIV')",
        NodeUtil.getCode(root));
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithBadUsage() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = new Node("goog.scope(function() { var dom = goog.dom; var newDom = goog.scope(function() { dom.createElement('DIV') }) })");

    // Then
    scopedAliases.process(null, root);

    // Exception should be thrown
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithBadParameters() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = new Node("goog.scope(function() { goog.scope(function() { }) })");

    // Then
    scopedAliases.process(null, root);

    // Exception should be thrown
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithThisReference() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = new Node("goog.scope(function() { this.createElement('DIV') })");

    // Then
    scopedAliases.process(null, root);

    // Exception should be thrown
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithReturn() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = new Node("goog.scope(function() { return goog.dom.createElement('DIV') })");

    // Then
    scopedAliases.process(null, root);

    // Exception should be thrown
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithThrow() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = new Node("goog.scope(function() { throw new Error('Test error') })");

    // Then
    scopedAliases.process(null, root);

    // Exception should be thrown
  }
}