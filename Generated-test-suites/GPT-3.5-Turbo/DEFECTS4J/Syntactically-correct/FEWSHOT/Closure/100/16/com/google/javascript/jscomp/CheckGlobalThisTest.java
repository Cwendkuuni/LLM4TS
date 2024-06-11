package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        assertFalse(CheckGlobalThis.shouldTraverse(null, new Node(), new Node()));
        assertFalse(CheckGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node()));
        assertFalse(CheckGlobalThis.shouldTraverse(null, new Node(Token.NAME), new Node(Token.ASSIGN)));
        assertTrue(CheckGlobalThis.shouldTraverse(null, new Node(Token.NAME), new Node(null)));
    }

    @Test
    public void testVisit() {
        CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
        assertTrue(check.shouldReportThis(new Node(Token.THIS), new Node()));
        assertFalse(check.shouldReportThis(new Node(Token.THIS), new Node(null)));
    }
}