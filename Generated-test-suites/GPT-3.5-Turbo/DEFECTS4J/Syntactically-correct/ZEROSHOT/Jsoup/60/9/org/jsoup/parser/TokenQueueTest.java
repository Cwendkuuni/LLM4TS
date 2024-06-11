package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        String data = "test data";
        tokenQueue = new TokenQueue(data);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testRemainingLength() {
        int expected = 9;
        assertEquals(expected, tokenQueue.remainingLength());
    }

    @Test
    public void testPeek() {
        char expected = 't';
        assertEquals(expected, tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('a');
        char expected = 'a';
        assertEquals(expected, tokenQueue.peek());
    }

    @Test
    public void testAddFirstString() {
        tokenQueue.addFirst("new string");
        String expected = "new stringtest data";
        assertEquals(expected, tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("TEST"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("test", "data"));
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("test"));
        String expected = " data";
        assertEquals(expected, tokenQueue.toString());
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
        char expected = 'e';
        assertEquals(expected, tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        char expected = 't';
        assertEquals(expected, tokenQueue.consume());
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("test");
        String expected = " data";
        assertEquals(expected, tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        String expected = "test";
        assertEquals(expected, tokenQueue.consumeTo("data"));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        String expected = "test";
        assertEquals(expected, tokenQueue.consumeToIgnoreCase("DATA"));
    }

    @Test
    public void testConsumeToAny() {
        String expected = "tes";
        assertEquals(expected, tokenQueue.consumeToAny("t", "data"));
    }

    @Test
    public void testChompTo() {
        String expected = "test";
        assertEquals(expected, tokenQueue.chompTo("data"));
        String remaining = " data";
        assertEquals(remaining, tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        String expected = "test";
        assertEquals(expected, tokenQueue.chompToIgnoreCase("DATA"));
        String remaining = " data";
        assertEquals(remaining, tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        String expected = "test";
        assertEquals(expected, tokenQueue.chompBalanced('t', 'd'));
    }

    @Test
    public void testUnescape() {
        String in = "\\test";
        String expected = "\\test";
        assertEquals(expected, TokenQueue.unescape(in));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        String expected = "test";
        assertEquals(expected, tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        String expected = "test";
        assertEquals(expected, tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        String expected = "test";
        assertEquals(expected, tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        String expected = "test";
        assertEquals(expected, tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        String expected = "test";
        assertEquals(expected, tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        String expected = "test data";
        assertEquals(expected, tokenQueue.remainder());
    }

}