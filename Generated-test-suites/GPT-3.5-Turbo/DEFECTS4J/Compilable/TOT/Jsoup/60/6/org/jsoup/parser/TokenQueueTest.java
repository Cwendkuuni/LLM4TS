package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setup() {
        String data = "This is a test";
        tokenQueue = new TokenQueue(data);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('T', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('!');
        assertEquals('!', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("This"));
        assertFalse(tokenQueue.matches("this"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("This"));
        assertFalse(tokenQueue.matchesCS("this"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny('T', 't'));
        assertFalse(tokenQueue.matchesAny('t', 's'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("This"));
        assertFalse(tokenQueue.matchChomp("this"));
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
        assertEquals('h', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        assertEquals('T', tokenQueue.consume());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("This");
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeTo() {
        String consumed = tokenQueue.consumeTo("is");
        assertEquals("This ", consumed);
        assertEquals('i', tokenQueue.peek());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String consumed = tokenQueue.consumeToIgnoreCase("IS");
        assertEquals("Th", consumed);
        assertEquals('i', tokenQueue.peek());
    }

    @Test
    public void testConsumeToAny() {
        String consumed = tokenQueue.consumeToAny("This", "is");
        assertEquals("", consumed);
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testChompTo() {
        String consumed = tokenQueue.chompTo("is");
        assertEquals("This ", consumed);
        assertEquals('i', tokenQueue.peek());
    }

    @Test
    public void testChompToIgnoreCase() {
        String consumed = tokenQueue.chompToIgnoreCase("IS");
        assertEquals("Th", consumed);
        assertEquals('i', tokenQueue.peek());
    }

    @Test
    public void testChompBalanced() {
        String consumed = tokenQueue.chompBalanced('(', ')');
        assertEquals("", consumed);
    }

    @Test
    public void testUnescape() {
        String escapedString = "\\\"This is a test\\\"";
        String expectedString = "\"This is a test\"";
        String unescapedString = TokenQueue.unescape(escapedString);
        assertEquals(expectedString, unescapedString);
    }

    @Test
    public void testConsumeWhitespace() {
        boolean consumed = tokenQueue.consumeWhitespace();
        assertFalse(consumed);
    }

    @Test
    public void testConsumeWord() {
        String consumed = tokenQueue.consumeWord();
        assertEquals("This", consumed);
    }

    @Test
    public void testConsumeTagName() {
        String consumed = tokenQueue.consumeTagName();
        assertEquals("This", consumed);
    }

    @Test
    public void testConsumeElementSelector() {
        String consumed = tokenQueue.consumeElementSelector();
        assertEquals("This", consumed);
    }

    @Test
    public void testConsumeCssIdentifier() {
        String consumed = tokenQueue.consumeCssIdentifier();
        assertEquals("This", consumed);
    }

    @Test
    public void testConsumeAttributeKey() {
        String consumed = tokenQueue.consumeAttributeKey();
        assertEquals("This", consumed);
    }

    @Test
    public void testRemainder() {
        String remainder = tokenQueue.remainder();
        assertEquals("This is a test", remainder);
    }

}