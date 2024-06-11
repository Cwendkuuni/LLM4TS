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
        TokenQueue tokenQueue = new TokenQueue("example");
        assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tokenQueue = new TokenQueue("world");
        tokenQueue.addFirst('h');
        assertEquals("hello world", tokenQueue.toString());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tokenQueue = new TokenQueue("example");
        assertTrue(tokenQueue.matches("exa"));
    }
    
    @Test
    public void testMatchesCS() {
        TokenQueue tokenQueue = new TokenQueue("Example");
        assertTrue(tokenQueue.matchesCS("Example"));
    }
    
    @Test
    public void testMatchesAny() {
        TokenQueue tokenQueue = new TokenQueue("example");
        assertTrue(tokenQueue.matchesAny('a', 'e'));
    }
    
    @Test
    public void testMatchesStartTag() {
        TokenQueue tokenQueue = new TokenQueue("<div>");
        assertTrue(tokenQueue.matchesStartTag());
    }
    
    // Additional test cases for other methods can be added here
    
    @Test
    public void testConsume() {
        TokenQueue tokenQueue = new TokenQueue("example");
        assertEquals('e', tokenQueue.consume());
    }
    
    @Test
    public void testConsumeTo() {
        TokenQueue tokenQueue = new TokenQueue("Hello World");
        assertEquals("Hello", tokenQueue.consumeTo(" "));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tokenQueue = new TokenQueue("Hello World");
        assertEquals("Hello", tokenQueue.consumeToIgnoreCase("world"));
    }

    @Test
    public void testConsumeWhitespace() {
        TokenQueue tokenQueue = new TokenQueue("  example");
        assertTrue(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        TokenQueue tokenQueue = new TokenQueue("Hello123 World");
        assertEquals("Hello123", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        TokenQueue tokenQueue = new TokenQueue("div span");
        assertEquals("div", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        TokenQueue tokenQueue = new TokenQueue("div|span");
        assertEquals("div|span", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tokenQueue = new TokenQueue("example-class");
        assertEquals("example-class", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tokenQueue = new TokenQueue("data-attr");
        assertEquals("data-attr", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        TokenQueue tokenQueue = new TokenQueue("remainder");
        assertEquals("remainder", tokenQueue.remainder());
    }
}