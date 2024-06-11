package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
        
        queue.addFirst("foo");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals('f', queue.peek());

        queue.advance();
        assertEquals('o', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("foo");
        assertEquals("foo", queue.toString());

        queue.addFirst("bar");
        assertEquals("barfoo", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matches("f"));
        assertTrue(queue.matches("oo"));
        assertFalse(queue.matches("bar"));

        queue.advance();
        assertTrue(queue.matches("o"));
        assertFalse(queue.matches("f"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matchesCS("f"));
        assertTrue(queue.matchesCS("oo"));
        assertFalse(queue.matchesCS("bar"));

        queue.advance();
        assertFalse(queue.matchesCS("f"));
        assertTrue(queue.matchesCS("o"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matchesAny("o", "f"));
        assertTrue(queue.matchesAny("o", "o"));
        assertFalse(queue.matchesAny("b", "f"));

        queue.advance();
        assertTrue(queue.matchesAny("o", "f"));
        assertTrue(queue.matchesAny("o", "o"));
        assertFalse(queue.matchesAny("b", "f"));

        queue.advance();
        assertFalse(queue.matchesAny("o", "f"));
        assertFalse(queue.matchesAny("o", "f"));
        assertFalse(queue.matchesAny("b", "f"));
    }

    @Test
    public void testMatchesAnyWithEmptyArray() {
        TokenQueue queue = new TokenQueue("foo");
        assertFalse(queue.matchesAny(new String[]{}));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matchesAny(new char[]{'o'}));
        assertTrue(queue.matchesAny(new char[]{'f'}));
        assertFalse(queue.matchesAny(new char[]{'b'}));

        queue.advance();
        assertTrue(queue.matchesAny(new char[]{'o'}));
        assertTrue(queue.matchesAny(new char[]{'f'}));
        assertFalse(queue.matchesAny(new char[]{'b'}));

        queue.advance();
        assertFalse(queue.matchesAny(new char[]{'o'}));
        assertFalse(queue.matchesAny(new char[]{'o'}));
        assertFalse(queue.matchesAny(new char[]{'b'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<foo>");
        assertTrue(queue.matchesStartTag());

        queue.advance();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchChomp("foo"));
        assertEquals("barbaz", queue.toString());

        assertTrue(queue.matchChomp("<bar>"));
        assertEquals("baz", queue.toString());

        assertFalse(queue.matchChomp("qux"));
        assertEquals("barbaz", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" foo ");
        assertTrue(queue.matchesWhitespace());

        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matchesWord());

        queue.advance();
        assertTrue(queue.matchesWord());

        queue.addFirst("bar");
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals('f', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals(-1, queue.consume());

        queue.addFirst("bar");
        assertEquals('b', queue.consume());
        assertEquals('a', queue.consume());
        assertEquals('r', queue.consume());
        assertEquals(-1, queue.consume());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals('f', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals(-1, queue.consume());

        queue.addFirst("bar");
        assertEquals('b', queue.consume());
        assertEquals('a', queue.consume());
        assertEquals('r', queue.consume());
        assertEquals(-1, queue.consume());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeWithInvalidSequence() {
        TokenQueue queue = new TokenQueue("foo");
        queue.consume("bar");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeTo("foo"));
        assertEquals("<bar>baz", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("FOO<BAR>BAZ");
        assertEquals("FOO", queue.consumeToIgnoreCase("foo"));
        assertEquals("<BAR>BAZ", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo<bar>", queue.consumeToAny("foo", "bar"));
        assertEquals("baz", queue.toString());

        queue.addFirst("<baz>");
        assertEquals("foo<baz>", queue.consumeToAny("foo", "bar"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar) baz) qux");
        assertEquals("foo (bar) baz", queue.chompBalanced('(', ')').toString());
        assertEquals("qux", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("\\foo", TokenQueue.unescape("\\foo"));
        assertEquals("foo\\bar", TokenQueue.unescape("foo\\bar"));
        assertEquals("foo\\\\bar", TokenQueue.unescape("foo\\\\bar"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo  ");
        assertTrue(queue.consumeWhitespace());

        queue.addFirst("bar");
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals("foo", queue.consumeWord());

        queue.addFirst("bar");
        assertEquals("bar", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<foo>");
        assertEquals("foo", queue.consumeTagName());

        queue.addFirst("<bar>");
        assertEquals("bar", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#id.class foo:hover");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("#id.class foo:hover", queue.toString());

        queue.addFirst("p:hover");
        assertEquals("p:hover", queue.consumeElementSelector());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id: foo id: bar class: baz");
        assertEquals("foo", queue.consumeCssIdentifier());
        assertEquals("id: bar class: baz", queue.toString());

        queue.addFirst("class: qux");
        assertEquals("class: qux", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"foo\" id=\"bar\" class=\"baz\"");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id=\"bar\" class=\"baz\"", queue.toString());

        queue.addFirst("id");
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("baz", queue.remainder());
        assertEquals("foo<bar>", queue.toString());

        queue.advance();
        assertEquals("baz", queue.remainder());
        assertEquals("bar", queue.toString());

        queue.advance();
        assertEquals("", queue.remainder());
        assertEquals("", queue.toString());
    }

    @Test
    public void testToString() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo<bar>baz", queue.toString());

        queue.advance();
        assertEquals("bar", queue.toString());

        queue.advance();
        assertEquals("baz", queue.toString());

        queue.advance();
        assertEquals("", queue.toString());
    }
}