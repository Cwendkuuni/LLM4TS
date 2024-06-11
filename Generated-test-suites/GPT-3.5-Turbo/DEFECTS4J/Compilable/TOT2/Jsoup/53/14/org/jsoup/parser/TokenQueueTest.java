package org.jsoup.parser;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {
    
    private TokenQueue tokenQueue;
    
    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("This is a test string");
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
        tokenQueue.consume(); // Consume all characters
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        assertEquals('T', tokenQueue.peek());
        tokenQueue.consume(); // Consume first character
        assertEquals('h', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('A');
        assertEquals('A', tokenQueue.peek());
        tokenQueue.addFirst("123");
        assertEquals('1', tokenQueue.peek());
    }
    
    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("This"));
        assertFalse(tokenQueue.matchesCS("this"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny('T', 't'));
        assertFalse(tokenQueue.matchesAny('-'));
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("This"));
        assertFalse(tokenQueue.matchChomp("not"));
    }
    
    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
        tokenQueue.consume(); // Consume space
        assertTrue(tokenQueue.matchesWhitespace());
    }
    
    @Test
    public void testMatchesWord() {
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance(); // Consume 'T'
        assertFalse(tokenQueue.matchesWord());
    }
    
    // Add more test cases for other methods
    
}