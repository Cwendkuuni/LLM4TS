package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("foo");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals('f', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst("bar");
        assertEquals("bara", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matches("foo"));
        assertTrue(queue.matches("FOO"));
        assertFalse(queue.matches("bar"));

        queue = new TokenQueue("foobar");
        assertTrue(queue.matches("foo"));
        assertTrue(queue.matches("FOO"));
        assertTrue(queue.matches("bar"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matchesCS("foo"));
        assertFalse(queue.matchesCS("BAR"));

        queue = new TokenQueue("foobar");
        assertTrue(queue.matchesCS("foo"));
        assertTrue(queue.matchesCS("FOO"));
        assertFalse(queue.matchesCS("bar"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matchesAny("foo", "bar"));
        assertFalse(queue.matchesAny("qux"));

        queue = new TokenQueue("foobar");
        assertTrue(queue.matchesAny("foo", "bar"));
        assertTrue(queue.matchesAny("qux"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matchesAny(new char[]{'f', 'o'}));
        assertFalse(queue.matchesAny(new char[]{'b'}));

        queue = new TokenQueue("foobar");
        assertTrue(queue.matchesAny(new char[]{'f', 'o'}));
        assertTrue(queue.matchesAny(new char[]{'b'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("foo<x>bar");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("foo<x>bar");
        assertTrue(queue.matchChomp("x"));
        assertFalse(queue.matches("y"));

        queue = new TokenQueue("foo<x>bar");
        assertTrue(queue.matchChomp("x"));
        assertEquals("bar", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" foo ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("foo");
        queue.advance();
        assertEquals('o', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals('f', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals('', queue.toString());
    }

    @Test
    public void testConsumeToString() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals("foo", queue.consumeTo("foo").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("fooBAR");
        assertEquals("fooBAR", queue.consumeToIgnoreCase("foo").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("foo<x>bar");
        assertEquals("foo", queue.consumeToAny("x", "y").toString());

        queue = new TokenQueue("foo<x>bar");
        assertEquals("bar", queue.consumeToAny("x", "y").toString());

        queue = new TokenQueue("foo<x>bar");
        assertEquals("foo", queue.consumeToAny("foo", "bar").toString());
    }

    @Test
    public void testConsumeToAnyStringArray() {
        TokenQueue queue = new TokenQueue("foo<x>bar");
        assertEquals("foo", queue.consumeToAny(new String[]{"x", "y"}).toString());

        queue = new TokenQueue("foo<x>bar");
        assertEquals("bar", queue.consumeToAny(new String[]{"x", "y"}).toString());

        queue = new TokenQueue("foo<x>bar");
        assertEquals("foo", queue.consumeToAny(new String[]{"foo", "bar"}).toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar) baz) qux");
        assertEquals("foo (bar) baz", queue.chompBalanced('(', ')').toString());
        assertEquals("qux", queue.toString());

        queue = new TokenQueue("'foo' (bar) baz (quux)");
        assertEquals("foo", queue.chompBalanced('\'', '\'').toString());
        assertEquals("(bar) baz (quux)", queue.toString());

        queue = new TokenQueue("\"foo\" (bar) baz (quux)");
        assertEquals("foo", queue.chompBalanced('"', "\"").toString());
        assertEquals("(bar) baz (quux)", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("foo\\bar\\baz", TokenQueue.unescape("foo\\\\bar\\\\baz"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals("foo", queue.consumeWord());
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("foobar");
        assertEquals("foo", queue.consumeWord());
        assertEquals("bar", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("foo<tag>bar");
        assertEquals("tag", queue.consumeTagName());
        assertEquals("foo<bar>bar", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("foo<tag>bar");
        assertEquals("tag", queue.consumeElementSelector());
        assertEquals("foo<bar>bar", queue.toString());

        queue = new TokenQueue("foo|tag|bar");
        assertEquals("tag", queue.consumeElementSelector());
        assertEquals("foo|bar", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id:foo");
        assertEquals("foo", queue.consumeCssIdentifier());
        assertNull(queue.consume());

        queue = new TokenQueue("class:foo bar id:bar");
        assertEquals("foo", queue.consumeCssIdentifier());
        assertEquals("class:bar id:bar", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("name='foo'");
        assertEquals("name", queue.consumeAttributeKey());
        assertEquals("'foo'", queue.toString());

        queue = new TokenQueue("class='foo bar'");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("'foo bar'", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals("", queue.remainder());

        queue = new TokenQueue("foo<x>bar");
        assertEquals("bar", queue.remainder().toString());
    }
}