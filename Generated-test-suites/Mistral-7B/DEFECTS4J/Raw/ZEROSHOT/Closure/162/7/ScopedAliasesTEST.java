package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

/**
 * Unit tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    String sourceCode = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + " * @suppress {checkTypes}\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  var dom = goog.dom;\n"
        + "  var DIV = dom.TagName.DIV;\n"
        + "\n"
        + "  dom.createElement(DIV);\n"
        + "});\n";

    Node rootNode = new JsCompiler().parse(sourceCode, "test.js");

    AliasTransformationHandler handler = new AliasTransformationHandler();

    ScopedAliases testee = new ScopedAliases(
        new NoOpCompiler(), null, handler);

    testee.process(null, rootNode);

    Collection<Node> aliasDefinitionsInOrder = testee.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = testee.getScopeCalls();
    List<AliasUsage> aliasUsages = testee.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(1, aliasUsages.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("DIV", aliasDefinition.getString());
    assertEquals(Token.VAR, aliasDefinition.getType());
    assertEquals(Token.NAME, aliasDefinition.getFirstChild().getType());
    assertEquals("dom", aliasDefinition.getFirstChild().getString());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());
    assertEquals(Token.FUNCTION, scopeCall.getChildAtIndex(1).getType());

    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    assertEquals(aliasDefinition, ((AliasedNode) aliasUsage).aliasDefinition);
    assertEquals("DIV", ((AliasedNode) aliasUsage).aliasReference.getString());

    assertEquals("goog.dom.createElement",
        ((AliasedNode) aliasUsage).aliasDefinition.getQualifiedName());
  }

  @Test
  public void testScopedAliasesWithType() {
    String sourceCode = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  var dom = goog.dom;\n"
        + "  var DIV = goog.dom.TagName.DIV;\n"
        + "\n"
        + "  goog.scope(function() {\n"
        + "    var element = goog.dom.createElement(DIV);\n"
        + "  });\n"
        + "});\n";

    Node rootNode = new JsCompiler().parse(sourceCode, "test.js");

    AliasTransformationHandler handler = new AliasTransformationHandler();

    ScopedAliases testee = new ScopedAliases(
        new NoOpCompiler(), null, handler);

    testee.process(null, rootNode);

    Collection<Node> aliasDefinitionsInOrder = testee.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = testee.getScopeCalls();
    List<AliasUsage> aliasUsages = testee.getAliasUsages();

    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(1, aliasUsages.size());

    Node firstAliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("DIV", firstAliasDefinition.getString());
    assertEquals(Token.VAR, firstAliasDefinition.getType());
    assertEquals(Token.NAME, firstAliasDefinition.getFirstChild().getType());
    assertEquals("dom", firstAliasDefinition.getFirstChild().getString());

    Node secondAliasDefinition = aliasDefinitionsInOrder.get(1);
    assertEquals("DIV", secondAliasDefinition.getString());
    assertEquals(Token.VAR, secondAliasDefinition.getType());
    assertEquals(Token.NAME, secondAliasDefinition.getFirstChild().getType());
    assertEquals("DOM", secondAliasDefinition.getFirstChild().getString());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());
    assertEquals(Token.FUNCTION, scopeCall.getChildAtIndex(1).getType());

    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    assertEquals(secondAliasDefinition, ((AliasedNode) aliasUsage).aliasDefinition);
    assertEquals("DIV", ((AliasedNode) aliasUsage).aliasReference.getString());

    assertEquals("goog.dom.DOM.createElement",
        ((AliasedNode) aliasUsage).aliasDefinition.getQualifiedName());
  }

  @Test
  public void testScopedAliasesWithFunctionReturn() {
    String sourceCode = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  function createElement() {\n"
        + "    return goog.dom.createElement('DIV');\n"
        + "  }\n"
        + "});\n";

    Node rootNode = new JsCompiler().parse(sourceCode, "test.js");

    AliasTransformationHandler handler = new AliasTransformationHandler();

    ScopedAliases testee = new ScopedAliases(
        new NoOpCompiler(), null, handler);

    testee.process(null, rootNode);

    Collection<Node> aliasDefinitionsInOrder = testee.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = testee.getScopeCalls();
    List<AliasUsage> aliasUsages = testee.getAliasUsages();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());
    assertEquals(Token.FUNCTION, scopeCall.getChildAtIndex(1).getType());

    assertFalse(testee.hasErrors());
  }

  @Test
  public void testScopedAliasesWithFunctionThis() {
    String sourceCode = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  function createElement() {\n"
        + "    this.foo = 'bar';\n"
        + "    return goog.dom.createElement('DIV');\n"
        + "  }\n"
        + "});\n";

    Node rootNode = new JsCompiler().parse(sourceCode, "test.js");

    AliasTransformationHandler handler = new AliasTransformationHandler();

    ScopedAliases testee = new ScopedAliases(
        new NoOpCompiler(), null, handler);

    testee.process(null, rootNode);

    Collection<Node> aliasDefinitionsInOrder = testee.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = testee.getScopeCalls();
    List<AliasUsage> aliasUsages = testee.getAliasUsages();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());
    assertEquals(Token.FUNCTION, scopeCall.getChildAtIndex(1).getType());

    assertFalse(testee.hasErrors());

    AliasTransformation transformation = handler.getLastAliasTransformation();
    assertNull(transformation);
  }

  @Test
  public void testScopedAliasesWithFunctionThrow() {
    String sourceCode = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  function createElement() {\n"
        + "    throw new Error('test');\n"
        + "    return goog.dom.createElement('DIV');\n"
        + "  }\n"
        + "});\n";

    Node rootNode = new JsCompiler().parse(sourceCode, "test.js");

    AliasTransformationHandler handler = new AliasTransformationHandler();

    ScopedAliases testee = new ScopedAliases(
        new NoOpCompiler(), null, handler);

    testee.process(null, rootNode);

    Collection<Node> aliasDefinitionsInOrder = testee.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = testee.getScopeCalls();
    List<AliasUsage> aliasUsages = testee.getAliasUsages();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());
    assertEquals(Token.FUNCTION, scopeCall.getChildAtIndex(1).getType());

    assertFalse(testee.hasErrors());

    AliasTransformation transformation = handler.getLastAliasTransformation();
    assertNull(transformation);
  }

  @Test
  public void testScopedAliasesWithFunctionReturnThrow() {
    String sourceCode = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  function createElement() {\n"
        + "    throw new Error('test');\n"
        + "    return goog.dom.createElement('DIV');\n"
        + "  }\n"
        + "});\n";

    Node rootNode = new JsCompiler().parse(sourceCode, "test.js");

    AliasTransformationHandler handler = new AliasTransformationHandler();

    ScopedAliases testee = new ScopedAliases(
        new NoOpCompiler(), null, handler);

    testee.process(null, rootNode);

    Collection<Node> aliasDefinitionsInOrder = testee.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = testee.getScopeCalls();
    List<AliasUsage> aliasUsages = testee.getAliasUsages();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());
    assertEquals(Token.FUNCTION, scopeCall.getChildAtIndex(1).getType());

    assertTrue(testee.hasErrors());

    AliasTransformation transformation = handler.getLastAliasTransformation();
    assertNull(transformation);
  }

  @Test
  public void testScopedAliasesWithDuplicateAliases() {
    String sourceCode = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  var DIV = goog.dom.TagName.DIV;\n"
        + "  var DIV = typeof DIV === 'undefined' ? 'DIV' : DIV;\n"
        + "});\n";

    Node rootNode = new JsCompiler().parse(sourceCode, "test.js");

    AliasTransformationHandler handler = new AliasTransformationHandler();

    ScopedAliases testee = new ScopedAliases(
        new NoOpCompiler(), null, handler);

    testee.process(null, rootNode);

    Collection<Node> aliasDefinitionsInOrder = testee.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = testee.getScopeCalls();
    List<AliasUsage> aliasUsages = testee.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("DIV", aliasDefinition.getString());
    assertEquals(Token.VAR, aliasDefinition.getType());
    assertEquals(Token.NAME, aliasDefinition.getFirstChild().getType());
    assertEquals("div", aliasDefinition.getFirstChild().getString());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(2, scopeCall.getChildCount());
    assertEquals(Token.FUNCTION, scopeCall.getChildAtIndex(1).getType());

    assertTrue(testee.hasErrors());

    AliasTransformation transformation = handler.getLastAliasTransformation();
    assertEquals(AliasTransformation.Type.ALIAS, transformation.getType());
    assertEquals("DIV", transformation.getAlias());
    assertEquals("div", transformation.getTarget());
  }
}