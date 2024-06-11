package org.jsoup.parser;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tq = new TokenQueue("");
        assertTrue(tq.isEmpty());

        tq = new TokenQueue("data");
        assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tq = new TokenQueue("data");
        assertEquals('d', tq.peek());

        tq = new TokenQueue("");
        assertEquals(0, tq.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("data");
        tq.addFirst('A');
        assertEquals("Adata", tq.toString());
    }

    // Additional test methods for other public methods in TokenQueue class

}