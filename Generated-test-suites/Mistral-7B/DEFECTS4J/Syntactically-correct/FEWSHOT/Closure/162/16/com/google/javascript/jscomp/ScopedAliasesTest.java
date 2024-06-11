package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testProcess() {
    Node root = Node.nullLiteral();
    Node externs = Node.nullLiteral();
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testProcessNoErrors() {
    Node root = Node.literal(
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  dom.createElement(DIV);" +
            "});");
    Node externs = Node.nullLiteral();
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    if (!traversal.hasErrors()) {
      // Apply the aliases.
      for (AliasUsage aliasUsage : traversal.getAliasUsages()) {
        aliasUsage.applyAlias();
      }

      // Remove the alias definitions.
      for (Node aliasDefinition : traversal.getAliasDefinitionsInOrder()) {
        assertNull(aliasDefinition.getParent());
      }

      // Collapse the scopes.
      for (Node scopeCall : traversal.getScopeCalls()) {
        Node expressionWithScopeCall = scopeCall.getParent();
        Node scopeClosureBlock = scopeCall.getLastChild().getLastChild();
        assertNull(scopeClosureBlock.getParent());
        assertSame(expressionWithScopeCall, scopeClosureBlock.getParent());
      }

      assertFalse(traversal.hasErrors());
    }
  }

  @Test
  public void testHotSwapScript() {
    Node root = Node.literal(
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  dom.createElement(DIV);" +
            "});");
    Node externs = Node.nullLiteral();
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    if (!traversal.hasErrors()) {
      // Apply the aliases.
      for (AliasUsage aliasUsage : traversal.getAliasUsages()) {
        aliasUsage.applyAlias();
      }

      // Remove the alias definitions.
      for (Node aliasDefinition : traversal.getAliasDefinitionsInOrder()) {
        assertNull(aliasDefinition.getParent());
      }

      // Collapse the scopes.
      for (Node scopeCall : traversal.getScopeCalls()) {
        Node expressionWithScopeCall = scopeCall.getParent();
        Node scopeClosureBlock = scopeCall.getLastChild().getLastChild();
        assertNull(scopeClosureBlock.getParent());
        assertSame(expressionWithScopeCall, scopeClosureBlock.getParent());
      }

      // Check that the scoped alias has been transformed.
      List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
      Node aliasDefinition = aliasDefinitionsInOrder.get(0);
      assertEquals("goog.dom", aliasDefinition.getString());

      // Check that the scoped alias has been applied.
      List<AliasUsage> aliasUsages = traversal.getAliasUsages();
      Node aliasUsage = aliasUsages.get(0);
      assertEquals(aliasDefinition, aliasUsage.aliasDefinition);
      assertEquals("goog.dom", aliasUsage.aliasReference.getString());
      assertEquals("goog.dom", aliasUsage.aliasDefinition.getString());

      // Check that the scope call has been removed.
      List<Node> scopeCalls = traversal.getScopeCalls();
      assertEmpty(scopeCalls);
    }
  }

  @Test
  public void testHotSwapScriptWithErrors() {
    Node root = Node.literal(
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  var THIS = 1;" +
            "  dom.createElement(DIV);" +
            "});");
    Node externs = Node.nullLiteral();
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(traversal.hasErrors());

    // Check that the errors are reported.
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertSame(root, scopeCalls.get(0));

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    // Check that the errors are reported with the correct position.
    DiagnosticType error = traversal.getDiagnosticType();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", error.getType());
    SourcePosition position = traversal.getDiagnosticPosition();
    assertEquals(1, position.getLineNumber());
    assertEquals(6, position.getColumnNumber());
  }
}