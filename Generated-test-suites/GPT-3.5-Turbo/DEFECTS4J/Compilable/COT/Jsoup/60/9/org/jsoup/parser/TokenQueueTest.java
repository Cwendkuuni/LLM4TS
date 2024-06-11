package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst("hello");
        assertEquals("hello world", tokenQueue.toString());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertTrue(tokenQueue.matches("hello"));
    }
    
    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Hello");
        assertTrue(tokenQueue.matchesCS("Hello"));
    }
    
    @Test
    public void testMatchesAnyStringArray() {
        TokenQueue tokenQueue = new TokenQueue("a");
        assertTrue(tokenQueue.matchesAny("a", "b"));
    }
    
    @Test
    public void testMatchesAnyCharArray() {
        TokenQueue tokenQueue = new TokenQueue("a");
        assertTrue(tokenQueue.matchesAny('a', 'b'));
    }
    
    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<tag");
        assertTrue(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        TokenQueue tokenQueue = new TokenQueue("hello");
        assertTrue(tokenQueue.matchChomp("hello"));
    }
    
    @Test
    public void testMatchesWhitespace() {
        TokenQueue tokenQueue = new TokenQueue(" ");
        assertTrue(tokenQueue.matchesWhitespace());
    }
    
    @Test
    public void testMatchesWord() {
        TokenQueue tokenQueue = new TokenQueue("a");
        assertTrue(tokenQueue.matchesWord());
    }
    
    @Test
    public void testAdvance() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        tokenQueue.advance();
        assertEquals('b', tokenQueue.peek());
    }
    
    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals('a', tokenQueue.consume());
        assertEquals('b', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeString() {
        TokenQueue tokenQueue = new TokenQueue("hello world");
        tokenQueue.consume("hello");
        assertEquals(" world", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("one two three");
        assertEquals("one ", tokenQueue.consumeTo("two"));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("one TWO three");
        assertEquals("one ", tokenQueue.consumeToIgnoreCase("two"));
    }
    
    @Test
    public void testConsumeToAny() {
        TokenQueue tokenQueue = new TokenQueue("one two three");
        assertEquals("one ", tokenQueue.consumeToAny("two"));
    }
    
    @Test
    public void testChompTo() {
        TokenQueue tokenQueue = new TokenQueue("one two three");
        assertEquals("one two", tokenQueue.chompTo("three"));
    }
    
    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("one TWO three");
        assertEquals("one ", tokenQueue.chompToIgnoreCase("two"));
    }
    
    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
    }
    
    @Test
    public void testUnescape() {
        String escaped = "\\\"hello\\\"";
        assertEquals("\"hello\"", TokenQueue.unescape(escaped));
    }
    
    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("   ");
        assertTrue(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("abc123");
        assertEquals("abc123", tokenQueue.consumeWord());
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
        TokenQueue tokenQueue = new TokenQueue("class-name");
        assertEquals("class-name", tokenQueue.consumeCssIdentifier());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("data-key");
        assertEquals("data-key", tokenQueue.consumeAttributeKey());
    }
    
    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertEquals("abc", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }
}