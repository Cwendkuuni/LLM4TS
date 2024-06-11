package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("Hello"));
    }

    @Test
    public void testMatchesAnyStringArray() {
        assertTrue(tokenQueue.matchesAny("Hello", "World"));
    }

    @Test
    public void testMatchesAnyCharArray() {
        assertTrue(tokenQueue.matchesAny('H', 'W'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("Hello"));
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
    }

    @Test
    public void testConsumeString() {
        tokenQueue.consume("Hello");
        assertEquals(' ', tokenQueue.peek());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello", tokenQueue.consumeTo(" "));
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("Hello", tokenQueue.consumeToIgnoreCase(" "));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", tokenQueue.consumeToAny(" ", "W"));
    }

    @Test
    public void testChompTo() {
        assertEquals("Hello", tokenQueue.chompTo(" "));
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("Hello", tokenQueue.chompToIgnoreCase(" "));
    }

    @Test
    public void testChompBalanced() {
        String result = tokenQueue.chompBalanced('(', ')');
        assertEquals("Hello World", result);
    }

    @Test
    public void testUnescape() {
        String escapedString = "Hello\\ World";
        assertEquals("Hello World", TokenQueue.unescape(escapedString));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
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
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testToString() {
        assertEquals("Hello World", tokenQueue.toString());
    }
}