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
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst("Hello, ");
        assertEquals("Hello, world", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertTrue(tokenQueue.matches("hello"));
    }

    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.consume());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeException() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.consume("tes");
    }

    // Add more test cases for other methods...

}