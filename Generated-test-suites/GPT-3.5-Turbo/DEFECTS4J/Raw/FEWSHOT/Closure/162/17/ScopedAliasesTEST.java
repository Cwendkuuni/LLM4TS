package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
    ScopedAliases scopedAliases;

    @Before
    public void setUp() {
        scopedAliases = new ScopedAliases();
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();
        scopedAliases.process(externs, root);
    }

    @Test
    public void testHotSwapScript() {
        Node root = new Node();
        Node originalRoot = new Node();
        scopedAliases.hotSwapScript(root, originalRoot);
    }

    @Test
    public void testEnterScope() {
        NodeTraversal t = new NodeTraversal();
        scopedAliases.enterScope(t);
    }

    @Test
    public void testExitScope() {
        NodeTraversal t = new NodeTraversal();
        scopedAliases.exitScope(t);
    }

    @Test
    public void testShouldTraverse() {
        NodeTraversal t = new NodeTraversal();
        boolean result = scopedAliases.shouldTraverse(t, new Node(), new Node());
        assertEquals(true, result);
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        scopedAliases.visit(t, n, parent);
    }
}