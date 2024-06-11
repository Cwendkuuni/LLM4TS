package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

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
        queue.addFirst('a');
        assertEquals("atest", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matches("te"));
        assertFalse(queue.matches("tt"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesCS("te"));
        assertFalse(queue.matchesCS("Te"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesAny("te", "es"));
        assertFalse(queue.matchesAny("tt", "es"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<tag>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchChomp("te"));
        assertFalse(queue.matchChomp("tt"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("test");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("test");
        queue.advance();
        assertEquals('e', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals('t', queue.consume());
        assertEquals('e', queue.peek());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("test abc");
        assertEquals("test", queue.consumeTo(" "));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("TeSt abc");
        assertEquals("TeSt", queue.consumeToIgnoreCase(" "));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("test abc");
        assertEquals("test", queue.consumeToAny(" ", "def"));
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("test-abc");
        assertEquals("test", queue.chompTo("-"));
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue queue = new TokenQueue("test-ABC");
        assertEquals("test", queue.chompToIgnoreCase("-"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("test(abc)123");
        assertEquals("test(abc)", queue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("test\\", TokenQueue.unescape("test\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   ");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("test123");
        assertEquals("test123", queue.consumeWord());
        assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("test_123");
        assertEquals("test_123", queue.consumeTagName());
        assertEquals("", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("test*|123");
        assertEquals("test*|123", queue.consumeElementSelector());
        assertEquals("", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("test-123");
        assertEquals("test-123", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("test-:123");
        assertEquals("test-:123", queue.consumeAttributeKey());
        assertEquals("", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("test");
        assertEquals("test", queue.remainder());
        assertTrue(queue.isEmpty());
    }
}