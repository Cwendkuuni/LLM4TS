package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        // Typical use case: Queue is empty
        TokenQueue queue1 = new TokenQueue("");
        assertTrue(queue1.isEmpty());

        // Edge case: Queue has only one character
        TokenQueue queue2 = new TokenQueue("a");
        assertFalse(queue2.isEmpty());

        // Error scenario: Queue is null
        TokenQueue queue3 = new TokenQueue(null);
        assertTrue(queue3.isEmpty());
    }

    @Test
    public void testPeek() {
        // Typical use case: Queue is not empty
        TokenQueue queue1 = new TokenQueue("abc");
        assertEquals('a', queue1.peek());

        // Edge case: Queue has only one character
        TokenQueue queue2 = new TokenQueue("d");
        assertEquals('d', queue2.peek());

        // Error scenario: Queue is empty
        TokenQueue queue3 = new TokenQueue("");
        assertEquals(0, queue3.peek());
    }

    @Test
    public void testAddFirst() {
        // Typical use case: Add a character to the start of the queue
        TokenQueue queue1 = new TokenQueue("abc");
        queue1.addFirst('x');
        assertEquals("xabc", queue1.toString());

        // Edge case: Add an empty string to the start of the queue
        TokenQueue queue2 = new TokenQueue("xyz");
        queue2.addFirst("");
        assertEquals("xyz", queue2.toString());

        // Error scenario: Queue is null
        TokenQueue queue3 = new TokenQueue(null);
        queue3.addFirst('y');
        assertEquals("y", queue3.toString());
    }

    @Test
    public void testMatches() {
        // Typical use case: Queue matches the sequence
        TokenQueue queue1 = new TokenQueue("hello world");
        assertTrue(queue1.matches("hello"));

        // Edge case: Queue does not match the sequence
        TokenQueue queue2 = new TokenQueue("goodbye");
        assertFalse(queue2.matches("hello"));

        // Error scenario: Queue is null
        TokenQueue queue3 = new TokenQueue(null);
        assertFalse(queue3.matches("hello"));
    }

    // Add test cases for all other methods in TokenQueue class


}