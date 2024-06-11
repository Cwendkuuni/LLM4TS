package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {
    
    @Test
    public void testEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst("Hello, ");
        assertEquals("Hello, world", tokenQueue.toString());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matches("test"));
    }
    
    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matchChomp("test"));
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("Hello, world");
        assertEquals("Hello, ", tokenQueue.chompTo(","));
    }
    
    // Additional test cases for other methods can be added here
    
}