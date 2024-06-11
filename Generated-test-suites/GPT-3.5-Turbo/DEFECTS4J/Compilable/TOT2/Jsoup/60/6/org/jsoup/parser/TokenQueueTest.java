package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
        
        tokenQueue.addFirst("Test");
        assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("Test");
        assertEquals('T', tokenQueue.peek());
        
        TokenQueue emptyTokenQueue = new TokenQueue("");
        assertEquals((char) 0, emptyTokenQueue.peek());
    }
    
    // Additional test cases for other methods will be added here
    
}