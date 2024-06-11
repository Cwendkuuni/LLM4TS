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
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.peek());
        queue.advance();
        assertEquals('b', queue.peek());
        queue.consume();
        queue.consume();
        queue.consume();
        assertNull(queue.peek());
    }

    @Test
    public void testAddFirst() {
        TokenQueue queue = new TokenQueue("abc");
        queue.addFirst("x");
        assertEquals('x', queue.peek());
        queue.advance();
        assertEquals('a', queue.peek());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abc");
        assertTrue(queue.matches("a"));
        assertTrue(queue.matches("ab"));
        assertFalse(queue.matches("ba"));
        assertFalse(queue.matches("123"));
        queue.consume();
        assertFalse(queue.matches("a"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("AbC");
        assertTrue(queue.matchesCS("a"));
        assertTrue(queue.matchesCS("Ab"));
        assertFalse(queue.matchesCS("ba"));
        assertFalse(queue.matchesCS("123"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesAny("a", "b", "c"));
        assertTrue(queue.matchesAny("ab", "bc", "ac"));
        assertTrue(queue.matchesAny("1", "2", "3"));
        assertFalse(queue.matchesAny("d", "e", "f"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesAny(new char[]{'a', 'b', 'c'}));
        assertTrue(queue.matchesAny(new char[]{'a', 'b', 'c'}));
        assertTrue(queue.matchesAny(new char[]{'1', '2', '3'}));
        assertFalse(queue.matchesAny(new char[]{'d', 'e', 'f'}));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdefg<xyz>");
        assertTrue(queue.matchChomp("xyz"));
        assertEquals("defg", queue.consumeToIgnoreCase("defg"));
        assertFalse(queue.matchChomp("xyz"));
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue("  abc ");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertTrue(queue.matchesWord());
        assertTrue(queue.consumeWord().equals("abc"));
        assertTrue(queue.matchesWord());
        assertTrue(queue.consumeWord().equals("123"));
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals('b', queue.peek());
        queue.advance();
        assertEquals('c', queue.peek());
        queue.consume();
        queue.consume();
        assertNull(queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertNull(queue.peek());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abc123xyz");
        assertEquals("abc", queue.consumeTo("abc"));
        assertEquals("123", queue.consumeTo("123"));
        assertEquals("xyz", queue.consumeToIgnoreCase("xyz"));
        assertEquals("", queue.consumeTo("xyz"));
        assertEquals("", queue.consumeTo("123"));
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abc123xyz");
        assertEquals("abc", queue.consumeToAny("a", "b", "c"));
        assertEquals("xyz", queue.consumeToAny("x", "y", "z"));
        assertEquals("", queue.consumeToAny("1", "2", "3"));
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(abc)def((hij)kl)mno");
        assertEquals("abcdef", queue.chompBalanced('(', ')'));
        assertEquals("hijkl", queue.chompBalanced('(', ')'));
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testUnescape() {
        String escaped = "\\(\\)abc\\/def\\\\123";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("(())abc/def\\123", escaped);
        assertEquals("(())abcdef123", unescaped);
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue("   abc   ");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abc123");
        assertEquals("abc", queue.consumeWord());
        assertEquals("123", queue.consumeWord());
        assertNull(queue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<ab:c d:e>");
        assertEquals("ab:c", queue.consumeTagName());
        assertEquals("d:e", queue.consumeTagName());
        assertNull(queue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("div#foo p.class");
        assertEquals("div", queue.consumeElementSelector());
        assertEquals("p", queue.consumeElementSelector());
        assertNull(queue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("id:foo class:bar");
        assertEquals("foo", queue.consumeCssIdentifier());
        assertEquals("bar", queue.consumeCssIdentifier());
        assertNull(queue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class='red' id='foo'");
        assertEquals("class", queue.consumeAttributeKey());
        assertEquals("id", queue.consumeAttributeKey());
        assertNull(queue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdefg");
        assertEquals("g", queue.remainder().charAt(0));
        assertEquals("", queue.toString());
    }
}