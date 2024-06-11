package org.jsoup.parser;

import static org.junit.Assert.*; // for assertions
import org.junit.Test;

import java.util.Arrays; // for Arrays.asList()

public class TokenQueueTest {

    @Test
    public void testConstructor() {
        TokenQueue tq = new TokenQueue("hello world");
        assertEquals("hello world", tq.queue);
        assertFalse(tq.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        TokenQueue tq = new TokenQueue("");
        assertTrue(tq.isEmpty());

        tq.addFirst("a");
        assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertEquals('a', tq.peek());
        assertEquals('a', tq.consume());
        assertEquals('b', tq.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("");
        tq.addFirst("hello");
        assertEquals("hello", tq.queue);
        assertEquals("hello", tq.toString());
        assertEquals("hello", tq.consumeTo("hello"));

        tq.addFirst("world");
        assertEquals("worldhello", tq.queue);
    }

    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertTrue(tq.matches("ab"));
        assertTrue(tq.matches("bc"));
        assertFalse(tq.matches("cd"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("abcDEFgHij");
        assertTrue(tq.matchesCS("AB"));
        assertFalse(tq.matchesCS("ab"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("abcABC");
        assertTrue(tq.matchesAny("a", "b", "c"));
        assertTrue(tq.matchesAny("A", "B", "C"));
        assertFalse(tq.matchesAny("d"));
        assertFalse(tq.matchesAny("e"));

        TokenQueue tq2 = new TokenQueue("123@#!$%");
        assertTrue(tq2.matchesAny("#", "@"));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue tq = new TokenQueue("<html>");
        assertTrue(tq.matchesStartTag());

        tq = new TokenQueue("abc<x>def");
        assertFalse(tq.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue tq = new TokenQueue("abc<tag>def");
        assertTrue(tq.matchChomp("<tag>"));

        tq = new TokenQueue("<html><head><title>Test</title></head>");
        assertTrue(tq.matchChomp("<title>"));
        assertEquals("Test</title>", tq.consumeToIgnoreCase("<title>"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue tq = new TokenQueue(" hello world ");
        assertTrue(tq.matchesWhitespace());
        assertTrue(tq.consumeWhitespace());
        assertFalse(tq.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue tq = new TokenQueue("hello world");
        assertTrue(tq.matchesWord());
        assertTrue(tq.consumeWord().equals("hello"));
        assertFalse(tq.matchesWord());
    }

    @Test
    public void testConsume() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertEquals('a', tq.consume());
        assertEquals('b', tq.consume());
        assertEquals('c', tq.consume());
        assertEquals('d', tq.consume());
        assertEquals('e', tq.consume());
        assertEquals('f', tq.consume());
        assertTrue(tq.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeEmptyQueue() {
        TokenQueue tq = new TokenQueue("");
        tq.consume("test");
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeInvalidSequence() {
        TokenQueue tq = new TokenQueue("abc<xyz>def");
        tq.consume("xyz");
    }

    @Test
    public void testConsumeTo() {
        TokenQueue tq = new TokenQueue("abc<xyz>def");
        assertEquals("abc", tq.consumeTo("abc"));
        assertEquals("def", tq.consumeToIgnoreCase("def"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue tq = new TokenQueue("abc<xyz>def");
        assertEquals("abc", tq.consumeToAny("a", "b", "c"));
        assertEquals("def", tq.consumeToAny("d", "e", "f"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue tq = new TokenQueue("()()()");
        assertEquals("()()()", tq.chompBalanced('(', ')'));
        assertEquals("()()()", tq.toString());

        tq = new TokenQueue("()<abc>()");
        assertEquals("abc", tq.chompBalanced('(', ')'));
        assertEquals("()<></>", tq.toString()); // note quote escapes

        tq = new TokenQueue("()<abc>def()");
        assertEquals("abcdef", tq.chompBalanced('(', ')'));
        assertEquals("", tq.toString());
    }

    @Test
    public void testUnescape() {
        String input = "\\/a\\/b\\c\\d\\e\\f";
        String expected = "/a/b/c/d/e/f";
        String output = TokenQueue.unescape(input);
        assertEquals(expected, output);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tq = new TokenQueue(" hello world ");
        assertTrue(tq.consumeWhitespace());
        assertTrue(tq.consumeWhitespace());
        assertFalse(tq.consumeWhitespace());
        assertEquals("hello", tq.toString());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tq = new TokenQueue("hello world");
        assertEquals("hello", tq.consumeWord());
        assertEquals("world", tq.consumeWord());
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tq = new TokenQueue("<html><head><title>Test</title></head>");
        assertEquals("html", tq.consumeTagName());
        assertEquals("head", tq.consumeTagName());
        assertEquals("title", tq.consumeTagName());
        assertEquals("", tq.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tq = new TokenQueue("div#id.class:hover");
        assertEquals("div", tq.consumeElementSelector());
        assertEquals("id", tq.consumeAttributeKey());
        assertEquals("class", tq.consumeAttributeKey());
        assertEquals(":hover", tq.consumeAny(":", "hover"));
        assertEquals("", tq.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue tq = new TokenQueue("abcdef");
        String remainder = tq.remainder();
        assertEquals("def", remainder);
        assertEquals("", tq.toString());
    }

    @Test
    public void testToString() {
        TokenQueue tq = new TokenQueue("abcdef");
        assertEquals("def", tq.toString());

        tq = new TokenQueue("<html><head><title>Test</title></head>");
        assertEquals("<html><head><title>Test</title></head>", tq.toString());
    }
}