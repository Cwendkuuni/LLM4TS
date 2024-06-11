import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {
    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("example");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        assertEquals('e', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('a');
        assertEquals('a', tokenQueue.peek());
    }
    
    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("exam"));
    }
    
    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("Ex"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("x", "y", "ex"));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("exam"));
        assertEquals(4, tokenQueue.remainingLength());
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
        assertEquals('x', tokenQueue.peek());
    }
    
    @Test
    public void testConsume() {
        assertEquals('e', tokenQueue.consume());
        assertEquals('x', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("ex", tokenQueue.consumeTo("a"));
        assertEquals("a", tokenQueue.remainder());
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("exam", tokenQueue.consumeToIgnoreCase("pLE"));
        assertEquals("pLE", tokenQueue.remainder());
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("exam", tokenQueue.consumeToAny("pLE", "mple"));
        assertEquals("ple", tokenQueue.remainder());
    }
    
    @Test
    public void testChompTo() {
        assertEquals("ex", tokenQueue.chompTo("a"));
        assertEquals("a", tokenQueue.remainder());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("exam", tokenQueue.chompToIgnoreCase("pLE"));
        assertEquals("pLE", tokenQueue.remainder());
    }
    
    @Test
    public void testChompBalanced() {
        String result = tokenQueue.chompBalanced('(', ')');
        assertEquals("example", result);
        assertEquals("", tokenQueue.remainder());
    }
    
    @Test
    public void testUnescape() {
        String result = TokenQueue.unescape("\\\\");
        assertEquals("\\", result);
    }
    
    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        assertEquals("example", tokenQueue.consumeWord());
    }
    
    @Test
    public void testConsumeTagName() {
        assertEquals("example", tokenQueue.consumeTagName());
    }
    
    @Test
    public void testConsumeElementSelector() {
        assertEquals("example", tokenQueue.consumeElementSelector());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("example", tokenQueue.consumeCssIdentifier());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        assertEquals("example", tokenQueue.consumeAttributeKey());
    }
    
    @Test
    public void testRemainder() {
        assertEquals("example", tokenQueue.remainder());
    }
    
    @Test
    public void testToString() {
        assertEquals("example", tokenQueue.toString());
    }
}