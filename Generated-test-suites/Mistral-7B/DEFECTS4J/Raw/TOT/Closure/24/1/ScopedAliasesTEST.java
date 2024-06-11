package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  @Test
  public void testSimpleAlias() {
    String input = "goog.scope(function() {" +
        "dom = goog.dom;" +
        "DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.aliasTransformations.add(new AliasTransformation("dom", "goog.dom"));
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node rootNode = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliasesPass.getAliasDefinitionsInOrder();
    assertFalse(aliasDefinitionsInOrder.isEmpty());
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinition.getString());
    assertEquals(Node.IDENT, aliasDefinition.getType());

    List<AliasUsage> aliasUsages = scopedAliasesPass.getAliasUsages();
    assertFalse(aliasUsages.isEmpty());
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    AliasedNode aliasedNode = (AliasedNode) aliasUsage;
    Node aliasedNodeReference = aliasedNode.aliasReference;
    assertEquals("dom", aliasedNodeReference.getString());
    assertEquals(Node.IDENT, aliasedNodeReference.getType());
    Node aliasDefinitionNode = aliasedNode.aliasDefinition;
    assertEquals("goog.dom", aliasDefinitionNode.getString());
    assertEquals(Node.IDENT, aliasDefinitionNode.getType());

    Node rootWithAliasesApplied = aliasDefinitionNode.getParent();
    assertEquals(Node.CALL, rootWithAliasesApplied.getType());
    assertEquals("createElement", rootWithAliasesApplied.getLastChild().getString());
    assertEquals(Node.IDENT, rootWithAliasesApplied.getLastChild().getLastChild().getType());
    assertEquals("DIV", rootWithAliasesApplied.getLastChild().getLastChild().getString());
  }

  @Test
  public void testNestedAliases() {
    String input = "goog.scope(function() {" +
        "dom = goog.dom;" +
        "dom2 = function(node) {" +
        "return goog.dom.createElement(node);" +
        "};" +
        "DIV = dom.TagName.DIV;" +
        "dom2.createElement(DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.aliasTransformations.add(new AliasTransformation("dom", "goog.dom"));
    options.aliasTransformations.add(new AliasTransformation("dom2", "goog.dom2"));
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node rootNode = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliasesPass.getAliasDefinitionsInOrder();
    assertFalse(aliasDefinitionsInOrder.isEmpty());
    assertEquals(2, aliasDefinitionsInOrder.size());

    List<AliasUsage> aliasUsages = scopedAliasesPass.getAliasUsages();
    assertFalse(aliasUsages.isEmpty());
    assertEquals(2, aliasUsages.size());

    AliasUsage aliasUsage1 = aliasUsages.get(0);
    assertEquals("dom", aliasUsage1.aliasReference.getString());
    assertEquals("goog.dom", aliasUsage1.aliasDefinition.getString());

    AliasUsage aliasUsage2 = aliasUsages.get(1);
    assertEquals("dom2", aliasUsage2.aliasReference.getString());
    assertEquals("goog.dom2", aliasUsage2.aliasDefinition.getString());
  }

  @Test
  public void testErrorScenarios() {
    String input1 = "goog.scope(function() {" +
        "function dom() {" +
        "};" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    String input2 = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "dom = goog.dom;" +
        "});" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);

    assertThrows(IllegalArgumentException.class, () -> {
      scopedAliasesPass.process(null, compiler.parseTree(input1));
    });

    assertThrows(IllegalArgumentException.class, () -> {
      scopedAliasesPass.process(null, compiler.parseTree(input2));
    });
  }

  @Test
  public void testEmptyAliasDefinition() {
    String input = "goog.scope(function() {" +
        "goog.scope(function() {});" +
        "});";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);

    Node rootNode = compiler.parseTree(input);
    scopedAliasesPass.process(null, rootNode);

    Collection<Node> aliasDefinitionsInOrder = scopedAliasesPass.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitionsInOrder.isEmpty());
  }

  @Test
  public void testAliasTransformationWithSideEffects() {
    String input = "goog.scope(function() {" +
        "dom = goog.dom;" +
        "dom.innerHTML = 'Hello World';" +
        "DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.aliasTransformations.add(new AliasTransformation("dom", "goog.dom"));;
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node rootNode = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliasesPass.getAliasDefinitionsInOrder();
    assertFalse(aliasDefinitionsInOrder.isEmpty());
    assertEquals(1, aliasDefinitionsInOrder.size());

    List<AliasUsage> aliasUsages = scopedAliasesPass.getAliasUsages();
    assertFalse(aliasUsages.isEmpty());
    assertEquals(2, aliasUsages.size());

    AliasUsage aliasUsage1 = aliasUsages.get(0);
    assertEquals("dom", aliasUsage1.aliasReference.getString());
    assertEquals("goog.dom", aliasUsage1.aliasDefinition.getString());

    AliasUsage aliasUsage2 = aliasUsages.get(1);
    assertEquals("innerHTML", aliasUsage2.aliasReference.getString());
    assertEquals("goog.dom", aliasUsage2.aliasDefinition.getString()); // Should be an error

    Map<String, String> expectedErrorMessage = new HashMap<>();
    expectedErrorMessage.put("JSC_GOOG_SCOPE_ALIAS_REDEFINED", "innerHTML");
    assertThrows(RuntimeException.class, () -> {
      Node rootWithAliasesApplied = aliasUsage2.aliasDefinition;
    }, expectedErrorMessage.toString());
  }
}