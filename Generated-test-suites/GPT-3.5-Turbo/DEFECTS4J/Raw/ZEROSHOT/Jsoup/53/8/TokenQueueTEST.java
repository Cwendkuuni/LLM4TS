package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirstCharacter() {
        TokenQueue tokenQueue = new TokenQueue("bc");
        tokenQueue.addFirst('a');
        assertEquals("abc", tokenQueue.toString());
    }
    
    @Test
    public void testAddFirstString() {
        TokenQueue tokenQueue = new TokenQueue("c");
        tokenQueue.addFirst("ab");
        assertEquals("abc", tokenQueue.toString());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matches("abc"));
        assertFalse(tokenQueue.matches("def"));
    }
    
    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchesCS("abc"));
        assertFalse(tokenQueue.matchesCS("ABC"));
    }
    
    @Test
    public void testMatchesAnyWithStrings() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchesAny("abc", "def"));
        assertFalse(tokenQueue.matchesAny("def", "ghi"));
    }
    
    @Test
    public void testMatchesAnyWithChars() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertTrue(tokenQueue.matchesAny('a', 'b', 'c'));
        assertFalse(tokenQueue.matchesAny('d', 'e', 'f'));
    }
    
    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<html>");
        assertTrue(tokenQueue.matchesStartTag());
        
        tokenQueue = new TokenQueue("< html>");
        assertFalse(tokenQueue.matchesStartTag());
    }
    
    // Repeat the test methods for the remaining methods in the TokenQueue class
    
    @Test
    public void testMatchChomp() {
        
    }
    
    @Test
    public void testMatchesWhitespace() {
        
    }
    
    @Test
    public void testMatchesWord() {
        
    }
    
    @Test
    public void testAdvance() {
        
    }
    
    @Test
    public void testConsume() {
        
    }
    
    @Test
    public void testConsumeString() {
        
    }
    
    @Test
    public void testConsumeTo() {
        
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        
    }
    
    @Test
    public void testConsumeToAny() {
        
    }
    
    @Test
    public void testChompTo() {
        
    }
    
    @Test
    public void testChompToIgnoreCase() {
        
    }
    
    @Test
    public void testChompBalanced() {
        
    }
    
    @Test
    public void testUnescape() {
        
    }
    
    @Test
    public void testConsumeWhitespace() {
        
    }
    
    @Test
    public void testConsumeWord() {
        
    }
    
    @Test
    public void testConsumeTagName() {
        
    }
    
    @Test
    public void testConsumeElementSelector() {
        
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        
    }
    
    @Test
    public void testConsumeAttributeKey() {
        
    }
    
    @Test
    public void testRemainder() {
        
    }
    
}