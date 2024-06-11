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
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals('f', queue.peek());
        assertEquals('f', queue.consume());

        queue = new TokenQueue("foo<bar>baz");
        assertEquals('f', queue.peek());
        assertEquals('f', queue.consume());
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
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matches("fo"));
        assertTrue(queue.matches("foo"));
        assertTrue(queue.matches("foo<"));
        assertFalse(queue.matches("baz"));

        queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matches("foo"));
        assertTrue(queue.matches("foo<"));
        assertFalse(queue.matches("bar"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("foo<BAR>baz");
        assertTrue(queue.matchesCS("FOO"));
        assertTrue(queue.matchesCS("FOO<"));
        assertFalse(queue.matchesCS("BAR"));

        queue = new TokenQueue("foo<BAR>baz");
        assertTrue(queue.matchesCS("FOO"));
        assertTrue(queue.matchesCS("FOO<"));
        assertFalse(queue.matchesCS("bar"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny("foo", "bar"));
        assertFalse(queue.matchesAny("baz"));

        queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny("foo"));
        assertFalse(queue.matchesAny("bar", "baz"));

        queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny("foo", "bar", "baz"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny(new char[]{'f', 'o'}));
        assertFalse(queue.matchesAny(new char[]{'b', 'z'}));

        queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny(new char[]{'f'}));
        assertFalse(queue.matchesAny(new char[]{'b', 'z'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchChomp("foo"));
        assertEquals("barbaz", queue.toString());

        queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchChomp("foobar"));
        assertEquals("baz", queue.toString());

        queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchChomp("foo<"));
        assertEquals("barbaz", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" foo\nabcd ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());

        queue = new TokenQueue(" foo\nabcd ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesWord());
        assertEquals("oo", queue.consumeWord());
        assertTrue(queue.matchesWord());
        assertEquals("ba", queue.consumeWord());
        assertFalse(queue.matchesWord());

        queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesWord());
        assertEquals("foo", queue.consumeWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals('f', queue.peek());
        queue.advance();
        assertEquals('o', queue.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        queue.consume("foo");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeNotMatchedSequence() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        queue.consume("baz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("bar", queue.consumeTo("ar"));
        assertEquals("baz", queue.toString());

        queue = new TokenQueue("foo<bar>baz");
        assertEquals("foobar", queue.consumeTo("foo"));
        assertEquals("baz", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("foo<BAR>baz");
        assertEquals("BAR", queue.consumeToIgnoreCase("bar"));
        assertEquals("baz", queue.toString());

        queue = new TokenQueue("foo<BAR>baz");
        assertEquals("FOO", queue.consumeToIgnoreCase("foo"));
        assertEquals("baz", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("barbaz", queue.consumeToAny("baz", "bar"));
        assertEquals("", queue.toString());

        queue = new TokenQueue("foo<bar>baz");
        assertEquals("foobar", queue.consumeToAny("foo", "bar"));
        assertEquals("baz", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar (baz) qux) quux) quuz");
        assertEquals("foo (bar (baz) qux)", queue.chompBalanced('(', ')'));
        assertEquals("quux quuz", queue.toString());

        queue = new TokenQueue("(foo (bar (baz) qux) quux) quuz");
        assertEquals("foo (bar (baz) qux)", queue.chompBalanced('(', ')'));
        assertEquals("quux quuz", queue.toString());

        queue = new TokenQueue("(foo (bar (baz) qux) quux) quuz");
        assertEquals("quux quuz", queue.toString());
        assertEquals("(foo (bar (baz) qux)", queue.chompBalanced('(', ')'));
        assertEquals("quuz", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("foo\\bar\\baz", TokenQueue.unescape("foo\\\\bar\\\\baz"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo\nabcd \t");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());

        queue = new TokenQueue(" foo\nabcd \t");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("oo", queue.consumeWord());
        assertEquals("ba", queue.consumeWord());
        assertFalse(queue.consumeWord() != null);

        queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeWord());
        assertFalse(queue.consumeWord() != null);
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("bar", queue.consumeTagName());
        assertEquals("baz", queue.toString());

        queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeTagName());
        assertEquals("baz", queue.toString());

        queue = new TokenQueue("foo<bar>baz");
        assertEquals("baz", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("foo<bar>baz:class=foo:id=bar");
        assertEquals("foo", queue.consumeElementSelector());
        assertEquals(":class=foo:id=bar", queue.toString());

        queue = new TokenQueue("foo<bar>baz:class=foo:id=bar");
        assertEquals("bar", queue.consumeElementSelector());
        assertEquals(":class=foo", queue.toString());

        queue = new TokenQueue("foo<bar>baz:class=foo:id=bar");
        assertEquals(":class", queue.consumeElementSelector());
        assertEquals(":id=bar", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("foo<bar>baz class=foo id=bar style=\"color: foo;\"");
        assertEquals("foo", queue.consumeCssIdentifier());
        assertEquals("id=bar style=\"color: foo;\",", queue.toString());

        queue = new TokenQueue("foo<bar>baz class=foo id=bar style=\"color: foo;\"");
        assertEquals("id", queue.consumeCssIdentifier());
        assertEquals("class=foo style=\"color: foo;\",", queue.toString());

        queue = new TokenQueue("foo<bar>baz class=foo id=bar style=\"color: foo;\"");
        assertEquals("style", queue.consumeCssIdentifier());
        assertEquals("class=foo id=bar,", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("foo<bar>baz class=foo id=bar style=\"color: foo;\"");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id=bar style=\"color: foo;\",", queue.toString());

        queue = new TokenQueue("foo<bar>baz class=foo id=bar style=\"color: foo;\"");
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("class=foo,", queue.toString());

        queue = new TokenQueue("foo<bar>baz class=foo id=bar style=\"color: foo;\"");
        assertEquals("style", queue.consumeAttributeKey());
        assertEquals("class=foo id=bar", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("baz", queue.remainder());
        assertEquals("", queue.toString());

        queue = new TokenQueue("foo<bar>baz");
        assertEquals("baz", queue.remainder());
        assertEquals("", queue.toString());

        queue = new TokenQueue("foo<bar>baz");
        assertEquals("", queue.remainder());
        assertEquals("baz", queue.toString());
    }
}