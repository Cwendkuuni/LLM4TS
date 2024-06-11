package org.jsoup.parser;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
        
        tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("testing");
        assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.addFirst("add");
        assertEquals("addtest", tokenQueue.toString());
    }
    
    // More test cases for other methods
    
}