package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

    @Test
    public void testScopedAliases() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);

        // Test empty case
        Node root = Node.createTree("", null);
        scopedAliases.process(null, root);
        List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
        List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
        List<Node> scopeCalls = scopedAliases.getScopeCalls();
        assertFalse("No alias definitions should be created", aliasDefinitionsInOrder.isEmpty());
        assertFalse("No alias usages should be created", aliasUsages.isEmpty());
        assertFalse("No scope calls should be created", scopeCalls.isEmpty());
        assertNull("No transformation should be set", scopedAliases.getTransformation());

        // Test alias definition case
        root = Node.createTree("goog.scope(function() {" +
                "var dom = goog.dom;" +
                "var DIV = dom.TagName.DIV;" +
                "})()", null);
        scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.process(null, root);
        aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
        aliasUsages = scopedAliases.getAliasUsages();
        scopeCalls = scopedAliases.getScopeCalls();
        assertTrue("Alias definitions should be in order",
                aliasDefinitionsInOrder.get(0).getQualifiedName().equals("dom"));
        assertTrue("Alias usage count should match definition count",
                aliasUsages.size() == 1);
        assertTrue("Alias usage should use the alias",
                aliasUsages.get(0) instanceof AliasedNode &&
                        ((AliasedNode) aliasUsages.get(0)).aliasReference.getQualifiedName()
                                .equals("dom"));
        assertTrue("Scope calls should be empty", scopeCalls.isEmpty());

        // Test alias redefinition case
        root = Node.createTree("goog.scope(function() {" +
                "var dom = goog.dom;" +
                "var dom = window;" +
                "})()", null);
        scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.process(null, root);
        aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
        aliasUsages = scopedAliases.getAliasUsages();
        scopeCalls = scopedAliases.getScopeCalls();
        assertTrue("Alias redefinition error should be reported",
                scopedAliases.hasErrors());
        assertTrue("Alias definition count should be the same",
                aliasDefinitionsInOrder.size() == 1);
        assertTrue("Alias usage count should be zero", aliasUsages.isEmpty());
        assertTrue("Scope calls should be empty", scopeCalls.isEmpty());

        // Test alias usage outside of scope case
        root = Node.createTree("goog.scope(function() {" +
                "var dom = goog.dom;" +
                "})();" +
                "dom.createElement('DIV')", null);
        scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.process(null, root);
        aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
        aliasUsages = scopedAliases.getAliasUsages();
        scopeCalls = scopedAliases.getScopeCalls();
        assertFalse("Alias definitions should be empty", aliasDefinitionsInOrder.isEmpty());
        assertTrue("Alias usage should not be in the list",
                aliasUsages.isEmpty());
        assertTrue("Scope calls should be empty", scopeCalls.isEmpty());

        // Test alias usage inside of scope case
        root = Node.createTree("goog.scope(function() {" +
                "var dom = goog.dom;" +
                "dom.createElement('DIV')" +
                "})()", null);
        scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.process(null, root);
        aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
        aliasUsages = scopedAliases.getAliasUsages();
        scopeCalls = scopedAliases.getScopeCalls();
        assertTrue("Alias definitions should be empty", aliasDefinitionsInOrder.isEmpty());
        assertTrue("Alias usage count should match definition count",
                aliasUsages.size() == 1);
        assertTrue("Alias usage should use the alias",
                aliasUsages.get(0) instanceof AliasedNode &&
                        ((AliasedNode) aliasUsages.get(0)).aliasDefinition.getQualifiedName()
                                .equals("dom"));
        assertTrue("Scope calls should be empty", scopeCalls.isEmpty());

        // Test alias definition with type case
        root = Node.createTree("goog.scope(function() {" +
                "var dom = goog.dom;" +
                "var DIV = goog.dom.TagName.DIV;" +
                "})()", null);
        scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.process(null, root);
        aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
        aliasUsages = scopedAliases.getAliasUsages();
        scopeCalls = scopedAliases.getScopeCalls();
        assertTrue("Alias definitions should be in order",
                aliasDefinitionsInOrder.get(0).getQualifiedName().equals("dom"));
        assertTrue("Alias usage count should match definition count",
                aliasUsages.size() == 1);
        assertTrue("Alias usage should use the alias",
                aliasUsages.get(0) instanceof AliasedTypeNode &&
                        ((AliasedTypeNode) aliasUsages.get(0)).aliasReference.getString()
                                .equals("DIV"));
        assertTrue("Scope calls should be empty", scopeCalls.isEmpty());

        // Test alias definition with function case
        root = Node.createTree("goog.scope(function() {" +
                "function googleMap() {" +
                "}" +
                "var google = this;" +
                "})()", null);
        scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.process(null, root);
        aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
        aliasUsages = scopedAliases.getAliasUsages();
        scopeCalls = scopedAliases.getScopeCalls();
        assertTrue("Alias definitions should be empty", aliasDefinitionsInOrder.isEmpty());
        assertTrue("Alias usage count should be zero", aliasUsages.isEmpty());
        assertTrue("Scope calls should be empty", scopeCalls.isEmpty());

        // Test alias definition with this case
        root = Node.createTree("goog.scope(function() {" +
                "this.google = goog;" +
                "})()", null);
        scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.process(null, root);
        aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
        aliasUsages = scopedAliases.getAliasUsages();
        scopeCalls = scopedAliases.getScopeCalls();
        assertTrue("Alias definitions should be empty", aliasDefinitionsInOrder.isEmpty());
        assertTrue("Alias usage count should be zero", aliasUsages.isEmpty());
        assertTrue("Scope calls should be empty", scopeCalls.isEmpty());
    }
}