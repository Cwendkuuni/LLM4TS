package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    
    private TokenQueue tokenQueue;
    
    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("Test String");
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
        tokenQueue.consume();
        assertTrue(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        assertEquals('T', tokenQueue.peek());
        tokenQueue.consume();
        assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('X');
        assertEquals("XTest String", tokenQueue.toString());
    }
    
    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("Test"));
        assertFalse(tokenQueue.matches("Different"));
    }
    
    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("Test"));
        assertFalse(tokenQueue.matchesCS("test"));
    }
    
    @Test
    public void testMatchesAnyString() {
        assertTrue(tokenQueue.matchesAny("Not", "Different", "Test"));
        assertFalse(tokenQueue.matchesAny("Not", "Different"));
    }
    
    @Test
    public void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('T', 'N', 'D'));
        assertFalse(tokenQueue.matchesAny('A', 'B', 'C'));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
        tokenQueue.addFirst('<');
        assertTrue(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("Test"));
        assertEquals(" String", tokenQueue.toString());
    }
    
    @Test
    public void testMatchesWhitespace() {
        assertFalse(tokenQueue.matchesWhitespace());
        tokenQueue.consume();
        assertTrue(tokenQueue.matchesWhitespace());
    }
    
    @Test
    public void testMatchesWord() {
        assertTrue(tokenQueue.matchesWord());
        tokenQueue.consume();
        assertFalse(tokenQueue.matchesWord());
    }
    
    @Test
    public void testAdvance() {
        tokenQueue.advance();
        assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', tokenQueue.consume());
        assertEquals('e', tokenQueue.consume());
    }
    
    @Test
    public void testConsumeString() {
        tokenQueue.consume("Test");
        assertEquals(" String", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("Test", tokenQueue.consumeTo(" "));
        assertEquals("String", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("Test S", tokenQueue.consumeToIgnoreCase(" s"));
        assertEquals("tring", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("Test ", tokenQueue.consumeToAny("x", " "));
        assertEquals("String", tokenQueue.toString());
    }
    
    @Test
    public void testChompTo() {
        assertEquals("Test", tokenQueue.chompTo(" "));
        assertEquals("String", tokenQueue.toString());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("Test ", tokenQueue.chompToIgnoreCase("s"));
        assertEquals("String", tokenQueue.toString());
    }
    
    @Test
    public void testChompBalanced() {
        tokenQueue.addFirst('(');
        tokenQueue.consume();
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
    }
    
    @Test
    public void testUnescape() {
        String escaped = "T\\e\\s\\t";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("Test", unescaped);
    }
    
    @Test
    public void testConsumeWhitespace() {
        tokenQueue.addFirst("    ");
        assertTrue(tokenQueue.consumeWhitespace());
        assertEquals("Test String", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeWord() {
        assertEquals("Test", tokenQueue.consumeWord());
        assertEquals(" String", tokenQueue.toString());
    }
    
    @Test
    public void testConsumeTagName() {
        tokenQueue.addFirst(":Test-name123");
        assertEquals(":Test-name123", tokenQueue.consumeTagName());
    }
    
    @Test
    public void testConsumeElementSelector() {
        tokenQueue.addFirst("*|Test-element");
        assertEquals("*|Test-element", tokenQueue.consumeElementSelector());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        tokenQueue.addFirst("test-class");
        assertEquals("test-class", tokenQueue.consumeCssIdentifier());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        tokenQueue.addFirst("attr:key");
        assertEquals("attr:key", tokenQueue.consumeAttributeKey());
    }
    
    @Test
    public void testRemainder() {
        assertEquals("Test String", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }
    
}