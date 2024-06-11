package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        queue.addFirst("bar");
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("foo");
        queue.addFirst("bar");
        assertEquals("barfoo", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matches("foo"));
        assertTrue(queue.matches("bar"));
        assertTrue(queue.matches("baz"));
        assertFalse(queue.matches("qux"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("FOO<BAR>BAZ");
        assertTrue(queue.matchesCS("FOO"));
        assertTrue(queue.matchesCS("BAR"));
        assertTrue(queue.matchesCS("BAZ"));
        assertFalse(queue.matchesCS("QUX"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny("foo", "bar"));
        assertTrue(queue.matchesAny("bar", "foo"));
        assertFalse(queue.matchesAny("qux"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html>");
        assertTrue(queue.matchesStartTag());

        queue.advance();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchChomp("foo"));
        assertEquals("barbaz", queue.toString());

        assertTrue(queue.matchChomp("bar"));
        assertEquals("baz", queue.toString());

        assertFalse(queue.matchChomp("qux"));
        assertEquals("baz", queue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals('f', queue.consume());
        assertEquals("oobarbaz", queue.toString());

        assertEquals('b', queue.consume());
        assertEquals("ooaaz", queue.toString());

        assertEquals('a', queue.consume());
        assertEquals("oa", queue.toString());

        assertEquals('z', queue.consume());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeTo("foo"));
        assertEquals("barbaz", queue.toString());

        assertEquals("bar", queue.consumeTo("bar"));
        assertEquals("baz", queue.toString());

        assertEquals("baz", queue.consumeTo("baz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("FOO<BAR>BAZ");
        assertEquals("FOO", queue.consumeToIgnoreCase("Foo"));
        assertEquals("BARBAZ", queue.toString());

        assertEquals("BAR", queue.consumeToIgnoreCase("bar"));
        assertEquals("BAZ", queue.toString());

        assertEquals("BAZ", queue.consumeToIgnoreCase("Baz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeToAny("foo", "bar"));
        assertEquals("barbaz", queue.toString());

        assertEquals("bar", queue.consumeToAny("bar", "foo"));
        assertEquals("baz", queue.toString());

        assertEquals("baz", queue.consumeToAny("baz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar) baz) qux");
        assertEquals("foo (bar) baz", queue.chompBalanced('(', ')').toString());
        assertEquals("qux", queue.toString());

        assertEquals("(bar)", queue.chompBalanced('(', ')').toString());
        assertEquals("baz", queue.toString());

        assertEquals("baz", queue.chompBalanced('(', ')').toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("\\", TokenQueue.unescape("\\"));
        assertEquals("foo\\bar", TokenQueue.unescape("foo\\bar"));
        assertEquals("foo<bar>", TokenQueue.unescape("foo<bar>"));
        assertEquals("foo\\(\\)bar", TokenQueue.unescape("foo()bar"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo\tbar\n baz ");
        assertTrue(queue.consumeWhitespace());
        assertEquals("", queue.toString());

        assertTrue(queue.consumeWhitespace());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo word bar baz");
        assertEquals("foo", queue.consumeWord());
        assertEquals("word bar baz", queue.toString());

        assertEquals("word", queue.consumeWord());
        assertEquals("bar baz", queue.toString());

        assertEquals("bar", queue.consumeWord());
        assertEquals("baz", queue.toString());

        assertEquals("baz", queue.consumeWord());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("div<p class=\"foo\">bar<span class=\"baz\">baz</span>");
        assertEquals("div", queue.consumeTagName());
        assertEquals("<p class=\"foo\">bar<span class=\"baz\">baz</span>", queue.toString());

        assertEquals("p", queue.consumeTagName());
        assertEquals("<class=\"foo\">bar<span class=\"baz\">baz</span>", queue.toString());

        assertEquals("class", queue.consumeTagName());
        assertEquals("<foo>bar<span class=\"baz\">baz</span>", queue.toString());

        assertEquals("foo", queue.consumeTagName());
        assertEquals("<bar><span class=\"baz\">baz</span>", queue.toString());

        assertEquals("bar", queue.consumeTagName());
        assertEquals("<span class=\"baz\">baz</span>", queue.toString());

        assertEquals("span", queue.consumeTagName());
        assertEquals("baz", queue.toString());

        assertEquals("baz", queue.consumeTagName());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.foo p.bar span.baz");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals(".foo p.bar span.baz", queue.toString());

        assertEquals("p", queue.consumeElementSelector());
        assertEquals(".bar span.baz", queue.toString());

        assertEquals("span", queue.consumeElementSelector());
        assertEquals(".baz", queue.toString());

        assertEquals(".baz", queue.consumeElementSelector());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("foo.bar.baz class=\"qux quux\" id=\"quux\"");
        assertEquals("foo", queue.consumeCssIdentifier());
        assertEquals(".bar.baz class=\"qux quux\" id=\"quux\"", queue.toString());

        assertEquals("bar", queue.consumeCssIdentifier());
        assertEquals(".baz class=\"qux quux\" id=\"quux\"", queue.toString());

        assertEquals("baz", queue.consumeCssIdentifier());
        assertEquals("class=\"qux quux\" id=\"quux\"", queue.toString());

        assertEquals("qux", queue.consumeCssIdentifier());
        assertEquals("class=\"quux\" id=\"quux\"", queue.toString());

        assertEquals("quux", queue.consumeCssIdentifier());
        assertEquals("id=\"quux\"", queue.toString());

        assertEquals("id", queue.consumeCssIdentifier());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"foo bar baz\" id=\"qux\"");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("foo bar baz id=\"qux\"", queue.toString());

        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("qux", queue.toString());

        assertEquals("qux", queue.consumeAttributeKey());
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("baz", queue.remainder().toString());
        assertEquals("foo<bar>", queue.toString());

        assertEquals("", queue.remainder().toString());
        assertEquals("", queue.toString());
    }
}