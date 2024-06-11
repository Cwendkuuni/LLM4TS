package org.jsoup.parser;
import org.jsoup.parser.TokenQueue;
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
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.addFirst('Z');
        assertEquals("Zabc", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("HelloWorld");
        assertTrue(tokenQueue.matches("Hello"));
    }

    // More test cases for other methods can be added based on similar approach

}