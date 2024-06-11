package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenQueueTest {

    @Test
    public void testTokenQueueIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueueIsNotEmpty() {
        TokenQueue tokenQueue = new TokenQueue("foo");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testTokenQueuePeek() {
        TokenQueue tokenQueue = new TokenQueue("foo");
        assertEquals('f', tokenQueue.peek());
    }

    @Test
    public void testTokenQueuePeekEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertEquals((char) 0, tokenQueue.peek());
    }

    @Test
    public void testTokenQueueAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst('a');
        tokenQueue.addFirst('b');
        tokenQueue.addFirst('c');
        assertEquals("cba", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueAddFirstString() {
        TokenQueue tokenQueue = new TokenQueue("");
        tokenQueue.addFirst("abc");
        assertEquals("abc", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueMatches() {
        TokenQueue tokenQueue = new TokenQueue("foo");
        tokenQueue.advance(); // consume f
        assertTrue(tokenQueue.matches("oo"));
    }

    @Test
    public void testTokenQueueMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("foo");
        assertTrue(tokenQueue.matchesCS("FoO"));
    }

    @Test
    public void testTokenQueueMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("foo");
        tokenQueue.advance(); // consume f
        tokenQueue.matchesAny('o', 'n');
        assertTrue(tokenQueue.matches("o"));
        assertEquals("o", tokenQueue.consume());
    }

    @Test
    public void testTokenQueueMatchesAnyChar() {
        TokenQueue tokenQueue = new TokenQueue("foo");
        tokenQueue.advance(); // consume f
        assertTrue(tokenQueue.matchesAny('o', 'n'));
        assertEquals('o', tokenQueue.consume());
    }

    @Test
    public void testTokenQueueMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<foo>");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testTokenQueueMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("foo<bar>baz");
        tokenQueue.matchChomp("foo");
        assertEquals("<bar>baz", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" foo ");
        assertTrue(tokenQueue.matchesWhitespace());
        tokenQueue.consumeWhitespace();
        tokenQueue.consumeWhitespace();
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("foo");
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.advance();
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testTokenQueueAdvance() {
        TokenQueue tokenQueue = new TokenQueue("foo");
        tokenQueue.advance();
        assertEquals("o", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsume() {
        TokenQueue tokenQueue = new TokenQueue("foo");
        assertEquals('f', tokenQueue.consume());
        assertEquals("o", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeSequence() {
        TokenQueue tokenQueue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", tokenQueue.consumeTo("foo"));
        assertEquals("<bar>baz", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeSequenceIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("Foo<BAR>BAZ");
        assertEquals("Foo", tokenQueue.consumeToIgnoreCase("foo"));
        assertEquals("<BAR>BAZ", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueConsumeAny() {
        TokenQueue tokenQueue = new TokenQueue("foo<bar>baz");
        assertEquals("foo", tokenQueue.consumeToAny("bar", "baz"));
        assertEquals("<bar>baz", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(foo (bar) baz) qux");
        assertEquals("foo (bar) baz", tokenQueue.chompBalanced('(', ')').toString());
        assertEquals("qux", tokenQueue.toString());
    }

    @Test
    public void testTokenQueueUnescape() {
        assertEquals("abc\\def\\ghi", TokenQueue.unescape("abc\\def\\ghi"));
    }
}