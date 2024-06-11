package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {
    
    private TokenQueue tokenQueue;
    
    @Before
    public void setup() {
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
        assertEquals('a', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirstString() {
        tokenQueue.addFirst("example");
        assertEquals('e', tokenQueue.peek());
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
    public void testMatchesAnyChar() {
        assertFalse(tokenQueue.matchesAny('a', 'b', 'c'));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("test"));
        assertTrue(tokenQueue.isEmpty());
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
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testConsume() {
        assertEquals('t', tokenQueue.consume());
    }
    
    @Test
    public void testConsumeString() {
        tokenQueue.consume("test");
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("te", tokenQueue.consumeTo("s"));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("t", tokenQueue.consumeToIgnoreCase("S"));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("test", tokenQueue.consumeToAny("s"));
    }
    
    @Test
    public void testChompTo() {
        assertEquals("te", tokenQueue.chompTo("s"));
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("t", tokenQueue.chompToIgnoreCase("S"));
    }
    
    @Test
    public void testChompBalanced() {
        assertEquals("es", tokenQueue.chompBalanced('t', 't'));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("escape", TokenQueue.unescape("escape"));
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
        assertEquals("test", tokenQueue.remainder());
    }
    
    @Test
    public void testToString() {
        assertEquals("test", tokenQueue.toString());
    }
}