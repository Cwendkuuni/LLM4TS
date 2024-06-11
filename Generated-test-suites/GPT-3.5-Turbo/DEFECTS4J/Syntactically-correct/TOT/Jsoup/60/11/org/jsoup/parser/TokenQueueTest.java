package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;
    
    @Before
    public void setUp() {
        String data = "abcdef";
        tokenQueue = new TokenQueue(data);
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
        
        String emptyData = "";
        TokenQueue emptyQueue = new TokenQueue(emptyData);
        
        assertTrue(emptyQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        assertEquals('a', tokenQueue.peek());
        
        String emptyData = "";
        TokenQueue emptyQueue = new TokenQueue(emptyData);
        
        assertEquals(0, emptyQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('z');
        
        assertEquals('z', tokenQueue.peek());
        assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("abc"));
        assertFalse(tokenQueue.matches("def"));
    }
    
    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("ABC"));
        assertFalse(tokenQueue.matchesCS("abc"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("cde"));
        assertFalse(tokenQueue.matchesAny("xyz"));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
        
        String startTagData = "<div>";
        TokenQueue startTagQueue = new TokenQueue(startTagData);
        
        assertTrue(startTagQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("abc"));
        assertFalse(tokenQueue.matchChomp("def"));
    }
    
    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
        
        String whitespaceData = " ";
        TokenQueue whitespaceQueue = new TokenQueue(whitespaceData);
        
        assertTrue(whitespaceQueue.matchesWhitespace());
    }
    
    @Test
    public void testMatchesWord() {
        assertFalse(tokenQueue.matchesWord());
        
        String wordData = "abc1";
        TokenQueue wordQueue = new TokenQueue(wordData);
        
        assertTrue(wordQueue.matchesWord());
    }
    
    @Test
    public void testAdvance() {
        tokenQueue.advance();
        
        assertEquals('b', tokenQueue.peek());
    }
    
    @Test
    public void testConsume() {
        char consumed = tokenQueue.consume();
        
        assertEquals('a', consumed);
        assertEquals('b', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTo() {
        String consumed = tokenQueue.consumeTo("d");
        
        assertEquals("abc", consumed);
        assertEquals('d', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        String consumed = tokenQueue.consumeToIgnoreCase("D");
        
        assertEquals("abc", consumed);
        assertEquals('d', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeToAny() {
        String consumed = tokenQueue.consumeToAny("d");
        
        assertEquals("abc", consumed);
        assertEquals('d', tokenQueue.peek());
    }
    
    @Test
    public void testChompTo() {
        String chomped = tokenQueue.chompTo("d");
        
        assertEquals("abc", chomped);
        assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        String chomped = tokenQueue.chompToIgnoreCase("D");
        
        assertEquals("abc", chomped);
        assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testChompBalanced() {
        String balanced = tokenQueue.chompBalanced('(', ')');
        
        assertEquals("(abc", balanced);
        assertEquals('d', tokenQueue.peek());
    }
    
    @Test
    public void testUnescape() {
        String escaped = "\\\"";
        String unescaped = TokenQueue.unescape(escaped);
        
        assertEquals("\"", unescaped);
    }
    
    @Test
    public void testConsumeWhitespace() {
        boolean consumed = tokenQueue.consumeWhitespace();
        
        assertTrue(consumed);
        assertEquals('a', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeWord() {
        String consumed = tokenQueue.consumeWord();
        
        assertEquals("abc", consumed);
        assertEquals(0, tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTagName() {
        String consumed = tokenQueue.consumeTagName();
        
        assertEquals("abc", consumed);
        assertEquals(0, tokenQueue.peek());
    }
    
    @Test
    public void testConsumeElementSelector() {
        String consumed = tokenQueue.consumeElementSelector();
        
        assertEquals("abc", consumed);
        assertEquals(0, tokenQueue.peek());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        String consumed = tokenQueue.consumeCssIdentifier();
        
        assertEquals("abc", consumed);
        assertEquals(0, tokenQueue.peek());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        String consumed = tokenQueue.consumeAttributeKey();
        
        assertEquals("abc", consumed);
        assertEquals(0, tokenQueue.peek());
    }
    
    @Test
    public void testRemainder() {
        String remainder = tokenQueue.remainder();
        
        assertEquals("abcdef", remainder);
    }
}