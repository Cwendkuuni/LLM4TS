package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("test string");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('a');
        assertEquals("atest string", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("test"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("test", "string"));
        assertFalse(tokenQueue.matchesAny("invalid", "value"));
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
        assertEquals('t', tokenQueue.consume());
    }

    @Test
    public void testConsumeWithSequence() {
        tokenQueue.consume("test");
        assertEquals(" string", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("", tokenQueue.consumeTo("test"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("", tokenQueue.consumeToIgnoreCase("test"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", tokenQueue.consumeToAny("test", "string"));
    }

    @Test
    public void testChompTo() {
        assertEquals("", tokenQueue.chompTo("test"));
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("", tokenQueue.chompToIgnoreCase("test"));
    }

    @Test
    public void testChompBalanced() {
        assertEquals("", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("\\\\", TokenQueue.unescape("\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("test", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("test", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("test", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("test", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("test", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        assertEquals("test string", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }
}