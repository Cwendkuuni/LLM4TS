package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());

        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        assertEquals('a', queue.consume());

        queue.addFirst("123");
        assertEquals("123", queue.consumeToIgnoreCase(new String[]{"123"}));
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("bc"));
        assertTrue(queue.matches("a"));
        assertFalse(queue.matches("cd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABC");
        assertTrue(queue.matchesCS("ABC"));
        assertFalse(queue.matchesCS("abc"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcd");
        assertTrue(queue.matchesAny("a", "d"));
        assertFalse(queue.matchesAny("e"));

        queue.addFirst("xyz");
        assertTrue(queue.matchesAny("x", "y", "z"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<tag>");
        assertTrue(queue.matchesStartTag());

        queue.advance();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<tag class=\"foo\" id=\"bar\">");
        assertTrue(queue.matchChomp("class"));
        assertTrue(queue.consume().equals("foo"));

        queue.matchChomp("id");
        assertTrue(queue.consume().equals("bar"));
        assertFalse(queue.matchChomp("class"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \n\tabc ");
        assertTrue(queue.matchesWhitespace());
        queue.advance();
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcd123ef");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());

        queue.addFirst("xyz");
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.consume());

        queue.advance();
        assertEquals('c', queue.consume());

        queue.addFirst("123");
        queue.advance();
        assertEquals('1', queue.consume());
        assertEquals('2', queue.consume());
        assertEquals('3', queue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertNull(queue.consume());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abc123def");
        assertEquals("abc", queue.consumeTo("abc"));
        assertEquals("123", queue.consumeToIgnoreCase(new String[]{"123"}));
        assertEquals("def", queue.remainder());

        queue.addFirst("xyz");
        assertEquals("xyz", queue.consumeToIgnoreCase(new String[]{"xyz"}));
        assertEquals("def", queue.remainder());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abc123def");
        assertEquals("abc123", queue.consumeToAny(new String[]{"abc", "123"}));
        assertEquals("def", queue.remainder());

        queue.addFirst("xyz");
        assertEquals("xyz", queue.consumeToAny(new String[]{"xyz"}));
        assertEquals("def", queue.remainder());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("((())())");
        assertEquals("((())", queue.chompBalanced('(', ')'));
        assertEquals(")", queue.remainder());

        queue.addFirst("((((()");
        assertEquals("((((", queue.chompBalanced('(', '('));
        assertEquals("()", queue.chompBalanced(')', ')'));
        assertEquals(")", queue.remainder());
    }

    @Test
    public void testUnescape() {
        assertEquals("\\\\\\\\", TokenQueue.unescape("\\\\\\\\\\\\\\\\\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" \n\tabc ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("", queue.consume());
        assertEquals("", queue.consume());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcd123ef");
        assertEquals("abcd", queue.consumeWord());
        assertEquals("ef", queue.consumeWord());
        assertNull(queue.consumeWord());

        queue.addFirst("xyz");
        assertEquals("xyz", queue.consumeWord());
        assertNull(queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag>");
        assertEquals("tag", queue.consumeTagName());

        queue.addFirst("<tag class=\"foo\" id=\"bar\">");
        assertEquals("tag", queue.consumeTagName());

        queue.addFirst("<tag name=\"foo\" class=\"bar\">");
        assertEquals("tag", queue.consumeTagName());
        assertEquals("name", queue.consumeAttributeKey());
        assertEquals("foo", queue.consume());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<tag class=\"foo\" id=\"bar\">");
        assertEquals("tag", queue.consumeElementSelector());

        queue.addFirst("<tag name=\"foo\" class=\"bar\">");
        assertEquals("tag", queue.consumeElementSelector());
        assertEquals("name", queue.consumeAttributeKey());
        assertEquals("foo", queue.consume());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class=\"foo\" id=\"bar\"");
        assertEquals("foo", queue.consumeCssIdentifier());
        assertEquals("bar", queue.consumeCssIdentifier());
        assertNull(queue.consumeCssIdentifier());

        queue.addFirst("class=\"bar\" id=\"foo\"");
        assertEquals("bar", queue.consumeCssIdentifier());
        assertEquals("foo", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"foo\" id=\"bar\"");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id", queue.consumeAttributeKey());
        assertNull(queue.consumeAttributeKey());

        queue.addFirst("class=\"bar\" id=\"foo\"");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abc123def");
        assertEquals("def", queue.remainder());

        queue.addFirst("xyz");
        assertEquals("", queue.consumeToAny(new String[]{}));
        assertEquals("", queue.remainder());
    }
}