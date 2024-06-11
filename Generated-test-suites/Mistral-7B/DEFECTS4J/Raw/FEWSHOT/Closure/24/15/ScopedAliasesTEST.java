package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test case 1: Simple alias
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Node root = Node.parse("goog.scope(function() {\n" +
        "  var foo = 'bar';\n" +
        "});");
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    AliasedNode aliasedNode = (AliasedNode) aliasUsage;
    assertEquals("foo", aliasedNode.aliasReference.getString());
    assertEquals("goog.scope(function() {}\n" +
        "  var bar = 'bar';\n" +
        ")", aliasedNode.aliasDefinition.toString());

    assertEquals("goog.scope(function() {\n" +
        "  var foo = bar;\n" +
        "});", root.toString());

    // Test case 2: Multiple aliases
    root = Node.parse("goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "});");

    scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    aliasUsages = scopedAliases.getAliasUsages();
    scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(2, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    aliasedNode = (AliasedNode) aliasUsage;
    assertEquals("dom", aliasedNode.aliasReference.getString());
    assertEquals("goog.dom", aliasedNode.aliasDefinition.toString());

    aliasUsage = aliasUsages.get(1);
    assertTrue(aliasUsage instanceof AliasedTypeNode);
    AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
    assertEquals("Div", aliasedTypeNode.aliasReference.getString());
    assertEquals("goog.dom.TagName.DIV", aliasedTypeNode.correctedType);

    assertEquals("goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "});", root.toString());
  }

  @Test
  public void testScopedAliasesErrors() {
    // Test case 1: goog.scope with bad parameters
    Node root = Node.parse("goog.scope(function() {});goog.scope(1)");

    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    // Test case 2: goog.scope used improperly
    root = Node.parse("goog.scope(function() {\n" +
        "  return goog.scope(function() {});\n" +
        "});");

    scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    aliasUsages = scopedAliases.getAliasUsages();
    scopeCalls = scopedAliases.getScopeCalls();

    assertTrue(scopedAliases.hasErrors());
    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY: The call to goog.scope must" +
        " be alone in a single statement.", scopedAliases.getLog().get(0).getMessage());
  }

  @Test
  public void testScopedAliasesInvalidAliases() {
    // Test case 1: goog.scope with non-alias local variable
    Node root = Node.parse("goog.scope(function() {\n" +
        "  var foo = 1;\n" +
        "});");

    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertFalse(scopedAliases.hasErrors());
    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL: The local variable foo is in a" +
        " goog.scope and is not an alias.", scopedAliases.getLog().get(0).getMessage());
  }
}