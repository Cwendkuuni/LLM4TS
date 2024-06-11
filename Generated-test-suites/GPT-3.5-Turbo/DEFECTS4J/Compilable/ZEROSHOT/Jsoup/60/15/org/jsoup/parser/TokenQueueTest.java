package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setup() {
        tokenQueue = new TokenQueue("Test String");
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
        tokenQueue.addFirst('A');
        assertEquals("ATest String", tokenQueue.toString());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("Test"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("Test"));
    }

    @Test
    public void testMatchesAnyString() {
        assertTrue(tokenQueue.matchesAny("Test"));
    }

    @Test
    public void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('T'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("Test"));
        assertEquals(" String", tokenQueue.toString());
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
        assertEquals("est String", tokenQueue.toString());
    }

    @Test
    public void testConsume() {
        assertEquals('T', tokenQueue.consume());
        assertEquals("est String", tokenQueue.toString());
    }

    @Test
    public void testConsumeSeq() {
        tokenQueue.consume("Test");
        assertEquals(" String", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("", tokenQueue.consumeTo("Test"));
        assertEquals("Test String", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("", tokenQueue.consumeToIgnoreCase("test"));
        assertEquals("Test String", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", tokenQueue.consumeToAny("Test"));
        assertEquals("Test String", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        assertEquals("", tokenQueue.chompTo("Test"));
        assertEquals(" ", tokenQueue.toString());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("", tokenQueue.chompToIgnoreCase("test"));
        assertEquals(" ", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        assertEquals("", tokenQueue.chompBalanced('(', ')'));
    }

    @Test
    public void testUnescape() {
        assertEquals("Test\\", TokenQueue.unescape("Test\\\\"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("Test", tokenQueue.consumeWord());
        assertEquals(" String", tokenQueue.toString());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Test", tokenQueue.consumeTagName());
        assertEquals(" String", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeElementSelector() {
        assertEquals("Test", tokenQueue.consumeElementSelector());
        assertEquals(" String", tokenQueue.toString());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("Test", tokenQueue.consumeCssIdentifier());
        assertEquals(" String", tokenQueue.toString());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("Test", tokenQueue.consumeAttributeKey());
        assertEquals(" String", tokenQueue.toString());
    }

    @Test
    public void testRemainder() {
        assertEquals("Test String", tokenQueue.remainder());
        assertEquals("", tokenQueue.toString());
    }
}