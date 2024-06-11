package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; " +
        "dom.createElement(DIV); " +
        "})";

    Node root = new Parser().parseCompilationUnit(sourceCode, null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.traversal.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.traversal.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node aliasUsage = aliasUsages.get(0);

    assertEquals(aliasDefinition, aliasUsage.aliasDefinition);
    assertEquals(aliasDefinition.getParent(), aliasUsage.aliasReference.getParent());
    assertEquals(aliasDefinition.getChildCount(), aliasUsage.aliasDefinition.getChildCount());

    // Check that the alias has been applied
    Node elementCreationNode = aliasUsage.aliasReference;
    assertEquals(1, elementCreationNode.getChildCount());
    Node newNode = elementCreationNode.getChildAtIndex(0);
    assertEquals("goog.dom.createElement", newNode.getQualifiedName());

    // Check that the alias definition has been removed
    assertNull(aliasDefinition.getParent());

    // Check that the scopes have been collapsed
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());
    assertEquals(0, scopeCall.getChildCount());
  }

  @Test
  public void testProcessWithErrors() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom; " + // ok: scoped alias
        "var DIV = goog.dom.TagName.DIV; " + // ok: scoped alias
        "goog.scope(function() { dom.createElement(DIV); });" + // error: nested scopes
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.traversal.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.traversal.getScopeCalls();

    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("DIV", aliasDefinitionsInOrder.get(1).getString());
    assertEquals(1, aliasUsages.size());
    assertEquals(2, scopeCalls.size());

    // Check that there is an error for the nested scope
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY",
        scopedAliases.compiler.getError(scopeCalls.get(1)).getType().getName());

    // Check that the alias definitions have not been removed
    for (Node aliasDefinition : aliasDefinitionsInOrder) {
      assertNotNull(aliasDefinition.getParent());
    }
  }

  @Test
  public void testProcessWithAliasRedefinition() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom; " + // ok: scoped alias
        "var DIV = dom.TagName.DIV; " + // ok: scoped alias
        "var DIV = 'div'; " + // error: alias redefinition
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.traversal.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.traversal.getScopeCalls();

    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("DIV", aliasDefinitionsInOrder.get(1).getString());
    assertEquals(1, aliasUsages.size());

    // Check that there is an error for the alias redefinition
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        scopedAliases.compiler.getError(aliasDefinitionsInOrder.get(1)).getType().getName());

    // Check that the alias definitions have not been removed
    for (Node aliasDefinition : aliasDefinitionsInOrder) {
      assertNotNull(aliasDefinition.getParent());
    }
  }

  @Test
  public void testProcessWithNonAliasLocalVariable() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom; " + // ok: scoped alias
        "var element = document.createElement('div'); " + // error: non-alias local variable
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.traversal.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.traversal.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals(0, aliasUsages.size());

    // Check that there is an error for the non-alias local variable
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        scopedAliases.compiler.getError(aliasDefinitionsInOrder.get(0)).getType().getName());

    // Check that the alias definitions have not been removed
    for (Node aliasDefinition : aliasDefinitionsInOrder) {
      assertNotNull(aliasDefinition.getParent());
    }
  }

  @Test
  public void testProcessWithAliasUsingReturn() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "return goog.dom.createElement('div');" + // error: alias using return
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.traversal.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.traversal.getScopeCalls();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    // Check that there is an error for the alias using return
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN",
        scopedAliases.compiler.getError(scopeCalls.get(0)).getType().getName());

    // Check that the alias definitions have not been removed
    for (Node aliasDefinition : aliasDefinitionsInOrder) {
      assertNull(aliasDefinition);
    }
  }

  @Test
  public void testProcessWithAliasUsingThrow() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "throw new Error('Test');" + // error: alias using throw
        "});";

    Node root = new Parser().parseCompilationUnit(sourceCode, null);

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.traversal.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.traversal.getScopeCalls();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    // Check that there is an error for the alias using throw
    assertEquals("JSC_GOOG_SCOPE_USES_THROW",
        scopedAliases.compiler.getError(scopeCalls.get(0)).getType().getName());

    // Check that the alias definitions have not been removed
    for (Node aliasDefinition : aliasDefinitionsInOrder) {
      assertNull(aliasDefinition);
    }
  }
}