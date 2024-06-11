package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.helper.StringUtil;

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
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.peek());

        queue.advance();
        assertEquals('b', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abcd");
        queue.addFirst("1");
        assertEquals("1abcd", queue.toString());

        queue.addFirst("0");
        assertEquals("01abcd", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("--foo--bar baz");
        assertTrue(queue.matches("--"));
        assertTrue(queue.matches("foo"));
        assertFalse(queue.matches("bar"));
        assertFalse(queue.matches("baz"));

        queue.advance();
        assertFalse(queue.matches("--"));
        assertTrue(queue.matches("foo"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("--foo--bar baz");
        assertFalse(queue.matchesCS("--"));
        assertTrue(queue.matchesCS("foo"));
        assertFalse(queue.matchesCS("bar"));
        assertFalse(queue.matchesCS("baz"));

        queue.advance();
        assertFalse(queue.matchesCS("--"));
        assertTrue(queue.matchesCS("foo"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("--foo--bar baz");
        assertTrue(queue.matchesAny("--", "foo", "bar"));
        assertTrue(queue.matchesAny("--", "foo"));
        assertFalse(queue.matchesAny("--", "baz"));

        queue.advance();
        assertTrue(queue.matchesAny("--", "foo"));
    }

    @Test
    public void testMatchesAnyChars() {
        TokenQueue queue = new TokenQueue("--foo--bar baz");
        assertTrue(queue.matchesAny('--', 'f', 'o', 'a'));
        assertTrue(queue.matchesAny('--', 'f'));
        assertFalse(queue.matchesAny('--', 'z'));

        queue.advance();
        assertTrue(queue.matchesAny('--', 'f'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abcd><efg><hij>");
        assertTrue(queue.matchesStartTag());
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());

        queue.advance();
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("--foo--bar baz--quux--");
        assertTrue(queue.matchChomp("--"));
        assertFalse(queue.matchChomp("bar"));
        assertTrue(queue.matchChomp("quux"));

        queue.advance();
        assertFalse(queue.matchChomp("bar"));
        assertFalse(queue.matchChomp("quux"));
        assertTrue(queue.matchChomp("--"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" \tfoo\n \t bar\r ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());

        queue.advance();
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("foo bar baz quux");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());

        queue.advance();
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("foo bar baz quux");
        queue.advance();
        assertEquals("bar baz quux", queue.toString());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("foo bar baz quux");
        assertEquals("foo", queue.consume());
        assertEquals("bar baz quux", queue.toString());

        queue.advance();
        assertEquals("bar", queue.consume());
        assertEquals("baz quux", queue.toString());

        queue.consume("ba");
        assertEquals("az quux", queue.toString());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("foo bar baz quux--quux--quux--");
        assertEquals("foo bar", queue.consumeTo("bar").toString());
        assertEquals("baz quux--quux--quux--", queue.toString());

        assertEquals("quux--quux--", queue.consumeTo("--quux--").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("foo bar baz quux--quux--quux--");
        assertEquals("foo bar", queue.consumeToIgnoreCase("BAR").toString());
        assertEquals("baz quux--quux--quux--", queue.toString());

        assertEquals("quux--quux--", queue.consumeToIgnoreCase("--quux--").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("foo bar baz quux--quux--quux--");
        assertEquals("foo bar baz", queue.consumeToAny("foo", "bar", "baz").toString());
        assertEquals("quux--quux--quux--", queue.toString());

        assertEquals("quux--quux--", queue.consumeToAny("--quux--").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(foo (bar (baz) quux)) quux");
        assertEquals("foo (bar (baz) quux)", queue.chompBalanced('(', ')').toString());
        assertEquals("quux", queue.toString());

        queue.addFirst("(hello (world))");
        assertEquals("(hello (world)) foo (bar (baz) quux) quux", queue.toString());
        assertEquals("(hello (world))", queue.chompBalanced('(', ')').toString());
        assertEquals("foo (bar (baz) quux) quux", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("foo", TokenQueue.unescape("foo"));
        assertEquals("foo\\bar", TokenQueue.unescape("foo\\bar"));
        assertEquals("foo\\\\bar", TokenQueue.unescape("foo\\\\bar"));
        assertEquals("foobar", TokenQueue.unescape("foo\\baz"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" foo   bar  baz ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("foo bar baz quux");
        assertEquals("foo", queue.consumeWord().toString());
        assertEquals("bar baz quux", queue.toString());

        assertEquals("baz", queue.consumeWord().toString());
        assertEquals("quux", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("div id=foo class=bar");
        assertEquals("div", queue.consumeTagName().toString());
        assertEquals("id=foo class=bar", queue.toString());

        assertEquals("id", queue.consumeTagName().toString());
        assertEquals("foo class=bar", queue.toString());

        assertEquals("class", queue.consumeTagName().toString());
        assertEquals("bar", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#foo.bar.baz");
        assertEquals("div", queue.consumeElementSelector().toString());
        assertEquals("#foo.bar.baz", queue.toString());

        assertEquals("#foo", queue.consumeElementSelector().toString());
        assertEquals(".bar.baz", queue.toString());

        assertEquals(".bar", queue.consumeElementSelector().toString());
        assertEquals(".baz", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("foo bar baz quux#foo.bar.baz.quux");
        assertEquals("foo", queue.consumeCssIdentifier().toString());
        assertEquals("bar baz quux#foo.bar.baz.quux", queue.toString());

        assertEquals("bar", queue.consumeCssIdentifier().toString());
        assertEquals("baz quux#foo.bar.baz.quux", queue.toString());

        assertEquals("quux", queue.consumeCssIdentifier().toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("id=foo class=bar data-foo=bar");
        assertEquals("id", queue.consumeAttributeKey().toString());
        assertEquals("class data-foo=bar", queue.toString());

        assertEquals("class", queue.consumeAttributeKey().toString());
        assertEquals("data-foo=bar", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("foo bar baz quux");
        assertEquals("quux", queue.remainder().toString());
        assertEquals("", queue.toString());
    }
}