package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue("Queue should be empty", tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("example");
        assertEquals('e', tokenQueue.peek());
    }
    
    // Additional test cases for each method can be added
    
    // Edge cases and exception handling tests can be added here
    
}
