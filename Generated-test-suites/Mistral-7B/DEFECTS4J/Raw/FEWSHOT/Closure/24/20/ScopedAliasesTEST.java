package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);

    // Test empty tree
    Node root = Node.NULL;
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test invalid goog.scope call
    Node rootWithInvalidScopeCall = Node.newBlock("badScopeCall", new Node[] {
        Node.newCall(
            "goog.scope",
            new Node[] { Node.newFunction("", null, null) })
    });
    scopedAliases.process(null, rootWithInvalidScopeCall);
    assertTrue(scopedAliases.hasErrors());

    // Test valid goog.scope call
    Node rootWithValidScopeCall = Node.newBlock("validScopeCall", new Node[] {
        Node.newCall(
            "goog.scope",
            new Node[] { Node.newFunction("", null, null) })
    });

    Node scopeFunction = rootWithValidScopeCall.getChildAtIndex(0);
    Node functionBody = scopeFunction.getLastChild();

    Node aliasDefinition = Node.newAssign(
        Node.newVar("alias", null, null, null),
        Node.newName("dom"));
    functionBody.appendChild(aliasDefinition);

    Node aliasUsage = Node.newAssign(
        Node.newVar("newAlias", null, null, null),
        Node.newName("alias"));
    functionBody.appendChild(aliasUsage);

    Node aliasDefinitionInFunction = Node.newAssign(
        Node.newVar("aliasInFunction", null, null, null),
        Node.newName("alias"));
    aliasDefinition.replaceChild(aliasDefinition.getLastChild(), aliasDefinitionInFunction);

    scopedAliases.process(null, rootWithValidScopeCall);
    assertFalse(scopedAliases.hasErrors());

    // Test alias redefinition
    Node rootWithAliasRedefinition = Node.newBlock("aliasRedefinition", new Node[] {
        Node.newCall(
            "goog.scope",
            new Node[] { Node.newFunction("", null, null) })
    });

    scopeFunction = rootWithAliasRedefinition.getChildAtIndex(0);
    functionBody = scopeFunction.getLastChild();

    aliasDefinition = Node.newAssign(
        Node.newVar("alias", null, null, null),
        Node.newName("dom"));
    functionBody.appendChild(aliasDefinition);

    aliasDefinition = Node.newAssign(
        Node.newVar("alias", null, null, null),
        Node.newName("dom"));
    functionBody.appendChild(aliasDefinition);

    scopedAliases.process(null, rootWithAliasRedefinition);
    assertTrue(scopedAliases.hasErrors());

    // Test alias usage
    Node rootWithAliasUsage = Node.newBlock("aliasUsage", new Node[] {
        Node.newCall(
            "goog.scope",
            new Node[] { Node.newFunction("", null, null) })
    });

    scopeFunction = rootWithAliasUsage.getChildAtIndex(0);
    functionBody = scopeFunction.getLastChild();

    aliasDefinition = Node.newAssign(
        Node.newVar("alias", null, null, null),
        Node.newName("dom"));
    functionBody.appendChild(aliasDefinition);

    aliasUsage = Node.newAssign(
        Node.newVar("newAlias", null, null, null),
        Node.newName("alias"));
    functionBody.appendChild(aliasUsage);

    Node aliasUsageNode = aliasUsage.getLastChild();
    scopedAliases.process(null, rootWithAliasUsage);
    assertFalse(scopedAliases.hasErrors());

    assertEquals("dom", aliasUsageNode.getString());

    // Test alias definition order
    Node rootWithAliasDefinitionOrder = Node.newBlock("aliasDefinitionOrder", new Node[] {
        Node.newCall(
            "goog.scope",
            new Node[] { Node.newFunction("", null, null) })
    });

    scopeFunction = rootWithAliasDefinitionOrder.getChildAtIndex(0);
    functionBody = scopeFunction.getLastChild();

    aliasDefinition = Node.newAssign(
        Node.newVar("alias1", null, null, null),
        Node.newName("dom"));
    functionBody.appendChild(aliasDefinition);

    aliasDefinition = Node.newAssign(
        Node.newVar("alias2", null, null, null),
        Node.newName("dom"));
    functionBody.appendChild(aliasDefinition);

    scopedAliases.process(null, rootWithAliasDefinitionOrder);
    assertFalse(scopedAliases.hasErrors());

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("alias2", aliasDefinitionsInOrder.get(1).getChildAtIndex(0).getString());
    assertEquals("alias1", aliasDefinitionsInOrder.get(0).getChildAtIndex(0).getString());

    // Test alias usage order
    Node rootWithAliasUsageOrder = Node.newBlock("aliasUsageOrder", new Node[] {
        Node.newCall(
            "goog.scope",
            new Node[] { Node.newFunction("", null, null) })
    });

    Node aliasDefinition1 = Node.newAssign(
        Node.newVar("alias1", null, null, null), Node.newName("dom"));
    Node aliasUsage1 = Node.newAssign(
        Node.newVar("newAlias1", null, null, null),
        Node.newName("alias1"));
    Node aliasDefinition2 = Node.newAssign(
        Node.newVar("alias2", null, null, null), Node.newName("dom"));
    Node aliasUsage2 = Node.newAssign(
        Node.newVar("newAlias2", null, null, null),
        Node.newName("alias2"));

    scopeFunction = rootWithAliasUsageOrder.getChildAtIndex(0);
    functionBody = scopeFunction.getLastChild();
    functionBody.appendChild(aliasDefinition1);
    functionBody.appendChild(aliasUsage1);
    functionBody.appendChild(aliasDefinition2);
    functionBody.appendChild(aliasUsage2);

    scopedAliases.process(null, rootWithAliasUsageOrder);
    assertFalse(scopedAliases.hasErrors());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    assertEquals("newAlias1", aliasUsages.get(1).aliasReference.getString());
    assertEquals("newAlias2", aliasUsages.get(0).aliasReference.getString());
  }
}