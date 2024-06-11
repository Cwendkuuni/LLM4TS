package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());

        TokenQueue tokenQueueNotEmpty = new TokenQueue("data");
        assertFalse(tokenQueueNotEmpty.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.addFirst('z');
        assertEquals("zabc", tokenQueue.toString());
    }

    // Additional test cases for other methods will be added here...

}