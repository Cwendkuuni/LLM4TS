import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenQueueTest {
    private TokenQueue tokenQueue;
    
    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("Hello World");
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }
    
    @Test
    public void testPeek() {
        assertEquals('H', tokenQueue.peek());
    }
    
    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('!');
        assertEquals('!', tokenQueue.peek());
    }
    
    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("Hello"));
    }
    
    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("Hello"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(tokenQueue.matchesAny("Hello", "World"));
    }
    
    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }
    
    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("Hello"));
        assertEquals(' ', tokenQueue.peek());
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
        assertEquals('H', tokenQueue.consume());
    }
    
    @Test
    public void testConsumeString() {
        tokenQueue.consume("Hello ");
        assertEquals('W', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("Hello ", tokenQueue.consumeTo(" "));
    }
    
    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("Hello", tokenQueue.consumeToIgnoreCase(" "));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", tokenQueue.consumeToAny(" ", "W"));
    }
    
    @Test
    public void testChompTo() {
        assertEquals("Hello ", tokenQueue.chompTo(" "));
        assertEquals('W', tokenQueue.peek());
    }
    
    @Test
    public void testChompToIgnoreCase() {
        assertEquals("Hello", tokenQueue.chompToIgnoreCase(" "));
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testChompBalanced() {
        assertEquals("Hello World", tokenQueue.chompBalanced('(', ')'));
    }
    
    @Test
    public void testUnescape() {
        String escaped = "\\n\\t\\r\\\"\\'";
        String unescaped = TokenQueue.unescape(escaped);
        assertEquals("\n\t\r\"'", unescaped);
    }
    
    @Test
    public void testConsumeWhitespace() {
        assertTrue(tokenQueue.consumeWhitespace());
        assertFalse(tokenQueue.consumeWhitespace());
    }
    
    @Test
    public void testConsumeWord() {
        assertEquals("Hello", tokenQueue.consumeWord());
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeTagName() {
        assertEquals("Hello", tokenQueue.consumeTagName());
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeElementSelector() {
        assertEquals("Hello", tokenQueue.consumeElementSelector());
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("Hello", tokenQueue.consumeCssIdentifier());
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testConsumeAttributeKey() {
        assertEquals("Hello", tokenQueue.consumeAttributeKey());
        assertEquals(' ', tokenQueue.peek());
    }
    
    @Test
    public void testRemainder() {
        tokenQueue.consumeTo(" ");
        assertEquals(" World", tokenQueue.remainder());
    }
}