package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;
    
    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("This is a test");
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
        
        tokenQueue.consume();  // Consume one character
        
        assertFalse(tokenQueue.isEmpty());
        
        tokenQueue.consume("This");  // Consume the string "This"
        
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        assertEquals('T', tokenQueue.peek());
        
        tokenQueue.consume();
        
        assertEquals('h', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        tokenQueue.addFirst("Hello");
        
        assertEquals("HelloThis is a test", tokenQueue.toString());
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
    public void testMatchesAnyStringArray() {
        assertTrue(tokenQueue.matchesAny("This", "This is"));
        
        assertFalse(tokenQueue.matchesAny("this", "this"));
    }
    
    @Test
    public void testMatchesAnyCharArray() {
        assertTrue(tokenQueue.matchesAny('T', 't'));
        
        assertFalse(tokenQueue.matchesAny('t'));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
        
        tokenQueue.consumeTo("is");
        
        assertTrue(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("This is"));
        
        assertFalse(tokenQueue.matchChomp("This isnot"));
    }
    
    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
        
        tokenQueue.consumeTo("is");
        
        assertTrue(tokenQueue.matchesWhitespace());
    }
    
    @Test
    public void testMatchesWord() {
        assertFalse(tokenQueue.matchesWord());
        
        tokenQueue.advance();  // Consume the space
        
        assertTrue(tokenQueue.matchesWord());
    }
    
    @Test
    public void testAdvance() {
        assertEquals('T', tokenQueue.peek());
        
        tokenQueue.advance();
        
        assertEquals('h', tokenQueue.peek());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', tokenQueue.consume());
        
        assertEquals('h', tokenQueue.consume());
    }
    
    @Test
    public void testConsumeString() {
        tokenQueue.consume("This");
        
        assertEquals(" is a test", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("This", tokenQueue.consumeTo("is"));
        
        assertEquals(" is", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("This", tokenQueue.consumeToIgnoreCase("IS"));
        
        assertEquals(" is", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("This", tokenQueue.consumeToAny("is", " it"));
        
        assertEquals(" is a test", tokenQueue.toString());
    }
    
    @Test
    public void testChompTo() {
        assertEquals("This", tokenQueue.chompTo("is"));
        
        assertEquals(" is", tokenQueue.toString());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("This", tokenQueue.chompToIgnoreCase("IS"));
        
        assertEquals(" is", tokenQueue.toString());
    }
    
    @Test
    public void testChompBalanced() {
        assertEquals("(one (two) three)", tokenQueue.chompBalanced('(', ')'));
        
        assertEquals(" four", tokenQueue.toString());
    }
    
    @Test
    public void testUnescape() {
        assertEquals("\\", TokenQueue.unescape("\\\\"));
        
        assertEquals("a\\b", TokenQueue.unescape("a\\\\b"));
    }
    
    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
        
        tokenQueue.consumeTo("is");
        
        tokenQueue.advance();  // Consume the space
        
        assertTrue(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        assertEquals("This", tokenQueue.consumeWord());
        
        assertEquals(" is a test", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeTagName() {
        assertEquals("This", tokenQueue.consumeTagName());
        
        assertEquals(" is a test", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeElementSelector() {
        assertEquals("This", tokenQueue.consumeElementSelector());
        
        assertEquals(" is a test", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("This", tokenQueue.consumeCssIdentifier());
        
        assertEquals(" is a test", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        assertEquals("This", tokenQueue.consumeAttributeKey());
        
        assertEquals(" is a test", tokenQueue.toString());
    }
    
    @Test
    public void testRemainder() {
        assertEquals("This is a test", tokenQueue.remainder());
        
        assertTrue(tokenQueue.isEmpty());
    }
}

