package org.jsoup.parser;

import static org.junit.Assert.*;

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
        assertTrue(queue.matches("<bar>"));
        assertFalse(queue.matches("baz"));

        queue.advance();
        assertTrue(queue.matches("<bar>"));
        assertFalse(queue.matches("foo"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("FOO<BAR>BAZ");
        assertTrue(queue.matchesCS("FOO"));
        assertTrue(queue.matchesCS("<BAR>"));
        assertFalse(queue.matchesCS("BAZ"));

        queue.advance();
        assertTrue(queue.matchesCS("<BAR>"));
        assertFalse(queue.matchesCS("FOO"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny("f", "b"));
        assertFalse(queue.matchesAny("z"));

        queue.advance();
        assertTrue(queue.matchesAny("b", "z"));
        assertFalse(queue.matchesAny("f"));

        queue.addFirst("qux");
        assertTrue(queue.matchesAny("q", "u", "x"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesAny('f', 'b'));
        assertFalse(queue.matchesAny('z'));

        queue.advance();
        assertTrue(queue.matchesAny('b', 'z'));
        assertFalse(queue.matchesAny('f'));

        queue.addFirst("qux");
        assertTrue(queue.matchesAny('q', 'u', 'x'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());

        queue.advance();
        assertFalse(queue.matchesStartTag());
        assertTrue(queue.matchesStartTag());

        queue.consume("bar");
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("<foo><bar>baz");
        assertTrue(queue.matchChomp("<foo>"));
        assertEquals("<bar>baz", queue.toString());

        queue.advance();
        assertTrue(queue.matchChomp("baz"));
        assertEquals("", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" foo\tbar ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());

        queue.advance();
        assertFalse(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foo bar baz");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());

        queue.advance();
        assertFalse(queue.matchesWord());
        assertTrue(queue.matchesWord());

        queue.consume("foo");
        assertFalse(queue.matchesWord());
        assertTrue(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        queue.advance();
        assertEquals("<bar>", queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", queue.consume());
        assertEquals("<bar>baz", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals(" four", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("(ONE (TWO) THREE) Four");
        assertEquals("ONE (TWO) THREE", queue.chompBalanced('(', ')').toString());
        assertEquals(" Four", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", queue.chompBalanced('(', ')').toString());
        assertEquals(" four", queue.toString());

        queue = new TokenQueue("(one (two \"three\") three) four");
        assertEquals("one (two \"three\") three", queue.chompBalanced('(', ')').toString());
        assertEquals(" four", queue.toString());

        queue = new TokenQueue("(one (two \\) three) four");
        assertEquals("one (two \\) three", queue.chompBalanced('(', ')').toString());
        assertEquals(" four", queue.toString());
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo\tbar \n\r ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo bar baz");
        assertEquals("foo", queue.consumeWord());
        assertEquals(" bar baz", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("div id=foo class=bar");
        assertEquals("div", queue.consumeTagName());
        assertEquals(" id=foo class=bar", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div.foo.bar span.baz:hover");
        assertEquals("div.foo.bar", queue.consumeElementSelector());
        assertEquals(" span.baz:hover", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: red; background-color: #f00;");
        assertEquals("color", queue.consumeCssIdentifier());
        assertEquals(" background-color: #f00;", queue.toString());

        queue.advance();
        assertEquals("background-color", queue.consumeCssIdentifier());
        assertEquals(" #f00;", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=foo data-test-id=bar");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals(" data-test-id=bar", queue.toString());

        queue.advance();
        assertEquals("data-test-id", queue.consumeAttributeKey());
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("foo<bar>baz");
        assertEquals("baz", queue.remainder());
        assertEquals("foo<bar>", queue.toString());

        queue.advance();
        assertEquals("baz", queue.remainder());
        assertEquals("", queue.toString());
    }
}