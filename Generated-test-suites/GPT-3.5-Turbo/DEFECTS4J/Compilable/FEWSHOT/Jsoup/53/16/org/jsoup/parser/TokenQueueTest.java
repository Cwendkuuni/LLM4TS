
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenQueueTest {
    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals('t', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("test");
        queue.addFirst('A');
        assertEquals("Atest", queue.toString());

        queue.addFirst("B");
        assertEquals("BAtest", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matches("test"));
        assertFalse(queue.matches("TesT"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesCS("test"));
        assertFalse(queue.matchesCS("TesT"));
    }
    
    // ... continue with more test methods for the other methods in the TokenQueue class
    
}
