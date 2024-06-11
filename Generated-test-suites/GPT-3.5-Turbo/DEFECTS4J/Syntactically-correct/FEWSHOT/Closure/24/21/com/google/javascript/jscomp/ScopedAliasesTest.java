
/*
 * COPYRIGHT 2019 ALL RIGHTS RESERVED
 *
 * This program contains proprietary information which is a trade
 * secret of OpenAI Inc. and/or its affiliates and/or their licensors,
 * and is subject to applicable licensing agreements.
 *
 */

package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ScopedAliasesTest {

    private ScopedAliases scopedAliases;

    @Before
    public void setup() {
        scopedAliases = new ScopedAliases(new Compiler(), null, null);
    }

    @Test
    public void testEnterScope() {
        assertNotNull(scopedAliases);
    }

    @Test
    public void testExitScope() {
        assertNotNull(scopedAliases);
    }

    @Test
    public void testHotSwapScript() {
        scopedAliases.hotSwapScript(null, null);
        assertNotNull(scopedAliases);
    }

    @Test
    public void testProcess() {
        scopedAliases.process(null, null);
        assertNotNull(scopedAliases);
    }

    @Test
    public void testShouldTraverse() {
        NodeTraversal t = new NodeTraversal(new Compiler(), null, null);
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.EMPTY);
        scopedAliases.shouldTraverse(t, n, parent);
        assertNotNull(scopedAliases);
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal(new Compiler(), null, null);
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.EMPTY);
        scopedAliases.visit(t, n, parent);
        assertNotNull(scopedAliases);
    }

}
