package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    // Test for isEmpty() method
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());

        tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.isEmpty());
    }

    // Test for peek() method
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());

        tokenQueue = new TokenQueue("");
        assertEquals((char) 0, tokenQueue.peek());
    }

    // Additional test cases and method tests can be added here

}