package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("");
    }

    @Test
    public void testIsEmpty() {
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals(0, tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('a');
        assertEquals("a", tokenQueue.toString());

        tokenQueue.addFirst("bc");
        assertEquals("bca", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches(""));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS(""));
    }

    @Test
    public void testMatchesAny() {
        assertFalse(tokenQueue.matchesAny("a", "b", "c"));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertFalse(tokenQueue.matchChomp(""));
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertFalse(tokenQueue.matchesWord());
    }

    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        assertEquals(0, tokenQueue.consume());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("");
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("", tokenQueue.consumeTo(""));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("", tokenQueue.consumeToIgnoreCase(""));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", tokenQueue.consumeToAny());
    }

    @Test
    public void testChompTo() {
        assertEquals("", tokenQueue.chompTo(""));
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("", tokenQueue.chompToIgnoreCase(""));
    }

    @Test
    public void testChompBalanced() {
        assertEquals("", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("", TokenQueue.unescape(""));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        assertEquals("", tokenQueue.remainder());
    }
}