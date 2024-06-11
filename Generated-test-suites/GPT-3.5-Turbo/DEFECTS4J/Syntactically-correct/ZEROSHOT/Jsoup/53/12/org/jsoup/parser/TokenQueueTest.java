package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        TokenQueue tokenQueue = new TokenQueue("test data");
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(tokenQueue.isEmpty());
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
        tokenQueue.addFirst("abc");
        char expected = 'a';
        assertEquals(expected, tokenQueue.peek());
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
    public void testMatchesAnyString() {
        assertTrue(tokenQueue.matchesAny("test", "data"));
    }
    
    @Test
    public void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('t', 'd'));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertTrue(tokenQueue.matchesStartTag());
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
        char expected = ' ';
        assertEquals(expected, tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTo() {
        String expected = "te";
        assertEquals(expected, tokenQueue.consumeTo("s"));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        String expected = "te";
        assertEquals(expected, tokenQueue.consumeToIgnoreCase("S"));
    }
    
    @Test
    public void testConsumeToAny() {
        String expected = "t";
        assertEquals(expected, tokenQueue.consumeToAny("d"));
    }
    
    @Test
    public void testChompTo() {
        String expected = "te";
        assertEquals(expected, tokenQueue.chompTo("s"));
    }
    
    @Test
    public void testChompToIgnoreCase() {
        String expected = "te";
        assertEquals(expected, tokenQueue.chompToIgnoreCase("S"));
    }
    
    @Test
    public void testChompBalanced() {
        String expected = "(on";
        assertEquals(expected, tokenQueue.chompBalanced('(', ')'));
    }
    
    @Test
    public void testUnescape() {
        String in = "\\test";
        String expected = "test";
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
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testToString() {
        String expected = "";
        assertEquals(expected, tokenQueue.toString());
    }
}