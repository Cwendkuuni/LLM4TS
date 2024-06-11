package org.jsoup.parser;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    
    @Test
    public void testIsEmpty() {
        TokenQueue tq = new TokenQueue("");
        assertTrue(tq.isEmpty());
        
        tq = new TokenQueue("abc");
        assertFalse(tq.isEmpty());
    }
    
    @Test
    public void testPeek() {
        TokenQueue tq = new TokenQueue("abc");
        assertEquals('a', tq.peek());
    }
    
    @Test
    public void testAddFirst() {
        TokenQueue tq = new TokenQueue("abc");
        tq.addFirst('x');
        assertEquals("xabc", tq.toString());
    }
    
    @Test
    public void testMatches() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matches("abc"));
        assertFalse(tq.matches("xyz"));
    }
    
    @Test
    public void testMatchesCS() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matchesCS("ABC"));
        assertFalse(tq.matchesCS("XYZ"));
    }
    
    @Test
    public void testMatchesAny() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matchesAny("xyz", "abc"));
        assertFalse(tq.matchesAny("xyz", "def"));
    }
    
    @Test
    public void testMatchesStartTag() {
        TokenQueue tq = new TokenQueue("<tag>");
        assertTrue(tq.matchesStartTag());
        tq.consume();
        
        tq = new TokenQueue("<123>");
        assertFalse(tq.matchesStartTag());
        
        tq = new TokenQueue("<-abc>");
        assertTrue(tq.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matchChomp("abc"));
        assertEquals("", tq.toString());
    }
    
    @Test
    public void testMatchesWhitespace() {
        TokenQueue tq = new TokenQueue(" ");
        assertTrue(tq.matchesWhitespace());
        
        tq = new TokenQueue("abc");
        assertFalse(tq.matchesWhitespace());
    }
    
    @Test
    public void testMatchesWord() {
        TokenQueue tq = new TokenQueue("abc");
        assertTrue(tq.matchesWord());
        
        tq = new TokenQueue("123");
        assertTrue(tq.matchesWord());
        
        tq = new TokenQueue("@");
        assertFalse(tq.matchesWord());
    }
    
    @Test
    public void testAdvance() {
        TokenQueue tq = new TokenQueue("abc");
        tq.advance();
        assertEquals('b', tq.peek());
    }
    
    @Test
    public void testConsume() {
        TokenQueue tq = new TokenQueue("abc");
        assertEquals('a', tq.consume());
        assertEquals('b', tq.consume());
        assertEquals('c', tq.consume());
    }
    
    @Test
    public void testConsumeString() {
        TokenQueue tq = new TokenQueue("abc");
        tq.consume("ab");
        assertEquals("c", tq.toString());
    }
    
    @Test
    public void testConsumeTo() {
        TokenQueue tq = new TokenQueue("abc123");
        assertEquals("abc", tq.consumeTo("1"));
        assertEquals("123", tq.toString());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        TokenQueue tq = new TokenQueue("abc123");
        assertEquals("abc", tq.consumeToIgnoreCase("1"));
        assertEquals("123", tq.toString());
    }
    
    @Test
    public void testConsumeToAny() {
        TokenQueue tq = new TokenQueue("abc123");
        assertEquals("abc", tq.consumeToAny("1"));
        assertEquals("123", tq.toString());
    }
    
    @Test
    public void testChompTo() {
        TokenQueue tq = new TokenQueue("abc123");
        assertEquals("abc", tq.chompTo("1"));
        assertEquals("23", tq.toString());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        TokenQueue tq = new TokenQueue("abc123");
        assertEquals("abc", tq.chompToIgnoreCase("1"));
        assertEquals("23", tq.toString());
    }
    
    @Test
    public void testChompBalanced() {
        TokenQueue tq = new TokenQueue("(one (two) three) four");
        assertEquals("one (two) three", tq.chompBalanced('(', ')'));
        assertEquals(" four", tq.toString());
    }
    
    @Test
    public void testUnescape() {
        String escaped = "\\\"";
        assertEquals("\"", TokenQueue.unescape(escaped));
    }
    
    @Test
    public void testConsumeWhitespace() {
        TokenQueue tq = new TokenQueue("  abc");
        assertTrue(tq.consumeWhitespace());
        assertEquals("abc", tq.toString());
    }
    
    @Test
    public void testConsumeWord() {
        TokenQueue tq = new TokenQueue("abc123");
        assertEquals("abc", tq.consumeWord());
        assertEquals("123", tq.toString());
    }
    
    @Test
    public void testConsumeTagName() {
        TokenQueue tq = new TokenQueue("abc:123");
        assertEquals("abc:123", tq.consumeTagName());
    }
    
    @Test
    public void testConsumeElementSelector() {
        TokenQueue tq = new TokenQueue("abc|123");
        assertEquals("abc|123", tq.consumeElementSelector());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        TokenQueue tq = new TokenQueue("abc-123");
        assertEquals("abc-123", tq.consumeCssIdentifier());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        TokenQueue tq = new TokenQueue("abc:123");
        assertEquals("abc:123", tq.consumeAttributeKey());
    }
    
    @Test
    public void testRemainder() {
        TokenQueue tq = new TokenQueue("abc");
        assertEquals("abc", tq.remainder());
        assertTrue(tq.isEmpty());
    }
}