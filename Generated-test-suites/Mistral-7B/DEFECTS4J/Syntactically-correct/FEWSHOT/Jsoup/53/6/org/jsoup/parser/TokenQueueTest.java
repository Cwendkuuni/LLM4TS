package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        TokenQueue queue = new TokenQueue("");
        assertEquals(0, queue.peek());

        queue = new TokenQueue("foo");
        assertEquals('f', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst('b');
        assertEquals("ba", queue.toString());

        queue = new TokenQueue("foo");
        queue.addFirst('a');
        queue.addFirst('b');
        assertEquals("abfoo", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("foo");

        assertTrue(queue.matches("f"));
        assertTrue(queue.matches("foo"));
        assertFalse(queue.matches("bar"));

        queue = new TokenQueue("--foo");
        assertTrue(queue.matches("foo"));
        assertFalse(queue.matches("--bar"));

        queue = new TokenQueue("--bar--foo");
        assertTrue(queue.matches("bar"));
        assertTrue(queue.matches("foo"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("FOO");

        assertFalse(queue.matchesCS("foo"));
        assertTrue(queue.matchesCS("FOO"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("foo");

        assertFalse(queue.matchesAny(new String[]{"bar"}));
        assertTrue(queue.matchesAny(new String[]{"foo", "bar"}));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("foo");

        assertFalse(queue.matchesAny(new char[]{'b'}));
        assertTrue(queue.matchesAny(new char[]{'f', 'o'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<foo>");
        assertTrue(queue.matchesStartTag());

        queue = new TokenQueue("bar");
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("foo<bar>");
        assertTrue(queue.matchChomp("foo"));
        assertEquals("bar", queue.toString());

        queue = new TokenQueue("foo<bar>");
        assertFalse(queue.matchChomp("baz"));
        assertEquals("foo<bar>", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" foo ");
        assertTrue(queue.matchesWhitespace());

        queue = new TokenQueue("foo");
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foo");
        assertTrue(queue.matchesWord());

        queue = new TokenQueue("foo bar");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("foo");
        queue.advance();
        assertEquals("o", queue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals('f', queue.consume());
        assertEquals("o", queue.toString());

        queue = new TokenQueue("foo");
        queue.consume("foo");
        assertNull(queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo<bar>", queue.consumeTo("foo<bar>"));
        assertEquals("baz", queue.toString());

        queue = new TokenQueue("foo<bar>baz");
        assertEquals("baz", queue.consumeTo("baz"));
        assertNull(queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("FOO<BAR>BAZ");
        assertEquals("FOO<BAR>", queue.consumeToIgnoreCase("foo<bar>"));
        assertEquals("BAZ", queue.toString());

        queue = new TokenQueue("FOO<BAR>BAZ");
        assertEquals("BAZ", queue.consumeToIgnoreCase("baz"));
        assertNull(queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("foo<bar><baz>quux");
        assertEquals("foo<bar>", queue.consumeToAny(new String[]{"foo<bar>"}));
        assertEquals("</baz>quux", queue.toString());

        queue = new TokenQueue("foo<bar><baz>quux");
        assertEquals("</baz>quux", queue.consumeToAny(new String[]{"baz"}));
        assertEquals("quux", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar) baz) quux");
        assertEquals("foo (bar) baz", queue.chompBalanced('(', ')').toString());
        assertEquals("quux", queue.toString());

        queue = new TokenQueue("(foo (bar) baz) quux");
        assertEquals("quux", queue.chompBalanced('z', 'z').toString());
        assertNull(queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("foo\\bar\\baz", TokenQueue.unescape("foo\\bar\\baz"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("", queue.toString());

        queue = new TokenQueue("foo");
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo bar");
        assertEquals("foo", queue.consumeWord());
        assertEquals("bar", queue.toString());

        queue = new TokenQueue("foo bar");
        assertEquals("bar", queue.consumeWord());
        assertNull(queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<foo>");
        assertEquals("foo", queue.consumeTagName());

        queue = new TokenQueue("foo <bar>");
        assertEquals("bar", queue.consumeTagName());

        queue = new TokenQueue("foo :bar");
        assertEquals("bar", queue.consumeTagName());

        queue = new TokenQueue("foo _bar");
        assertEquals("bar", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals("foo", queue.consumeElementSelector());

        queue = new TokenQueue("foo |bar");
        assertEquals("bar", queue.consumeElementSelector());

        queue = new TokenQueue("foo _bar");
        assertEquals("bar", queue.consumeElementSelector());

        queue = new TokenQueue("foo :bar");
        assertEquals("bar", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("foo-bar");
        assertEquals("foo-bar", queue.consumeCssIdentifier());

        queue = new TokenQueue("foo_bar");
        assertEquals("foo_bar", queue.consumeCssIdentifier());

        queue = new TokenQueue("foo:bar");
        assertNull(queue.consumeCssIdentifier());

        queue = new TokenQueue("foo:bar-baz");
        assertEquals("bar-baz", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("foo class=bar");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("bar", queue.toString());

        queue = new TokenQueue("foo class=bar");
        assertEquals("bar", queue.consumeAttributeKey());
        assertNull(queue.toString());

        queue = new TokenQueue("foo id=foo");
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("foo", queue.toString());

        queue = new TokenQueue("foo id=foo class=bar");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("bar", queue.toString());

        queue = new TokenQueue("foo id=foo class=bar id=baz");
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("baz", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("foo");
        assertEquals("", queue.remainder());

        queue = new TokenQueue("foo");
        queue.consume("foo");
        assertEquals("", queue.remainder());

        queue = new TokenQueue("foo<bar>baz");
        assertEquals("baz", queue.remainder());
    }
}