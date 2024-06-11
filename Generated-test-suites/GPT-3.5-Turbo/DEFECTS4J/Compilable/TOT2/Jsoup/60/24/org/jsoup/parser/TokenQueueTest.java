package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("test");
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
        assertEquals('a', tokenQueue.consume());
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
        assertTrue(tokenQueue.matchesAny("test"));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("test"));
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
    public void testConsumeTo() {
        assertEquals("", tokenQueue.consumeTo("x"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("", tokenQueue.consumeToIgnoreCase("X"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("test", tokenQueue.consumeToAny("x"));
    }

    @Test
    public void testChompTo() {
        assertEquals("test", tokenQueue.chompTo("x"));
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("test", tokenQueue.chompToIgnoreCase("X"));
    }

    @Test
    public void testChompBalanced() {
        assertEquals("test", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("test", TokenQueue.unescape("test"));
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
        assertEquals("", tokenQueue.remainder());
    }

    @Test
    public void testToString() {
        assertEquals("test", tokenQueue.toString());
    }
}