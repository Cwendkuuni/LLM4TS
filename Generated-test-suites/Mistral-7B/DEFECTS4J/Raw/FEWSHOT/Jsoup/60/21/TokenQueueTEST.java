package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());

        queue = new TokenQueue("abc");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());

        queue.advance();
        assertEquals('b', queue.peek());

        queue.consume();
        assertEquals('c', queue.peek());

        queue.consumeTo("bc");
        assertEquals(' ', queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("");
        queue.addFirst('a');
        queue.addFirst("xyz");
        assertEquals("xyz a", queue.consumeToIgnoreCase("xyz"));
        assertEquals("a", queue.consume());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("cd"));
        assertTrue(queue.matches("ef"));
        assertFalse(queue.matches("hij"));

        queue = new TokenQueue("");
        assertFalse(queue.matches(""));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCDEFG");
        assertTrue(queue.matchesCS("ab"));
        assertTrue(queue.matchesCS("CD"));
        assertFalse(queue.matchesCS("HIJ"));

        queue = new TokenQueue("abcdefg");
        assertFalse(queue.matchesCS("AB"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertFalse(queue.matchesAny("hij", "efg"));

        queue = new TokenQueue("");
        assertFalse(queue.matchesAny(""));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchesAny('a', 'b'));
        assertTrue(queue.matchesAny('c', 'g'));
        assertFalse(queue.matchesAny('h', 'i'));

        queue = new TokenQueue("");
        assertFalse(queue.matchesAny(' '));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<html><head><title>Test</title></head><body>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.matchChomp("ab");
        assertEquals("cd efg", queue.consumeToIgnoreCase("cd"));

        queue = new TokenQueue("");
        assertFalse(queue.matches(""));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" abc def g ");
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdefg");
        queue.advance();
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('c', queue.peek());

        queue.advance();
        assertEquals('d', queue.peek());

        queue.consumeTo("def");
        assertEquals('g', queue.peek());

        queue.consumeToIgnoreCase("g");
        assertEquals(' ', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertEquals('g', queue.consume());
        assertEquals("", queue.consumeToIgnoreCase(""));
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("def", queue.consumeToIgnoreCase("def"));
        assertEquals("abc", queue.consumeToIgnoreCase("abc"));
        assertEquals("", queue.consumeToIgnoreCase(""));

        queue = new TokenQueue("\"abcdefg\"");
        assertEquals("abcdefg", queue.consumeToIgnoreCase("abcdefg"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("efg", queue.consumeToAny("ef", "g"));
        assertEquals("", queue.consumeToAny("ab", "xyz"));

        queue = new TokenQueue("\"abcdefg\"");
        assertEquals("abcdefg", queue.consumeToAny("abcdefg"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("()()()()");
        assertEquals("()()", queue.chompBalanced('(', ')'));
        assertEquals("()", queue.chompBalanced('(', ')'));
        assertEquals("", queue.chompBalanced('(', ')'));

        queue = new TokenQueue("(abc)def(xyz)123");
        assertEquals("abcdef", queue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("abc\\def\\ghi", TokenQueue.unescape("abc\\def\\ghi"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc def g   ");
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("abc", queue.consumeWord());
        assertEquals("def", queue.consumeWord());
        assertEquals("g", queue.consumeWord());
        assertEquals("", queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<tag name='value' attr='value'/>");
        assertEquals("tag", queue.consumeTagName());
        assertEquals("name", queue.consumeTagName());
        assertEquals("attr", queue.consumeTagName());
        assertEquals("", queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#id1.class1 > ul > li:first-child");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("id1", queue.consumeElementSelector());
        assertEquals("class1", queue.consumeElementSelector());
        assertEquals("ul", queue.consumeElementSelector());
        assertEquals("li", queue.consumeElementSelector());
        assertEquals("", queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("color: red; background-color: #ff0000;");
        assertEquals("color", queue.consumeCssIdentifier());
        assertEquals("background-color", queue.consumeCssIdentifier());
        assertEquals("red", queue.consumeCssIdentifier());
        assertEquals("#ff0000", queue.consumeCssIdentifier());
        assertEquals("", queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("data-foo='bar' data-baz='qux'");
        assertEquals("data-foo", queue.consumeAttributeKey());
        assertEquals("data-baz", queue.consumeAttributeKey());
        assertEquals("", queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("", queue.remainder());

        queue = new TokenQueue("abcdefg123");
        assertEquals("123", queue.remainder());

        queue = new TokenQueue("\"abcdefg123\"");
        assertEquals("123", queue.remainder());
    }
}