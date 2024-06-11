package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("hello world");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('h', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('!');
        assertEquals('!', tokenQueue.peek());
    }

    @Test
    public void testAddFirstString() {
        tokenQueue.addFirst("!");
        assertEquals('!', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("hello world"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("hello world"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("hello", "world"));
    }

    @Test
    public void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('h', 'w'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("hello"));
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertTrue(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        assertEquals('h', tokenQueue.consume());
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("hello");
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("hello", tokenQueue.consumeTo(" "));
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("hello", tokenQueue.consumeToIgnoreCase(" "));
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("hello", tokenQueue.consumeToAny(" ", "world"));
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testChompTo() {
        assertEquals("hello", tokenQueue.chompTo(" "));
        assertEquals(' ', tokenQueue.consume());
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("hello", tokenQueue.chompToIgnoreCase(" "));
        assertEquals(' ', tokenQueue.consume());
    }

    @Test
    public void testChompBalanced() {
        assertEquals(" hello ", tokenQueue.chompBalanced('h', 'd'));
    }

    @Test
    public void testUnescape() {
        assertEquals("hello\\\\world", TokenQueue.unescape("hello\\\\\\\\world"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals('w', tokenQueue.peek());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("hello", tokenQueue.consumeWord());
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("hello", tokenQueue.consumeTagName());
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("hello", tokenQueue.consumeElementSelector());
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("hello", tokenQueue.consumeCssIdentifier());
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("hello", tokenQueue.consumeAttributeKey());
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testRemainder() {
        assertEquals("hello world", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }

}