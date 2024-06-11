package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.jsoup.helper.StringUtil;

public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo<bar>baz", queue.queue);
        assertEquals(0, queue.pos);
    }

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("foo");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals('f', queue.peek());

        queue.advance();
        assertEquals('o', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        queue.addFirst('x');
        assertEquals("xfoo<bar>baz", queue.queue);
        assertEquals(0, queue.pos);
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
        TokenQueue queue = new TokenQueue("foo<BAR>baz");
        assertTrue(queue.matchesCS("FOO"));
        assertTrue(queue.matchesCS("BAR"));
        assertFalse(queue.matchesCS("QUX"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny("foo", "bar", "baz"));
        assertFalse(queue.matchesAny("qux"));
        assertFalse(queue.matchesAny("quux", "corge"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny(new char[]{'f', 'o'}));
        assertTrue(queue.matchesAny(new char[]{'b', 'a', 'z'}));
        assertFalse(queue.matchesAny(new char[]{'q', 'u', 'x'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchChomp("foo"));
        assertTrue(queue.matchChomp("bar"));
        assertFalse(queue.matchChomp("qux"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" foo<bar> baz ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        queue.advance();
        assertEquals('o', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals('f', queue.consume());
        assertEquals('o', queue.consume());
        assertEquals('b', queue.consume());
        assertNull(queue.consume());
    }

    @Test
    public void testConsumeSequence() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeTo("foo"));
        assertEquals("bar", queue.consumeTo("bar"));
        assertEquals("baz", queue.consumeTo("baz"));
        assertEquals("", queue.consumeTo("qux"));
    }

    @Test
    public void testConsumeSequenceIgnoreCase() {
        TokenQueue queue = new TokenQueue("FOO<BAR>BAZ");
        assertEquals("FOO", queue.consumeToIgnoreCase("foo"));
        assertEquals("BAR", queue.consumeToIgnoreCase("bar"));
        assertEquals("BAZ", queue.consumeToIgnoreCase("baz"));
        assertEquals("", queue.consumeToIgnoreCase("qux"));
    }

    @Test
    public void testConsumeSequenceAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeToAny("foo", "bar", "baz"));
        assertEquals("bar", queue.consumeToAny("bar"));
        assertEquals("baz", queue.consumeToAny("baz"));
        assertEquals("", queue.consumeToAny("qux", "quuz"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar) baz) qux");
        assertEquals("foo (bar) baz", queue.chompBalanced('(', ')'));
        assertEquals("", queue.chompBalanced('(', ')'));
        assertEquals("qux", queue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("foo\\bar\\baz", TokenQueue.unescape("foo\\\\bar\\\\baz"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo<bar> baz ");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consumeWord());
        assertEquals("bar", queue.consumeWord());
        assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<html><head><title>");
        assertEquals("html", queue.consumeTagName());
        assertEquals("head", queue.consumeTagName());
        assertEquals("title", queue.consumeTagName());
        assertEquals("", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#id.class p");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("id", queue.consumeElementSelector());
        assertEquals("class", queue.consumeElementSelector());
        assertEquals("p", queue.consumeElementSelector());
        assertEquals("", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("#id.class1.class2");
        assertEquals("id", queue.consumeCssIdentifier());
        assertEquals("class1", queue.consumeCssIdentifier());
        assertEquals("class2", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("style='color:red' class='class' id='id'");
        assertEquals("style", queue.consumeAttributeKey());
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id", queue.consumeAttributeKey());
        assertEquals("", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("", queue.remainder());
        assertEquals("baz", queue.remainder());
    }
}