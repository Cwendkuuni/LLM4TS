package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        TokenQueue queue2 = new TokenQueue("test");
        assertFalse(queue2.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals('t', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("test");
        queue.addFirst('a');
        assertEquals("atest", queue.toString());

        queue.addFirst("hello");
        assertEquals("helloatest", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matches("tes"));
        assertFalse(queue.matches("tesa"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesCS("tes"));
        assertFalse(queue.matchesCS("TES"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesAny("tes"));
        assertFalse(queue.matchesAny("aaa"));
    }

    // continue with the rest of the methods in TokenQueue class...

}