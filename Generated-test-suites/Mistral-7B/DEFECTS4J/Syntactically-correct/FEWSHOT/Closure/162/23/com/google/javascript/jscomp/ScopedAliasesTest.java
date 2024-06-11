package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test basic functionality
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    Node root = Node.createTree("function foo() { var goog = {}; goog.dom = {}; goog.dom.createElement = function() {}; }");
    scopedAliases.process(null, root);

    // Check that aliases were correctly detected and applied
    List<Node> aliasDefinitions = Lists.newArrayList(
        root.getChildAtIndex(1),
        root.getChildAtIndex(2)
    );
    List<Node> aliases = Lists.newArrayList(
        root.getChildAtIndex(4),
        root.getChildAtIndex(5)
    );
    List<Node> scopeCalls = Lists.newArrayList(root.getChildAtIndex(0));

    assertTrue(scopedAliases.getAliasDefinitionsInOrder().equals(aliasDefinitions));
    assertTrue(scopedAliases.getAliasUsages().isEmpty());
    assertTrue(scopedAliases.getScopeCalls().equals(scopeCalls));

    // Check that aliases were correctly applied
    for (Node alias : aliases) {
      Node aliasDefinition = aliasDefinitions.get(aliasDefinitions.indexOf(alias));
      Node aliasReference = alias.getFirstChild();
      Node aliasDefinitionReference = aliasDefinition.getFirstChild();

      assertSame(aliasDefinitionReference, aliasReference);
    }

    // Test error handling
    Node rootWithErrors = Node.createTree("function foo() { var goog = {}; goog.scope(); }");
    scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, rootWithErrors);

    List<Node> aliasDefinitionsWithErrors = Lists.newArrayList(
        rootWithErrors.getChildAtIndex(1)
    );
    List<Node> aliasesWithErrors = Lists.newArrayList(
        rootWithErrors.getChildAtIndex(3)
    );

    List<Node> scopeCallsWithErrors = Lists.newArrayList(rootWithErrors.getChildAtIndex(0));

    assertTrue(scopedAliases.getAliasDefinitionsInOrder().equals(aliasDefinitionsWithErrors));
    assertTrue(scopedAliases.getAliasUsages().isEmpty());
    assertTrue(scopedAliases.getScopeCalls().equals(scopeCallsWithErrors));
    assertTrue(scopedAliases.hasErrors());

    // Test that aliases are not redefined
    Node rootWithRedefinedAlias = Node.createTree("function foo() { var goog = {}; var goog = {}; }");
    scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, rootWithRedefinedAlias);

    List<Node> aliasDefinitionsWithRedefinedAlias = Lists.newArrayList();
    List<Node> aliasesWithRedefinedAlias = Lists.newArrayList();
    List<Node> scopeCallsWithRedefinedAlias = Lists.newArrayList();

    assertFalse(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(scopedAliases.getAliasUsages().isEmpty());
    assertTrue(scopedAliases.getScopeCalls().isEmpty());
    assertTrue(scopedAliases.hasErrors());

    // Test that non-alias locals are not processed
    Node rootWithNonAliasLocal = Node.createTree("function foo() { var goog = {}; var local = {}; }");
    scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, rootWithNonAliasLocal);

    List<Node> aliasDefinitionsWithNonAliasLocal = Lists.newArrayList();
    List<Node> aliasesWithNonAliasLocal = Lists.newArrayList();
    List<Node> scopeCallsWithNonAliasLocal = Lists.newArrayList();

    assertTrue(scopedAliases.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(scopedAliases.getAliasUsages().isEmpty());
    assertTrue(scopedAliases.getScopeCalls().isEmpty());
    assertNull(scopedAliases.getErrorMessage());
  }
}