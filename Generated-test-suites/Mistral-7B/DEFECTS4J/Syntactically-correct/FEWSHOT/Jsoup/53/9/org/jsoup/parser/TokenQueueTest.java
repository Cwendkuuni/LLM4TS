package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

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
        TokenQueue queue = new TokenQueue("");
        queue.addFirst("abc");
        assertEquals("abc", queue.toString());

        queue.addFirst("def");
        assertEquals("defabc", queue.toString());
    }

    @Test
    public void testMatches() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matches("ab"));
        assertTrue(queue.matches("cd"));
        assertFalse(queue.matches("ef"));
        assertFalse(queue.matches("xyz"));
    }

    @Test
    public void testMatchesCS() {
        TokenQueue queue = new TokenQueue("ABCdef");
        assertTrue(queue.matchesCS("Ab"));
        assertTrue(queue.matchesCS("Cd"));
        assertFalse(queue.matchesCS("ef"));
        assertFalse(queue.matchesCS("xyz"));
    }

    @Test
    public void testMatchesAny() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny("ab", "cd"));
        assertTrue(queue.matchesAny("ab", "ef"));
        assertFalse(queue.matchesAny("xyz", "ab"));
    }

    @Test
    public void testMatchesAnyChar() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesAny('a', 'b', 'c'));
        assertTrue(queue.matchesAny('d', 'e', 'f'));
        assertFalse(queue.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesStartTag() {
        TokenQueue queue = new TokenQueue("<abc>def");
        assertTrue(queue.matchesStartTag());
        assertFalse(queue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchChomp("ab"));
        assertEquals("cdef", queue.toString());

        assertTrue(queue.matchChomp("cd"));
        assertEquals("ef", queue.toString());

        assertFalse(queue.matchChomp("xyz"));
        assertEquals("ef", queue.toString());
    }

    @Test
    public void testMatchesWhitespace() {
        TokenQueue queue = new TokenQueue(" abc def");
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertTrue(queue.matchesWhitespace());
        assertFalse(queue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertTrue(queue.matchesWord());
        assertFalse(queue.matchesWord());
    }

    @Test
    public void testAdvance() {
        TokenQueue queue = new TokenQueue("abcdef");
        queue.advance();
        assertEquals('b', queue.peek());

        queue.advance();
        assertEquals('c', queue.peek());

        queue.advance();
        assertEquals('d', queue.peek());

        queue.advance();
        assertEquals('e', queue.peek());

        queue.advance();
        assertEquals('f', queue.peek());

        queue.advance();
        assertEquals('\u0000', queue.peek());
    }

    @Test
    public void testConsume() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals('a', queue.consume());
        assertEquals('b', queue.consume());
        assertEquals('c', queue.consume());
        assertEquals('d', queue.consume());
        assertEquals('e', queue.consume());
        assertEquals('f', queue.consume());
        assertEquals('\u0000', queue.consume());
    }

    @Test
    public void testConsumeTo() {
        TokenQueue queue = new TokenQueue("abcd<efg>hij");
        assertEquals("abcd", queue.consumeTo("efg").toString());
        assertEquals("hij", queue.toString());

        assertEquals("efg", queue.consumeTo("hij").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue queue = new TokenQueue("AbCd<efG>HiJ");
        assertEquals("AbCd", queue.consumeToIgnoreCase("efG").toString());
        assertEquals("HiJ", queue.toString());

        assertEquals("efG", queue.consumeToIgnoreCase("HiJ").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeToAny() {
        TokenQueue queue = new TokenQueue("abcd<efg>hij");
        assertEquals("abcd", queue.consumeToAny("efg").toString());
        assertEquals("hij", queue.toString());

        assertEquals("efg", queue.consumeToAny("hij").toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testChompBalanced() {
        TokenQueue queue = new TokenQueue("(abc)def");
        assertEquals("abc", queue.chompBalanced('(', ')').toString());
        assertEquals("def", queue.toString());

        assertEquals("(xyz)", queue.chompBalanced('(', ')').toString());
        assertEquals("", queue.toString());

        assertEquals("(abc)", queue.chompBalanced('(', ')').toString());
        assertEquals("def", queue.toString());
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
        assertEquals("\\", TokenQueue.unescape("\\"));
        assertEquals("foo\\bar", TokenQueue.unescape("foo\\bar"));
        assertEquals("foo\\\\bar", TokenQueue.unescape("foo\\\\bar"));
        assertEquals("foo\\\\\\\\bar", TokenQueue.unescape("foo\\\\\\\\bar"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue queue = new TokenQueue(" abc def");
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertTrue(queue.consumeWhitespace());
        assertFalse(queue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("abc", queue.consumeWord().toString());
        assertEquals("def", queue.toString());

        assertEquals("def", queue.consumeWord().toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue queue = new TokenQueue("<abc:def>ghi");
        assertEquals("abc", queue.consumeTagName().toString());
        assertEquals("def", queue.toString());

        assertEquals("def", queue.consumeTagName().toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue queue = new TokenQueue("<abc:def>ghi");
        assertEquals("abc", queue.consumeElementSelector().toString());
        assertEquals("def", queue.toString());

        assertEquals("def", queue.consumeElementSelector().toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue queue = new TokenQueue("class:id abc:def");
        assertEquals("abc", queue.consumeCssIdentifier().toString());
        assertEquals("def", queue.toString());

        assertEquals("def", queue.consumeCssIdentifier().toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue queue = new TokenQueue("class=\"id:name\" abc:def");
        assertEquals("id", queue.consumeAttributeKey().toString());
        assertEquals("name", queue.toString());

        assertEquals("name", queue.consumeAttributeKey().toString());
        assertEquals("", queue.toString());
    }

    @Test
    public void testRemainder() {
        TokenQueue queue = new TokenQueue("abcdef");
        assertEquals("def", queue.remainder().toString());
        assertEquals("", queue.toString());
    }
}