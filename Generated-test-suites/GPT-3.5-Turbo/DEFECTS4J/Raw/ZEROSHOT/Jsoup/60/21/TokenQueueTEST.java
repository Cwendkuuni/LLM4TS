
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    private TokenQueue tokenQueue;
    
    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("test string");
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
        tokenQueue.addFirst("test ");
        assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("t"));
    }
    
    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("t"));
    }
    
    @Test
    public void testMatchesAnyString() {
        assertTrue(tokenQueue.matchesAny("t", "e", "s"));
    }
    
    @Test
    public void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('t', 'e', 's'));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("t"));
        assertEquals('e', tokenQueue.peek());
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
    public void testConsumeString() {
        tokenQueue.consume("test");
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("test ", tokenQueue.consumeTo("s"));
        assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("test ", tokenQueue.consumeToIgnoreCase("S"));
        assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("tes", tokenQueue.consumeToAny("st", "a"));
        assertEquals('t', tokenQueue.peek());
    }
    
    @Test
    public void testChompTo() {
        assertEquals("test", tokenQueue.chompTo(" "));
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("test", tokenQueue.chompToIgnoreCase(" "));
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testChompBalanced() {
        tokenQueue = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testUnescape() {
        assertEquals("\\t\\e\\s\\t", TokenQueue.unescape("\\\\t\\\\e\\\\s\\\\t"));
    }
    
    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        assertEquals("test", tokenQueue.consumeWord());
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTagName() {
        assertEquals("test", tokenQueue.consumeTagName());
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeElementSelector() {
        assertEquals("test", tokenQueue.consumeElementSelector());
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("test", tokenQueue.consumeCssIdentifier());
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        assertEquals("test", tokenQueue.consumeAttributeKey());
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testRemainder() {
        assertEquals("test string", tokenQueue.remainder());
        assertEquals(11, tokenQueue.pos);
    }
    
    @Test
    public void testToString() {
        assertEquals("test string", tokenQueue.toString());
    }
}
