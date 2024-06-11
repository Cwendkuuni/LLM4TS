package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertEquals('H', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("World");
        tokenQueue.addFirst('!');
        assertEquals('!', tokenQueue.peek());
        tokenQueue.addFirst("Hello");
        assertEquals('H', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matches("Hello"));
        assertFalse(tokenQueue.matches("World"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matchesCS("Hello"));
        assertFalse(tokenQueue.matchesCS("hello"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matchesAny("Hello", "World"));
        assertFalse(tokenQueue.matchesAny("World", "Bye"));
    }

    @Test
    public void testConsumes() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        tokenQueue.advance();
        assertEquals('e', tokenQueue.consume());
    }

    // Continue adding test cases for the remaining methods
    // ...

    // Edge cases
    // ...

    // Error scenarios
    // ...
}
