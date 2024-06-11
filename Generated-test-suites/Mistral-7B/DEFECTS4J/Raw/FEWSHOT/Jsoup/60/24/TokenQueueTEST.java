package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue.addFirst("x");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());

        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("x");
        queue.addFirst("y");
        assertEquals("xy", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("bc"));
        assertFalse(queue.matches("ab"));

        queue.advance();
        assertTrue(queue.matches("b"));
        assertFalse(queue.matches("a"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC");
        assertTrue(queue.matchesCS("A"));
        assertTrue(queue.matchesCS("C"));
        assertFalse(queue.matchesCS("B"));

        queue.advance();
        assertTrue(queue.matchesCS("B"));
        assertFalse(queue.matchesCS("A"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchesAny("a", "b", "c"));
        assertTrue(queue.matchesAny("a", "z", "b"));
        assertFalse(queue.matchesAny("x", "y", "z"));

        queue.advance();
        assertTrue(queue.matchesAny("b", "c", "a"));
        assertFalse(queue.matchesAny("x", "y", "z"));
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matchChomp("a"));
        assertEquals("bc", queue.toString());

        assertTrue(queue.matchChomp("b"));
        assertEquals("c", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" abc ");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesWord());
        queue.advance();
        assertTrue(queue.matchesWord());
        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('c', queue.peek());

        queue.advance();
        assertEquals(-1, queue.remainingLength());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals(-1, queue.remainingLength());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeTo("bc"));
        assertEquals("def", queue.toString());

        assertEquals("def", queue.consumeToIgnoreCase("Def"));
        assertEquals("def", queue.consumeToIgnoreCase("DEf"));
        assertEquals("", queue.consumeToIgnoreCase("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeToAny("a", "b", "c"));
        assertEquals("ef", queue.toString());

        assertEquals("ef", queue.consumeToAny("e", "f"));
        assertEquals("def", queue.consumeToAny("d", "e", "f"));
        assertEquals("", queue.consumeToAny("x", "y", "z"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(abc)def");
        assertEquals("abc", queue.chompBalanced('(', ')'));
        assertEquals("def", queue.toString());

        queue = new TokenQueue("(abc)def\\");
        assertEquals("abc", queue.chompBalanced('(', ')'));
        assertEquals("def\\", queue.toString());

        queue = new TokenQueue("(abc)def\\(xyz)123");
        assertEquals("abc(xyz)123", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testMismatchedBalanced() {
        TokenQueue queue = new TokenQueue("(abc)def)");
        queue.chompBalanced('(', ')');
    }
}