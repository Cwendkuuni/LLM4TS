package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("test");
        
        assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tokenQueue = new TokenQueue("test");
        
        assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.addFirst('c');
        
        assertEquals('c', tokenQueue.peek());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("test");
        
        assertTrue(tokenQueue.matches("test"));
    }
    
    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("test");
        
        assertTrue(tokenQueue.matchesCS("tes"));
    }
    
    @Test
    public void testMatchesAny_string() {
        TokenQueue tokenQueue = new TokenQueue("test");
        
        assertTrue(tokenQueue.matchesAny("tes", "abcd"));
    }
    
    @Test
    public void testMatchesAny_char() {
        TokenQueue tokenQueue = new TokenQueue("test");
        
        assertTrue(tokenQueue.matchesAny('t', 'a'));
    }
    
    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<test>");
        
        assertTrue(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("test");
        
        assertTrue(tokenQueue.matchChomp("test"));
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("  ");
        
        assertTrue(tokenQueue.matchesWhitespace());
    }
    
    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("test123");
        
        assertTrue(tokenQueue.matchesWord());
    }
    
    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.advance();
        
        assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("test");
        
        assertEquals('t', tokenQueue.consume());
    }
    
    @Test
    public void testConsume_string() {
        TokenQueue tokenQueue = new TokenQueue("test");
        tokenQueue.consume("test");
        
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("test123");
        
        assertEquals("test", tokenQueue.consumeTo("1"));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("test123");
        
        assertEquals("test", tokenQueue.consumeToIgnoreCase("1"));
    }
    
    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("test");
        
        assertEquals("te", tokenQueue.consumeToAny("st"));
    }
    
    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("test123");
        
        assertEquals("test", tokenQueue.chompTo("1"));
    }
    
    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("test123");
        
        assertEquals("test", tokenQueue.chompToIgnoreCase("1"));
    }
    
    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(test)");
        
        assertEquals("test", tokenQueue.chompBalanced('(', ')'));
    }
    
    @Test
    public void testUnescape() {
        String escapedString = "test\\t";
        
        assertEquals("test\t", TokenQueue.unescape(escapedString));
    }
    
    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("  ");
        
        assertTrue(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("test123");
        
        assertEquals("test123", tokenQueue.consumeWord());
    }
    
    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("test1");
        
        assertEquals("test1", tokenQueue.consumeTagName());
    }
    
    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("test1");
        
        assertEquals("test1", tokenQueue.consumeElementSelector());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("test_1");
        
        assertEquals("test_1", tokenQueue.consumeCssIdentifier());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("test-1");
        
        assertEquals("test-1", tokenQueue.consumeAttributeKey());
    }
    
    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("test");
        
        assertEquals("test", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }
}
