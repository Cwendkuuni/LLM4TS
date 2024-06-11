package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        
        TokenQueue queue2 = new TokenQueue("abc");
        assertFalse(queue2.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
        
        TokenQueue queue2 = new TokenQueue("");
        assertEquals(0, queue2.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("bc");
        queue.addFirst('a');
        assertEquals("abc", queue.toString());
        
        TokenQueue queue2 = new TokenQueue("c");
        queue2.addFirst("ab");
        assertEquals("abc", queue2.toString());
    }
    
    // Add more test cases for each method...
    
    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals("abc", queue.remainder());
        assertTrue(queue.isEmpty());
    }
}