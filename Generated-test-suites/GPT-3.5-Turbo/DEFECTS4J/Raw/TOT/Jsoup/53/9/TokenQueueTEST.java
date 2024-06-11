import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenQueueTest {

    private TokenQueue tokenQueue;

    @Before
    public void setUp() {
        tokenQueue = new TokenQueue("This is a test string");
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek() {
        assertEquals('T', tokenQueue.peek());
    }

    @Test
    public void testAddFirst() {
        tokenQueue.addFirst('!');
        assertEquals('!', tokenQueue.peek());
    }

    @Test
    public void testMatches() {
        assertTrue(tokenQueue.matches("This"));
    }

    @Test
    public void testMatchesCS() {
        assertTrue(tokenQueue.matchesCS("This"));
    }

    @Test
    public void testMatchesAny_String() {
        assertTrue(tokenQueue.matchesAny("is", "a", "test"));
    }

    @Test
    public void testMatchesAny_Char() {
        assertTrue(tokenQueue.matchesAny('T', 'h', 'i'));
    }

    @Test
    public void testMatchesStartTag() {
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp() {
        assertTrue(tokenQueue.matchChomp("This"));
        assertEquals(" is a test string", tokenQueue.toString());
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
        assertEquals('h', tokenQueue.peek());
    }

    @Test
    public void testConsume() {
        assertEquals('T', tokenQueue.consume());
    }

    @Test
    public void testConsume_String() {
        tokenQueue.consume("This");
        assertEquals(" is a test string", tokenQueue.toString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This ", tokenQueue.consumeTo("is"));
        assertEquals("is a test string", tokenQueue.toString());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        assertEquals("This ", tokenQueue.consumeToIgnoreCase("is"));
        assertEquals("is a test string", tokenQueue.toString());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This", tokenQueue.consumeToAny("is", "a"));
        assertEquals(" is a test string", tokenQueue.toString());
    }

    @Test
    public void testChompTo() {
        assertEquals("This", tokenQueue.chompTo(" "));
        assertEquals("is a test string", tokenQueue.toString());
    }

    @Test
    public void testChompToIgnoreCase() {
        assertEquals("This", tokenQueue.chompToIgnoreCase(" "));
        assertEquals("is a test string", tokenQueue.toString());
    }

    @Test
    public void testChompBalanced() {
        assertEquals("test", tokenQueue.chompBalanced('a', 's'));
    }

    @Test
    public void testUnescape() {
        assertEquals("This is a test string", TokenQueue.unescape("This is a test string"));
    }

    @Test
    public void testConsumeWhitespace() {
        assertFalse(tokenQueue.consumeWhitespace());
    }

    @Test
    public void testConsumeWord() {
        assertEquals("This", tokenQueue.consumeWord());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("This", tokenQueue.consumeTagName());
    }

    @Test
    public void testConsumeElementSelector() {
        assertEquals("This", tokenQueue.consumeElementSelector());
    }

    @Test
    public void testConsumeCssIdentifier() {
        assertEquals("This", tokenQueue.consumeCssIdentifier());
    }

    @Test
    public void testConsumeAttributeKey() {
        assertEquals("This", tokenQueue.consumeAttributeKey());
    }

    @Test
    public void testRemainder() {
        assertEquals("This is a test string", tokenQueue.remainder());
    }
}