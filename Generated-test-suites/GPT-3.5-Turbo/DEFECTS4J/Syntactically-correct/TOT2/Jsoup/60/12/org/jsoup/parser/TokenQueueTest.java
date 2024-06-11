import org.jsoup.parser.TokenQueue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {
    
    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("test123");
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
        assertTrue(tokenQueue.matchesCS("Test"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("abc", "test"));
    }
    
    @Test
    public void testMatchesAnyChar() {
        assertTrue(tokenQueue.matchesAny('t', 'x'));
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
    public void testConsumeString() {
        tokenQueue.consume("tes");
        assertEquals('1', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("te", tokenQueue.consumeTo("s"));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("test", tokenQueue.consumeToIgnoreCase("ST"));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("t", tokenQueue.consumeToAny("1", "x"));
    }
    
    @Test
    public void testChompTo() {
        assertEquals("te", tokenQueue.chompTo("s"));
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("te", tokenQueue.chompToIgnoreCase("S"));
    }
    
    @Test
    public void testChompBalanced() {
        assertEquals("123", tokenQueue.chompBalanced('t', 't'));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("abc", TokenQueue.unescape("a\\bc"));
    }
    
    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        assertEquals("test123", tokenQueue.consumeWord());
    }
    
    @Test
    public void testConsumeTagName() {
        assertEquals("test123", tokenQueue.consumeTagName());
    }
    
    @Test
    public void testConsumeElementSelector() {
        assertEquals("test123", tokenQueue.consumeElementSelector());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("test123", tokenQueue.consumeCssIdentifier());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        assertEquals("test123", tokenQueue.consumeAttributeKey());
    }
    
    @Test
    public void testRemainder() {
        tokenQueue.consume(3);
        assertEquals("123", tokenQueue.remainder());
    }
}