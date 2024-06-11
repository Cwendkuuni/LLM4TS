package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
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
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.addFirst('A');
        assertEquals("Atest", tokenQueue.toString());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matches("te"));
    }
    
    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matchesAny('a', 'e'));
    }
    
    // Include more test cases for other methods
    
}