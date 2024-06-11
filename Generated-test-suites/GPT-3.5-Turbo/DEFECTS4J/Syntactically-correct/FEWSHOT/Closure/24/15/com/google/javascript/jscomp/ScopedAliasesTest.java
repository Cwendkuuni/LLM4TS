package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ScopedAliasesTest {
    @Test
    public void testEnterScope() {
        // Test case for entering a scope
        NodeTraversal t = new NodeTraversal();
        Node currentNode = new Node();
        currentNode.setParent(null);
        t.setCurrentNode(currentNode);
        ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
        traversal.enterScope(t);
        
        assertEquals(null, traversal.transformation);
        assertEquals(true, traversal.aliases.isEmpty());
        assertEquals(false, traversal.hasErrors);
    }

    @Test
    public void testExitScope() {
        // Test case for exiting a scope
        NodeTraversal t = new NodeTraversal();
        Node currentNode = new Node();
        currentNode.setParent(null);
        t.setCurrentNode(currentNode);
        ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
        traversal.enterScope(t);
        traversal.exitScope(t);
        
        assertEquals(null, traversal.transformation);
        assertEquals(true, traversal.aliases.isEmpty());
        assertEquals(false, traversal.hasErrors);
    }

    @Test
    public void testShouldTraverse() {
        // Test case for shouldTraverse method
        NodeTraversal t = new NodeTraversal();
        Node currentNode = new Node();
        currentNode.setParent(null);
        t.setCurrentNode(currentNode);
        ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
        boolean result = traversal.shouldTraverse(t, currentNode, null);
        
        assertEquals(true, result);
    }

    @Test
    public void testValidateScopeCall() {
        // Test case for validateScopeCall method
        NodeTraversal t = new NodeTraversal();
        Node currentNode = new Node();
        currentNode.setParent(null);
        t.setCurrentNode(currentNode);
        ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
        traversal.validateScopeCall(t, currentNode, currentNode);
        
        assertEquals(true, traversal.scopeCalls.contains(currentNode));
        assertEquals(false, traversal.hasErrors);
    }

    @Test
    public void testVisit() {
        // Test case for visiting a node
        NodeTraversal t = new NodeTraversal();
        Node currentNode = new Node();
        currentNode.setParent(null);
        t.setCurrentNode(currentNode);
        ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
        traversal.visit(t, currentNode, currentNode);
        
        assertEquals(false, traversal.hasErrors);
    }

    @Test
    public void testFixTypeNode() {
        // Test case for fixing a type node
        ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
        Node typeNode = new Node();
        typeNode.setString("foo.Bar");
        traversal.fixTypeNode(typeNode);
        
        assertEquals(true, traversal.aliasUsages.get(0) instanceof ScopedAliases.AliasedTypeNode);
    }
}