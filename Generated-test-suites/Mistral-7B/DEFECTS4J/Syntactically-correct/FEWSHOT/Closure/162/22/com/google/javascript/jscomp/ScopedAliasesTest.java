package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import javax.annotation.Nullable;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    Node externs = Node.newNode(Token.NULL);
    Node root = Node.newNode(Token.SCRIPT);

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    scopedAliases.process(externs, root);

    // Test that no errors were reported
    assertFalse(compiler.hasErrors());

    // Test that aliases were applied
    for (Node aliasDefinition : scopedAliases.getTraversal().getAliasDefinitionsInOrder()) {
      assertEquals("goog.dom.createElement",
          aliasDefinition.getParent().getString());
      Node aliasReference = aliasDefinition.getParent().getFirstChild();
      assertEquals("createElement", aliasReference.getString());
    }

    // Test that scopes were collapsed
    Node scopeCall = scopedAliases.getTraversal().getScopeCalls().get(0);
    assertEquals("goog.dom.createElement", scopeCall.getLastChild().getString());

    // Test that aliases were removed
    assertNull(preprocessorSymbolTable.getVariable("DIV"));

    // Test that scopes were collapsed correctly
    Node scopeClosureBlock = scopeCall.getLastChild().getLastChild();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        scopeClosureBlock.getString());

    // Test that aliases were applied in the correct order
    for (AliasUsage aliasUsage : scopedAliases.getTraversal().getAliasUsages()) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        assertEquals("goog.dom.createElement", aliasedNode.aliasDefinition.getParent().getString());
        assertEquals("createElement", aliasedNode.aliasReference.getString());
      } else if (aliasUsage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
        assertEquals("goog.dom.Element", aliasedTypeNode.aliasDefinition.getParent().getString());
        assertEquals("goog.dom.Element", aliasedTypeNode.aliasReference.getParent().getString());
        assertEquals("TagName", aliasedTypeNode.correctedType);
      }
    }
  }

  @Test
  public void testInvalidScopeCall() {
    Node externs = Node.newNode(Token.NULL);
    Node root = Node.newNode(Token.SCRIPT);

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node invalidScopeCall = Node.newNode(
        new Token(Token.CALL, "goog.scope", null),
        Node.newNode(Token.NAME, "invalidFunction", null)
    );

    Node invalidScopeRoot = Node.newNode(Token.SCRIPT);
    invalidScopeRoot.addChild(invalidScopeCall);

    scopedAliases.process(externs, invalidScopeRoot);

    // Test that an error was reported
    assertEquals(1, compiler.getDiagnostics().size());
    Diagnostic diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getType().name());
  }
}