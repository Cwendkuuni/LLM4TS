package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
        tokenQueue.addFirst("test");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("abcdef");
        assertEquals('a', tokenQueue.peek());
        tokenQueue.consume();
        assertEquals('b', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("123");
        tokenQueue.addFirst('0');
        assertEquals("0123", tokenQueue.toString());
    }

    // Add test cases for other public methods as well...

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("Hello World, welcome");
        assertEquals("Hello World", tokenQueue.consumeToIgnoreCase(","));
        assertEquals(", ", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("It's escaped", TokenQueue.unescape("It\\'s escaped"));
    }

    // Additional test cases for edge cases and exception handling scenarios...

}