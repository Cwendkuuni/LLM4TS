package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("test");
        assertFalse(queue.isEmpty());
        queue.consume("test");
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals('t', queue.peek());
        queue.consume();
        assertEquals('e', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("test");
        queue.addFirst("new");
        assertEquals("newtest", queue.toString());
    }
    
    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matches("test"));
        assertFalse(queue.matches("TEST"));
    }
    
    // Add more test cases for the rest of the methods in the TokenQueue class

}