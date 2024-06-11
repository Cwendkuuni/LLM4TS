package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;

import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("Hello World");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('H', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('!');
        assertEquals('!', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("Hello"));
        assertFalse(tokenQueue.matches("world"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("hello"));
        assertFalse(tokenQueue.matchesCS("Hello"));
    }

    @Test
    public void testMatchesAny_String() {
        assertTrue(tokenQueue.matchesAny("Hello", "Hi", "Hola"));
        assertFalse(tokenQueue.matchesAny("World", "Hi", "Hola"));
    }

    @Test
    public void testMatchesAny_Char() {
        assertTrue(tokenQueue.matchesAny('H', 'W'));
        assertFalse(tokenQueue.matchesAny('h', 'W'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("Hello"));
        assertFalse(tokenQueue.matchChomp("World"));
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
        assertEquals('H', tokenQueue.consume());
        assertEquals('e', tokenQueue.peek());
    }

    @Test
    public void testConsume_String() {
        tokenQueue.consume("Hello");
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello", tokenQueue.consumeTo(" "));
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("Hello", tokenQueue.consumeToIgnoreCase(" "));
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", tokenQueue.consumeToAny(" ", "W"));
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testChompTo() {
        assertEquals("Hello", tokenQueue.chompTo(" "));
        assertEquals('W', tokenQueue.peek());
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("Hello", tokenQueue.chompToIgnoreCase(" "));
        assertEquals('W', tokenQueue.peek());
    }

    @Test
    public void testChompBalanced() {
        assertEquals("Hello", tokenQueue.chompBalanced('H', 'o'));
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testUnescape() {
        String escapedString = "\\\"";
        assertEquals("\"", TokenQueue.unescape(escapedString));
    }

    @Test
    public void testConsumeWhitespace() {
        assertTrue(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("Hello", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Hello", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("Hello", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("Hello", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("Hello", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        assertEquals("Hello World", tokenQueue.remainder());
    }

    @Test
    public void testToString() {
        assertEquals("Hello World", tokenQueue.toString());
    }
}