package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testNewInstance() {
        TokenQueue tq = new TokenQueue("foo");
        assertEquals("foo", tq.queue);
        assertEquals(0, tq.pos);
        assertTrue(tq.isEmpty());
        assertFalse(tq.matches("bar"));
    }

    @Test
    public void testPeek() {
        TokenQueue tq = new TokenQueue("foo");
        assertEquals('f', tq.peek());
        tq.advance();
        assertEquals('o', tq.peek());
        tq.advance();
        assertNull(tq.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue tq = new TokenQueue("foo");
        assertEquals('f', tq.consume());
        assertEquals('o', tq.consume());
        assertEquals('', tq.consume());
        assertNull(tq.consume());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("foo");
        tq.addFirst('a');
        assertEquals('a', tq.consume());
        assertEquals('f', tq.consume());
        assertEquals('o', tq.consume());
        assertEquals('', tq.consume());
        assertNull(tq.consume());
    }

    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("foo");
        assertTrue(tq.matches("foo"));
        assertTrue(tq.matches("foo"));
        assertFalse(tq.matches("bar"));
        assertFalse(tq.matches(""));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("foo");
        assertTrue(tq.matchesCS("foo"));
        assertFalse(tq.matchesCS("FOO"));
        assertFalse(tq.matchesCS("bar"));
        assertFalse(tq.matchesCS(""));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("foo");
        assertTrue(tq.matchesAny("foo", "bar"));
        assertFalse(tq.matchesAny("qux"));
        assertTrue(tq.matchesAny("foo", "qux", "bar"));
        assertFalse(tq.matchesAny(""));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue tq = new TokenQueue("foo");
        assertTrue(tq.matchesAny('f', 'o'));
        assertFalse(tq.matchesAny('x'));
        assertTrue(tq.matchesAny('f', 'o', 'x'));
        assertFalse(tq.matchesAny(''));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tq = new TokenQueue("<html>");
        assertTrue(tq.matchesStartTag());
        assertFalse(tq.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tq = new TokenQueue("foo<bar>baz");
        assertTrue(tq.matchChomp("foo"));
        assertEquals("bar", tq.consume());
        assertEquals("baz", tq.consume());
        assertFalse(tq.matchChomp("foo"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tq = new TokenQueue(" foo ");
        assertTrue(tq.matchesWhitespace());
        assertTrue(tq.matchesWhitespace());
        assertFalse(tq.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tq = new TokenQueue("foobar");
        assertTrue(tq.matchesWord());
        assertTrue(tq.matchesWord());
        assertFalse(tq.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue tq = new TokenQueue("foo");
        tq.advance();
        assertEquals('o', tq.consume());
        assertEquals('', tq.consume());
        assertNull(tq.consume());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tq = new TokenQueue("(foo (bar) baz) qux");
        assertEquals("foo (bar) baz", tq.chompBalanced('(', ')'));
        assertEquals("qux", tq.consume());
        assertEquals("", tq.consume());
        assertNull(tq.consume());
    }

    @Test
    public void testUnescape() {
        assertEquals("foo\\bar\\baz\\quux", TokenQueue.unescape("foo\\\\bar\\\\baz\\\\quux"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tq = new TokenQueue(" foo ");
        assertTrue(tq.consumeWhitespace());
        assertTrue(tq.consumeWhitespace());
        assertFalse(tq.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tq = new TokenQueue("foobar");
        assertEquals("foo", tq.consumeWord());
        assertEquals("bar", tq.consumeWord());
        assertEquals("", tq.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tq = new TokenQueue("<html>");
        assertEquals("html", tq.consumeTagName());
        assertEquals("", tq.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tq = new TokenQueue("div#id.class");
        assertEquals("div", tq.consumeElementSelector());
        assertEquals("id", tq.consumeElementSelector());
        assertEquals("class", tq.consumeElementSelector());
        assertEquals("", tq.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tq = new TokenQueue("#id .class .class2");
        assertEquals("id", tq.consumeCssIdentifier());
        assertEquals(".class", tq.consumeCssIdentifier());
        assertEquals(".class2", tq.consumeCssIdentifier());
        assertEquals("", tq.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tq = new TokenQueue("class='class' id='id'");
        assertEquals("class", tq.consumeAttributeKey());
        assertEquals("id", tq.consumeAttributeKey());
        assertEquals("", tq.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue tq = new TokenQueue("foo");
        assertEquals("", tq.remainder());
        assertEquals("", tq.remainder());
        TokenQueue tq2 = new TokenQueue("foo<bar>baz");
        assertEquals("baz", tq2.remainder());
    }
}