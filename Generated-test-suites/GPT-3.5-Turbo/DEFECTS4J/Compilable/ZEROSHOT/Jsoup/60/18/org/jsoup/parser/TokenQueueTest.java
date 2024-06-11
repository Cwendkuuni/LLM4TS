package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {
    private TokenQueue tq;

    @Before
    public void setUp() {
        tq = new TokenQueue("test");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tq.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('t', tq.peek());
    }

    @Test
    public void testAddFirst() {
        tq.addFirst(" ");
        assertEquals(' ', tq.peek());
    }

    @Test
    public void testMatches() {
        assertTrue(tq.matches("test"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tq.matchesCS("test"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(tq.matchesAny("t", "e", "s"));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tq.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tq.matchChomp("test"));
    }

    @Test
    public void testMatchesWhitespace() {
        assertFalse(tq.matchesWhitespace());
    }

    @Test
    public void testMatchesWord() {
        assertTrue(tq.matchesWord());
    }

    @Test
    public void testAdvance() {
        tq.advance();
        assertEquals('e', tq.peek());
    }

    @Test
    public void testConsume() {
        assertEquals('t', tq.consume());
        assertEquals('e', tq.peek());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("t", tq.consumeTo("e"));
        assertEquals("st", tq.remainder());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("t", tq.consumeToIgnoreCase("E"));
        assertEquals("st", tq.remainder());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("t", tq.consumeToAny("e"));
        assertEquals("st", tq.remainder());
    }

    @Test
    public void testChompTo() {
        assertEquals("t", tq.chompTo("e"));
        assertEquals("st", tq.remainder());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("t", tq.chompToIgnoreCase("E"));
        assertEquals("st", tq.remainder());
    }

    @Test
    public void testChompBalanced() {
        assertEquals("test", tq.chompBalanced('(', ')'));
        assertEquals("", tq.remainder());
    }

    @Test
    public void testUnescape() {
        assertEquals("test", TokenQueue.unescape("test"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tq.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("test", tq.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("test", tq.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("test", tq.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("test", tq.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("test", tq.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        assertEquals("test", tq.remainder());
    }

    @Test
    public void testToString() {
        assertEquals("test", tq.toString());
    }
}