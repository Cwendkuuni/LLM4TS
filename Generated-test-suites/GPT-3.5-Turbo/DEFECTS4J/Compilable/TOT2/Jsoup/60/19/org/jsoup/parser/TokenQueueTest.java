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
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst('H');
        assertEquals("Hello, world", tokenQueue.toString());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("hello, world");
        assertTrue(tokenQueue.matches("hello"));
    }
    
    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("hello, world");
        assertTrue(tokenQueue.matchesAny(',', '!'));
    }
    
    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("hello, world");
        assertTrue(tokenQueue.matchChomp("hello"));
        assertEquals(", world", tokenQueue.toString());
    }
    
    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertEquals('h', tokenQueue.consume());
    }
    
    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("hello, world");
        assertEquals("hello", tokenQueue.consumeTo(","));
    }
    
    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("hello, world");
        assertEquals("hello", tokenQueue.consumeToAny(",", "!"));
    }
    
    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("hello, world");
        assertEquals("hello", tokenQueue.chompTo(","));
    }
    
    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(hello (world))");
        assertEquals("hello (world)", tokenQueue.chompBalanced('(', ')'));
    }

    // Include more test cases for other methods as needed
    
}