
package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("Test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("Test");
        assertEquals('T', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("Test");
        queue.addFirst('T');
        assertEquals("TTest", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("Test");
        assertTrue(queue.matches("Test"));
        assertFalse(queue.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("Test");
        assertTrue(queue.matchesCS("Test"));
        assertFalse(queue.matchesCS("test"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("Test");
        assertTrue(queue.matchesAny("Test", "test"));
        assertFalse(queue.matchesAny("abc", "def"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<a>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("Test");
        assertTrue(queue.matchChomp("Test"));
        assertFalse(queue.matchChomp("test"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("Test123");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("Test");
        queue.advance();
        assertEquals('e', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("Test");
        assertEquals('T', queue.consume());
        assertEquals('e', queue.peek());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("Test123");
        assertEquals("Test", queue.consumeTo("1"));
        assertEquals("123", queue.remainder());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Test123");
        assertEquals("Te", queue.consumeToIgnoreCase("1"));
        assertEquals("st123", queue.remainder());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("Test123");
        assertEquals("Test", queue.consumeToAny("1", "2"));
        assertEquals("123", queue.remainder());
    }

    @Test
    public void testChompTo() {
        TokenQueue queue = new TokenQueue("Test123");
        assertEquals("Test", queue.chompTo("1"));
        assertEquals("23", queue.remainder());
    }

    @Test
    public void testChompToIgnoreCase() {
        TokenQueue queue = new TokenQueue("Test123");
        assertEquals("Te", queue.chompToIgnoreCase("1"));
        assertEquals("23", queue.remainder());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')'));
        assertEquals("four", queue.remainder());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\", TokenQueue.unescape("\\\\"));
        assertEquals("a", TokenQueue.unescape("\\a"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   Test");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("Test123");
        assertEquals("Test", queue.consumeWord());
        assertEquals("123", queue.remainder());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("div");
        assertEquals("div", queue.consumeTagName());
        assertEquals("", queue.remainder());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div|name");
        assertEquals("div|name", queue.consumeElementSelector());
        assertEquals("", queue.remainder());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("test-identifier");
        assertEquals("test-identifier", queue.consumeCssIdentifier());
        assertEquals("", queue.remainder());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("data-key");
        assertEquals("data-key", queue.consumeAttributeKey());
        assertEquals("", queue.remainder());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("Test");
        assertEquals("Test", queue.remainder());
        assertTrue(queue.isEmpty());
    }
}

