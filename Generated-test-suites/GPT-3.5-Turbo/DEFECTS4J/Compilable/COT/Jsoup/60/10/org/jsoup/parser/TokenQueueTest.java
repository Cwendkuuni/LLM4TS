package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
        
        tokenQueue.addFirst("hello");
        assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertEquals('h', tokenQueue.peek());
        
        tokenQueue.consume();
        assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst('h');
        assertEquals("hworld", tokenQueue.toString());
        
        tokenQueue.addFirst("hello ");
        assertEquals("hello hworld", tokenQueue.toString());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertTrue(tokenQueue.matches("hello"));
        assertFalse(tokenQueue.matches("Hello"));
    }
    
    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertTrue(tokenQueue.matchesCS("hello"));
        assertFalse(tokenQueue.matchesCS("Hello"));
    }
    
    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertTrue(tokenQueue.matchesAny("hello", "world"));
        assertFalse(tokenQueue.matchesAny("Hello", "world"));
    }
    
    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html>");
        assertTrue(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertTrue(tokenQueue.matchChomp("hello"));
        assertFalse(tokenQueue.matchChomp("world"));
    }
    
    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" ");
        assertTrue(tokenQueue.matchesWhitespace());
    }
    
    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertTrue(tokenQueue.matchesWord());
    }
    
    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        tokenQueue.advance();
        assertEquals("ello", tokenQueue.toString());
    }
    
    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertEquals('h', tokenQueue.consume());
    }
    
    @Test
    public void testConsumeString() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        tokenQueue.consume("hello");
        assertEquals("", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("hello,world");
        assertEquals("hello", tokenQueue.consumeTo(","));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("hello,world");
        assertEquals("hello", tokenQueue.consumeToIgnoreCase(","));
    }
    
    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("hello,world");
        assertEquals("hello", tokenQueue.consumeToAny(",", "!"));
    }
    
    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("hello,world");
        assertEquals("hello", tokenQueue.chompTo(","));
        assertEquals(",world", tokenQueue.toString());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("hello,world");
        assertEquals("hello", tokenQueue.chompToIgnoreCase(","));
        assertEquals(",world", tokenQueue.toString());
    }
    
    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
    }
    
    @Test
    public void testUnescape() {
        String escapedString = "\\\"hello\\\"";
        assertEquals("\"hello\"", TokenQueue.unescape(escapedString));
    }
    
    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("    hello");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("hello", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("hello123");
        assertEquals("hello123", tokenQueue.consumeWord());
    }
    
    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("div");
        assertEquals("div", tokenQueue.consumeTagName());
    }
    
    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("*|div");
        assertEquals("*|div", tokenQueue.consumeElementSelector());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("my-class");
        assertEquals("my-class", tokenQueue.consumeCssIdentifier());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("data-key");
        assertEquals("data-key", tokenQueue.consumeAttributeKey());
    }
    
    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("hello,world");
        assertEquals("hello,world", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }
}