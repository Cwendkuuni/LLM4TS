package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {
    
    private TokenQueue tokenQueue;
    
    @Before
    public void setup() {
        tokenQueue = new TokenQueue("test123");
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
        tokenQueue.addFirst('A');
        assertEquals("Atest123", tokenQueue.toString());
    }
    
    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("test"));
        assertFalse(tokenQueue.matches("123"));
    }
    
    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("test"));
        assertFalse(tokenQueue.matchesCS("Test"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("test", "123"));
        assertFalse(tokenQueue.matchesAny("abc", "def"));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("test"));
        assertFalse(tokenQueue.matchChomp("123"));
        assertEquals("123", tokenQueue.toString());
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
        assertEquals("est123", tokenQueue.toString());
    }
    
    @Test
    public void testConsume() {
        assertEquals('t', tokenQueue.consume());
        assertEquals("est123", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("t", tokenQueue.consumeTo("e"));
        assertEquals("st123", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("t", tokenQueue.consumeToIgnoreCase("E"));
        assertEquals("st123", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("test", tokenQueue.consumeToAny("123"));
        assertEquals("123", tokenQueue.toString());
    }
    
    @Test
    public void testChompTo() {
        assertEquals("t", tokenQueue.chompTo("e"));
        assertEquals("st123", tokenQueue.toString());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("t", tokenQueue.chompToIgnoreCase("E"));
        assertEquals("st123", tokenQueue.toString());
    }
    
    @Test
    public void testChompBalanced() {
        assertEquals("test", tokenQueue.chompBalanced('t', 't'));
        assertEquals("123", tokenQueue.toString());
    }
    
    @Test
    public void testUnescape() {
        String escaped = "\\\"test\\\"";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("\"test\"", unescaped);
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
        assertEquals("test123", tokenQueue.remainder());
        assertEquals("", tokenQueue.toString());
    }
}

