package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {
    private TokenQueue tokenQueue;
    
    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("test data");
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
    public void testAddFirstWithSequence() {
        tokenQueue.addFirst("sequence");
        assertEquals('s', tokenQueue.peek());
    }
    
    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("test"));
        assertFalse(tokenQueue.matches("notmatch"));
    }
    
    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("test"));
        assertFalse(tokenQueue.matchesCS("TEST"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("test", "notmatch"));
        assertFalse(tokenQueue.matchesAny("notmatch1", "notmatch2"));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("test"));
        assertFalse(tokenQueue.matchChomp("notmatch"));
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
        assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeWithStringSeq() {
        tokenQueue.consume("test");
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTo() {
        String data = tokenQueue.consumeTo("data");
        assertEquals("test ", data);
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        String data = tokenQueue.consumeToIgnoreCase("DATA");
        assertEquals("test ", data);
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeToAny() {
        String data = tokenQueue.consumeToAny("data", "notmatch");
        assertEquals("test ", data);
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testChompTo() {
        String data = tokenQueue.chompTo("data");
        assertEquals("test ", data);
        assertEquals('d', tokenQueue.peek());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        String data = tokenQueue.chompToIgnoreCase("DATA");
        assertEquals("test ", data);
        assertEquals('d', tokenQueue.peek());
    }
    
    @Test
    public void testChompBalanced() {
        String data = tokenQueue.chompBalanced('t', 'a');
        assertEquals("est d", data);
        assertEquals('a', tokenQueue.peek());
    }
    
    @Test
    public void testUnescape() {
        String escapedString = "\\\"text\\\"";
        String unescapedString = TokenQueue.unescape(escapedString);
        assertEquals("\"text\"", unescapedString);
    }
    
    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        String word = tokenQueue.consumeWord();
        assertEquals("test", word);
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTagName() {
        String tagName = tokenQueue.consumeTagName();
        assertEquals("test", tagName);
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeElementSelector() {
        String elementSelector = tokenQueue.consumeElementSelector();
        assertEquals("test", elementSelector);
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        String cssIdentifier = tokenQueue.consumeCssIdentifier();
        assertEquals("test", cssIdentifier);
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        String attributeKey = tokenQueue.consumeAttributeKey();
        assertEquals("test", attributeKey);
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testRemainder() {
        String remainder = tokenQueue.remainder();
        assertEquals("test data", remainder);
        assertTrue(tokenQueue.isEmpty());
    }
}