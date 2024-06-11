import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("test");
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
        tokenQueue.addFirst('A');
        assertEquals('A', tokenQueue.peek());
    }
    
    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("test"));
    }
    
    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("test"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("test", "hello"));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("test"));
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
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("t", tokenQueue.consumeTo("e"));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("t", tokenQueue.consumeToIgnoreCase("E"));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("test", tokenQueue.consumeToAny("a", "e"));
    }
    
    @Test
    public void testChompTo() {
        assertEquals("t", tokenQueue.chompTo("e"));
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("t", tokenQueue.chompToIgnoreCase("E"));
    }
    
    @Test
    public void testChompBalanced() {
        assertEquals("one (two) three", tokenQueue.chompBalanced('(', ')'));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("test", TokenQueue.unescape("test"));
    }
    
    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        assertEquals("test", tokenQueue.consumeWord());
    }
    
    @Test
    public void testConsumeTagName() {
        assertEquals("test", tokenQueue.consumeTagName());
    }
    
    @Test
    public void testConsumeElementSelector() {
        assertEquals("test", tokenQueue.consumeElementSelector());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("test", tokenQueue.consumeCssIdentifier());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        assertEquals("test", tokenQueue.consumeAttributeKey());
    }
    
    @Test
    public void testRemainder() {
        assertEquals("test", tokenQueue.remainder());
        assertTrue(tokenQueue.isEmpty());
    }
}