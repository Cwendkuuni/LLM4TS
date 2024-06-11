package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue.addFirst("a");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.peek());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst('x');
        assertEquals('x', queue.consume());
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("efg"));
        assertFalse(queue.matches("abcd"));
        assertTrue(queue.matches("ef"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEF");
        assertTrue(queue.matchesCS("AB"));
        assertFalse(queue.matchesCS("efg"));
        assertFalse(queue.matchesCS("abcd"));
        assertTrue(queue.matchesCS("EF"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("efg"));
        assertFalse(queue.matchesAny("xyz"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny('a', 'b', 'c'));
        assertFalse(queue.matchesAny('g'));
        assertFalse(queue.matchesAny('x'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head></head><body></body>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<html><head><title>My Page</title></head><body></body>");
        assertTrue(queue.matchChomp("title"));
        assertEquals("My Page", queue.consumeToIgnoreCase("title"));
        assertFalse(queue.matchChomp("body"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("  foo  ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foobar");
        assertTrue(queue.matchesWord());
        assertTrue(queue.consumeWord().equals("foo"));
        assertTrue(queue.matchesWord());
        assertTrue(queue.consumeWord().equals("bar"));
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.advance();
        assertEquals('b', queue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.consume("ab");
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("<html><head><title>My Page</title></head><body></body>");
        assertEquals("title", queue.consumeToIgnoreCase("title"));
        assertEquals("My Page", queue.consumeToIgnoreCase("title"));
        assertEquals("body", queue.consumeToIgnoreCase("body"));
        assertEquals("", queue.consumeToIgnoreCase("script"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdefabcdef");
        assertEquals("abcdef", queue.consumeToAny("abc", "def"));
        assertEquals("def", queue.consumeToAny("abc", "def", "ghi"));
        assertEquals("", queue.consumeToAny("xyz"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar (baz) qux) quux) quux");
        assertEquals("foo (bar (baz) qux)", queue.chompBalanced('(', ')'));
        assertEquals("bar (baz) qux", queue.chompBalanced('(', ')'));
        assertEquals("baz", queue.chompBalanced('(', ')'));
        assertEquals("qux", queue.chompBalanced('(', ')'));
        assertEquals("quux", queue.chompBalanced('(', ')'));
        assertEquals("", queue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("\\abc\\def\\ghi", TokenQueue.unescape("\\abc\\def\\ghi"));
        assertEquals("abcdefghi", TokenQueue.unescape("abcdefghi"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   foo   ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foobar");
        assertEquals("foo", queue.consumeWord());
        assertEquals("bar", queue.consumeWord());
        assertFalse(queue.consumeWord().isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>My Page</title></head><body></body>");
        assertEquals("html", queue.consumeTagName());
        assertEquals("head", queue.consumeTagName());
        assertEquals("title", queue.consumeTagName());
        assertEquals("body", queue.consumeTagName());
        assertFalse(queue.consumeTagName().isEmpty());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<html><head><title>My Page</title></head><body><div class=\"foo\"></div></body>");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("foo", queue.consumeTagName());
        assertFalse(queue.consumeElementSelector().isEmpty());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("background-color: #f00; color: blue;");
        assertEquals("background-color", queue.consumeCssIdentifier());
        assertEquals("#f00", queue.consumeCssIdentifier());
        assertEquals("color", queue.consumeCssIdentifier());
        assertEquals("blue", queue.consumeCssIdentifier());
        assertFalse(queue.consumeCssIdentifier().isEmpty());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("<img src=\"image.jpg\" alt=\"Image\">");
        assertEquals("src", queue.consumeAttributeKey());
        assertEquals("alt", queue.consumeAttributeKey());
        assertFalse(queue.consumeAttributeKey().isEmpty());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder());
        assertEquals("", queue.remainder());
    }
}