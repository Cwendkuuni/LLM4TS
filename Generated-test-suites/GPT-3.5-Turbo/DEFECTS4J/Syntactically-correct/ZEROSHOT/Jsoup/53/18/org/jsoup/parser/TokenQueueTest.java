package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
        
        tokenQueue.addFirst("abc");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        
        assertEquals('a', tokenQueue.peek());
        
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.addFirst("xyz");
        
        assertEquals("xyzabc", tokenQueue.toString());
        
        tokenQueue.addFirst("123");
        assertEquals("123xyzabc", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        
        assertTrue(tokenQueue.matches("abc"));
        assertFalse(tokenQueue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        
        assertTrue(tokenQueue.matchesCS("abc"));
        assertFalse(tokenQueue.matchesCS("ABC"));
    }

    // ... continue with comprehensive testing of all methods
}